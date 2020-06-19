package app.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;

import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;

import app.ApplicationTest;

public class VotacaoControllerTest extends ApplicationTest {

    @Test
    public void testVotar() {
        given()
            .body("{\"idpauta\": \"1\", \"iduser\": \"1\", \"cpf\": \"36587128050\", \"voto\": \"1\"}")
            .header("Content-Type", MediaType.APPLICATION_JSON)
        .when()
            .post("/votacao/votar")
        .then()
            .statusCode(200)
            .body("$.size()", is(1),
                    "idpauta", containsInAnyOrder("1"),
                    "iduser", containsInAnyOrder("1"),
                    "cpf", containsInAnyOrder("36587128050"),
                    "voto", containsInAnyOrder("1"));
	}

}