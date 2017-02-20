package net.csa.conference.rest;

import com.jayway.restassured.RestAssured;
import net.csa.conference.ConferenceTweetsMicroservice;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.hasSize;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ConferenceTweetsMicroservice.class)
@WebAppConfiguration
@IntegrationTest()
@SpringBootTest
public class ConferenceTweetsControllerTest {
    @SuppressWarnings("unused")
    @Autowired
    private ConferenceTweetsController controller;

    @Before
    public void clearDatabase() {
        RestAssured.port = 8080;
    }

    @Test
    public void testGetByName() {
        when()
            .get("/csa/v1/conferences?name=Baum")
        .then()
            .statusCode(HttpStatus.SC_OK)
            .body("uuid", hasSize(10))
            .body("tweets", hasSize(10));
    }


}
