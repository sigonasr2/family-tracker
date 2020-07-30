package sig.family;

import java.net.URI;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootApplication
@Service
public class FamilyApp {
	
	static HashMap<Long,GPSUser> map = new HashMap<>();
	static RestTemplate connection = new RestTemplate();
	public final static int WAITMULT=3;
	
	public static Location postMessage(Message message) {
	    // Construct a URI from a template
	    URI uri = UriComponentsBuilder
	            .fromUriString("http://localhost:8080/location")
	            .buildAndExpand("")
	            .toUri();

	    // Create the request object
	    RequestEntity<?> request = RequestEntity.post(uri)
	            .body(message);

	    // Execute the request
	    ResponseEntity<Location> response = connection.exchange(
	            request,
	            Location.class // Declare the _type_ of the response
	    );

	    // Get the deserialized response body
	    return response.getBody();
	}
	
	@RequestMapping
	static class Message{
		Long member;
		double x;
		double y;
		public Long getMember() {
			return member;
		}
		public void setMember(Long member) {
			this.member = member;
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

	public static void main(String[] args) {
		SpringApplication.run(FamilyApp.class, args);
		
		Timer t2 = new Timer();
		t2.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {

				for (FamilyMember m : Endpoints.members.findAll()) {
					if (!map.containsKey(m.getId())) {
						GPSUser g = new GPSUser(m.getId());
						if (m.getLastLocationId()!=null) {
							g.x = Endpoints.knownlocations.findById(m.getLastLocationId()).get().getX();
							g.y = Endpoints.knownlocations.findById(m.getLastLocationId()).get().getY();
						}
						else {
							//Choose a random known location to start at.
							Long count = (long)(Math.random()*Endpoints.knownlocations.count());
							for (KnownLocation l : Endpoints.knownlocations.findAll()) {
								if (count>0) {
									count--;
								} else {
									g.x=l.getX();
									g.y=l.getY();
									//System.out.println(connection.postForObject("http://localhost:8080", String.class, "6ba5969a",q));
									break;
								}
							}
						}
						g.waitTime=((int)Math.random()*28)+2;
						//Choose a random known location to go to.
						Long count = (long)(Math.random()*Endpoints.knownlocations.count());
						for (KnownLocation l : Endpoints.knownlocations.findAll()) {
							if (count>0) {
								count--;
							} else {
								g.targetX=l.getX();
								g.targetY=l.getY();
								//System.out.println(connection.postForObject("http://localhost:8080", String.class, "6ba5969a",q));
								break;
							}
						}
						g.postLocation();
						map.put(m.getId(), g);
					}
				}

				for (Long id : map.keySet()) {
					GPSUser u = map.get(id);
					
					if (u.waitTime>0) {
						u.waitTime--;
					} else {
						if (u.targetX!=u.x || u.targetY!=u.y) {
							switch ((int)(Math.random()*6)) {
								case 0:{
									//Sometimes go a random direction.
									switch ((int)(Math.random()*4)) {
										case 0:{
											u.x+=0.001;
										}break;
										case 1:{
											u.x-=0.001;
										}break;
										case 2:{
											u.y+=0.001;
										}break;
										case 3:{
											u.y-=0.001;
										}break;
									}
								}break;
								default:{
									//Move towards.
									if (Math.random()<0.5) {
										u.x+=Math.signum(u.targetX-u.x)*0.001;
									}
									if (Math.random()<0.5) {
										u.y+=Math.signum(u.targetY-u.y)*0.001;
									}
								}
							}
							u.waitTime=((int)Math.random()*6*WAITMULT)+2*WAITMULT;
						} else {
							//Select a new target.
							//Choose a random known location to go to.
							Long count = (long)(Math.random()*Endpoints.knownlocations.count());
							for (KnownLocation l : Endpoints.knownlocations.findAll()) {
								if (count>0) {
									count--;
								} else {
									u.targetX=l.getX();
									u.targetY=l.getY();
									//System.out.println(connection.postForObject("http://localhost:8080", String.class, "6ba5969a",q));
									break;
								}
							}
							u.waitTime=((int)Math.random()*28*WAITMULT)+30*WAITMULT;
						}
						u.postLocation();
					}
				}
				
			}
			
		}, 0l, 1000l);
	}

}
