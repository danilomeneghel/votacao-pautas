package app.controller;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;

import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;

@QuarkusTestResource(H2DatabaseTestResource.class)
public class SessoesControllerTest {
    
    @Test
    public void testAdd() {
        given()
            .body("{\"nome\": \"Sessão Nova\", \"duracao\": \"1\", \"data_inicio_sessao\": \"2020-01-01\", \"data_fim_sessao\": \"2020-02-02\", \"status\": \"ABERTO\"}")
            .header("Content-Type", MediaType.APPLICATION_JSON)
        .when()
            .post("/sessoes/new")
        .then()
            .statusCode(200)
            .body("$.size()", is(1));
    }
    
    @Test
    public void testList() {
        given()
          .when()
             .get("/sessoes/list")
          .then()
             .statusCode(200)
             .body("$.size()", is(1),
                     "nome", containsInAnyOrder("Sessão Nova"),
                     "duracao", containsInAnyOrder("1"),
                     "data_inicio_sessao", containsInAnyOrder("2020-01-01"),
                     "data_fim_sessao", containsInAnyOrder("2020-02-02"),
                     "status", containsInAnyOrder("ABERTO"));
    }
    
    @Test
    public void testView() {
        given()
          .when()
             .get("/sessoes/view/1")
          .then()
             .statusCode(200)
             .body("$.size()", is(1),
                    "nome", containsInAnyOrder("Sessão Nova"),
                    "duracao", containsInAnyOrder("1"),
                    "data_inicio_sessao", containsInAnyOrder("2020-01-01"),
                    "data_fim_sessao", containsInAnyOrder("2020-02-02"),
                    "status", containsInAnyOrder("ABERTO"));
    }

	@Test
    public void testEdit() {
        given()
            .body("{\"nome\": \"Sessão Editada\", \"duracao\": \"2\", \"data_inicio_sessao\": \"2020-03-03\", \"data_fim_sessao\": \"2020-04-04\", \"status\": \"ABERTO\"}")
            .header("Content-Type", MediaType.APPLICATION_JSON)
        .when()
            .post("/sessoes/edit/1")
        .then()
            .statusCode(200)
            .body("$.size()", is(1),
                    "nome", containsInAnyOrder("Sessão Editada"),
                    "duracao", containsInAnyOrder("2"),
                    "data_inicio_sessao", containsInAnyOrder("2020-01-01"),
                    "data_fim_sessao", containsInAnyOrder("2020-02-02"),
                    "status", containsInAnyOrder("FECHADO"));
	}

	@Test
    public void testDelete() {
        given()
            .body("{\"nome\": \"Sessão Editada\", \"duracao\": \"2\", \"data_inicio_sessao\": \"2020-03-03\", \"data_fim_sessao\": \"2020-04-04\", \"status\": \"ABERTO\"}")
            .header("Content-Type", MediaType.APPLICATION_JSON)
        .when()
            .post("/sessoes/delete/1")
        .then()
            .statusCode(200)
            .body("$.size()", is(1));
	}

}