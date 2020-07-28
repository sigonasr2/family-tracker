package sig.family;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface FamilyRepository extends CrudRepository<Family,Long>{
	List<Family> findByName(String name);
}
