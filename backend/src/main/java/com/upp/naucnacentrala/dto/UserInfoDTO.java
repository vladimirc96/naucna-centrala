package com.upp.naucnacentrala.dto;

public class UserInfoDTO {

    private String username;
    private String role;

    public UserInfoDTO() {
    }

    public UserInfoDTO(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
