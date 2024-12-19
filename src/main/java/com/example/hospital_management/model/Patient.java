package com.example.hospital_management.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String ailment;

    // Many-to-one relationship with Doctor
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id")
//    @JsonBackReference  // Prevents infinite recursion during JSON serialization

    private Doctor doctor;
}
//