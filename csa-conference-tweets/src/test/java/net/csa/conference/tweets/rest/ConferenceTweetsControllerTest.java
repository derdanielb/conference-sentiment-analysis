package net.csa.conference.tweets.rest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author philipp.amkreutz
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class ConferenceTweetsControllerTest {

	// Change Hostname for local Test (ConferenceIntegration + TwitterIntegration) and start docker-compose.yaml!!!!

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void testGetConferenceTweets() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/combinedSearch/test");
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		String response = result.getResponse().getContentAsString();
		Assert.assertTrue(!response.isEmpty());
		Assert.assertTrue(response.contains("test"));
	}

}
