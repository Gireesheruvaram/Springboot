package com.example.hospital_management.service;

import com.example.hospital_management.model.Doctor;
import com.example.hospital_management.repository.DoctorRepository;
import com.example.hospital_management.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    // Constructor injection
    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    // Retrieve all doctors
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    // Retrieve a doctor by ID
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: " + id));
    }

    // Save a new doctor
    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    // Update an existing doctor
    public Doctor updateDoctor(Long id, Doctor doctorDetails) {
        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: " + id));

        existingDoctor.setName(doctorDetails.getName());
        existingDoctor.setSpecialization(doctorDetails.getSpecialization());
        // You can also update other fields here

        return doctorRepository.save(existingDoctor);
    }

    // Delete a doctor
    public boolean deleteDoctor(Long id) {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        if (doctor.isPresent()) {
            doctorRepository.delete(doctor.get());
            return true;
        }
        return false;
    }
}
