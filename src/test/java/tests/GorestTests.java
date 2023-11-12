package tests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class GorestTests {

    @Test
    @DisplayName("Create a new user")
    void createNewUser() {

        String bearerToken = "19ee007e0168035c5964be3345cff3b61cf75006354715101094521994cebd6e";
        String body = "{ \"name\": \"Gennadiy\", \"email\": \"gena100@example\", \"gender\": \"male\", \"status\": \"active\" }";
        given()
                .headers(
                        "Authorization",
                        "Bearer " + bearerToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .body(body)
                .contentType(JSON)
                .when()
                .post("https://gorest.co.in/public/v2/users")
                .then()
                .log().all()
                .statusCode(201);
    }

    @Test
    @DisplayName("Get user details")
    void getUserDetails() {
        given()
                .when()
                .get("https://gorest.co.in/public/v2/users/1700701")
                .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Lila Verma"));
    }

}

