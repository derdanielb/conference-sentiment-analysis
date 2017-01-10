package net.csa.conference.repository;

import net.csa.conference.model.Conference;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.concurrent.Future;

/**
 * @author philipp.amkreutz
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ConferenceRepositoryTest {

	@Autowired
	private ConferenceRepositoryService conferenceRepositoryService;

	@Before
	public void setUp() {
		conferenceRepositoryService.deleteAll();
		conferenceRepositoryService.createTestdata(100);
	}

	@Test
	public void testConferenceRepository() {
		List<Conference> c1 = conferenceRepositoryService.findByConferenceName("TestKonferenz50");
		Assert.assertTrue(!c1.isEmpty());
		List<Conference> notExisting = conferenceRepositoryService.findByConferenceName("blabla");
		Assert.assertTrue(notExisting.isEmpty());

		int test = 0;
		Future<List<Conference>> conferenceFutureList = conferenceRepositoryService.findByLocationName("TestLocationName");
		if (!conferenceFutureList.isDone()) {
			test = 1;
		}
		Assert.assertTrue(test == 1);

	}

	@After
	public void deleteEntries() {
		conferenceRepositoryService.deleteAll();
	}

}
