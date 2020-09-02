
package com.myfood;

import  static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import org.flywaydb.core.Flyway;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import com.myfood.domain.model.Cozinha;
import com.myfood.domain.repository.CozinhaRepository;
import com.myfood.util.DataBaseCleaner;
import com.myfood.util.ResourceUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
class KitchenResourceIT {
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private Flyway flyway;
	
	@Autowired
	private DataBaseCleaner dataBaseCleaner;
	@Autowired
	private CozinhaRepository repository;

	private static Cozinha KITCHEN_AMERICAN;
	private static int COUNT_KITCHEN_REGISTER;
	private static String JSON_CORRECT;
	
	
	@BeforeEach
	public void setUp() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/cozinhas";
		flyway.migrate();
		//dataBaseCleaner.clearTables();
		buildKitchen();
		
		JSON_CORRECT = ResourceUtils.getContentFromResource("/json/correto/cozinha-chinesa.json");
	}
	
	@Test
	public void whenConsultKitchen_thenReturnStatus200() {		
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());		
	}


	@Test
	public void whenConsultKitchen_thenReturn4Kitchen() {		
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("", Matchers.hasSize(COUNT_KITCHEN_REGISTER))	
			.body("nome", Matchers.hasItems("Indiana", "Tailandesa"));
	}
	
	@Test
	public void whenRegisterKitchen_thenReturnStatus201() {
		given()
			.body(JSON_CORRECT)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
		}
	
	
	@Test
	public void whenConsultKitchenExisting_thenReturnStatusCorrect() {		
		
		given()
			.pathParam("cozinhaId", KITCHEN_AMERICAN.getId())
			.accept(ContentType.JSON)
		.when()
			.get("/{cozinhaId}")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("nome", equalTo(KITCHEN_AMERICAN.getNome()));
	}
	

	private void buildKitchen() {
		KITCHEN_AMERICAN = new Cozinha();
		KITCHEN_AMERICAN.setNome("Americana");
		repository.save(KITCHEN_AMERICAN);
		
		var kitchen2 = new Cozinha();
		kitchen2.setNome("Tailandesa");
		repository.save(kitchen2);
		
		var kitchen3 = new Cozinha();
		kitchen3.setNome("Chinesa");
		repository.save(kitchen3);
		
		var kitchen4 = new Cozinha();
		kitchen4.setNome("Indiana");
		repository.save(kitchen4);
		
		COUNT_KITCHEN_REGISTER = (int) repository.count();
	}
}
