package com.bizmda.entity;

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

/**
 * 列的属性
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年12月20日 上午12:01:45
 */
@Data
@ToString
public class FieldEntity {
    private String name;
    private String label;
    private String type;
    private String showType;
    private String dictName;
    private boolean isNull;
    private String className;
//    private String classname;
    private String javaType;
    private int len;
    private Map view;

    public  FieldEntity() {
        super();
    }

    public FieldEntity(Map map) throws MdaException {
    	this.name = (String)map.get("name");
		this.className = GenUtils.camelName(this.name);
//		this.classname = StringUtils.uncapitalize(this.className);
    	this.label = (String)map.get("label");
    	this.type = (String)map.get("type");
//        int,float,decimal(19,2)-money,timestamp,tinyint-boolean,varchar(100),char(10),blob
        if (this.type.equalsIgnoreCase("int")) {
            this.javaType = "int";
            this.len = 4;
        }
        else if (this.type.equalsIgnoreCase("money") || this.type.startsWith("decimal(")) {
            this.javaType = "BigDecimal";
            this.len = 8;
        }
        else if (this.type.equalsIgnoreCase("date")) {
            this.javaType = "Date";
            this.len = 10;
        }
        else if (this.type.equalsIgnoreCase("datetime")) {
            this.javaType = "Date";
            this.len = 19;
        }
        else if (this.type.equalsIgnoreCase("boolean")) {
            this.javaType = "boolean";
            this.len = 2;
        }
        else if (this.type.equalsIgnoreCase("blob")) {
            this.javaType = "byte[]";
            this.len = 256;
        }
        else if (this.type.startsWith("varchar(") || this.type.startsWith("char(")) {
            this.javaType = "String";
            this.len = Integer.parseInt(this.type.substring(this.type.indexOf('(')+1,this.type.lastIndexOf(')')));
        }
        else {
            throw new MdaException("域["+this.name+"]类型出错:"+this.type);
        }
    	this.showType =  (String)map.get("showType");
    	this.dictName = (String)map.get("dictName");
    	if (map.get("isNull") == null) {
    	    this.isNull = true;
        }
    	else {
            this.isNull = (Boolean)map.get("isNull");
        }
    }

    public String getClassName() {
        return className;
    }

    public String getClassname() {
        return StringUtils.uncapitalize(this.className);
    }

}
