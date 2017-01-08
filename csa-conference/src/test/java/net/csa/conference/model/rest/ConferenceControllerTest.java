package net.csa.conference.model.rest;

import net.csa.conference.model.Conference;
import net.csa.conference.service.ConferenceService;
import net.csa.conference.testdata.Testdata;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class ConferenceControllerTest {

    @Autowired
    private ConferenceService service;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

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
    public void init() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();

        service.createTestDB();
        Conference conference = service.save(new Conference("Test"));

    }

    @Test
    public void findAll() throws Exception {
        MvcResult mvcResult = mvc.perform(
                    get("/conference").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        Assert.assertTrue(response.contains("\"name\":\"Test Konferenz\""));
    }

    @Test
    public void create() throws Exception {
        Conference conference = new Conference("REST-Konferenz");
        String conferenceJson = this.json(conference);

        MvcResult mvcResult = mvc.perform(post("/conference/create")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(conferenceJson))
                .andExpect(status().isCreated()).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        Assert.assertTrue(response.contains("\"name\":\"REST-Konferenz\""));
    }

    @Test
    public void findById() throws Exception {
        List<Conference> conferences = service.findByName("Test Konferenz");
        Conference conference = conferences.get(0);

        MvcResult mvcResult = mvc.perform(get("/conference/uuid/" + conference.getUuid())
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        Assert.assertTrue(response.contains("\"name\":\"Test Konferenz\""));
    }

    @Test
    public void findByName() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/conference/name/Test Konferenz")
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        Assert.assertTrue(response.contains("\"name\":\"Test Konferenz\""));
    }

    @Test
    public void findByHashtag() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/conference/hashtag/testkonf")
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        Assert.assertTrue(response.contains("\"name\":\"Test Konferenz\""));
    }

    @Test
    public void update() throws Exception {
        List<Conference> conferences = service.findByName("Test Konferenz");
        Conference conference = conferences.get(0);
        conference.setLocation("New Location");
        String conferenceJson = json(conference);

        MvcResult mvcResult = mvc.perform(put("/conference/update/" + conference.getUuid())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(conferenceJson))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        Assert.assertTrue(response.contains("\"location\":\"New Location\""));
    }

    @Test
    public void deleteById() throws Exception {
        List<Conference> conferences = service.findByName("Test Konferenz");
        Conference conference = conferences.get(0);

        mvc.perform(delete("/conference/delete/" + conference.getUuid())).andExpect(status().isOk()).andReturn();

        conference = service.findOne(conference.getUuid());
        Assert.assertTrue(conference == null);
    }

    @Test
    public void clearDatabase() throws Exception {
        mvc.perform(get("/conference/clearDB")).andExpect(status().isOk()).andReturn();

        List<Conference> conferences = service.findAll();
        Assert.assertTrue(conferences.size() == 0);
    }

    @Test
    public void createTestDB() throws Exception {
        mvc.perform(get("/conference/createTestDB")).andExpect(status().isOk()).andReturn();

        List<Conference> conferences = service.findAll();
        Assert.assertTrue(conferences.size() == Testdata.names.length);
    }

    @After
    public void clean() {
        service.deleteAll();
    }


    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}
