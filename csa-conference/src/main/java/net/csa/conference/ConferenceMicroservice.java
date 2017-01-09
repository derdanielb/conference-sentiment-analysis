package net.csa.conference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;

import net.csa.conference.model.Konferenz;
import net.csa.conference.repository.CRUD_operationen.KonferenzRepository;

@SpringBootApplication
class ConferenceMicroservice {
	KonferenzRepository kr;
    public static void main(String[] args) {
        SpringApplication.run(ConferenceMicroservice.class, args);
        /*
        Konferenz K = new Konferenz();
        K.setUUID("t1");
        K.setName("Jan");
        K.setZeitinterval(5);

        /*
        ApplicationConfiguration ac = new ApplicationConfiguration();
        ac.m*/
    }

}
