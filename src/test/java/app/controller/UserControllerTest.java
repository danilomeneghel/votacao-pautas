package app.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;

import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;

import app.ApplicationTest;

public class UserControllerTest extends ApplicationTest {

    @Test
    public void testList() {
        given()
          .when()
             .get("/user/list")
          .then()
             .statusCode(200)
             .body("$.size()", is(1),
                     "name", containsInAnyOrder("Carlos"),
                     "email", containsInAnyOrder("carlos@carlos.com"),
                     "cpf", containsInAnyOrder("87466621031"));
    }
    
    @Test
    public void testView() {
        given()
          .when()
             .get("/user/view", 1)
          .then()
             .statusCode(200)
             .body("$.size()", is(1),
                     "name", containsInAnyOrder("Carlos"),
                     "email", containsInAnyOrder("carlos@carlos.com"),
                     "cpf", containsInAnyOrder("87466621031"));
    }

    @Test
    public void testNew() {
        given()
            .body("{\"name\": \"Luana\", \"email\": \"luana@luana.com\", \"cpf\": \"36587128050\"}")
            .header("Content-Type", MediaType.APPLICATION_JSON)
        .when()
            .post("/user/new")
        .then()
            .statusCode(200)
            .body("$.size()", is(1),
                    "name", containsInAnyOrder("Luana"),
                    "email", containsInAnyOrder("luana@luana.com"),
                    "cpf", containsInAnyOrder("36587128050"));
	}

	@Test
    public void testEdit() {
        given()
            .body("{\"name\": \"Joana\", \"email\": \"joana@joana.com\", \"cpf\": \"36587128050\"}")
            .header("Content-Type", MediaType.APPLICATION_JSON)
        .when()
            .post("/user/edit", 2)
        .then()
            .statusCode(200)
            .body("$.size()", is(1),
                    "name", containsInAnyOrder("Joana"),
                    "email", containsInAnyOrder("joana@joana.com"),
                    "cpf", containsInAnyOrder("71932407065"));
	}

	@Test
    public void testDelete() {
        given()
            .body("{\"name\": \"Joana\", \"email\": \"joana@joana.com\", \"cpf\": \"36587128050\"}")
            .header("Content-Type", MediaType.APPLICATION_JSON)
        .when()
            .post("/user/delete", 2)
        .then()
            .statusCode(200)
            .body("$.size()", is(1));
	}

}