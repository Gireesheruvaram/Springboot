package com.example.hospital_management.exception;

import com.example.hospital_management.controller.PatientController;
import com.example.hospital_management.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(patientController)
                .setControllerAdvice(new GlobalExceptionHandler())  // Attach the exception handler
                .build();
    }

    @Test
    void testEntityNotFoundException() throws Exception {
        when(patientService.getPatientById(1L)).thenThrow(new EntityNotFoundException("Patient not found"));

        mockMvc.perform(get("/patients/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Patient not found"));
    }

    @Test
    void testGeneralException() throws Exception {
        // Instead of throwing java.lang.Exception, use RuntimeException
        when(patientService.getPatientById(1L)).thenThrow(new RuntimeException("General error"));

        mockMvc.perform(get("/patients/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("General error"));
    }

}
