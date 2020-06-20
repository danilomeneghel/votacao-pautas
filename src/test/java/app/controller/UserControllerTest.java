package app.controller;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;

import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;

@QuarkusTestResource(H2DatabaseTestResource.class)
public class UserControllerTest {
    
    @Test
    public void testAdd() {
        given()
            .body("{\"name\": \"Carlos\", \"email\": \"carlos@carlos.com\", \"cpf\": \"39828275074\"}")
            .header("Content-Type", MediaType.APPLICATION_JSON)
        .when()
            .post("/user/new")
        .then()
            .statusCode(200)
            .body("$.size()", is(1));
    }
    
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
                     "cpf", containsInAnyOrder("39828275074"));
    }
    
    @Test
    public void testView() {
        given()
          .when()
             .get("/user/view/1")
          .then()
             .statusCode(200)
             .body("$.size()", is(1),
                    "name", containsInAnyOrder("Carlos"),
                    "email", containsInAnyOrder("carlos@carlos.com"),
                    "cpf", containsInAnyOrder("39828275074"));
    }

	@Test
    public void testEdit() {
        given()
            .body("{\"name\": \"Carlos\", \"email\": \"carlos@sssss.com\", \"cpf\": \"39828275074\"}")
            .header("Content-Type", MediaType.APPLICATION_JSON)
        .when()
            .post("/user/edit/1")
        .then()
            .statusCode(200)
            .body("$.size()", is(1),
                    "name", containsInAnyOrder("Carlos"),
                    "email", containsInAnyOrder("carlos@sssss.com"),
                    "cpf", containsInAnyOrder("71179454006"));
	}

	@Test
    public void testDelete() {
        given()
            .body("{\"name\": \"Carlos\", \"email\": \"carlos@sssss.com\", \"cpf\": \"71179454006\"}")
            .header("Content-Type", MediaType.APPLICATION_JSON)
        .when()
            .post("/user/delete/1")
        .then()
            .statusCode(200)
            .body("$.size()", is(1));
	}

}