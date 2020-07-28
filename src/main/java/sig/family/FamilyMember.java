package sig.family;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name="familymember")
@RequestMapping
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FamilyMember {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long id;
	
	String firstName,lastName;
	
	Long mobileDeviceId;
	
	Long lastLocationId;
	
	FamilyMember(){}
	
	public FamilyMember(String firstName, String lastName, Long mobileDeviceId) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.mobileDeviceId = mobileDeviceId;
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
}
