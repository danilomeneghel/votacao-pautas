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
public class PautaControllerTest {
    
    @Test
    public void testAdd() {
        given()
            .body("{\"titulo\": \"Pauta Nova\", \"descricao\": \"qweqweweqe\", \"status\": \"ATIVO\"}")
            .header("Content-Type", MediaType.APPLICATION_JSON)
        .when()
            .post("/pautas/add")
        .then()
        .assertThat()
            .statusCode(200);
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
    public void testGetId() {
        given()
             .pathParam("id", Long.valueOf(1))
          .when()
             .get("/pautas/id/{id}")
          .then()
             .statusCode(200);
    }

}