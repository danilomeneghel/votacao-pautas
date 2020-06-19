package app.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;

import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;

import app.ApplicationTest;

public class SessaoControllerTest extends ApplicationTest {

    @Test
    public void testList() {
        given()
          .when()
             .get("/sessoes/list")
          .then()
             .statusCode(200)
             .body("$.size()", is(1),
                     "nome", containsInAnyOrder("Sessão 1"),
                     "duracao", containsInAnyOrder(2),
                     "data_inicio_sessao", containsInAnyOrder("2020-01-01"),
                     "data_fim_sessao", containsInAnyOrder("2020-02-02"),
                     "status", containsInAnyOrder("ABERTO"));
    }
    
    @Test
    public void testView() {
        given()
          .when()
             .get("/sessoes/view", 1)
          .then()
             .statusCode(200)
             .body("$.size()", is(1),
                     "nome", containsInAnyOrder("Sessão 1"),
                     "duracao", containsInAnyOrder(2),
                     "data_inicio_sessao", containsInAnyOrder("2020-01-01"),
                     "data_fim_sessao", containsInAnyOrder("2020-02-02"),
                     "status", containsInAnyOrder("ABERTO"));
    }

    @Test
    public void testNew() {
        given()
            .body("{\"nome\": \"Sessão Nova\", \"duracao\": \"3\", \"data_inicio_sessao\": \"2020-03-03\", \"data_fim_sessao\": \"2020-04-04\", \"status\": \"FECHADO\"}")
            .header("Content-Type", MediaType.APPLICATION_JSON)
        .when()
            .post("/sessoes/new")
        .then()
            .statusCode(200)
            .body("$.size()", is(1),
                    "nome", containsInAnyOrder("Sessão Nova"),
                    "duracao", containsInAnyOrder(3),
                    "data_inicio_sessao", containsInAnyOrder("2020-03-03"),
                    "data_fim_sessao", containsInAnyOrder("2020-04-04"),
                    "status", containsInAnyOrder("FECHADO"));
	}

	@Test
    public void testEdit() {
        given()
            .body("{\"nome\": \"Sessão Editada\", \"duracao\": \"3\", \"data_inicio_sessao\": \"2020-03-03\", \"data_fim_sessao\": \"2020-05-05\", \"status\": \"ABERTO\"}")
            .header("Content-Type", MediaType.APPLICATION_JSON)
        .when()
            .post("/sessoes/edit", 2)
        .then()
            .statusCode(200)
            .body("$.size()", is(1),
                    "nome", containsInAnyOrder("Sessão Editada"),
                    "duracao", containsInAnyOrder(3),
                    "data_inicio_sessao", containsInAnyOrder("2020-03-03"),
                    "data_fim_sessao", containsInAnyOrder("2020-05-05"),
                    "status", containsInAnyOrder("ABERTO"));
	}

	@Test
    public void testDelete() {
        given()
            .body("{\"nome\": \"Sessão Editada\", \"duracao\": \"3\", \"data_inicio_sessao\": \"2020-03-03\", \"data_fim_sessao\": \"2020-05-05\", \"status\": \"ABERTO\"}")
            .header("Content-Type", MediaType.APPLICATION_JSON)
        .when()
            .post("/sessoes/delete", 2)
        .then()
            .statusCode(200)
            .body("$.size()", is(1));
	}

}