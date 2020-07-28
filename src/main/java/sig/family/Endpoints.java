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
	RestTemplate connection = new RestTemplate();
	
	public Endpoints(FamilyRepository families,
			FamilyMemberRepository members,
			FamilyRelationshipRepository relationships,
			LocationRepository locations,
			KnownLocationRepository knownlocations) {
		this.families=families;
		this.members=members;
		this.relationships=relationships;
		this.locations=locations;
		this.knownlocations=knownlocations;
	}
	
	@GetMapping("/family")
	public Iterable<Family> _1() {
		return families.findAll();
	}
	
	@GetMapping("/family/{id}")
	public Optional<Family> _2(@PathVariable Long id) {
		return families.findById(id);
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
			return locations.save(new Location(Double.parseDouble(body.get("x")),Double.parseDouble(body.get("y")),Long.parseLong(body.get("member")),new Date()));
		} else {
			return null;
		}
	}
	
	@PostMapping("/knownlocation")
	/**
	 * @RequestBody requires:
	 * 	name - The name of the location.
	 *  x - The X coordinate of the location (latitude).
	 *  y - The Y coordination of the location (longitude).
	 *  safe - True if safe, false if unsafe location.
	 * @return
	 */
	public KnownLocation _8(@RequestBody Map<String,String> body) {
		if (body.containsKey("name")&&body.containsKey("x")&&body.containsKey("y")&&body.containsKey("safe")) {
			return knownlocations.save(new KnownLocation(Double.parseDouble(body.get("x")),Double.parseDouble(body.get("y")),body.get("name"),Boolean.parseBoolean(body.get("safe"))));
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
}