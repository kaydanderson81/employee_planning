package com.employee_planning.model.chart;

public class ChartEmployeeProject {

    private Long id;
    private Long projectId;
    private String projectName;

    private double employeeBookedMonths;

    private boolean confirmBooking;

    private String startDate;

    private int percentage;

    public ChartEmployeeProject() {}

    public ChartEmployeeProject(Long id, Long projectId, String projectName, double employeeBookedMonths,
                                boolean confirmBooking, String startDate, int percentage) {
        this.id = id;
        this.projectId = projectId;
        this.projectName = projectName;
        this.employeeBookedMonths = employeeBookedMonths;
        this.confirmBooking = confirmBooking;
        this.startDate = startDate;
        this.percentage = percentage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public double getEmployeeBookedMonths() {
        return employeeBookedMonths;
    }

    public void setEmployeeBookedMonths(double employeeBookedMonths) {
        this.employeeBookedMonths = employeeBookedMonths;
    }

    public boolean isConfirmBooking() {
        return confirmBooking;
    }

    public void setConfirmBooking(boolean confirmBooking) {
        this.confirmBooking = confirmBooking;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    @Override
    public String toString() {
        return "ChartEmployeeProject{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", projectName='" + projectName + '\'' +
                ", employeeBookedMonths=" + employeeBookedMonths +
                ", confirmBooking=" + confirmBooking +
                ", startDate='" + startDate + '\'' +
                ", percentage=" + percentage +
                '}';
    }
}
