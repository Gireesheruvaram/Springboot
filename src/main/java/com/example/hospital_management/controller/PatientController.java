package com.example.hospital_management.controller;

import com.example.hospital_management.dto.PatientDTO;
import com.example.hospital_management.model.Doctor;
import com.example.hospital_management.model.Patient;
import com.example.hospital_management.service.DoctorService;
import com.example.hospital_management.service.PatientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;
    private final DoctorService doctorService;

    // Constructor injection
    public PatientController(PatientService patientService, DoctorService doctorService) {
        this.patientService = patientService;
        this.doctorService = doctorService;
    }

    // POST: Save a new Patient
    @PostMapping
    public PatientDTO savePatient(@RequestBody PatientDTO patientDTO) {
        Patient patient = convertToEntity(patientDTO);
        Patient savedPatient = patientService.savePatient(patient);
        return convertToDTO(savedPatient);
    }

    // GET: Retrieve all patients
    @GetMapping
    public List<PatientDTO> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        return patients.stream()
                .map(this::convertToDTO)
                .toList(); // Replaced collect(Collectors.toList()) with toList()
    }

    // GET: Retrieve a patient by ID
    @GetMapping("/{id}")
    public PatientDTO getPatientById(@PathVariable Long id) {
        Patient patient = patientService.getPatientById(id);
        return convertToDTO(patient);
    }

    // PUT: Update a patient's details
    @PutMapping("/{id}")
    public PatientDTO updatePatient(@PathVariable Long id, @RequestBody PatientDTO updatedPatientDTO) {
        Patient updatedPatient = convertToEntity(updatedPatientDTO);
        Patient savedPatient = patientService.updatePatient(id, updatedPatient);
        return convertToDTO(savedPatient);
    }

    // Helper: Convert Patient entity to PatientDTO
    private PatientDTO convertToDTO(Patient patient) {
        PatientDTO dto = new PatientDTO();
        dto.setId(patient.getId());
        dto.setName(patient.getName());
        dto.setAilment(patient.getAilment());
        dto.setDoctorId(patient.getDoctor() != null ? patient.getDoctor().getId() : null);
        return dto;
    }

    // Helper: Convert PatientDTO to Patient entity
    private Patient convertToEntity(PatientDTO dto) {
        Patient patient = new Patient();
        patient.setId(dto.getId());
        patient.setName(dto.getName());
        patient.setAilment(dto.getAilment());
        if (dto.getDoctorId() != null) {
            Doctor doctor = doctorService.getDoctorById(dto.getDoctorId());
            patient.setDoctor(doctor);
        }
        return patient;
    }

}
