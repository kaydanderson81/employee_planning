package com.employee_planning.model.chart;

import com.employee_planning.EmployeeProjectTestBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChartYearTest extends EmployeeProjectTestBase {

    @Test
    void getYear() {
        String year = "2023";
        String retrievedYear = this.chartYear.getYear();
        assertNotNull(retrievedYear);
        assertEquals(year, retrievedYear);
    }

    @Test
    void setYear() {
        String setYear = "2000";
        this.chartYear.setYear(setYear);
        assertEquals(setYear, this.chartYear.getYear());
    }

    @Test
    void testToString() {
        String retrievedYear = this.chartYear.toString();
        String expectedString = "ChartYear{" +
                "year='" + chartYear.getYear() + '\'' +
                '}';
        assertEquals(retrievedYear, expectedString);
    }
}