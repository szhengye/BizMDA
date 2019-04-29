package com.bizmda.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import com.bizmda.entity.TemplateEntity;
import org.yaml.snakeyaml.Yaml;

import com.bizmda.utils.MdaException;

import lombok.Getter;
import lombok.extern.java.Log;

@Getter
@Log
public class TemplateConfig {
	private TemplateEntity templateEntity;
	
    private static TemplateConfig instance = new TemplateConfig(); 
    private TemplateConfig(){} 
    public static TemplateConfig getInstance(){  
         return instance;  
     } 
    
    
	public void load(String configPath) throws MdaException {
		Map map = null;
		Yaml yaml = new Yaml();

		try {
			map = (Map)yaml.load(new FileInputStream(new File(configPath+"/index.yml")));
//			log.info(map.toString());
			this.templateEntity = new TemplateEntity(map);
//			log.info(this.templateEntity.toString());
		} catch (FileNotFoundException e1) {
			throw new MdaException("model/mda.yml文件不存在！",e1);
		}

	}
}
