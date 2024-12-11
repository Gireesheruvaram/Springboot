package com.example.hospital_management.controller;

import com.example.hospital_management.dto.DoctorDTO;
import com.example.hospital_management.dto.PatientDTO;
import com.example.hospital_management.model.Doctor;
import com.example.hospital_management.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    // Constructor injection
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // GET: Retrieve all doctors
    @GetMapping
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        List<DoctorDTO> doctorDTOs = doctors.stream()
                .map(doctor -> {
                    DoctorDTO dto = new DoctorDTO();
                    dto.setId(doctor.getId());
                    dto.setName(doctor.getName());
                    dto.setSpecialization(doctor.getSpecialization());

                    // Map patients to PatientDTO, ensure patients list is not null
                    List<PatientDTO> patientDTOs = doctor.getPatients() != null ?
                            doctor.getPatients().stream()
                                    .map(patient -> {
                                        PatientDTO patientDTO = new PatientDTO();
                                        patientDTO.setId(patient.getId());
                                        patientDTO.setName(patient.getName());
                                        patientDTO.setAilment(patient.getAilment());
                                        return patientDTO;
                                    })
                                    .toList() : null;  // Replaced collect(Collectors.toList()) with toList()

                    dto.setPatients(patientDTOs);  // Set patients in the DoctorDTO
                    return dto;
                })
                .toList();  // Replaced collect(Collectors.toList()) with toList()

        return ResponseEntity.ok(doctorDTOs);
    }

    // GET: Retrieve a doctor by ID
    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorService.getDoctorById(id);
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setId(doctor.getId());
        doctorDTO.setName(doctor.getName());
        doctorDTO.setSpecialization(doctor.getSpecialization());

        // Map patients to PatientDTO, ensure patients list is not null
        List<PatientDTO> patientDTOs = doctor.getPatients() != null ?
                doctor.getPatients().stream()
                        .map(patient -> {
                            PatientDTO patientDTO = new PatientDTO();
                            patientDTO.setId(patient.getId());
                            patientDTO.setName(patient.getName());
                            patientDTO.setAilment(patient.getAilment());
                            return patientDTO;
                        })
                        .toList() : null;  // Replaced collect(Collectors.toList()) with toList()

        doctorDTO.setPatients(patientDTOs);  // Set patients in the DoctorDTO
        return ResponseEntity.ok(doctorDTO);
    }

    // POST: Save a new doctor
    @PostMapping
    public ResponseEntity<DoctorDTO> saveDoctor(@RequestBody DoctorDTO doctorDTO) {
        Doctor doctor = new Doctor();
        doctor.setName(doctorDTO.getName());
        doctor.setSpecialization(doctorDTO.getSpecialization());

        // Save doctor using the service
        Doctor savedDoctor = doctorService.saveDoctor(doctor);

        // Create the DoctorDTO response
        DoctorDTO savedDoctorDTO = new DoctorDTO();
        savedDoctorDTO.setId(savedDoctor.getId());
        savedDoctorDTO.setName(savedDoctor.getName());
        savedDoctorDTO.setSpecialization(savedDoctor.getSpecialization());

        return ResponseEntity.status(201).body(savedDoctorDTO);  // Return with CREATED status
    }

    // PUT: Update an existing doctor
    @PutMapping("/{id}")
    public ResponseEntity<DoctorDTO> updateDoctor(@PathVariable Long id, @RequestBody DoctorDTO doctorDTO) {
        Doctor existingDoctor = doctorService.getDoctorById(id);
        if (existingDoctor == null) {
            return ResponseEntity.notFound().build();  // If doctor does not exist
        }

        existingDoctor.setName(doctorDTO.getName());
        existingDoctor.setSpecialization(doctorDTO.getSpecialization());

        // Save the updated doctor using the service
        Doctor updatedDoctor = doctorService.saveDoctor(existingDoctor);

        DoctorDTO updatedDoctorDTO = new DoctorDTO();
        updatedDoctorDTO.setId(updatedDoctor.getId());
        updatedDoctorDTO.setName(updatedDoctor.getName());
        updatedDoctorDTO.setSpecialization(updatedDoctor.getSpecialization());

        return ResponseEntity.ok(updatedDoctorDTO);  // Return with OK status
    }

    // DELETE: Delete a doctor by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        boolean isDeleted = doctorService.deleteDoctor(id);
        if (!isDeleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Return 404 if doctor not found
        }
        return ResponseEntity.noContent().build();  // Return 204 if delete is successful
    }
}
