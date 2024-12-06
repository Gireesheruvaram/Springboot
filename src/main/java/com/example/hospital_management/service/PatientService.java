

package com.example.hospital_management.service;

import com.example.hospital_management.model.Patient;
import com.example.hospital_management.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    // Save a new patient
    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    // Get all patients
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    // Get patient by ID
    public Patient getPatientById(Long id) {
        Optional<Patient> patient = patientRepository.findById(id);
        return patient.orElseThrow(() -> new RuntimeException("Patient not found"));
    }


    public Patient updatePatient(Long id, Patient updatedPatient) {

        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // Update the details
        existingPatient.setName(updatedPatient.getName());
        existingPatient.setAilment(updatedPatient.getAilment());
        existingPatient.setDoctor(updatedPatient.getDoctor());


        return patientRepository.save(existingPatient);
    }

}
