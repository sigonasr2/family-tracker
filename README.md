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
All endpoints require an `authentication token` provided by your mobile tracking device upon registration. They must be passed in as a parameter for all endpoints as `?auth=XXXXXXX`

`GET /family` - Gets all family profiles. Each family profile contains a `member` object. 

`GET /family/{name}` - Get a family by name.

`GET /member/{firstname}/{lastname}` - Gets a family member by first and last name.

`GET /member/location` - Gets current location of a member.

`POST /member/location` - Checks into a new location.

`PATCH /member/location` - Update current location statistics.

`PATCH /member/type` - Updates a member type. Valid types include `parent`,`user`, and `admin`.

`DELETE /family/{family}` - Removes a family.

`DELETE /family/{family}/member/{firstname}/{lastname}` - Removes family member from a family.
