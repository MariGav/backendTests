import api.CategoryService;
import api.ProductService;
import com.github.javafaker.Faker;

import dto.GetCategoryResponse;
import dto.Product;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import utils.RetrofitUtils;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


public class CreateProductTests {
    
    static ProductService productService;

    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit().create(ProductService.class);
    }

    @Test
    void createProductInFoodCategoryTest() throws IOException {
        Product product = new Product();
        product.setId(null);
        product.setTitle("Bread");
        product.setPrice(100);
        product.setCategoryTitle("Food");


        Response<Product> response = productService.createProduct(product)
                .execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getTitle(), equalTo("Bread"));
        assertThat(response.body().getCategoryTitle(), equalTo("Food"));

    }

    @Test
    void modifyProductTest() throws IOException {
        Product request = new Product();
        request.setId(1);
        request.setTitle("Bread");
        request.setPrice(100);
        request.setCategoryTitle("Food");


        Response<Product> response = productService.modifyProduct(request)
                .execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
    }


    @Test
    void getProductByIdTest() throws IOException {
        Response<Product> response = productService.getProductById(1)
                .execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getId(), equalTo(1));

    }

    @Test
    void deleteProductTest() throws IOException {
        Response<ResponseBody> response = productService.deleteProduct(1).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
    }

    @Test
    void getProductsTest() throws IOException {
        Response<ResponseBody> response = productService.getProducts().execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
    }
}

