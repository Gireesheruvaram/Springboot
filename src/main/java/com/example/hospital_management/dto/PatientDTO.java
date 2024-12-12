package com.example.hospital_management.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientDTO {

    private Long id;
    private String name;
    private String ailment;
    private Long doctorId;
}
