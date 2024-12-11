
package com.example.hospital_management.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.hospital_management.model.Patient;
import com.example.hospital_management.repository.PatientRepository;
import com.example.hospital_management.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    private Patient patient;
    private Patient updatedPatient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize patients
        patient = new Patient();
        patient.setId(1L);
        patient.setName("John Doe");
        patient.setAilment("Cold");

        updatedPatient = new Patient();
        updatedPatient.setName("John Doe Updated");
        updatedPatient.setAilment("Flu");
    }

    // Test savePatient() - happy path
    @Test
    void testSavePatient() {
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        Patient result = patientService.savePatient(patient);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("Cold", result.getAilment());

        verify(patientRepository).save(patient);
    }

    // Test getAllPatients() - happy path
    @Test
    void testGetAllPatients() {
        List<Patient> patients = Arrays.asList(patient);
        when(patientRepository.findAll()).thenReturn(patients);

        List<Patient> result = patientService.getAllPatients();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());

        verify(patientRepository).findAll();
    }

    // Test getPatientById() - happy path
    @Test
    void testGetPatientById() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        Patient result = patientService.getPatientById(1L);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("Cold", result.getAilment());

        verify(patientRepository).findById(1L);
    }

    // Test getPatientById() - patient not found
    @Test
    void testGetPatientByIdNotFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            patientService.getPatientById(1L);
        });

        assertEquals("Patient not found with id: 1", thrown.getMessage());
        verify(patientRepository).findById(1L);
    }

    // Test updatePatient() - happy path
    @Test
    void testUpdatePatient() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Patient result = patientService.updatePatient(1L, updatedPatient);

        assertNotNull(result);
        assertEquals("John Doe Updated", result.getName());
        assertEquals("Flu", result.getAilment());

        verify(patientRepository).findById(1L);
        verify(patientRepository).save(any(Patient.class));
    }

    // Test updatePatient() - patient not found
    @Test
    void testUpdatePatientNotFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            patientService.updatePatient(1L, updatedPatient);
        });

        assertEquals("Patient not found with id: 1", thrown.getMessage());
        verify(patientRepository).findById(1L);
    }

    // Test deletePatient() - happy path
    @Test
    void testDeletePatient() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        patientService.deletePatient(1L);

        verify(patientRepository).findById(1L);
        verify(patientRepository).delete(patient);
    }

    // Test deletePatient() - patient not found
    @Test
    void testDeletePatientNotFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            patientService.deletePatient(1L);
        });

        assertEquals("Patient not found with id: 1", thrown.getMessage());
        verify(patientRepository).findById(1L);
    }
}
