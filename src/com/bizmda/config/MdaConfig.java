package com.bizmda.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bizmda.entity.DictionaryEntity;
import com.bizmda.entity.MdaEntity;
import com.bizmda.entity.TableEntity;
import com.bizmda.entity.ViewEntity;
import org.yaml.snakeyaml.Yaml;

import com.bizmda.utils.GenUtils;
import com.bizmda.utils.MdaException;

import lombok.Getter;
import lombok.extern.java.Log;

@Getter
@Log
public class MdaConfig {
	private MdaEntity mdaEntity;
	private Map<String, TableEntity> dataMap;
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
            
			try {
				map = (Map)yaml.load(new FileInputStream(dictFile));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            log.info(map.toString());
            DictionaryEntity dictionaryEntity = new DictionaryEntity(map);
//            this.dictionaryMap.put(dictionaryEntity., value)
		}

		this.dataMap = new HashMap<String, TableEntity>();
		List<File> dataFileList = GenUtils.getFileList(configPath+"/data");
		for(File dataFile:dataFileList) {
 			try {
				map = (Map)yaml.load(new FileInputStream(dataFile));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            log.info(map.toString());
            TableEntity tableEntity = new TableEntity(map);
            log.info("data:"+tableEntity.toString());
            this.dataMap.put(tableEntity.getName(), tableEntity);
		}

		this.viewMap = new HashMap<String, ViewEntity>();
		List<File> viewFileList = GenUtils.getFileList(configPath+"/view");
		for(File viewFile:viewFileList) {
			try {
				map = (Map)yaml.load(new FileInputStream(viewFile));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			log.info(map.toString());
			ViewEntity viewEntity = new ViewEntity(map);
			log.info("view:"+viewEntity.toString());
			this.viewMap.put(viewEntity.getName(), viewEntity);
		}

	}	
}
