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
public class UserControllerTest {
    
    @Test
    public void testAdd() {
        given()
            .body("{\"name\": \"Carlos\", \"email\": \"carlos@carlos.com\", \"cpf\": \"39828275074\"}")
            .header("Content-Type", MediaType.APPLICATION_JSON)
        .when()
            .post("/users/add")
        .then()
            .statusCode(200);
    }
    
    @Test
    public void testList() {
        given()
          .when()
             .get("/users/list")
          .then()
             .statusCode(200)
             .body("$.size()", is(1),
                     "name", containsInAnyOrder("Carlos"),
                     "email", containsInAnyOrder("carlos@carlos.com"),
                     "cpf", containsInAnyOrder(Long.valueOf("39828275074")));
    }
    
    @Test
    public void testGetCpf() {
        given()
             .pathParam("cpf", Long.valueOf("39828275074"))
          .when()
             .get("/users/cpf/{cpf}")
          .then()
             .statusCode(200);
    }

}