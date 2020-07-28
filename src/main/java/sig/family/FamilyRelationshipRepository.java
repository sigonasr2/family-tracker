package sig.family;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface FamilyRelationshipRepository extends CrudRepository<FamilyRelationship,Long>{
	List<FamilyRelationship> findByMemberId(Long memberId);
	List<FamilyRelationship> findByFamilyIdAndMemberId(Long familyId,Long memberId);
}
