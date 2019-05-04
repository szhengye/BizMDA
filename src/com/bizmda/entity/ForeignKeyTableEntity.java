package com.bizmda.entity;

import lombok.Getter;
import lombok.ToString;

@Getter
public class ForeignKeyTableEntity {
    TableEntity table;
    FieldEntity field;

    public ForeignKeyTableEntity(TableEntity tableEntity,FieldEntity fieldEntity) {
        this.table = tableEntity;
        this.field = fieldEntity;
    }
}
