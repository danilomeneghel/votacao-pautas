package app.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;

import org.junit.jupiter.api.Test;

//import javax.ws.rs.core.MediaType;

//import static io.restassured.RestAssured.given;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
public class VotacaoControllerTest {
    
    @Test
    public void testVotar() {
        /*given()
            .body("{\"idpauta\": \"1\", \"iduser\": \"1\", \"voto\": \"1\"}")
            .header("Content-Type", MediaType.APPLICATION_JSON)
        .when()
            .post("/votacao/votar")
        .then()
            .statusCode(200);*/
    }

}