package net.csa.conference.tweets.integration;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;

/**
 * @author philipp.amkreutz
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class IntegrationTest {

	@Autowired
	private ConferenceIntegration conferenceIntegration;

	@Autowired
	private TwitterIntegration twitterIntegration;

	@Test
	public void testConferenceIntegration() {
		List<HashMap> conferences = conferenceIntegration.getConferences("test");
		Assert.assertTrue(conferences != null);
	}

	@Test
	public void testTwitterIntegration() {
		List<String> tweets = twitterIntegration.getTweets("test");
		Assert.assertTrue(!tweets.isEmpty());
	}

}
