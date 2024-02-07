package com.employee_planning.model.chart;

import java.util.List;

public class ChartEmployee {

    private Long id;
    private String name;

    private List<ChartEmployeeProject> chartEmployeeProjects;

    public ChartEmployee() {}

    public ChartEmployee(Long id, String name, List<ChartEmployeeProject> chartEmployeeProjects) {
        this.id = id;
        this.name = name;
        this.chartEmployeeProjects = chartEmployeeProjects;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ChartEmployeeProject> getChartEmployeeProjects() {
        return chartEmployeeProjects;
    }

    public void setChartEmployeeProjects(List<ChartEmployeeProject> chartEmployeeProjects) {
        this.chartEmployeeProjects = chartEmployeeProjects;
    }

    @Override
    public String toString() {
        return "ChartEmployee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", chartEmployeeProjects=" + chartEmployeeProjects +
                '}';
    }
}
