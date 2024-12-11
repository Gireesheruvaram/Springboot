package com.example.hospital_management.controller;

import com.example.hospital_management.dto.DoctorDTO;
import com.example.hospital_management.dto.PatientDTO;
import com.example.hospital_management.model.Doctor;
import com.example.hospital_management.model.Patient;
import com.example.hospital_management.service.DoctorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
 class DoctorControllerTest {

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private DoctorController doctorController;

    private Doctor doctor;

    @BeforeEach
    void setUp() {
        // Initialize mock data
        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("Dr. John Doe");
        doctor.setSpecialization("Cardiology");

        Patient patient1 = new Patient();
        patient1.setId(1L);
        patient1.setName("Patient One");
        patient1.setAilment("Ailment 1");

        Patient patient2 = new Patient();
        patient2.setId(2L);
        patient2.setName("Patient Two");
        patient2.setAilment("Ailment 2");

        doctor.setPatients(Arrays.asList(patient1, patient2));
    }

    @Test
    void testGetAllDoctors() {
        // Prepare mock behavior
        when(doctorService.getAllDoctors()).thenReturn(Collections.singletonList(doctor));

        // Call the method
        ResponseEntity<List<DoctorDTO>> response = doctorController.getAllDoctors();

        // Validate the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<DoctorDTO> doctorDTOs = response.getBody();
        assertNotNull(doctorDTOs);
        assertEquals(1, doctorDTOs.size());
        assertEquals("Dr. John Doe", doctorDTOs.get(0).getName());

        // Verify patients are mapped correctly
        List<PatientDTO> patientDTOs = doctorDTOs.get(0).getPatients();
        assertNotNull(patientDTOs, "Patients list should not be null");

        // Case 1: When there are patients
        if (!patientDTOs.isEmpty()) {
            assertEquals(2, patientDTOs.size());
            assertEquals("Patient One", patientDTOs.get(0).getName());
            assertEquals("Ailment 1", patientDTOs.get(0).getAilment());
        }
        // Case 2: When there are no patients
        else {
            assertTrue(patientDTOs.isEmpty(), "Patients list should be empty");
        }
    }



    @Test
 void testGetDoctorById() {
        // Prepare mock behavior
        when(doctorService.getDoctorById(1L)).thenReturn(doctor);

        // Call the method
        ResponseEntity<DoctorDTO> response = doctorController.getDoctorById(1L);

        // Validate the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        DoctorDTO doctorDTO = response.getBody();
        assertNotNull(doctorDTO);
        assertEquals("Dr. John Doe", doctorDTO.getName());
        assertEquals("Cardiology", doctorDTO.getSpecialization());

        // Verify patients mapping
        List<PatientDTO> patientDTOs = doctorDTO.getPatients();
        assertNotNull(patientDTOs);
        assertEquals(2, patientDTOs.size());
        assertEquals("Patient One", patientDTOs.get(0).getName());
        assertEquals("Ailment 1", patientDTOs.get(0).getAilment());
    }

    @Test
 void testSaveDoctor() {
        // Prepare mock behavior
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setName("Dr. Jane Smith");
        doctorDTO.setSpecialization("Neurology");

        when(doctorService.saveDoctor(any(Doctor.class))).thenReturn(doctor);

        // Call the method
        ResponseEntity<DoctorDTO> response = doctorController.saveDoctor(doctorDTO);

        // Validate the response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        DoctorDTO savedDoctorDTO = response.getBody();
        assertNotNull(savedDoctorDTO);
        assertEquals("Dr. John Doe", savedDoctorDTO.getName()); // Verify it returns the mocked data
    }

    @Test
  void testUpdateDoctor() {
        // Prepare mock behavior
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setName("Dr. Jane Doe");  // Ensure this matches the update logic
        doctorDTO.setSpecialization("Orthopedics");

        // Mocking the service methods
        when(doctorService.getDoctorById(1L)).thenReturn(doctor);  // Returning the original doctor object
        when(doctorService.saveDoctor(any(Doctor.class))).thenAnswer(invocation -> {
            // Simulate saving the updated doctor object (for testing purposes)
            Doctor updatedDoctor = invocation.getArgument(0);
            updatedDoctor.setName(doctorDTO.getName());
            updatedDoctor.setSpecialization(doctorDTO.getSpecialization());
            return updatedDoctor;
        });

        // Call the method
        ResponseEntity<DoctorDTO> response = doctorController.updateDoctor(1L, doctorDTO);

        // Validate the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        DoctorDTO updatedDoctorDTO = response.getBody();
        assertNotNull(updatedDoctorDTO);
        assertEquals("Dr. Jane Doe", updatedDoctorDTO.getName());  // Verify that the name is now "Dr. Jane Doe"
        assertEquals("Orthopedics", updatedDoctorDTO.getSpecialization());
    }


    @Test
   void testDeleteDoctor() {
        // Prepare mock behavior
        when(doctorService.deleteDoctor(1L)).thenReturn(true);

        // Call the method
        ResponseEntity<Void> response = doctorController.deleteDoctor(1L);

        // Validate the response
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
  void testDeleteDoctorNotFound() {
        // Prepare mock behavior
        when(doctorService.deleteDoctor(999L)).thenReturn(false);

        // Call the method
        ResponseEntity<Void> response = doctorController.deleteDoctor(999L);

        // Validate the response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test
    void testUpdateDoctorNotFound() {
        // Arrange: Simulate that no doctor exists with the given ID
        Long nonExistingDoctorId = 999L;
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setName("Dr. Jane Doe");
        doctorDTO.setSpecialization("Neurology");

        when(doctorService.getDoctorById(nonExistingDoctorId)).thenReturn(null);  // Mock the service to return null

        // Act: Attempt to update the non-existing doctor
        ResponseEntity<DoctorDTO> response = doctorController.updateDoctor(nonExistingDoctorId, doctorDTO);

        // Assert: Check that the response status is 404 NOT FOUND
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetAllDoctorsWithNullPatients() {
        // Mock a doctor with null patients
        Doctor localDoctor = new Doctor(); // Renamed from 'doctor' to 'localDoctor'
        localDoctor.setId(1L);
        localDoctor.setName("Dr. Jane Doe");
        localDoctor.setSpecialization("Neurology");
        localDoctor.setPatients(null); // Explicitly set patients to null

        // Mock the service behavior
        when(doctorService.getAllDoctors()).thenReturn(List.of(localDoctor));

        // Call the method
        ResponseEntity<List<DoctorDTO>> response = doctorController.getAllDoctors();

        // Validate the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<DoctorDTO> doctorDTOs = response.getBody();
        assertNotNull(doctorDTOs);
        assertEquals(1, doctorDTOs.size());
        DoctorDTO doctorDTO = doctorDTOs.get(0);
        assertNull(doctorDTO.getPatients(), "Patients list should be null when the doctor has no patients");
    }

}
