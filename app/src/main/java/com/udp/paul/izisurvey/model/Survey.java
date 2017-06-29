package com.udp.paul.izisurvey.model;

public class Survey {
    private String name;
    private String createdAt;
    private String organization;
    private String createdBy;
    private String initDate;
    private String finishDate;

    public Survey() {
    }

    public Survey(String name, String createdAt, String organization, String createdBy, String initDate, String finishDate) {
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
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

    public String getInitDate() {
        return initDate;
    }

    public void setInitDate(String initDate) {
        this.initDate = initDate;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }
}
