package sig.family;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.websocket.server.PathParam;

@Service
@RestController
public class Endpoints {
	
	FamilyRepository families;
	FamilyMemberRepository members;
	FamilyRelationshipRepository relationships;
	LocationRepository locations;
	KnownLocationRepository knownlocations;
	NotificationRepository notifications;
	RestTemplate connection = new RestTemplate();
	
	public Endpoints(FamilyRepository families,
			FamilyMemberRepository members,
			FamilyRelationshipRepository relationships,
			LocationRepository locations,
			KnownLocationRepository knownlocations,
			NotificationRepository notifications) {
		this.families=families;
		this.members=members;
		this.relationships=relationships;
		this.locations=locations;
		this.knownlocations=knownlocations;
		this.notifications=notifications;
	}
	
	@GetMapping("/family")
	public List<FamilyContainer> _1() {
		List<FamilyContainer> list = new ArrayList<>();
		for (Family f : families.findAll()) {
			list.add(new FamilyContainer(f.getName(),families,relationships,members));
		}
		return list;
	}
	
	@GetMapping("/family/{id}")
	public FamilyContainer _2(@PathVariable Long id) {
		if (families.existsById(id)) {
			Family f = families.findById(id).get();
			return new FamilyContainer(f.getName(),families,relationships,members);
		} else {
			return null;
		}
	}
	
	@PostMapping("/family")
	/**
	 * @RequestBody requires:
	 * 	name - Name of new family.
	 * @return
	 */
	public Family _3(@RequestBody Map<String,String> body) {
		if (body.containsKey("name")) {
			return families.save(new Family(body.get("name")));
		}
		return null;
	}
	
	@PostMapping("/member/create")
	/**
	 * @RequestBody requires:
	 * 	firstName - First Name of family member.
	 * 	lastName - Last Name of family member.
	 * 	mobileId - ID of mobile device.
	 * @return
	 */
	public FamilyMember _4(
			@RequestBody Map<String,String> body) {
		if (body.containsKey("firstName")&&body.containsKey("lastName")&&body.containsKey("mobileId")) {
			return members.save(new FamilyMember(body.get("firstName"),body.get("lastName"),Long.parseLong(body.get("mobileId"))));
		}
		return null;
	}
	
	@PostMapping("/relationship/{familyid}/{memberid}/{relationship}")
	public FamilyRelationship _5(
			@PathVariable Long familyid,
			@PathVariable Long memberid,
			@PathVariable String relationship) {
		if (!families.existsById(familyid)||!members.existsById(memberid)) {return null;}
		List<FamilyRelationship> fr = relationships.findByMemberId(memberid);
		FamilyRelationship relation = null;
		if (fr.size()>0) {
			relation = fr.get(0);
			relation.setFamilyId(familyid);
			relation.setRelationship(relationship);
		} else {
			relation = new FamilyRelationship(familyid,memberid,relationship);
		}
		return relationships.save(relation);
	}
	
	@DeleteMapping("/relationship/{familyid}/{memberid}")
	public FamilyRelationship _6(
			@PathVariable Long familyid,
			@PathVariable Long memberid) {
		List<FamilyRelationship> r = relationships.findByFamilyIdAndMemberId(familyid, memberid);
		if (r.size()>0) {
			FamilyRelationship f = r.get(0);
			relationships.delete(f);
			return f;
		}
		return null;
	}
	
	@DeleteMapping("/member/{id}")
	public FamilyMember _7(
			@PathVariable Long id) {
		if (members.existsById(id)) {
			FamilyMember m = members.findById(id).get();
			members.delete(m);
			
			List<FamilyRelationship> s = relationships.findByMemberId(m.getId());
			for (FamilyRelationship ss : s) {
				relationships.delete(ss);
			}
			
			return m;
		} else {
			return null;
		}
	}
	
	@PatchMapping("/member/{id}")
	/**
	 * @RequestBody can have:
	 * 	firstName - (Optional)Modified first name.
	 *  lastName - (Optional)Modified last name.
	 *  mobileId - (Optional)Modified mobile Id.
	 * @return
	 */
	public FamilyMember _11(
			@PathVariable Long id,
			@RequestBody HashMap<String,String> body) {
		if (members.existsById(id)) {
			FamilyMember m = members.findById(id).get();
			if (body.containsKey("firstName")) {
				m.setFirstName(body.get("firstName"));
			}
			if (body.containsKey("lastName")) {
				m.setLastName(body.get("lastName"));
			}
			if (body.containsKey("mobileId")) {
				m.setMobileDeviceId(Long.parseLong(body.get("mobileId")));
			}
			members.save(m);
			return m;
		} else {
			return null;
		}
	}
	
	@PatchMapping("/family/{id}")
	/**
	 * @RequestBody can have:
	 * 	name - (Optional)New family name.
	 * @return
	 */
	public Family _12(
			@PathVariable Long id,
			@RequestBody HashMap<String,String> body) {
		if (families.existsById(id)) {
			Family m = families.findById(id).get();
			if (body.containsKey("name")) {
				m.setName(body.get("name"));
			}
			families.save(m);
			return m;
		} else {
			return null;
		}
	}
	
