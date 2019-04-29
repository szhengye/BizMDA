package com.bizmda;

import com.bizmda.config.MdaConfig;
import com.bizmda.config.TemplateConfig;
import com.bizmda.generator.DataGenerator;
import com.bizmda.utils.MdaException;

import lombok.extern.java.Log;

@Log
public class CodeGen {

	public static void main(String[] args) {
		String property =System.getProperty("user.dir");

		MdaConfig mdaConfig = MdaConfig.getInstance();
		try {
			mdaConfig.load(property+"/model");
		} catch (MdaException e) {
			e.printStackTrace();
			return;
		}

		String template = mdaConfig.getMdaEntity().getTemplate();
		TemplateConfig templateConfig = TemplateConfig.getInstance();
		try {
			templateConfig.load(property+"/template/"+template);
		} catch (MdaException e) {
			e.printStackTrace();
			return;
		}

		DataGenerator dataGenerator = new DataGenerator();
		try {
			dataGenerator.gen();
		} catch (MdaException e) {
			e.printStackTrace();
		}
	}


}
