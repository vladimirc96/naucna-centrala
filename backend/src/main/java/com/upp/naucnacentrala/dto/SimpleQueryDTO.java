package com.upp.naucnacentrala.dto;

public class SimpleQueryDTO {

    private String field;
    private String value;
    private String type;

    public SimpleQueryDTO() {
    }

    public SimpleQueryDTO(String field, String value, String type) {
        this.field = field;
        this.value = value;
        this.type = type;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
