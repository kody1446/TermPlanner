package com.example.termplanner;

class Assessment {
    long courseId;
    long assessmentId;
    String description;
    String title;
    int type;
    int state;
    String due;
    public Assessment(){
        this.title = " ";
        this.description = " ";
        this.due = " ";
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public long getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(long assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDue() {
        return due;
    }

    public void setDue(String due) {
        this.due = due;
    }



}
