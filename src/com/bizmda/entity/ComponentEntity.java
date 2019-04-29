package com.bizmda.entity;

import com.bizmda.utils.Constant;
import com.bizmda.utils.GenUtils;
import com.bizmda.utils.MdaException;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ToString
public class ComponentEntity {
	private String type;
	//表的名称
	private String dataName;
	//表的备注
	private String label;
	private String module;
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

	private Map view;

	private List<ActionEntity> actions;

	public ComponentEntity(TableEntity table,Map viewMap) throws MdaException {
		try {
			BeanUtils.copyProperties(this,table);
		} catch (IllegalAccessException e) {
			throw new MdaException("视图属性拷贝出错!",e);
		} catch (InvocationTargetException e) {
			throw new MdaException("视图属性拷贝出错!",e);
		}
		this.view = viewMap;
		this.fields = new ArrayList<FieldEntity>();
	}

	public String getClassName() {
		return className;
	}

	public String getClassname() {
		return StringUtils.uncapitalize(this.className);
	}

//	public Map toMap() {
//		Map map = new HashMap();
//		map.put("name", this.name);
//		map.put("label", this.label);
//		map.put("module",this.module);
//		map.put("primaryKey", this.primaryKey);
//		map.put("primaryKeyType", this.primaryKeyType);
//		map.put("hasStatus", this.hasStatus);
//		map.put("hasCreateUpdate", this.isHasCreateUpdate());
//		map.put("fields", this.fields);
//		map.put("primaryKeyField", this.primaryKeyField);
//		map.put("className", this.className);
//		map.put("classname", this.classname);
//		return map;
//	}
}