	@PostMapping("/location")
	/**
	 * @RequestBody requires:
	 * 	member - The member posting this location.
	 *  x - The X coordinate of the location (latitude).
	 *  y - The Y coordination of the location (longitude).
	 * @return
	 */
	public Location _7(@RequestBody Map<String,String> body) {
		if (body.containsKey("member")&&body.containsKey("x")&&body.containsKey("y")&&members.existsById(Long.parseLong(body.get("member")))) {
			double x=Double.parseDouble(String.format("%.3f",Double.parseDouble(body.get("x")))),y=Double.parseDouble(String.format("%.3f",Double.parseDouble(body.get("y"))));
			//If someone is within 0.001 distance of a location, they are "at" that location.
			//That's why all known locations will be restricted to 3 decimal places for lookups.
			List<KnownLocation> loc = knownlocations.findByLongitudeAndLatitude(x, y);
			/*- Notify parents of children location changes.
			 *- Notify anyone of dangerous locations.*/
			FamilyRelationship fr = relationships.findByMemberId(Long.parseLong(body.get("member"))).get(0);
			FamilyMember m = members.findById(fr.getMemberId()).get();
			boolean isParent = relationships.findByMemberIdAndRelationshipIn(Long.parseLong(body.get("member")),Arrays.asList("Father","Mother","Parent")).size()>0;
			if (loc.size()>0) {
				if (m.getLastLocationId()==null||m.getLastLocationId()!=loc.get(0).getId()) {
					KnownLocation ll = loc.get(0);
					if (!ll.isSafe()) {
						notifications.save(new Notification("You are arriving at "+ll.getName()+", which is considered an unsafe location! Be careful!",m.getId(),1,new Date()));
					}
					if (!isParent) {
						//Send a notification to parents.
						List<FamilyRelationship> parents = relationships.findByFamilyIdAndRelationshipIn(fr.getFamilyId(),Arrays.asList("Father","Mother","Parent"));
						for (FamilyRelationship f : parents) {
							if (!ll.isSafe()) {
								notifications.save(new Notification(m.getFirstName()+" "+m.getLastName()+" has arrived at "+ll.getName()+", this is an unsafe location!",f.getMemberId(),1,new Date()));
							} else {
								notifications.save(new Notification(m.getFirstName()+" "+m.getLastName()+" has arrived at "+ll.getName()+".",f.getMemberId(),0,new Date()));
							}
						}
					}
					m.setLastLocationId(loc.get(0).getId());
					members.save(m);
				}
			}
			
			//notifications.save(new Notification());
			return locations.save(new Location(Double.parseDouble(body.get("x")),Double.parseDouble(body.get("y")),Long.parseLong(body.get("member")),new Date()));
		} else {
			return null;
		}
	}
	
	@PostMapping("/knownlocation")
	/**
	 * @RequestBody requires:
	 * 	name - The name of the location.
	 *  x - The X coordinate of the location (longitude).
	 *  y - The Y coordination of the location (latitude).
	 *  safe - True if safe, false if unsafe location.
	 * @return
	 */
	public KnownLocation _8(@RequestBody Map<String,String> body) {
		if (body.containsKey("name")&&body.containsKey("x")&&body.containsKey("y")&&body.containsKey("safe")) {
			double x=Double.parseDouble(String.format("%.3f",Double.parseDouble(body.get("x")))),y=Double.parseDouble(String.format("%.3f",Double.parseDouble(body.get("y"))));
			return knownlocations.save(new KnownLocation(x,y,body.get("name"),Boolean.parseBoolean(body.get("safe"))));
		} else {
			return null;
		}
	}
	
	@DeleteMapping("/knownlocation/{id}")
	public KnownLocation _9(@PathVariable Long id) {
		if (knownlocations.existsById(id)) {
			KnownLocation k = knownlocations.findById(id).get();
			knownlocations.deleteById(id);
			return k;
		} else {
			return null;
		}
	}
	
	@GetMapping("/notification/{id}")
	public List<Notification> _10(@PathVariable Long id) {
		return notifications.findByMemberId(id);
	}
	
	@PostMapping("/notification")
	/**
	 * @RequestBody requires:
	 * 	fromMember - The ID of the member sending the notification.
	 *  toMember - The ID of the member receiving the notification.
	 *	message - The message.
	 * @return
	 */
	public Notification _11(@RequestBody Map<String,String> body) {
		if (body.containsKey("fromMember")&&body.containsKey("toMember")&&body.containsKey("message")) {
			FamilyMember m = members.findById(Long.parseLong(body.get("fromMember"))).get();
			return notifications.save(new Notification("Received Message from "+m.getFirstName()+" "+m.getLastName()+": "+body.get("message"),Long.parseLong(body.get("toMember")),2,new Date()));
		} else {
			return null;
		}
	}
	
	@DeleteMapping("/notification/{notificationid}")
	public Notification _11(@PathVariable Long notificationid) {
		if (notifications.existsById(notificationid)) {
			Notification n = notifications.findById(notificationid).get();
			notifications.delete(n);
			return n;
		} else {
			return null;
		}
	}
	
	
	
}