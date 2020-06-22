package app.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;

import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
public class SessoesControllerTest {
    
    @Test
    public void testAdd() {
        given()
            .body("{\"idpauta\": \"1\", \"nome\": \"Sessão Nova\", \"duracao\": \"1\", \"dataInicioSessao\": \"2020-01-01\", \"dataFimSessao\": \"2020-02-02\", \"status\": \"ABERTO\"}")
            .header("Content-Type", MediaType.APPLICATION_JSON)
        .when()
            .post("/sessoes/add")
        .then()
            .statusCode(200);
    }
    
    @Test
    public void testList() {
        given()
          .when()
             .get("/sessoes/list")
          .then()
             .statusCode(200)
             .body("$.size()", is(1),
                     "idpauta", containsInAnyOrder(1),
                     "nome", containsInAnyOrder("Sessão Nova"),
                     "duracao", containsInAnyOrder(1),
                     "dataInicioSessao", containsInAnyOrder("2020-01-01"),
                     "dataFimSessao", containsInAnyOrder("2020-02-02"),
                     "status", containsInAnyOrder("ABERTO"));
    }
    
    @Test
    public void testGetId() {
        given()
             .pathParam("id", Long.valueOf(2))
          .when()
             .get("/sessoes/id/{id}")
          .then()
             .statusCode(200);
    }

}