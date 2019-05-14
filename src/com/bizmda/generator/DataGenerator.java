package com.bizmda.generator;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.bizmda.entity.*;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import com.bizmda.config.MdaConfig;
import com.bizmda.config.TemplateConfig;
import com.bizmda.utils.GenUtils;
import com.bizmda.utils.MdaException;

import lombok.extern.java.Log;

@Log
public class DataGenerator {

	public void gen() throws MdaException {
		String property =System.getProperty("user.dir");

		MdaConfig mdaConfig = MdaConfig.getInstance();
		MdaEntity mdaEntity = mdaConfig.getMdaEntity();
		Map mdaEntityMap = mdaEntity.toMap();
		
		TemplateEntity templateEntity = TemplateConfig.getInstance().getTemplateEntity();
		
        Properties prop = new Properties();
        prop.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, property+"/template/"+mdaEntity.getTemplate());
        prop.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
        prop.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
        // 初始化Velocity引擎，init对引擎VelocityEngine配置了一组默认的参数 
        Velocity.init(prop);

        VelocityEngine ve = new VelocityEngine();
        ve.init();
        
		for(String key:mdaConfig.getTableMap().keySet()) {
			TableEntity tableEntity = mdaConfig.getTableMap().get(key);
            log.info("data before:"+tableEntity);
	        //封装模板数据
	        Map map = tableEntity.toMap();
	        map.put("mda", mdaEntityMap);
	        map.put("usd","$");
	        log.info("dataMap before:"+map);
	        VelocityContext context = new VelocityContext(map);            //渲染模板
	        
	        for(TemplateItemEntity templateItemEntity:templateEntity.getTemplates()) {
	        	String model = templateItemEntity.getModel();
	        	if (!model.equalsIgnoreCase("data"))
	        		continue;
	        	//设置输出  
	        	log.info("before:"+templateItemEntity.getTarget());
	        	StringWriter stringWriter = new StringWriter();
	        	ve.evaluate(context, stringWriter, "", templateItemEntity.getTarget());
	        	String targetFile = stringWriter.toString();
	        	log.info("after:"+targetFile);

	            GenUtils.mkdir(property+"/"+targetFile);  
	            
	        	PrintWriter writer;
				try {
					writer = new PrintWriter(property+"/"+targetFile);
				} catch (FileNotFoundException e) {
					throw new MdaException("文件不存在!",e);
				}  
	        	String template = templateItemEntity.getFile();
	            Template tpl = Velocity.getTemplate(template, "UTF-8" );
	            tpl.merge(context, writer);
	            writer.close();
	        }
		}

		for(String key:mdaConfig.getViewMap().keySet()) {
			ViewEntity viewEntity = mdaConfig.getViewMap().get(key);
			//封装模板数据
			Map map = viewEntity.toMap();
			map.put("mda", mdaEntityMap);
			map.put("usd","$");
			log.info("viewMap:"+map);
			VelocityContext context = new VelocityContext(map);            //渲染模板

			for(TemplateItemEntity templateItemEntity:templateEntity.getTemplates()) {
				String model = templateItemEntity.getModel();
				if (!model.equals(viewEntity.getModel()))
					continue;
				//设置输出
				log.info("before:"+templateItemEntity.getTarget());
				StringWriter stringWriter = new StringWriter();
				ve.evaluate(context, stringWriter, "", templateItemEntity.getTarget());
				String targetFile = stringWriter.toString();
				log.info("after:"+targetFile);

				GenUtils.mkdir(property+"/"+targetFile);

				PrintWriter writer;
				try {
					writer = new PrintWriter(property+"/"+targetFile);
				} catch (FileNotFoundException e) {
					throw new MdaException("文件不存在!",e);
				}
				String template = templateItemEntity.getFile();
				Template tpl = Velocity.getTemplate(template, "UTF-8" );
				tpl.merge(context, writer);
				writer.close();
			}
		}

		//封装模板数据
		Map map = new HashMap();

		map.put("dicts", mdaConfig.getDictionaryMap());
		map.put("mda", mdaEntityMap);
		map.put("usd","$");
		log.info("dictsMap:"+map);
		VelocityContext context = new VelocityContext(map);
		for(TemplateItemEntity templateItemEntity:templateEntity.getTemplates()) {
			String model = templateItemEntity.getModel();
			if (!model.equalsIgnoreCase("dicts"))
				continue;
			//设置输出
			log.info("before:"+templateItemEntity.getTarget());
			StringWriter stringWriter = new StringWriter();
			ve.evaluate(context, stringWriter, "", templateItemEntity.getTarget());
			String targetFile = stringWriter.toString();
			log.info("after:"+targetFile);

			GenUtils.mkdir(property+"/"+targetFile);

			PrintWriter writer;
			try {
				writer = new PrintWriter(property+"/"+targetFile);
			} catch (FileNotFoundException e) {
				throw new MdaException("文件不存在!",e);
			}
			String template = templateItemEntity.getFile();
			Template tpl = Velocity.getTemplate(template, "UTF-8" );
			tpl.merge(context, writer);
			writer.close();
		}

	}
}
