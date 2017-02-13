package net.csa.conference.repository;

import net.csa.conference.model.Konferenz;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.scheduling.annotation.Async;

import java.util.List;


public class CRUD_operationen {
	/*@NoRepositoryBean
	public interface CrudRepository<T, ID extends Serializable> extends Repository<T, ID> 
    {
		/*
		<S extends T> S save(S entity); 

		T findOne(ID primaryKey);       

		//Iterable<T> findAll();          

		//Long count();                   

		void delete(T entity);          

		boolean exists(ID primaryKey);
		  

	}
	
//typisiertes Repository für Konferenzen mit CRUD Operationen
	public interface CrudRepository_konferenz extends CrudRepository<Konferenz, String>{
		List<Konferenz> findById(String id);
		List<Konferenz> readById(String id);
		
	}*/
	public interface KonferenzRepository extends MongoRepository<Konferenz, String>{
		Konferenz findById(String id);
		void insertentity (String id, String name, Integer timeinterval);
		@Async
		Konferenz findByName(String name);

	}
}
