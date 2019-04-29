package com.bizmda.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bizmda.utils.DateUtils;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MdaEntity {
	private String author;
	private String email;
	private String version;
	private String packageName;
	private String packagePath;
	private String sourcePath;
	private String resourcePath;
	private String template;

	public MdaEntity(Map map) {
		this.author = (String)map.get("author");
		this.email = (String)map.get("email");
		this.version = (String)map.get("version");
		this.packageName = (String)map.get("packageName");
		this.packagePath = this.packageName.replace('.', '/');
		this.sourcePath = (String)map.get("sourcePath");
		this.resourcePath = (String)map.get("resourcePath");
		this.template = (String)map.get("template");
	}
	
	public Map toMap() {
		Map map = new HashMap();
		map.put("author", this.author);
		map.put("email", this.email);
		map.put("version",this.version);
		map.put("packageName", this.packageName);
		map.put("packagePath", this.packagePath);
		map.put("sourcePath", this.sourcePath);
		map.put("resourcePath", this.resourcePath);
		map.put("template", this.template);
		map.put("datetime",DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
		return map;
	}
	
}
