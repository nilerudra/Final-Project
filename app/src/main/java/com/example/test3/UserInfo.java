package com.example.test3;

public class UserInfo {
    public String id = "";
    public String name = "";
    public String phone = "";
    public String email = "";
    public String enr_no = "";
    public String identity = "";   // 0 - Student, 1 - Teacher

    public UserInfo() {
    }

    public UserInfo(String id, String name, String phone, String email, String enr_no, String identity) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.enr_no = enr_no;
        this.identity = identity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEnr_no() {
        return enr_no;
    }

    public void setEnr_no(String gender) {
        this.enr_no = gender;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", enr_no='" + enr_no + '\'' +
                ", identity=" + identity +
                '}';
    }
}
