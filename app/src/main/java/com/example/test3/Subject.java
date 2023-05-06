package com.example.test3;

public class Subject {
    String name;
    String description;
    String teacher_id;
    String subject_id;
    String estimated_lec;

    public Subject(String name, String description, String teacher_id, String subject_id, String estimated_hour) {
        this.name = name;
        this.description = description;
        this.teacher_id = teacher_id;
        this.subject_id = subject_id;
        this.estimated_lec = estimated_hour;
    }

    public String getEstimated_lec() {
        return estimated_lec;
    }

    public void setEstimated_lec(String estimated_lec) {
        this.estimated_lec = estimated_lec;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }
}
