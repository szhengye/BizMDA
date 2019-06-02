package com.bizmda.entity;

import lombok.Data;
import lombok.ToString;
import java.util.Map;

@Data
@ToString
public class MenuItemEntity {
    private String label;
    private String icon;
    private String action;
    private Map params;

    public MenuItemEntity(Map map) {
        this.label = (String)map.get("label");
        this.icon = (String)map.get("icon");
        this.action = (String)map.get("action");
        this.params = (Map)map.get("params");
    }
}
