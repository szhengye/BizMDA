package com.bizmda.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DictionaryEntity {
	private String name;
	private String text;
	private List<DictItemEntity> dictItems;
	
	public DictionaryEntity(Map map) {
		this.name = (String)map.get("name");
		this.text = (String)map.get("text");
		List<Map> itemList = (List<Map>)map.get("items");
		this.dictItems = new ArrayList<DictItemEntity>();
		for(Map item:itemList) {
			this.dictItems.add(new DictItemEntity(item));
		}
	}
	
}
