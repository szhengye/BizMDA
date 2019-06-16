package com.bizmda.entity;

import com.bizmda.config.MdaConfig;
import com.bizmda.utils.GenUtils;
import com.bizmda.utils.MdaException;
import lombok.Data;
import lombok.ToString;
import lombok.extern.java.Log;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Data
@ToString
@Log
public class ViewEntity {
    //表的名称
    private String name;
    //类名(第一个字母大写)，如：sys_user => SysUser
    private String className;
    //类名(第一个字母小写)，如：sys_user => sysUser
    private String classname;
    private String model;
    //表的备注
    private String label;
    private String module;
    private String modulePath;
    private String tableName;
    private TableEntity table;
    private Map view;
    private Set<DictionaryEntity> dicts;

    private List<ComponentEntity> components;

    public ViewEntity(Map map) throws MdaException {
        MdaConfig mdaConfig = MdaConfig.getInstance();
        this.name = (String)map.get("name");
        this.className = GenUtils.camelName(this.name);
        this.classname = StringUtils.uncapitalize(this.className);
        this.model = (String)map.get("model");
        this.label = (String)map.get("label");
        this.view = (Map)map.get("view");
//        this.module = (String)map.get("module");
        this.tableName = (String)map.get("tableName");
        if (tableName != null) {
            this.table = mdaConfig.getTableMap().get(tableName);
            if (this.table == null) {
                throw new MdaException("视图" + this.name + "中组件的数据表" + tableName + "没有定义！");
            }
        }
        List<Map> componentList = (List<Map>)map.get("components");

        this.dicts = new HashSet<DictionaryEntity>();
        this.components = new ArrayList<ComponentEntity>();
        for(Map component:componentList) {
            String type = (String)component.get("type");
            String tableName = (String)component.get("tableName");
            TableEntity tableEntity = null;
            if (tableName == null) {
                tableEntity = this.table;
            }
            else {
                tableEntity = mdaConfig.getTableMap().get(tableName);
                if (tableEntity == null) {
                    throw new MdaException("视图" + this.name + "中组件的数据表" + tableName + "没有定义！");
                }
            }
            Map viewMap = (Map)component.get("view");
            ComponentEntity componentEntity = new ComponentEntity(tableEntity,viewMap);
            componentEntity.setTableName(tableName);
            componentEntity.setType(type);
            List<FieldEntity> fieldEntityList = new ArrayList<FieldEntity>();
            List<Map> fieldList = (List<Map>)component.get("fields");
            if (fieldList == null) {
                fieldList = new ArrayList<Map>();
            }
            for(Map field:fieldList) {
                String fieldName = (String)field.get("name");
                FieldEntity fieldEntity1;
                if (tableEntity == null)
                    fieldEntity1 = new FieldEntity();
                else
                    fieldEntity1 = tableEntity.getFieldByName(fieldName);
                FieldEntity fieldEntity = new FieldEntity(field);
                try {
                    BeanUtils.copyProperties(fieldEntity1,fieldEntity);
                } catch (IllegalAccessException e) {
                    throw new MdaException("视图域（"+tableEntity.getName()+"."+fieldName+"）复制出错！",e);
                } catch (InvocationTargetException e) {
                    throw new MdaException("视图域（"+tableEntity.getName()+"."+fieldName+"）复制出错！",e);
                }
                viewMap = (Map)field.get("view");
                fieldEntity1.setView(viewMap);
                fieldEntityList.add(fieldEntity1);
                if(fieldEntity.getDictName() != null) {
                    DictionaryEntity dictionaryEntity = MdaConfig.getInstance().getDictionaryMap().get(fieldEntity.getDictName());
                    if(dictionaryEntity == null) {
                        throw new MdaException("视图" + this.name + "中组件的数据表" + tableName + "的域"+fieldName+"的字典关联定义有误！");
                    }
                    this.dicts.add(dictionaryEntity);
                }
            }
            componentEntity.setFields(fieldEntityList);

            List<ActionEntity> actionEntityList = new ArrayList<ActionEntity>();
            List<Map> actionList = (List<Map>)component.get("actions");
            if (actionList != null) {
                for (Map actionMap : actionList) {
                    ActionEntity actionEntity = new ActionEntity(actionMap);
                    log.info(actionMap.toString());
                    log.info("action:"+actionEntity.toString());

                    actionEntityList.add(actionEntity);
                }
            }
            componentEntity.setActions(actionEntityList);
            this.components.add(componentEntity);
        }
    }

    public String getClassName() {
        return className;
    }

    public String getClassname() {
        return classname;
    }


    public Map toMap() {
        Map map = new HashMap();
        map.put("name", this.name);
        map.put("label", this.label);
        map.put("module",this.module);
        map.put("modulePath",this.modulePath);
        map.put("model",this.model);
        map.put("components", this.components);
        map.put("table",this.table);
        map.put("className", this.className);
        map.put("classname", this.classname);
        map.put("dicts",this.dicts);
        map.put("view",this.view);
        return map;
    }
}
