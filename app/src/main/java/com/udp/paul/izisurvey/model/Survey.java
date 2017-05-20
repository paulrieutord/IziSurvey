package com.udp.paul.izisurvey.model;

public class Survey {
    private String name;
    private long createdAt;
    private String organization;
    private String createdBy;
    private long initDate;
    private long finishDate;

    public Survey() {
    }

    public Survey(String name, long createdAt, String organization, String createdBy, long initDate, long finishDate) {
        this.name = name;
        this.createdAt = createdAt;
        this.organization = organization;
        this.createdBy = createdBy;
        this.initDate = initDate;
        this.finishDate = finishDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public long getInitDate() {
        return initDate;
    }

    public void setInitDate(long initDate) {
        this.initDate = initDate;
    }

    public long getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(long finishDate) {
        this.finishDate = finishDate;
    }
}
