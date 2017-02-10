package net.csa.conference.tweets;

import net.csa.conference.model.Konferenz;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/orchestrator")
public class Orchestrator {
    public void orchestrate(){
        String fooResourceUrl;
        RestTemplate restTemplate = new RestTemplate();
        fooResourceUrl = "/findbyid/uuid/123";
        ResponseEntity<Konferenz> response = restTemplate.getForEntity(fooResourceUrl,Konferenz.class);
        //testcommit
    }
}
