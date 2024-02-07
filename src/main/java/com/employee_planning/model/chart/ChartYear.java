package com.employee_planning.model.chart;

public class ChartYear {
    private String year;

    public ChartYear(String year) {
        this.year = year;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "ChartYear{" +
                "year='" + year + '\'' +
                '}';
    }
}
