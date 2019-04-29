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
public class TemplateItemEntity {
	private String file;
	private String model;
	private String target;
	public TemplateItemEntity(Map map) {
		this.file = (String)map.get("file");
		this.model = (String)map.get("model");
		this.target = (String)map.get("target");
	}
}
