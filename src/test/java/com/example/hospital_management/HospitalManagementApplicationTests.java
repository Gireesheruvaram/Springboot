package com.example.hospital_management;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class HospitalManagementApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	void contextLoads() {
		// Ensures the application context loads successfully
		assertNotNull(applicationContext);  // Verify that the application context is loaded
	}

	@Test
	void main() {
		// Ensure the main method runs without throwing an exception
		assertDoesNotThrow(() -> HospitalManagementApplication.main(new String[]{}));
	}
}
