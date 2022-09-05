import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SpoonacularTests {


    @BeforeAll
    static void initTest()  {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build();

        requestSpecification = new RequestSpecBuilder()
                .addQueryParam("apiKey","5dc8612fe3d64733a8d6e5e43302f959")
                .build();
    }

    @Test
    void getRecipe() {
        JsonPath response = given()
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
        Ingredient ingredient = new Ingredient();
        ingredient.setName("1 banana");
        ArrayList<Ingredient> ingredients = new ArrayList();
        ingredients.add(ingredient);

        RequestValue value = new RequestValue();
        value.setIngredients(ingredients);

        Request request = new Request();
        request.setDate(1644881179);
        request.setSlot(1);
        request.setPosition(0);
        request.setType("INGREDIENTS");
        request.setValue(value);

        String id = given().spec(requestSpecification)
                .when()
                .queryParam("hash", "24669875cffa948b1d3dc00e55d481720a769cc5")
                .body(request)
                .post("https://api.spoonacular.com/mealplanner/your-users-name266/items")
                .then()
                .statusCode(200)
                .body("status", is("success"))
                .extract()
                .jsonPath()
                .get("id")
                .toString();



        given().spec(requestSpecification)
                .queryParam("hash", "24669875cffa948b1d3dc00e55d481720a769cc5")
                .delete("https://api.spoonacular.com/mealplanner/your-users-name266/items/" + id)
                .then()
                .statusCode(200);
    }

}

