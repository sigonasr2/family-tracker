package sig.family;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name="familyrelationships")
@RequestMapping
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FamilyRelationship {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long id;

	public FamilyRelationship() {};
	
	public FamilyRelationship(Long familyId, Long memberId, String relationship) {
		this.familyId = familyId;
		this.memberId = memberId;
		this.relationship = relationship;
	}
	Long familyId;
	Long memberId;
	String relationship;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getFamilyId() {
		return familyId;
	}
	public void setFamilyId(Long familyId) {
		this.familyId = familyId;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public String getRelationship() {
		return relationship;
	}
	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
}
