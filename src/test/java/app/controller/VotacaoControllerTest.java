package app.controller;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;

import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTestResource(H2DatabaseTestResource.class)
public class VotacaoControllerTest {
    
    @Test
    public void testVotar() {
        given()
            .body("{\"idpauta\": \"1\", \"iduser\": \"1\", \"voto\": \"1\"}")
            .header("Content-Type", MediaType.APPLICATION_JSON)
        .when()
            .post("/votacao/votar")
        .then()
            .statusCode(200)
            .body("$.size()", is(1));
    }

}