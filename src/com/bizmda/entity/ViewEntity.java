package com.bizmda.entity;

import com.bizmda.config.MdaConfig;
import com.bizmda.utils.Constant;
import com.bizmda.utils.GenUtils;
import com.bizmda.utils.MdaException;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.java.Log;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
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
    private List<ComponentEntity> components;

    public ViewEntity(Map map) throws MdaException {
        MdaConfig mdaConfig = MdaConfig.getInstance();
        this.name = (String)map.get("name");
        this.className = GenUtils.camelName(this.name);
        this.classname = StringUtils.uncapitalize(this.className);
        this.model = (String)map.get("model");
        this.label = (String)map.get("label");
        this.module = (String)map.get("module");
        List<Map> componentList = (List<Map>)map.get("components");

        this.components = new ArrayList<ComponentEntity>();
        for(Map component:componentList) {
            String dataName = (String)component.get("dataName");
            String type = (String)component.get("type");
            TableEntity tableEntity = mdaConfig.getDataMap().get(dataName);
            Map viewMap = (Map)component.get("view");
            ComponentEntity componentEntity = new ComponentEntity(tableEntity,viewMap);
            componentEntity.setDataName(dataName);
            componentEntity.setType(type);
            List<FieldEntity> fieldEntityList = new ArrayList<FieldEntity>();
            List<Map> fieldList = (List<Map>)component.get("fields");
            for(Map field:fieldList) {
                String fieldName = (String)field.get("name");
                FieldEntity fieldEntity = tableEntity.getFieldByName(fieldName);
                FieldEntity fieldEntity1 = new FieldEntity();
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
            }
            componentEntity.setFields(fieldEntityList);

            List<ActionEntity> actionEntityList = new ArrayList<ActionEntity>();
            List<Map> actionList = (List<Map>)component.get("actions");
            if (actionList != null) {
                for (Map actionMap : actionList) {
                    ActionEntity actionEntity = new ActionEntity(actionMap);
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
        map.put("model",this.model);
        map.put("components", this.components);
        map.put("className", this.className);
        map.put("classname", this.classname);
        return map;
    }
}
