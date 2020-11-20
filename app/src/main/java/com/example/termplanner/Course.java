package com.example.termplanner;

class Course {
    private long courseId;
    private long courseTermId;
    private String courseName;
    private String courseStart;
    private String courseEnd;
    private String courseDescription;
    private int courseActive;
    private String courseMentor;
    private String courseMentorPhone;
    private String courseMentorEmail;
    public Course(){
        this.courseName = " ";
        this.courseStart =" ";
        this.courseEnd = " ";
        this.courseDescription = "";
        this.courseMentor = "";
        this.courseMentorEmail = "";
        this.courseMentorPhone = "";
    }
    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public long getCourseTermId() {
        return courseTermId;
    }

    public void setCourseTermId(long courseTermId) {
        this.courseTermId = courseTermId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseStart(){return courseStart;}

    public void setCourseStart(String courseStart){this.courseStart = courseStart; }

    public String getCourseEnd(){return courseEnd; }

    public void setCourseEnd(String courseEnd){this.courseEnd = courseEnd; }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public int getCourseActive() {
        return courseActive;
    }

    public void setCourseActive(int courseActive) {
        this.courseActive = courseActive;
    }

    public String getCourseMentor() {
        return courseMentor;
    }

    public void setCourseMentor(String courseMentor) {
        this.courseMentor = courseMentor;
    }

    public String getCourseMentorPhone() {
        return courseMentorPhone;
    }

    public void setCourseMentorPhone(String courseMentorPhone) {
        this.courseMentorPhone = courseMentorPhone;
    }

    public String getCourseMentorEmail() {
        return courseMentorEmail;
    }

    public void setCourseMentorEmail(String courseMentorEmail) {
        this.courseMentorEmail = courseMentorEmail;
    }



}
