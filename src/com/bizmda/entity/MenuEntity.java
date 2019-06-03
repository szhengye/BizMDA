package com.bizmda.entity;

import com.bizmda.utils.GenUtils;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ToString
public class MenuEntity {
    private String name;
    private String modulePath;
    private String module;
    private String title;
    private String model;
    private List<MenuItemEntity> items;

    public MenuEntity(Map map) {
        this.title = (String)map.get("title");
        this.model = (String)map.get("model");
        List<Map> itemList = (List<Map>)map.get("items");
        if(itemList == null) {
            this.items = null;
            return;
        }
        this.items = new ArrayList<MenuItemEntity>();
        for(Map item:itemList) {
            this.items.add(new MenuItemEntity(item));
        }
    }

    public String getClassName() {
        return GenUtils.camelName(this.name);
    }

    public String getClassname() {
        return StringUtils.uncapitalize(GenUtils.camelName(this.name));
    }

    public Map toMap() {
        Map map = new HashMap();
        map.put("name", this.name);
        map.put("className",this.getClassName());
        map.put("classname",this.getClassname());
        map.put("module",this.module);
        map.put("modulePath",this.modulePath);
        map.put("title",this.title);
        map.put("model",this.model);
        map.put("items",this.items);
        return map;
    }
}
