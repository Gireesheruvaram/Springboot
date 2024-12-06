package com.example.hospital_management;

import com.example.hospital_management.controller.PatientController;
import com.example.hospital_management.model.Patient;
import com.example.hospital_management.service.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PatientControllerTest {

    @InjectMocks
    private PatientController patientController;

    @Mock
    private PatientService patientService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(patientController).build();
    }

    @Test
    public void testGetAllPatients() throws Exception {
        Patient patient1 = new Patient();
        patient1.setId(1L);
        patient1.setName("John Doe");
        patient1.setAilment("Flu");

        Patient patient2 = new Patient();
        patient2.setId(2L);
        patient2.setName("Jane Doe");
        patient2.setAilment("Headache");

        List<Patient> patients = Arrays.asList(patient1, patient2);

        when(patientService.getAllPatients()).thenReturn(patients);

        mockMvc.perform(get("/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].ailment").value("Headache"));
    }

    @Test
    public void testSavePatient() throws Exception {
        Patient patient = new Patient();
        patient.setName("Alice");
        patient.setAilment("Cold");

        when(patientService.savePatient(any(Patient.class))).thenReturn(patient);

        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(patient)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.ailment").value("Cold"));
    }

    @Test
    public void testGetPatientById() throws Exception {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setName("John Doe");
        patient.setAilment("Flu");

        when(patientService.getPatientById(1L)).thenReturn(patient);

        mockMvc.perform(get("/patients/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.ailment").value("Flu"));
    }

    @Test
    public void testUpdatePatient() throws Exception {
        Patient patient = new Patient();
        patient.setName("Jane Smith");
        patient.setAilment("Migraine");

        when(patientService.updatePatient(eq(1L), any(Patient.class))).thenReturn(patient);

        mockMvc.perform(put("/patients/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(patient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane Smith"))
                .andExpect(jsonPath("$.ailment").value("Migraine"));
    }
}
