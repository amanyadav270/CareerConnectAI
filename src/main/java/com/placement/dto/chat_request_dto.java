package com.placement.dto;

public class chat_request_dto {
    private String student_id;
    private String drive_id; 
    private String message;

    public String get_student_id() { return student_id; }
    public void set_student_id(String student_id) { this.student_id = student_id; }

    public String get_drive_id() { return drive_id; }
    public void set_drive_id(String drive_id) { this.drive_id = drive_id; }

    public String get_message() { return message; }
    public void set_message(String message) { this.message = message; }
}