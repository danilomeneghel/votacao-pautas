package app;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class ApplicationTest {

    @Test
    public void index() {
        given()
          .when()
             .get("/")
          .then()
             .statusCode(200);
    }

}