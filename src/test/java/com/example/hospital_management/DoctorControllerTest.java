package com.example.hospital_management;

import com.example.hospital_management.controller.DoctorController;
import com.example.hospital_management.model.Doctor;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class DoctorControllerTest {

    @InjectMocks
    private DoctorController doctorController;

    @Mock
    private DoctorService doctorService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(doctorController).build();
    }

    @Test
    public void testGetAllDoctors() throws Exception {
        Doctor doctor1 = new Doctor();
        doctor1.setId(1L);
        doctor1.setName("Dr. Smith");
        doctor1.setSpecialization("Cardiology");

        Doctor doctor2 = new Doctor();
        doctor2.setId(2L);
        doctor2.setName("Dr. Johnson");
        doctor2.setSpecialization("Neurology");

        List<Doctor> doctors = Arrays.asList(doctor1, doctor2);

        when(doctorService.getAllDoctors()).thenReturn(doctors);

        mockMvc.perform(get("/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Dr. Smith"))
                .andExpect(jsonPath("$[1].specialization").value("Neurology"));
    }

    @Test
    public void testSaveDoctor() throws Exception {
        Doctor doctor = new Doctor();
        doctor.setName("Dr. Adams");
        doctor.setSpecialization("Orthopedics");

        when(doctorService.saveDoctor(any(Doctor.class))).thenReturn(doctor);

        mockMvc.perform(post("/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(doctor)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Dr. Adams"))
                .andExpect(jsonPath("$.specialization").value("Orthopedics"));
    }

    @Test
    public void testUpdateDoctor() throws Exception {
        Doctor doctor = new Doctor();
        doctor.setName("Dr. Brown");
        doctor.setSpecialization("Pediatrics");

        when(doctorService.updateDoctor(eq(1L), any(Doctor.class))).thenReturn(doctor);

        mockMvc.perform(put("/doctors/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(doctor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Dr. Brown"))
                .andExpect(jsonPath("$.specialization").value("Pediatrics"));
    }

    @Test
    public void testDeleteDoctor() throws Exception {
        doNothing().when(doctorService).deleteDoctor(1L);

        mockMvc.perform(delete("/doctors/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
