package app.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;

import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;

import app.ApplicationTest;

public class PautaControllerTest extends ApplicationTest {

    @Test
    public void testList() {
        given()
          .when()
             .get("/pautas/list")
          .then()
             .statusCode(200)
             .body("$.size()", is(2),
                     "titulo", containsInAnyOrder("Pauta 1", "Pauta 2"),
                     "descricao", containsInAnyOrder("asdasdasdasd", "zczxcxczc"),
                     "status", containsInAnyOrder("ATIVO", "ATIVO"));
    }
    
    @Test
    public void testView() {
        given()
          .when()
             .get("/pautas/view", 1)
          .then()
             .statusCode(200)
             .body("$.size()", is(2),
                     "titulo", containsInAnyOrder("Pauta 1"),
                     "descricao", containsInAnyOrder("asdasdasdasd"),
                     "status", containsInAnyOrder("ATIVO"));
    }

    @Test
    public void testNew() {
        given()
            .body("{\"titulo\": \"Pauta Nova\", \"descricao\": \"qweqweweqe\", \"status\": \"ATIVO\"}")
            .header("Content-Type", MediaType.APPLICATION_JSON)
        .when()
            .post("/pautas/new")
        .then()
            .statusCode(200)
            .body("$.size()", is(1),
                    "titulo", containsInAnyOrder("Pauta Nova"),
                    "descricao", containsInAnyOrder("qweqweweqe"),
                    "status", containsInAnyOrder("ATIVO"));
	}

	@Test
    public void testEdit() {
        given()
            .body("{\"titulo\": \"Pauta Editada\", \"descricao\": \"qweqweweqe\", \"status\": \"ATIVO\"}")
            .header("Content-Type", MediaType.APPLICATION_JSON)
        .when()
            .post("/pautas/edit", 3)
        .then()
            .statusCode(200)
            .body("$.size()", is(1),
                    "titulo", containsInAnyOrder("Pauta Editada"),
                    "descricao", containsInAnyOrder("bmnbnmbbm"),
                    "status", containsInAnyOrder("ATIVO"));
	}

	@Test
    public void testDelete() {
        given()
            .body("{\"titulo\": \"Pauta Editada\", \"descricao\": \"qweqweweqe\", \"status\": \"ATIVO\"}")
            .header("Content-Type", MediaType.APPLICATION_JSON)
        .when()
            .post("/pautas/delete", 3)
        .then()
            .statusCode(200)
            .body("$.size()", is(1));
	}

}