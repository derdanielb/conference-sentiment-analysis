package net.csa.conference.rest;

import net.csa.conference.model.Konferenz;
import net.csa.conference.repository.CRUD_operationen.KonferenzRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conference/search")
public class ConferenceController implements KonferenzRepository{
    private final MongoOperations mongoOps;
	@Autowired
	public ConferenceController(MongoOperations mongoOps) {
		this.mongoOps = mongoOps;
	}
    @RequestMapping(path = "/findbyid/uuid/{id}", produces = "application/json")
    @ResponseBody
    public Konferenz findById(@PathVariable String id) {
        Konferenz k_search;
        k_search = mongoOps.findById(id, Konferenz.class);
        //System.out.println("k_search id: "+k_search.getUuid()+"\n"+"k_search name: "+k_search.getName()+"\n"+"k_search Zeitinterval: "+k_search.getZeitinterval()+"\n");
        return k_search;
    }

    @Override
    @RequestMapping(path = "/insertonebyparameter/{id}/{name}/{timeinterval}")
    public void insertentity(@PathVariable String id, @PathVariable String name, @PathVariable Integer timeinterval) {
        Konferenz k = new Konferenz();
        k.setUuid(id);
        k.setName(name);
        k.setZeitinterval(timeinterval);
        mongoOps.insert(k);
    }

    @RequestMapping(path = "/findbyname/name/{name}")
    public List<Konferenz> findByName(@PathVariable String input_name) {
        List<Konferenz> ve_ko;
        //Konferenz k_search_n;
        //ve_ko = new Vector<Konferenz>();
        ve_ko = mongoOps.find(org.springframework.data.mongodb.core.query.Query.query(Criteria.where("name").is(input_name)), Konferenz.class);
        //k_search_n = mongoOps.findOne(org.springframework.data.mongodb.core.query.Query.query(Criteria.where("name").is(name)), Konferenz.class);
        //System.out.println("k_search: "+k_search_n.getUuid()+"\n"+"name: "+k_search_n.getName()+"\n"+"Zeitinterval: "+k_search_n.getZeitinterval()+"\n");
        for (Konferenz aVe_ko : ve_ko) {
            System.out.println("ve_ko id: " + aVe_ko.getUuid() + "\n" + "ve_ko name: " + aVe_ko.getName() + "\n" + "ve_ko Zeitinterval: " + aVe_ko.getZeitinterval() + "\n");
        }
        return ve_ko;
    }


    //----------------------------------------------------------------------------------//

	@RequestMapping(path = "/findone", method = RequestMethod.GET)
	public Konferenz findOne(String id) {
		return null;
	}

	@RequestMapping(path = "/deleteone", method = RequestMethod.DELETE)
	public void delete(Konferenz entity) {
		// TODO Auto-generated method stub
	}

	public <S extends Konferenz> S insert(S entity) {
		return null;
	}


	@RequestMapping(path = "/saveone", method = RequestMethod.PUT)
	public <S extends Konferenz> S save(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Konferenz> List<S> save(Iterable<S> entites) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Konferenz> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Konferenz> findAll(Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public <S extends Konferenz> List<S> insert(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Konferenz> List<S> findAll(Example<S> example) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Konferenz> List<S> findAll(Example<S> example, Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Konferenz> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public boolean exists(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterable<Konferenz> findAll(Iterable<String> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub

	}



	@Override
	public void delete(Iterable<? extends Konferenz> entities) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub

	}

	@Override
	public <S extends Konferenz> S findOne(Example<S> example) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Konferenz> Page<S> findAll(Example<S> example, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Konferenz> long count(Example<S> example) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <S extends Konferenz> boolean exists(Example<S> example) {
		// TODO Auto-generated method stub
		return false;
	}



}
 