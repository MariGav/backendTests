import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SpoonacularTests {


    @BeforeAll
    static void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void getRecipe() {
        JsonPath response = given()
                .queryParam("apiKey","5dc8612fe3d64733a8d6e5e43302f959")
                .queryParam("query","pasta")
                .queryParam("maxFat","25")
                .queryParam("number","2")
                .when()
                .get("https://api.spoonacular.com/recipes/complexSearch")
                .body()
                .jsonPath();
        assertThat(response.get("results[0].title"), equalTo("Pasta With Tuna"));

    }

    @Test
    void getDiet() {
        JsonPath response = given()
                .queryParam("apiKey","5dc8612fe3d64733a8d6e5e43302f959")
                .queryParam("diet","vegan")
                .queryParam("type","breakfast")
                .queryParam("addRecipeInformation","true")
                .when()
                .get("https://api.spoonacular.com/recipes/complexSearch")
                .body()
                .jsonPath();
        assertThat(response.get("results[0].dishTypes[2]"), equalTo("breakfast"));
        assertThat(response.get("results[0].vegan"), is(true));

    }

    @Test
    void addCuisine() {
        JsonPath response = given()
                .queryParam("apiKey","5dc8612fe3d64733a8d6e5e43302f959")
                .queryParam("cuisine", "italian")
                .queryParam("type","main_course")
                .queryParam("addRecipeInformation","true")
                .when()
                .get("https://api.spoonacular.com/recipes/complexSearch")
                .body()
                .jsonPath();
        assertThat(response.get("results[0].cuisines[1]"), equalTo("Italian"));
        assertThat(response.get("results[0].dishTypes[1]"), equalTo("main course"));
    }

    @Test
    void addIncludeIngredientsNegativeTest() {
        JsonPath response = given()
                .queryParam("apiKey","5dc8612fe3d64733a8d6e5e43302f959")
                .queryParam("includeIngredients", "cheese")
                .queryParam("diet", "vegan")
                .when()
                .get("https://api.spoonacular.com/recipes/complexSearch")
                .body()
                .jsonPath();
        assertThat(response.get("totalResults"), equalTo(0));
    }

    @Test
    void author() {
        JsonPath response = given()
                .queryParam("apiKey","5dc8612fe3d64733a8d6e5e43302f959")
                .queryParam("author", "jeremyzkc")
                .queryParam("addRecipeInformation", "true")
                .when()
                .get("https://api.spoonacular.com/recipes/complexSearch")
                .body()
                .jsonPath();
        assertThat(response.get("results[0].author"), equalTo("jeremyzkc"));

    }

    @Test
    void americanCuisine() {
        JsonPath response = given()
                .queryParam("apiKey", "5dc8612fe3d64733a8d6e5e43302f959")
                .contentType("application/x-www-form-urlencoded")
                .formParam("title","Burger Sliders")
                .formParam("ingredientList","cheese")
                .when()
                .post("https://api.spoonacular.com/recipes/cuisine")
                .body()
                .jsonPath();
        assertThat(response.get("cuisine"), equalTo("American"));

    }

    @Test
    void ItalianCuisine() {
        JsonPath response = given()
                .queryParam("apiKey", "5dc8612fe3d64733a8d6e5e43302f959")
                .contentType("application/x-www-form-urlencoded")
                .formParam("title","Broccolini Quinoa Pilaf")
                .formParam("ingredientList","broccoli")
                .when()
                .post("https://api.spoonacular.com/recipes/cuisine")
                .body()
                .jsonPath();
        assertThat(response.get("cuisines[2]"), equalTo("Italian"));

    }

    @Test
    void africanCuisine() {
        JsonPath response = given()
                .queryParam("apiKey", "5dc8612fe3d64733a8d6e5e43302f959")
                .contentType("application/x-www-form-urlencoded")
                .formParam("title","African Chicken Peanut Stew")
                .formParam("ingredientList","chicken")
                .when()
                .post("https://api.spoonacular.com/recipes/cuisine")
                .body()
                .jsonPath();
        assertThat(response.get("cuisine"), equalTo("African"));

    }

    @Test
    void britishCuisine() {
        JsonPath response = given()
                .queryParam("apiKey", "5dc8612fe3d64733a8d6e5e43302f959")
                .contentType("application/x-www-form-urlencoded")
                .formParam("title","Beef Wellington")
                .formParam("ingredientList","beef")
                .when()
                .post("https://api.spoonacular.com/recipes/cuisine")
                .body()
                .jsonPath();
        assertThat(response.get("cuisines[2]"), equalTo("British"));

    }

    @Test
    void frenchCuisine() {
        JsonPath response = given()
                .queryParam("apiKey", "5dc8612fe3d64733a8d6e5e43302f959")
                .contentType("application/x-www-form-urlencoded")
                .formParam("title","Sun-Dried Tomato and Leek Quiche")
                .formParam("ingredientList","Tomato")
                .when()
                .post("https://api.spoonacular.com/recipes/cuisine")
                .body()
                .jsonPath();
        assertThat(response.get("cuisines[2]"), equalTo("French"));

    }

    @Test
    void addMealTest() {
        String id = given()
                .queryParam("hash", "24669875cffa948b1d3dc00e55d481720a769cc5")
                .queryParam("apiKey", "5dc8612fe3d64733a8d6e5e43302f959")
                .body("{\n"
                        + " \"date\": 1644881179,\n"
                        + " \"slot\": 1,\n"
                        + " \"position\": 0,\n"
                        + " \"type\": \"INGREDIENTS\",\n"
                        + " \"value\": {\n"
                        + " \"ingredients\": [\n"
                        + " {\n"
                        + " \"name\": \"1 banana\"\n"
                        + " }\n"
                        + " ]\n"
                        + " }\n"
                        + "}")
                .when()
                .post("https://api.spoonacular.com/mealplanner/your-users-name266/items")
                .then()
                .statusCode(200)
                .body("status",  is("success"))
                .extract()
                .jsonPath()
                .get("id")
                .toString();


        given()
                .queryParam("hash", "24669875cffa948b1d3dc00e55d481720a769cc5")
                .queryParam("apiKey", "5dc8612fe3d64733a8d6e5e43302f959")
                .delete("https://api.spoonacular.com/mealplanner/your-users-name266/items/" + id)
                .then()
                .statusCode(200);
    }

}
