package net.csa.conference.rest;

import com.google.gson.Gson;
import net.csa.conference.model.Conference;
import net.csa.conference.repository.ConferenceRepositoryService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;


/**
 * @author philipp.amkreutz
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class ConferenceControllerTest {

	// Change Hostname for local Test (application.properties) and start docker-compose.yaml!!!!

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private ConferenceRepositoryService conferenceRepositoryService;

	private MockMvc mockMvc;
	String[] organizerStringArray = {"Person;Philipp;Amkreutz", "Group;TestGroup", "Organisation;TestOrganisation"};
	String[] sponsorStringArray = {"Person;Philipp;Amkreutz", "Group;TestGroup", "Organisation;TestOrganisation"};
	private Conference testConference = new Conference("MockMVCTestConference", "from", "to", "LocationName", "Auf der Höhe", "28", "51429", "Bergisch Gladbach", "DE", "test", organizerStringArray, sponsorStringArray);

	private Gson gson = new Gson();

	@Before
	public void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/conference/add").content(gson.toJson(testConference)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		String c = result.getResponse().getContentAsString();
		Assert.assertTrue(c != null);
	}

	@Test
	public void testCreateConference() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/conference/add").content(gson.toJson(testConference)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		String conference = result.getResponse().getContentAsString();
		Assert.assertTrue(conference.contains(testConference.getConferenceName()));
		List<Conference> c = conferenceRepositoryService.findByConferenceName(testConference.getConferenceName());
		Assert.assertTrue(!c.isEmpty());
	}

	@Test
	public void testGetConference() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/conference/getByConferenceName/" + testConference.getConferenceName());
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		String conference = result.getResponse().getContentAsString();
		System.out.println(conference);
		Assert.assertTrue(conference.contains(testConference.getConferenceName()));
	}

	@Test
	public void testUpdateConference() throws Exception {
		List<Conference> conferences = conferenceRepositoryService.findByConferenceName(testConference.getConferenceName());
		conferences.get(0).setConferenceName("UpdateTestConferenceName");
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/conference/update").content(gson.toJson(conferences.get(0))).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		String conference = result.getResponse().getContentAsString();
		Assert.assertTrue(conference.contains("UpdateTestConferenceName"));
		List<Conference> c = conferenceRepositoryService.findByConferenceName("UpdateTestConferenceName");
		Assert.assertTrue(!c.isEmpty());
	}

	@Test
	public void testDeleteConference() throws Exception {
		List<Conference> conferences = conferenceRepositoryService.findByConferenceName(testConference.getConferenceName());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/conference/delete").content(gson.toJson(conferences.get(0))).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
		MvcResult result  = this.mockMvc.perform(requestBuilder).andReturn();
		String returnString = result.getResponse().getContentAsString();
		Assert.assertTrue(returnString.equals("1"));
		List<Conference> conference = conferenceRepositoryService.findByConferenceName(testConference.getConferenceName());
		Assert.assertTrue(conference.isEmpty());
	}

	@After
	public void deleteConferences() {
		conferenceRepositoryService.deleteAll();
	}

}
