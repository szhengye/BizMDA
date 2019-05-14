package com.bizmda.entity;

import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
public class ActionEntity {
    private String label;
    private String type;
    private String icon;
    private Map params;

    public ActionEntity(Map map) {
        this.label = (String)map.get("label");
        this.type = (String)map.get("type");
        this.icon = (String)map.get("icon");
        this.params = (Map)map.get("params");
    }
}
