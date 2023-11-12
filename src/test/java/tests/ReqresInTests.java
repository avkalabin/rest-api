package tests;

import models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.*;
import static specs.Spec.requestSpec;
import static specs.Spec.responseSpec;


public class ReqresInTests {

    User user = new User();

    @Test
    @DisplayName("Проверка успешного логина")
    void successfulLoginTest() {

        user.setEmail("eve.holt@reqres.in");
        user.setPassword("cityslicka");

        User responseUser = step("Make request to successfully login", () ->
                given(requestSpec)
                        .body(user)
                        .when()
                        .post("/login")
                        .then()
                        .statusCode(200)
                        .spec(responseSpec)
                        .extract().as(User.class));

        step("Verify response", () ->
                assertThat(responseUser.getToken()).isEqualTo("QpwL5tke4Pnpja7X4"));
    }

    @Test
    @DisplayName("Проверка наличия Michael Lawson в списке пользователей")
    void getListUsers() {

        step("Make request to get list of user and verify Michael Lawson with Groovy", () ->
                given(requestSpec)
                        .when()
                        .get("/users?page=2")
                        .then()
                        .statusCode(200)
                        .spec(responseSpec)
                        .body("data.findAll { it.first_name == 'Michael' }",
                                hasItems(hasEntry("first_name", "Michael"), hasEntry("last_name", "Lawson"))));
    }

    @Test
    @DisplayName("Проверка создания пользователя")
    void createUser() {

        user.setFirstName("morpheus");
        user.setJob("leader");

        User responseUser = step("Make request to create user", () ->
                given(requestSpec)
                        .body(user)
                        .when()
                        .post("/users")
                        .then()
                        .statusCode(201)
                        .spec(responseSpec)
                        .extract().as(User.class));

        step("Verify response", () -> {
            assertThat(responseUser.getFirstName()).isEqualTo("morpheus");
            assertThat(responseUser.getJob()).isEqualTo("leader");
        });
    }

    @Test
    @DisplayName("Проверка обновления данных пользователя")
    void updateUser() {

        user.setFirstName("morpheus");
        user.setJob("zion resident");

            User responseUser = step("Make request to update user", () ->
                given(requestSpec)
                        .body(user)
                        .when()
                        .put("/users/2")
                        .then()
                        .statusCode(200)
                        .spec(responseSpec)
                        .extract().as(User.class));

        step("Verify response", () -> {
            assertThat(responseUser.getFirstName()).isEqualTo("morpheus");
            assertThat(responseUser.getJob()).isEqualTo("zion resident");
        });
    }

    @Test
    @DisplayName("Проверка статус кода при удалении пользователя")
    void verifyDeleteCode() {

        step("Make request to delete user", () ->
                given(requestSpec)
                        .when()
                        .delete("https://reqres.in/api/users/2")
                        .then()
                        .spec(responseSpec)
                        .statusCode(204));
    }


    @Test
    @DisplayName("Проверка успешной регистрации")
    void successfulRegisterTest() {

        user.setEmail("eve.holt@reqres.in");
        user.setPassword("pistol");

        User responseUser = step("Make request to successfully register", () ->
                given(requestSpec)
                        .body(user)
                        .when()
                        .post("/register")
                        .then()
                        .spec(responseSpec)
                        .statusCode(200)
                        .extract().as(User.class));


        step("Verify response", () -> {
            assertThat(responseUser.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
            assertThat(responseUser.getId()).isEqualTo(4);
        });
    }
}