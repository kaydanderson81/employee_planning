package com.employee_planning.service.project;

import com.employee_planning.model.CustomProjectDetails;
import com.employee_planning.repository.CustomProjectDetailsRepository;
import com.employee_planning.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomProjectDetailsServiceImpl implements CustomProjectDetailsService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private CustomProjectDetailsRepository customProjectDetailsRepository;



    @Override
    public List<CustomProjectDetails> getAllCustomProjectDetailsByProjectId(Long id) {
        return this.customProjectDetailsRepository.listAllCustomProjectDetailsByProjectId(id);
    }

    @Override
    public void saveCustomProjectDetails(CustomProjectDetails customProjectDetails) {
        this.customProjectDetailsRepository.save(customProjectDetails);
    }

    @Override
    public CustomProjectDetails getCustomProjectDetailsById(Long id) {
        Optional<CustomProjectDetails> optional = customProjectDetailsRepository.findById(id);
        CustomProjectDetails customProjectDetails;
        if (optional.isPresent()) {
            customProjectDetails = optional.get();
        } else {
            throw new RuntimeException("Custom Project Details not found for id :: " + id);
        }
        return customProjectDetails;
    }

    @Override
    public void deleteCustomProjectDetails(Long id) {
        this.customProjectDetailsRepository.deleteById(id);
    }

    @Override
    public void deleteAllCustomProjectDetailsByProjectId(Long id) {
        List<CustomProjectDetails> customProjectDetailsList = getAllCustomProjectDetailsByProjectId(id);
        for (CustomProjectDetails customProjectDetails : customProjectDetailsList) {
            deleteCustomProjectDetails(customProjectDetails.getId());
        }
    }

    @Override
    public boolean checkIfCustomProjectYearExists(Long id, String year) {
        List<CustomProjectDetails> customProjectDetailsList = getAllCustomProjectDetailsByProjectId(id);
        for (CustomProjectDetails customProjectDetail : customProjectDetailsList) {
            if (customProjectDetail.getCustomProjectYear().equals(year)) {
                return true;
            }
        }
        return false;
    }

}
