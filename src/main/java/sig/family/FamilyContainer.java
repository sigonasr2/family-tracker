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
	
	List<FamilyMember> members = new ArrayList<>();
	
	FamilyContainer(String name
			,FamilyRepository families
			,FamilyRelationshipRepository relationships
			,FamilyMemberRepository members) {
		super(name);
		List<FamilyRelationship> relations = relationships.findByFamilyId(families.findByName(name).get(0).getId());
		for (FamilyRelationship r : relations) {
			this.members.add(members.findById(r.getMemberId()).get());
		}
		System.out.println("Called: "+this.members);
	}

	public List<FamilyMember> getMembers() {
		return members;
	}

	public void setMembers(List<FamilyMember> members) {
		this.members = members;
	}
}
