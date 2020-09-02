package com.myfood;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import java.math.BigDecimal;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import com.myfood.domain.model.Cozinha;
import com.myfood.domain.model.Restaurante;
import com.myfood.domain.repository.CozinhaRepository;
import com.myfood.domain.repository.RestauranteRepository;
import com.myfood.util.ResourceUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class RestaurantResourceIT {

	private static final String VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE = "Violação de regra de negócio";

	private static final String DADOS_INVALIDOS_PROBLEM_TITLE = "Dados inválidos";

	private static final int RESTAURANTE_ID_INEXISTENTE = 100;

	@LocalServerPort
	private int port;

//	@Autowired
//	private DataBaseCleaner databaseCleaner;

	@Autowired
	private CozinhaRepository cozinhaRepository;

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private Flyway flyway;
	
	private String jsonRestauranteCorreto;
	private String jsonRestauranteSemFrete;
	private String jsonRestauranteSemCozinha;
	private String jsonRestauranteComCozinhaInexistente;

	private Restaurante burgerTopRestaurante;

	@BeforeEach
	public void setUp() {
		
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/restaurantes";
		jsonRestauranteCorreto = ResourceUtils.getContentFromResource("/json/correto/restaurante-new-york-barbecue.json");
		jsonRestauranteSemFrete = ResourceUtils.getContentFromResource("/json/correto/restaurante-new-york-barbecue-sem-frete.json");
		jsonRestauranteSemCozinha = ResourceUtils.getContentFromResource("/json/correto/restaurante-new-york-barbecue-sem-cozinha.json");
		jsonRestauranteComCozinhaInexistente = ResourceUtils.getContentFromResource("/json/correto/restaurante-new-york-barbecue-com-cozinha-inexistente.json");
		//databaseCleaner.clearTables();
		flyway.migrate();
		builderRestaurant();
	}

	@Test
	public void whenConsultRestaurant_thenReturnStatus200() {
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());
	}

	@Test
	public void whenRegisterRestaurant_thenReturnStatus201() {
		given()
			.body(jsonRestauranteCorreto)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
			.when()
				.post()
			.then()
				.statusCode(HttpStatus.OK.value());
	}

	@Test
	public void whenRegisterRestaurantWithoutRateFreight_thenReturnStatus400() {
		given()
			.body(jsonRestauranteSemFrete)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
			.when()
				.post()
			.then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE));
	}

	@Test
	public void whenRegisterRestaurantWithoutKitchen_returnStatus400() {
		given()
			.body(jsonRestauranteSemCozinha)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
			.when()
				.post()
			.then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE));
	}

	@Test
	public void  whenRegisterRestaurantWithKitchenNonexistent_returnStatus400() {
		given()
			.body(jsonRestauranteComCozinhaInexistente)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
			.when()
				.post()
			.then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("title", equalTo(VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE));
	}

	@Test
	public void whenConsultRestaurantNonexistent_returnCorrectStatus() {
		given()
			.pathParam("restauranteId", burgerTopRestaurante.getId())
				.accept(ContentType.JSON)
			.when()
				.get("/{restauranteId}")
			.then()
				.statusCode(HttpStatus.OK.value())
				.body("nome", equalTo(burgerTopRestaurante.getNome()));
	}

	@Test
	public void whenConsultRestaurantNonexistent_returnStatus404() {
		given()
			.pathParam("restauranteId", RESTAURANTE_ID_INEXISTENTE)
				.accept(ContentType.JSON)
			.when()
				.get("/{restauranteId}")
			.then()
				.statusCode(HttpStatus.NOT_FOUND.value());
	}

	private void builderRestaurant() {
		Cozinha cozinhaBrasileira = new Cozinha();
		cozinhaBrasileira.setNome("Brasileira");
		cozinhaRepository.save(cozinhaBrasileira);

		Cozinha cozinhaAmericana = new Cozinha();
		cozinhaAmericana.setNome("Americana");
		cozinhaRepository.save(cozinhaAmericana);

		burgerTopRestaurante = new Restaurante();
		burgerTopRestaurante.setNome("Burger Top");
		burgerTopRestaurante.setTaxaFrete(new BigDecimal(10));
		burgerTopRestaurante.setCozinha(cozinhaAmericana);
		restauranteRepository.save(burgerTopRestaurante);

		Restaurante comidaMineiraRestaurante = new Restaurante();
		comidaMineiraRestaurante.setNome("Comida Mineira");
		comidaMineiraRestaurante.setTaxaFrete(new BigDecimal(10));
		comidaMineiraRestaurante.setCozinha(cozinhaBrasileira);
		restauranteRepository.save(comidaMineiraRestaurante);
	}

}
