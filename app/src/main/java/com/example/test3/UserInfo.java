package com.example.test3;

public class UserInfo {
    public String id = "";
    public String name = "";
    public String phone = "";
    public String email = "";
    public String gender = "";
    public String identity = "";   // 0 - Student, 1 - Teacher

    public UserInfo(String id, String name, String phone, String email, String indentify) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.identity = indentify;
    }

    public UserInfo(String id, String name, String phone, String email, String gender, String indentify) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.identity = indentify;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIndentify(String indentify) {
        this.identity = indentify;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", identity=" + identity +
                '}';
    }
}
