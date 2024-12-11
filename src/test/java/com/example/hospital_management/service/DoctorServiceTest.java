package com.example.hospital_management.service;

import com.example.hospital_management.model.Doctor;
import com.example.hospital_management.repository.DoctorRepository;
import com.example.hospital_management.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorService doctorService;

    private Doctor doctor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup a mock doctor for testing
        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("Dr. John");
    }

    @Test
    void testGetAllDoctors() {
        // Prepare a list of doctors
        Doctor doctor1 = new Doctor();
        doctor1.setId(1L);
        doctor1.setName("Dr. John");

        Doctor doctor2 = new Doctor();
        doctor2.setId(2L);
        doctor2.setName("Dr. Jane");

        List<Doctor> doctors = Arrays.asList(doctor1, doctor2);

        // Mock the repository call to return the list of doctors
        when(doctorRepository.findAll()).thenReturn(doctors);

        // Call the service method
        List<Doctor> result = doctorService.getAllDoctors();

        // Verify the result
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Dr. John", result.get(0).getName());
        assertEquals("Dr. Jane", result.get(1).getName());

        // Verify that findAll was called on the repository
        verify(doctorRepository).findAll();
    }

    @Test
    void testGetDoctorById() {
        // Mock repository to return a doctor by ID
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        Doctor result = doctorService.getDoctorById(1L);

        assertNotNull(result);
        assertEquals(doctor.getName(), result.getName());
        verify(doctorRepository).findById(1L);
    }

    @Test
    void testSaveDoctor() {
        // Mock repository to save a doctor
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        Doctor result = doctorService.saveDoctor(doctor);

        assertNotNull(result);
        assertEquals(doctor.getName(), result.getName());
        verify(doctorRepository).save(any(Doctor.class));
    }

    @Test
    void testGetDoctorByIdNotFound() {
        // Mock repository to return empty for doctor ID
        when(doctorRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            doctorService.getDoctorById(2L);
        });

        assertEquals("Doctor not found with id: 2", exception.getMessage());
    }

}
