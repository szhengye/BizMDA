package com.bizmda.entity;

import java.util.Map;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DictItemEntity {
	private String value;
	private String text;
	
	public DictItemEntity(Map map) {
		this.value = (String)map.get("value");
		this.text = (String)map.get("text");
	}
}
