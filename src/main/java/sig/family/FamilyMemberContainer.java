package sig.family;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.annotation.JsonInclude;

@RequestMapping
public class FamilyMemberContainer extends FamilyMember{
	Long id;
	
	String firstName,lastName;
	
	Long mobileDeviceId;
	
	Long lastLocationId;
	
	String relationship;
	
	FamilyMemberContainer(){}
	
	public FamilyMemberContainer(FamilyMember m,
			FamilyRelationshipRepository relationships) {
		this.firstName = m.firstName;
		this.lastName = m.lastName;
		this.mobileDeviceId = m.mobileDeviceId;
		this.id=m.id;
		relationship = relationships.findByMemberId(m.getId()).get(0).getRelationship();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Long getMobileDeviceId() {
		return mobileDeviceId;
	}
	public void setMobileDeviceId(Long mobileDeviceId) {
		this.mobileDeviceId = mobileDeviceId;
	}

	public Long getLastLocationId() {
		return lastLocationId;
	}

	public void setLastLocationId(Long lastLocationId) {
		this.lastLocationId = lastLocationId;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
}
