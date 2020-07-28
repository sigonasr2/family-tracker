package sig.family;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface FamilyRelationshipRepository extends CrudRepository<FamilyRelationship,Long>{
	List<FamilyRelationship> findByMemberId(Long memberId);
	List<FamilyRelationship> findByMemberIdAndRelationshipIn(Long memberId,Collection<String> relationships);
	List<FamilyRelationship> findByFamilyIdAndRelationshipIn(Long familyId,Collection<String> relationships);
	List<FamilyRelationship> findByFamilyId(Long familyId);
	List<FamilyRelationship> findByFamilyIdAndMemberId(Long familyId,Long memberId);
}
