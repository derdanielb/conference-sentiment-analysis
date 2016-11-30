package net.csa.conference.repository;

import net.csa.conference.model.Conference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ConferenceRepositoryTest {
    @Autowired ConferenceRepository repository;

    @Test
    public void readsFirstPageCorrectly() {
       Page<Conference> c = repository.findAll(new PageRequest(0, 10));
       assert(c.isFirst());
    }
}
