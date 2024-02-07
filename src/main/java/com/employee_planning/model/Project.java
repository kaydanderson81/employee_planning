package com.employee_planning.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "projects")
public class Project implements Serializable {
    @Id
    @Column(name = "project_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_number")
    private Long projectNumber;

    @Column(nullable = false, length = 45)
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    @Column(name = "start_date", nullable = false)
    private String startDate;

    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    @Column(name = "end_date", nullable = false)
    private String endDate;

    @Column(name = "project_length_months")
    private double projectLengthInMonths;

    @Column(name = "project_booked_months")
    private double currentBookedMonths;

    @Column(name = "remaining_booked_months")
    private double remainingBookedMonths;

    @Column(name = "number_of_employees")
    private int numberOfEmployees;

    private String comment;

    @Column(name = "archived", nullable = false)
    private boolean archived = false;

    public Project() {}

    public Project(Long id, Long projectNumber, String name, String startDate, String endDate,
                   double projectLengthInMonths, double currentBookedMonths, double remainingBookedMonths,
                   int numberOfEmployees, String comment, boolean archived) {
        this.id = id;
        this.projectNumber = projectNumber;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.projectLengthInMonths = projectLengthInMonths;
        this.currentBookedMonths = currentBookedMonths;
        this.remainingBookedMonths = remainingBookedMonths;
        this.numberOfEmployees = numberOfEmployees;
        this.comment = comment;
        this.archived = archived;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(Long projectNumber) {
        this.projectNumber = projectNumber;
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

    public double getProjectLengthInMonths() {
        return projectLengthInMonths;
    }

    public void setProjectLengthInMonths(double projectLengthInMonths) {
        this.projectLengthInMonths = projectLengthInMonths;
    }

    public double getCurrentBookedMonths() {
        return currentBookedMonths;
    }

    public void setCurrentBookedMonths(double currentBookedMonths) {
        this.currentBookedMonths = currentBookedMonths;
    }

    public double getRemainingBookedMonths() {
        return remainingBookedMonths;
    }

    public void setRemainingBookedMonths(double remainingBookedMonths) {
        this.remainingBookedMonths = remainingBookedMonths;
    }

    public int getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public void setNumberOfEmployees(int numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", projectNumber=" + projectNumber +
                ", name='" + name + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", projectLengthInMonths=" + projectLengthInMonths +
                ", currentBookedMonths=" + currentBookedMonths +
                ", remainingBookedMonths=" + remainingBookedMonths +
                ", numberOfEmployees=" + numberOfEmployees +
                ", comment='" + comment + '\'' +
                ", archived='" + archived + '\'' +
                '}';
    }
}
