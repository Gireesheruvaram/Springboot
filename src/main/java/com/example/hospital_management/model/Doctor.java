package com.example.hospital_management.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.util.List;

@Entity
@Getter
@Setter
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String specialization;

    // One-to-many relationship with Patient
    @OneToMany(mappedBy = "doctor", fetch = FetchType.EAGER)  // Set FetchType.EAGER if you need patients to be loaded automatically
//    @JsonManagedReference  // To avoid infinite recursion
    @JsonIgnoreProperties("doctor")
    private List<Patient> patients;
}
