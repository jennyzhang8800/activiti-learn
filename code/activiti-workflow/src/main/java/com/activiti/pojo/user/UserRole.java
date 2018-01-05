package com.activiti.pojo.user;

import java.io.Serializable;

/**
 * 用户角色
 */
public class UserRole implements Serializable {
    private static final long serialVersionUID = 2120869894112984147L;

    private int id;
    private String email;
    private String remarks;  //备注
    private String role;

    public UserRole() {
    }

    public UserRole(int id, String email, String remarks) {
        this.id = id;
        this.email = email;
        this.remarks = remarks;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
