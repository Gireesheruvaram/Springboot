package com.example.hospital_management;

import com.example.hospital_management.model.Patient;
import com.example.hospital_management.repository.PatientRepository;
import com.example.hospital_management.service.PatientService;
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

public class PatientServiceTest {

    @InjectMocks
    private PatientService patientService;

    @Mock
    private PatientRepository patientRepository;

    private Patient patient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        patient = new Patient();
        patient.setId(1L);
        patient.setName("John Doe");
        patient.setAilment("Flu");
    }

    @Test
    public void testGetAllPatients() {
        Patient patient2 = new Patient();
        patient2.setId(2L);
        patient2.setName("Jane Doe");
        patient2.setAilment("Headache");

        List<Patient> patients = Arrays.asList(patient, patient2);
        when(patientRepository.findAll()).thenReturn(patients);

        List<Patient> result = patientService.getAllPatients();
        assertEquals(2, result.size());
    }

    @Test
    public void testSavePatient() {
        when(patientRepository.save(patient)).thenReturn(patient);

        Patient savedPatient = patientService.savePatient(patient);
        assertNotNull(savedPatient);
        assertEquals("John Doe", savedPatient.getName());
    }

    @Test
    public void testUpdatePatient() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.save(patient)).thenReturn(patient);

        Patient updatedPatient = patientService.updatePatient(1L, patient);
        assertNotNull(updatedPatient);
        assertEquals("John Doe", updatedPatient.getName());
    }


}
