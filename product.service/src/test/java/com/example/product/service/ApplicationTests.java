package com.example.product.service;

import io.restassured.RestAssured;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;

import org.testcontainers.containers.MySQLContainer;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTests {
      @ServiceConnection
     static MySQLContainer mySQLContainer = new MySQLContainer<>("mysql:8.0");
	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	static {
		mySQLContainer.start();
	}


	@Test
	void shouldCreateProduct() {

		String requestBody = """
				{
				       "id": 1,
				       "name": "i phone 15pro max",
				       "description": "Need to buy it",
				       "price": 15000.0
				   }""";

		RestAssured.given().
				contentType("application/json")
				.body(requestBody)
				.when()
				.post("/api/product")
				.then()
				.statusCode(200)
				.body("id", Matchers.notNullValue())
				.body("name", Matchers.equalTo("i phone 15pro max"))
				.body("description", Matchers.equalTo("Need to buy it"))
				.body("price",Matchers.equalTo(15000.0f));
	}

}
