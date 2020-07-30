package sig.family;

import sig.family.FamilyApp.Message;

public class GPSUser {
	Long id;
	double x,y;
	int waitTime=0;
	double targetX,targetY;
	
	GPSUser(Long id) {
		this.id=id;
	}
	
	public Location postLocation() {
		Message mm = new Message();
		mm.member=id;
		mm.x=x;
		mm.y=y;
		Location res = FamilyApp.postMessage(mm);
		System.out.println(res);
		return res;
	}
}
