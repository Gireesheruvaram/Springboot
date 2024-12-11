package com.example.hospital_management.controller;

import com.example.hospital_management.dto.PatientDTO;
import com.example.hospital_management.model.Doctor;
import com.example.hospital_management.model.Patient;
import com.example.hospital_management.service.PatientService;
import com.example.hospital_management.service.DoctorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.hamcrest.Matchers.nullValue;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PatientControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PatientService patientService;

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private PatientController patientController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(patientController).build();
    }

    @Test
    void getAllPatients() throws Exception {
        Patient patientWithDoctor = new Patient();
        patientWithDoctor.setId(1L);
        patientWithDoctor.setName("John Doe");
        patientWithDoctor.setAilment("Fever");
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        patientWithDoctor.setDoctor(doctor);

        Patient patientWithoutDoctor = new Patient();
        patientWithoutDoctor.setId(2L);
        patientWithoutDoctor.setName("Jane Doe");
        patientWithoutDoctor.setAilment("Cold");

        when(patientService.getAllPatients()).thenReturn(Arrays.asList(patientWithDoctor, patientWithoutDoctor));

        mockMvc.perform(get("/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].doctorId").value(1)) // Doctor present
                .andExpect(jsonPath("$[1].doctorId").value(nullValue())); // No doctor
    }


    @Test
    void getPatientById() throws Exception {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setName("John Doe");
        patient.setAilment("Fever");

        when(patientService.getPatientById(1L)).thenReturn(patient);

        mockMvc.perform(get("/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.ailment").value("Fever"));
    }

    @Test
    void savePatient() throws Exception {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setName("Jane Doe");
        patientDTO.setAilment("Headache");
        patientDTO.setDoctorId(1L);

        Patient patient = new Patient();
        patient.setId(2L);
        patient.setName("Jane Doe");
        patient.setAilment("Headache");

        when(patientService.savePatient(any(Patient.class))).thenReturn(patient);

        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(patientDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.ailment").value("Headache"));
    }

    @Test
    void updatePatient() throws Exception {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setName("Jane Doe Updated");
        patientDTO.setAilment("Cold");
        patientDTO.setDoctorId(1L);

        Patient patient = new Patient();
        patient.setId(1L);
        patient.setName("Jane Doe Updated");
        patient.setAilment("Cold");

        when(patientService.updatePatient(eq(1L), any(Patient.class))).thenReturn(patient);

        mockMvc.perform(put("/patients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(patientDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Jane Doe Updated"))
                .andExpect(jsonPath("$.ailment").value("Cold"));
    }
    @Test
    void savePatientWithNullDoctorId() throws Exception {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setName("John Doe");
        patientDTO.setAilment("Cough");
        patientDTO.setDoctorId(null); // Null doctorId

        Patient patient = new Patient();
        patient.setId(3L);
        patient.setName("John Doe");
        patient.setAilment("Cough");
        patient.setDoctor(null); // No doctor assigned

        when(patientService.savePatient(any(Patient.class))).thenReturn(patient);

        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(patientDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.ailment").value("Cough"))
                .andExpect(jsonPath("$.doctorId").value(nullValue())); // Ensure doctorId is null
    }
}
