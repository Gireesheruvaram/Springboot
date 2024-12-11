package com.example.hospital_management.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorDTO {
    private Long id;
    private String name;
    private String specialization;
    private List<PatientDTO> patients;


}
