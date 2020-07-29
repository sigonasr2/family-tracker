package sig.family;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

@RequestMapping
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FamilyContainer extends Family{
	
	List<FamilyMemberContainer> members = new ArrayList<>();
	Long id = -1l;
	
	FamilyContainer(String name
			,FamilyRepository families
			,FamilyRelationshipRepository relationships
			,FamilyMemberRepository members) {
		super(name);
		id = families.findByName(name).get(0).getId();
		List<FamilyRelationship> relations = relationships.findByFamilyId(id);
		for (FamilyRelationship r : relations) {
			FamilyMember m = members.findById(r.getMemberId()).get();
			this.members.add(new FamilyMemberContainer(m,relationships));
		}
	}

	public List<FamilyMemberContainer> getMembers() {
		return members;
	}

	public void setMembers(List<FamilyMemberContainer> members) {
		this.members = members;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
