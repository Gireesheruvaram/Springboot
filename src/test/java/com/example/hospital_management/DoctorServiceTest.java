package com.example.hospital_management;

import com.example.hospital_management.model.Doctor;
import com.example.hospital_management.repository.DoctorRepository;
import com.example.hospital_management.service.DoctorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class DoctorServiceTest {

    @InjectMocks
    private DoctorService doctorService;

    @Mock
    private DoctorRepository doctorRepository;

    private Doctor doctor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("Dr. Smith");
        doctor.setSpecialization("Cardiology");
    }

    @Test
    public void testGetAllDoctors() {
        Doctor doctor2 = new Doctor();
        doctor2.setId(2L);
        doctor2.setName("Dr. Johnson");
        doctor2.setSpecialization("Neurology");

        List<Doctor> doctors = Arrays.asList(doctor, doctor2);
        when(doctorRepository.findAll()).thenReturn(doctors);

        List<Doctor> result = doctorService.getAllDoctors();
        assertEquals(2, result.size());
    }

    @Test
    public void testSaveDoctor() {
        when(doctorRepository.save(doctor)).thenReturn(doctor);

        Doctor savedDoctor = doctorService.saveDoctor(doctor);
        assertNotNull(savedDoctor);
        assertEquals("Dr. Smith", savedDoctor.getName());
    }

    @Test
    public void testUpdateDoctor() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(doctorRepository.save(doctor)).thenReturn(doctor);

        Doctor updatedDoctor = doctorService.updateDoctor(1L, doctor);
        assertNotNull(updatedDoctor);
        assertEquals("Dr. Smith", updatedDoctor.getName());
    }

    @Test
    public void testDeleteDoctor() {
        doNothing().when(doctorRepository).deleteById(1L);

        doctorService.deleteDoctor(1L);
        verify(doctorRepository, times(1)).deleteById(1L);
    }
}
