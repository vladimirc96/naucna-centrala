package com.upp.naucnacentrala.dto;

public class RegistrationResponseDTO {

    private String message;

    public RegistrationResponseDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
