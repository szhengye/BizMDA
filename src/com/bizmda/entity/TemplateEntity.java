package com.bizmda.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.java.Log;

@Log
@Getter
@ToString
public class TemplateEntity {
	private String name;
	private List<TemplateItemEntity> templates;
	public TemplateEntity(Map map) {
		this.name = (String)map.get("name");
		List<Map> list = (List<Map>)map.get("templates");

		this.templates = new ArrayList<TemplateItemEntity>();
		for(Map template:list) {
			TemplateItemEntity templateItemEntity = new TemplateItemEntity(template);
			this.templates.add(templateItemEntity);
		}
	}
}
