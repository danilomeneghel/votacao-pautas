package app.controller;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;

import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;

@QuarkusTestResource(H2DatabaseTestResource.class)
public class PautaControllerTest {
    
    @Test
    public void testAdd() {
        given()
            .body("{\"titulo\": \"Pauta Nova\", \"descricao\": \"qweqweweqe\", \"status\": \"ATIVO\"}")
            .header("Content-Type", MediaType.APPLICATION_JSON)
        .when()
            .post("/pautas/add")
        .then()
            .statusCode(200)
            .body("$.size()", is(1));
    }
    
    @Test
    public void testList() {
        given()
          .when()
             .get("/pautas/list")
          .then()
             .statusCode(200)
             .body("$.size()", is(1),
                     "titulo", containsInAnyOrder("Pauta Nova"),
                     "descricao", containsInAnyOrder("qweqweweqe"),
                     "status", containsInAnyOrder("ATIVO"));
    }
    
    @Test
    public void testView() {
        given()
          .when()
             .get("/pautas/view/1")
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
            .post("/pautas/edit/1")
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
            .post("/pautas/delete/1")
        .then()
            .statusCode(200)
            .body("$.size()", is(1));
	}

}