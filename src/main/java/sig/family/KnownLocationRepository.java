package sig.family;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface KnownLocationRepository extends CrudRepository<KnownLocation,Long>{
	List<KnownLocation> findByLongitudeAndLatitude(double x,double y);
}
