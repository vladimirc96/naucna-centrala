package com.upp.naucnacentrala.dto;

import java.util.List;

public class BooleanQueryDTO {

    private List<SimpleQueryDTO> simpleQueryDTOList;
    private String operation;

    public BooleanQueryDTO() {
    }

    public BooleanQueryDTO(List<SimpleQueryDTO> simpleQueryDTOList, String operation) {
        this.simpleQueryDTOList = simpleQueryDTOList;
        this.operation = operation;
    }

    public List<SimpleQueryDTO> getSimpleQueryDTOList() {
        return simpleQueryDTOList;
    }

    public void setSimpleQueryDTOList(List<SimpleQueryDTO> simpleQueryDTOList) {
        this.simpleQueryDTOList = simpleQueryDTOList;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
