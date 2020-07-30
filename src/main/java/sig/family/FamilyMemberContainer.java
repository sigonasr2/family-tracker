package sig.family;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.annotation.JsonInclude;

import sig.family.FamilyApp.Message;

@RequestMapping
public class FamilyMemberContainer extends FamilyMember{
	Long id;
	
	String firstName,lastName;
	
	Long mobileDeviceId;
	
	Long lastLocationId;
	
	String relationship;
	
	double x,y;
	
	FamilyMemberContainer(){}
	
	public FamilyMemberContainer(FamilyMember m,
			FamilyRelationshipRepository relationships,
			LocationRepository locations) {
		this.firstName = m.firstName;
		this.lastName = m.lastName;
		this.mobileDeviceId = m.mobileDeviceId;
		this.id=m.id;
		relationship = relationships.findByMemberId(m.getId()).get(0).getRelationship();
		List<Location> locs = locations.findTopByMemberIdOrderByIdDesc(m.getId());
		if (locs.size()>0) {
			Location l = locs.get(0);
			this.x=l.getX();
			this.y=l.getY();
		} else {
			//Use their assigned location.
			if (this.getLastLocationId()==null) {
				this.x=-110.253;
				this.y=31.554;
				Message mm = new Message();
				mm.member=m.getId();
				mm.setX(this.x);
				mm.setY(this.y);
				FamilyApp.postMessage(mm);
			} else {
				Location l = locations.findById(this.getLastLocationId()).get();
				this.x=l.getX();
				this.y=l.getY();
			}
		}
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

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
}
