package sig.family;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface NotificationRepository extends CrudRepository<Notification,Long>{
	List<Notification> findByMemberIdOrderByDateDesc(Long memberid);
	List<Notification> findByMemberIdOrderByDateDesc(Long memberid,Pageable p);
	List<Notification> OrderByDateDesc();
}
