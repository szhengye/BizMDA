package com.bizmda.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bizmda.entity.*;
import org.yaml.snakeyaml.Yaml;

import com.bizmda.utils.GenUtils;
import com.bizmda.utils.MdaException;

import lombok.Getter;
import lombok.extern.java.Log;

@Getter
@Log
public class MdaConfig {
	private MdaEntity mdaEntity;
	private Map<String, TableEntity> tableMap;
	private Map<String, DictionaryEntity> dictionaryMap;
	private Map<String, ViewEntity> viewMap;
	
    private static MdaConfig instance = new MdaConfig(); 
    private MdaConfig(){} 
    public static MdaConfig getInstance(){  
         return instance;  
     } 
	
	public void load(String configPath) throws MdaException {
		Map map = null;
		Yaml yaml = new Yaml();

		try {
			map = (Map)yaml.load(new FileInputStream(new File(configPath+"/mda.yml")));
			log.info(map.toString());
			this.mdaEntity = new MdaEntity(map);
		} catch (FileNotFoundException e1) {
			throw new MdaException("model/mda.yml文件不存在！",e1);
		}

		this.dictionaryMap = new HashMap<String, DictionaryEntity>();
		List<File> dictFileList = GenUtils.getFileList(configPath+"/dictionary");
		for(File dictFile:dictFileList) {
			if (!dictFile.getName().toLowerCase().endsWith(".yml"))
				continue;
			String name = dictFile.getName().substring(0,dictFile.getName().length()-4);

			try {
				map = (Map)yaml.load(new FileInputStream(dictFile));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			map.put("name",name);
//            log.info("dict:"+map.toString());
            DictionaryEntity dictionaryEntity = new DictionaryEntity(map);
			String modulePath = getModulePath(dictFile,configPath+"/dictionary");
			String module = modulePath.replace("/",".");
            dictionaryEntity.setModule(module);
            dictionaryEntity.setModulePath(modulePath);
            log.info("dictionaryEntity:"+dictionaryEntity.toString());
            this.dictionaryMap.put(dictionaryEntity.getName(), dictionaryEntity);
		}

		this.tableMap = new HashMap<String, TableEntity>();
		List<File> dataFileList = GenUtils.getFileList(configPath+"/data");
		for(File dataFile:dataFileList) {
			if (!dataFile.getName().endsWith(".yml"))
				continue;
			String name = dataFile.getName().substring(0,dataFile.getName().length()-4);

			try {
				map = (Map)yaml.load(new FileInputStream(dataFile));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			map.put("name",name);
//            log.info(map.toString());
            TableEntity tableEntity = new TableEntity(map);
			String modulePath = getModulePath(dataFile,configPath+"/data");
			String module = modulePath.replace("/",".");
			tableEntity.setModule(module);
			tableEntity.setModulePath(modulePath);
            log.info("data:"+tableEntity.toString());
            this.tableMap.put(tableEntity.getName(), tableEntity);
		}

		//处理primaryKeyTable
		for(String tableName:this.tableMap.keySet()) {
			TableEntity tableEntity = this.tableMap.get(tableName);
			for(FieldEntity fieldEntity:tableEntity.getFields()) {
				String primaryKeyTableName = fieldEntity.getPrimaryKeyTableName();
				if (primaryKeyTableName == null)
					continue;
				TableEntity primaryKeyTableEntity = this.tableMap.get(primaryKeyTableName);
				if (primaryKeyTableEntity == null) {
					throw new MdaException("错误：域["+primaryKeyTableName+"."
							+fieldEntity.getName()
							+"]的外键表"+primaryKeyTableName+"不存在！");
				}
				fieldEntity.setPrimaryKeyTable(primaryKeyTableEntity);
				ForeignKeyTableEntity foreignKeyTableEntity = new ForeignKeyTableEntity(tableEntity,fieldEntity);
				primaryKeyTableEntity.addForeignKeyTableEntity(foreignKeyTableEntity);
//				this.tableMap.put(primaryKeyTableName,primaryKeyTableEntity);
				log.info("处理primaryKeyTable:"+this.tableMap);
			}
		}

		this.viewMap = new HashMap<String, ViewEntity>();
		List<File> viewFileList = GenUtils.getFileList(configPath+"/view");
		for(File viewFile:viewFileList) {
			if (!viewFile.getName().endsWith(".yml"))
				continue;
			String name = viewFile.getName().substring(0,viewFile.getName().length()-4);
			try {
				map = (Map)yaml.load(new FileInputStream(viewFile));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			map.put("name",name);
//			log.info(map.toString());
			ViewEntity viewEntity = new ViewEntity(map);
			viewEntity.setView((Map)map.get("view"));
			String modulePath = getModulePath(viewFile,configPath+"/view");
			String module = modulePath.replace("/",".");
			viewEntity.setModule(module);
			viewEntity.setModulePath(modulePath);

			log.info("view:"+viewEntity.toString());
			this.viewMap.put(viewEntity.getName(), viewEntity);
		}

	}

	private String getModulePath(File file,String prePath) {
		String modulePath = file.getAbsolutePath().substring(prePath.length(),file.getAbsolutePath().lastIndexOf('/'));
		if (modulePath.startsWith("/")) {
			return modulePath.substring(1);
		}
		return modulePath;
	}
}
