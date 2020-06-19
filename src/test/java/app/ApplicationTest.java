package app;

import io.quarkus.launcher.shaded.com.google.inject.Inject;
import io.quarkus.qute.Template;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class ApplicationTest {

    @Inject
    Template index;

    @Test
    public void index() {
        given()
          .when().get("/")
          .then()
             .statusCode(200);
    }

}