package com.bizmda.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bizmda.utils.MdaException;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import com.bizmda.utils.Constant;
import com.bizmda.utils.GenUtils;

import lombok.Getter;
import lombok.ToString;

@Data
@ToString
public class TableEntity {
	//表的名称
	private String name;
	//表的备注
	private String label;
	private String module;
	private String modulePath;
	private String primaryKey;
	private String primaryKeyType;
	private boolean hasStatus;
	private boolean hasCreateUpdate;
	private List<FieldEntity> fields;
	
	//表的主键
	private FieldEntity primaryKeyField;
	
	//类名(第一个字母大写)，如：sys_user => SysUser
	private String className;
	//类名(第一个字母小写)，如：sys_user => sysUser
//	private String classname;
	//外键表
	private List<ForeignKeyTableEntity> foreignKeyTableList;
	
	public TableEntity(Map map) throws MdaException {
		this.name = (String)map.get("name");
		this.className = GenUtils.camelName(this.name);
//		this.classname = StringUtils.uncapitalize(this.className);
		this.label = (String)map.get("label");
//		this.module = (String)map.get("module");
		this.primaryKey = (String)map.get("primaryKey");
		this.primaryKeyType = (String)map.get("primaryKeyType");
		if (this.primaryKeyType == null) 
			this.primaryKeyType = "MANUAL";
		this.hasStatus = Constant.trueStringList.contains(map.get("hasStatus"));
		this.hasCreateUpdate = Constant.trueStringList.contains(map.get("hasCreateUpdate"));
		List<Map> fieldList = (List<Map>)map.get("fields");
		this.fields = new ArrayList<FieldEntity>();
		for(Map field:fieldList) {
			FieldEntity fieldEntity = new FieldEntity(field);
			this.fields.add(fieldEntity);
			if (fieldEntity.getName().equals(this.primaryKey)) 
				this.primaryKeyField = fieldEntity;
		}
		this.foreignKeyTableList = new ArrayList<ForeignKeyTableEntity>();
	}

	public FieldEntity getFieldByName(String fieldName) throws MdaException {
		for(FieldEntity fieldEntity:this.fields) {
			if (fieldEntity.getName().equals(fieldName)) {
				return fieldEntity;
			}
		}
		throw new MdaException("视图域在数据表中没找到:"+this.name+"."+fieldName);
	}

	public String getClassName() {
		return className;
	}

	public String getClassname() {
		return StringUtils.uncapitalize(this.className);
	}

	public void addForeignKeyTableEntity(ForeignKeyTableEntity foreignKeyTableEntity) {
		this.foreignKeyTableList.add(foreignKeyTableEntity);
	}

	public Map toMap() {
		Map map = new HashMap();
		map.put("name", this.name);
		map.put("label", this.label);
		map.put("module",this.module);
		map.put("modulePath",this.modulePath);
		map.put("primaryKey", this.primaryKey);
		map.put("primaryKeyType", this.primaryKeyType);
		map.put("hasStatus", this.hasStatus);
		map.put("hasCreateUpdate", this.isHasCreateUpdate());
		map.put("fields", this.fields);
		map.put("primaryKeyField", this.primaryKeyField);
		map.put("foreignKeyTableList",this.foreignKeyTableList);
		map.put("className", this.className);
		map.put("classname", this.getClassname());
		return map;
	}
}
