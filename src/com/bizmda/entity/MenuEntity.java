package com.bizmda.entity;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
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
}
