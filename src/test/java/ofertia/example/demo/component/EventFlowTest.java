package ofertia.example.demo.component;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONException;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EventFlowTest {

    private WireMockServer wireMockServer;

    @BeforeEach
    public void setup() {
        wireMockServer = new WireMockServer(9000);
        wireMockServer.start();
    }

    @Test
    public void testWholeFlowBarcelona() {

        wireMockServer.stubFor(WireMock.get(urlPathEqualTo("/events/Barcelona"))
                .willReturn(aResponse().withStatus(200)));

        //Actual request
        Response response = RestAssured.given()
                .get("http://localhost:8080/events/Barcelona");
        //Result assertion
        assertEquals(200, response.getStatusCode());

    }

    @Test
    public void testFailureScenarioMissingCity() {

        //Actual request
        Response response = RestAssured.given()
                .get("http://localhost:8080/events/");
        //Result assertion
        assertEquals(404, response.getStatusCode());
    }

    @Test
    public void testWholeFlowBarcelonaCheckingBody() throws JSONException {

        String expectResult = "{" +
                                "\"city\": \"Barcelona\"" +
                                "}";

        wireMockServer.stubFor(WireMock.get(urlPathEqualTo("/events/Barcelona"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(200)
                        .withBody(expectResult)));

        //Actual request
        Response response = RestAssured.given()
                .get("http://localhost:8080/events/Barcelona");
        //Result assertion
        assertEquals(200, response.getStatusCode());
        JSONAssert.assertEquals(expectResult, response.getBody().print(), false);

    }
    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }
}
