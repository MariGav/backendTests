import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;



import static io.restassured.RestAssured.*;


public class ShopTests {

    @BeforeAll
    static void initTest()  {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build();

        requestSpecification = new RequestSpecBuilder()
                .addQueryParam("apiKey","5dc8612fe3d64733a8d6e5e43302f959")
                .addQueryParam("hash","24669875cffa948b1d3dc00e55d481720a769cc5")
                .build();
    }



    @Test
    void ShopListTest() {

        RequestShopList request = new RequestShopList();
        request.setItem("1 package baking powder");
        request.setAisle("Baking");
        request.setParse(true);



        String id = given().spec(requestSpecification)
                .when()
                .body(request)
                .post("https://api.spoonacular.com/mealplanner/your-users-name266/shopping-list/items")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("id")
                .toString();


        given().spec(requestSpecification)
                .delete("https://api.spoonacular.com/mealplanner/your-users-name266/shopping-list/items/" + id)
                .then()
                .statusCode(200);
    }
}