package sig.family;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface NotificationRepository extends CrudRepository<Notification,Long>{
	List<Notification> findByMemberId(Long memberid);
}
