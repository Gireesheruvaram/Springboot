package com.example.hospital_management.service;



import com.example.hospital_management.model.Doctor;
import com.example.hospital_management.repository.DoctorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }


    public Doctor updateDoctor(Long id, Doctor updatedDoctor) {
        // Find the doctor by ID
        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));


        existingDoctor.setName(updatedDoctor.getName());
        existingDoctor.setSpecialization(updatedDoctor.getSpecialization());


        return doctorRepository.save(existingDoctor);
    }


    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }
}
