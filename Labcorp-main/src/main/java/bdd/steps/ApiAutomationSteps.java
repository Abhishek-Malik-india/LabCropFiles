package bdd.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class ApiAutomationSteps {
    Response response;

    @Given("I send a GET request to {string}")
    public void i_send_a_get_request_to(String url) {
        response = RestAssured
                .given()
                .config(RestAssuredConfig.newConfig().sslConfig(new SSLConfig().relaxedHTTPSValidation()))
                .when()
                .get(url)
                .then()
                .extract()
                .response();
    }

    @Then("I validate the response contains {string}, {string}, and all headers")
    public void i_validate_the_response_contains_and_all_headers(String pathKey, String ipKey) {
        // Validate status code
        assertEquals(200, response.getStatusCode());

        // Validate response body contains keys
        Map<String, Object> responseBody = response.jsonPath().getMap("$");
        assertTrue(responseBody.containsKey(pathKey));
        assertTrue(responseBody.containsKey(ipKey));

        // Validate headers are present
        assertNotNull(response.getHeaders());
    }

    @Given("I send a POST request to {string} with the payload")
    public void i_send_a_post_request_to_with_the_payload(String url) {
        String payload = "{\"order_id\":\"12345\",\"customer\":{\"name\":\"JaneSmith\",\"email\":\"janesmith@example.com\",\"phone\":\"1-987-654-3210\",\"address\":{\"street\":\"456OakStreet\",\"city\":\"Metropolis\",\"state\":\"NY\",\"zipcode\":\"10001\",\"country\":\"USA\"}},\"items\":[{\"product_id\":\"A101\",\"name\":\"WirelessHeadphones\",\"quantity\":1,\"price\":79.99},{\"product_id\":\"B202\",\"name\":\"SmartphoneCase\",\"quantity\":2,\"price\":15.99}],\"payment\":{\"method\":\"credit_card\",\"transaction_id\":\"txn_67890\",\"amount\":111.97,\"currency\":\"USD\"},\"shipping\":{\"method\":\"standard\",\"cost\":5.99,\"estimated_delivery\":\"2024-11-15\"},\"order_status\":\"processing\",\"created_at\":\"2024-11-07T12:00:00Z\"}";

        response = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(payload)
                .when()
                .post(url)
                .then()
                .extract()
                .response();
    }

    @Then("I validate the POST response for customer, payment, and items")
    public void i_validate_the_post_response_for_customer_payment_and_items() {
        // Validate status code
        assertEquals(200, response.getStatusCode());

        // Print response body for debugging
        System.out.println("Response Body: " + response.getBody().asString());

        // Extract the parsedBody section
        Map<String, Object> parsedBody = response.jsonPath().getMap("parsedBody");
        assertNotNull("parsedBody section is missing", parsedBody);

        // Validate customer section
        Map<String, Object> customer = (Map<String, Object>) parsedBody.get("customer");
        assertNotNull("Customer section is missing", customer);
        assertEquals("JaneSmith", customer.get("name"));
        assertEquals("janesmith@example.com", customer.get("email"));
        assertEquals("1-987-654-3210", customer.get("phone"));

        Map<String, Object> address = (Map<String, Object>) customer.get("address");
        assertNotNull("Address section is missing", address);
        assertEquals("456OakStreet", address.get("street"));
        assertEquals("Metropolis", address.get("city"));
        assertEquals("NY", address.get("state"));
        assertEquals("10001", address.get("zipcode"));
        assertEquals("USA", address.get("country"));

        // Validate payment section
        Map<String, Object> payment = (Map<String, Object>) parsedBody.get("payment");
        assertNotNull("Payment section is missing", payment);
        assertEquals("credit_card", payment.get("method"));
        assertEquals("txn_67890", payment.get("transaction_id"));
        assertEquals(111.97, (Float) payment.get("amount"), 0.01); // With delta for floating-point comparison
        assertEquals("USD", payment.get("currency"));

        // Validate items section
        List<Map<String, Object>> items = (List<Map<String, Object>>) parsedBody.get("items");
        assertNotNull("Items section is missing", items);
        assertEquals(2, items.size()); // Ensure the number of items is correct

        // Validate first item
        Map<String, Object> firstItem = items.get(0);
        assertEquals("A101", firstItem.get("product_id"));
        assertEquals("WirelessHeadphones", firstItem.get("name"));
        assertEquals(1, firstItem.get("quantity"));
        assertEquals(79.99, (Float) firstItem.get("price"), 0.01);

        // Validate second item
        Map<String, Object> secondItem = items.get(1);
        assertEquals("B202", secondItem.get("product_id"));
        assertEquals("SmartphoneCase", secondItem.get("name"));
        assertEquals(2, secondItem.get("quantity"));
        assertEquals(15.99, (Float) secondItem.get("price"), 0.01);

        // Additional validations for shipping
        Map<String, Object> shipping = (Map<String, Object>) parsedBody.get("shipping");
        assertNotNull("Shipping section is missing", shipping);
        assertEquals("standard", shipping.get("method"));
        assertEquals(5.99, (Float) shipping.get("cost"), 0.01);
        assertEquals("2024-11-15", shipping.get("estimated_delivery"));

        // Validate order status and timestamp
        assertEquals("processing", parsedBody.get("order_status"));
        assertEquals("2024-11-07T12:00:00Z", parsedBody.get("created_at"));
    }
}