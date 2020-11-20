package com.example.termplanner;

public class Term {
    private long termId;
    private String name;
    private String startDate;
    private String endDate;
    private int active;

    public Term(){
        this.name = "";
        this.startDate = "";
        this.endDate = "";
        this.active = 0;
    }
    public long getTermId() {
        return termId;
    }

    public void setTermId(long termId) {
        this.termId = termId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
