package net.csa.conference.tweets.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringJoiner;

import static org.junit.Assert.*;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ConferenceTweetsControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private static final Logger log = getLogger(ConferenceTweetsControllerTest.class);

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void init() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();

    }

    @Test
    public void findConferenceByHashtag() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/conferencetweets/hashtag/froscon")
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        log.info(response);

        HashMap<String,ArrayList<String>> result =
                new ObjectMapper().readValue(response, HashMap.class);
        ArrayList<String> conferences = result.get("conferences");
        log.info("Conferences found: " + conferences.size());

        Assert.assertTrue(conferences.size() > 0);
    }

    @Test
    public void findTweetsByHashtag() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/conferencetweets/hashtag/food")
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        log.info(response);

        HashMap<String,ArrayList<String>> result =
                new ObjectMapper().readValue(response, HashMap.class);
        ArrayList<String> tweets = result.get("tweets");
        log.info("Tweets found: " + tweets.size());

        Assert.assertTrue(tweets.size() > 0);
    }

    @After
    public void clean() {  }

}
