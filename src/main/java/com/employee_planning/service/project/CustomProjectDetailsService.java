package com.employee_planning.service.project;

import com.employee_planning.model.CustomProjectDetails;

import java.util.List;

public interface CustomProjectDetailsService {

    List<CustomProjectDetails> getAllCustomProjectDetailsByProjectId(Long id);
    void saveCustomProjectDetails(CustomProjectDetails customProjectDetails);

    CustomProjectDetails getCustomProjectDetailsById(Long id);

    void deleteCustomProjectDetails(Long id);

    void deleteAllCustomProjectDetailsByProjectId(Long id);

    boolean checkIfCustomProjectYearExists(Long id, String year);
}
