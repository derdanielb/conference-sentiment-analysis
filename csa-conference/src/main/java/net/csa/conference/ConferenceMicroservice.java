package net.csa.conference;

import net.csa.conference.repository.CRUD_operationen;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
class ConferenceMicroservice {

    public static void main(String[] args) {
        CRUD_operationen.KonferenzRepository kr;
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
