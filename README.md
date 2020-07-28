# Family Tracker
An application that allows a family to track family members via live tracking and check-in to locations with notifications.

## App Features
1) Create Family Profiles
2) Add Family Members
3) Add Tracking Device
4) View GeoMap showing live GPS data.
5) Get notifications when Family Members have arrived at new destinations.

## User Stories
- As a parent, I want to be able to know where my kids are while they are at school.
- As a parent, I want to be able to access all locations where my kids have been that day.
- As a parent, I want to know when my kids have checked into a new location.
- As a parent, if my kid is not at an intended location I would like to be notified.
- As a parent, my kids' data should not be accessible to others.
- As a user, I should be able to view the locations of all my family members.
- As a user, I should be able to opt-in and opt-out of tracking as appropriate.
- As a user, I should be warned if a location I am going to is suspicious or susceptible to danger.

## Data Endpoints
`GET /family` - Gets all family profiles. Each family profile contains a `members` object. Sample output:
```[{"id":1,"name":"Test","members":[]},{"id":2,"name":"Sigona","members":[{"id":1,"firstName":"Joshua","lastName":"Sigona","mobileDeviceId":43891005},{"id":2,"firstName":"Michelle","lastName":"Sigona","mobileDeviceId":16829175},{"id":3,"firstName":"Joseph","lastName":"Sigona","mobileDeviceId":65881096},{"id":4,"firstName":"Jonathan","lastName":"Sigona","mobileDeviceId":99971653},{"id":5,"firstName":"Robert","lastName":"Sigona","mobileDeviceId":987198324598},{"id":6,"firstName":"Son Yong","lastName":"Sigona","mobileDeviceId":87657155}]}]```

`POST /family` - Adds a new family. Required: ```@RequestBody: 
name - Name of new family.```

`POST /member/create` - Adds a new family. Required: ```	 * @RequestBody requires:
	 * 	firstName - First Name of family member.
	 * 	lastName - Last Name of family member.
	 * 	mobileId - ID of mobile device.```
   
`POST /relationship/{familyid}/{memberid}/{relationship}` - Establish a new relationship between a member and its family. Valid relationships are: `Parent/Mother/Father/Brother/Sister`

`DELETE /relationship/{familyid}/{memberid}` - Removes a created relationship from a member.

`DELETE /member/{id}` - Deletes a member by id. Removes all established relationships of this member as well.

`POST /location` - Records a new location a member is currently at. Will automatically notify the appropriate users about dangerous locations as well as notify parents of their childrens' whereabouts. Required: ```@RequestBody:
	 * 	member - The member posting this location.
	 *  x - The X coordinate of the location (latitude).
	 *  y - The Y coordination of the location (longitude).```
   
`POST /knownlocation` - Records a new known location. Required: ```@RequestBody 
	 * 	name - The name of the location.
	 *  x - The X coordinate of the location (longitude).
	 *  y - The Y coordination of the location (latitude).
	 *  safe - True if safe, false if unsafe location.```
   
`DELETE /knownlocation/{id}` - Deletes a known location by id.

`GET /notification/{memberid}` - Gets the notifications a user has by member id.

`POST /notification` - Posts a new notification to a user from another user. Required: ```@RequestBody
	 * 	fromMember - The ID of the member sending the notification.
	 *  toMember - The ID of the member receiving the notification.
	 *	message - The message.```
   
`DELETE /notification/{notificationid}` - Deletes a notification by id.
