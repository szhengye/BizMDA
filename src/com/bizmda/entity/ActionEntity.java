package com.bizmda.entity;

import com.bizmda.utils.Constant;
import com.bizmda.utils.GenUtils;
import com.bizmda.utils.MdaException;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@ToString
public class ActionEntity {
    private String label;
    private String action;
    private String type;

    public ActionEntity(Map map) {
        this.label = (String)map.get("label");
        this.action = (String)map.get("action");
        this.type = (String)map.get("type");
    }
}
