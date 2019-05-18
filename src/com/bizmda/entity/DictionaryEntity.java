package com.bizmda.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bizmda.utils.GenUtils;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@Data
@ToString
public class DictionaryEntity {
	private String name;
	private String text;
	private String modulePath;
	private String module;
	private List<DictItemEntity> dictItems;
	
	public DictionaryEntity(Map map) {
		this.name = (String)map.get("name");
		this.text = (String)map.get("text");
		List<Map> itemList = (List<Map>)map.get("items");
		if(itemList == null) {
			this.dictItems = null;
			return;
		}
		this.dictItems = new ArrayList<DictItemEntity>();
		for(Map item:itemList) {
			this.dictItems.add(new DictItemEntity(item));
		}
	}

	public String getClassName() {
		return GenUtils.camelName(this.name);
	}

	public String getClassname() {
		return StringUtils.uncapitalize(GenUtils.camelName(this.name));
	}
}
