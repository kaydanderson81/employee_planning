package com.employee_planning.model;

import javax.persistence.*;

@Entity
@Table(name = "custom_project_details")
public class CustomProjectDetails {
    @Id
    @Column(name = "custom_project_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "custom_project_year")
    private String customProjectYear;

    @Column(name = "custom_project_months")
    private double customProjectPersonMonths;

    public CustomProjectDetails(){}

    public CustomProjectDetails(Long id, Project project, String customProjectYear, double customProjectPersonMonths) {
        this.id = id;
        this.project = project;
        this.customProjectYear = customProjectYear;
        this.customProjectPersonMonths = customProjectPersonMonths;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getCustomProjectYear() {
        return customProjectYear;
    }

    public void setCustomProjectYear(String customProjectYear) {
        this.customProjectYear = customProjectYear;
    }

    public double getCustomProjectPersonMonths() {
        return customProjectPersonMonths;
    }

    public void setCustomProjectPersonMonths(double customProjectPersonMonths) {
        this.customProjectPersonMonths = customProjectPersonMonths;
    }

    @Override
    public String toString() {
        return "CustomProjectDetails{" +
                "id=" + id +
                ", project=" + project +
                ", customProjectYear='" + customProjectYear + '\'' +
                ", customProjectPersonMonths=" + customProjectPersonMonths +
                '}';
    }
}
