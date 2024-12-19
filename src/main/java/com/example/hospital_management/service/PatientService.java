package com.example.hospital_management.service;

import com.example.hospital_management.model.Patient;
import com.example.hospital_management.repository.PatientRepository;
import com.example.hospital_management.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    // Constant for the error message
    private static final String PATIENT_NOT_FOUND_MESSAGE = "Patient not found with id: ";

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    // Save a new Patient
    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    // Retrieve all Patients
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    // Retrieve a Patient by ID
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PATIENT_NOT_FOUND_MESSAGE + id));
    }

    // Update an existing Patient
    public Patient updatePatient(Long id, Patient updatedPatient) {
        // Fetch the existing patient
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PATIENT_NOT_FOUND_MESSAGE + id));
        // Update fields
        existingPatient.setName(updatedPatient.getName());
        existingPatient.setAilment(updatedPatient.getAilment());
        existingPatient.setDoctor(updatedPatient.getDoctor());
        // Save the updated patient
        return patientRepository.save(existingPatient);
    }

    // Delete a Patient by ID
    public boolean deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PATIENT_NOT_FOUND_MESSAGE + id));
        patientRepository.delete(patient);
        return false;
    }
}
