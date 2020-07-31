import React, {useState,useEffect} from 'react';
import logo from './logo.svg';
import './App.css';
import AppBar from '@material-ui/core/AppBar';
import MuiAppBar from '@material-ui/core/AppBar';
import { ThemeProvider } from '@material-ui/core/styles';
import { createMuiTheme } from '@material-ui/core/styles';
import { Container } from '@material-ui/core';
import MuiBottomNavigation from '@material-ui/core/BottomNavigation';
import MuiBottomNavigationAction from '@material-ui/core/BottomNavigationAction';
import MuiGroupAddTwoToneIcon from '@material-ui/icons/GroupAddTwoTone';
import FavoriteTwoToneIcon from '@material-ui/icons/FavoriteTwoTone';
import AnnouncementTwoToneIcon from '@material-ui/icons/AnnouncementTwoTone';
import RoomTwoToneIcon from '@material-ui/icons/RoomTwoTone';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link,
  useRouteMatch,
  useParams
} from "react-router-dom";

const axios = require('axios');

var mapX=-110.267
var mapY=31.544
var mapXScale=0.00005
var mapYScale=0.00005
var ctx=undefined,locations=[],mousePos={x:0,y:0},members=[],requiresUpdate=false;
function getMousePos(canvas, evt) {
	var rect = canvas.getBoundingClientRect();
	return {
	  x: evt.clientX - rect.left,
	  y: evt.clientY - rect.top
	};
}
setInterval(()=>{
	if (document.getElementById("map")!==null) {
		var c = document.getElementById("map");
		//console.log("Update map.")
		requiresUpdate=!requiresUpdate;
		if (ctx===undefined||ctx===null) {
			ctx = c.getContext("2d");
			//Draw all known locations.
			axios.get("http://localhost:8080/knownlocation")
			.then((data)=>{locations=data.data})
		} else {
			ctx.clearRect(0, 0, c.width, c.height);
			for (var loc of locations) {
				ctx.beginPath();
				if (loc.safe) {
					ctx.fillStyle="#6a6"
				} else {
					ctx.fillStyle="#d66"
				}
				ctx.arc((loc.x-mapX)/mapXScale,(loc.y-mapY)/mapYScale,10,0,2*Math.PI)
				ctx.fill();
			}
			for (var m of members) {
				ctx.beginPath();
				switch (m.relationship) {
					case "Father":{
						ctx.fillStyle="#22f"
					}break;
					case "Mother":{
						ctx.fillStyle="#f26"
					}break;
					case "Brother":{
						ctx.fillStyle="#4df"
					}break;
					case "Sister":{
						ctx.fillStyle="#f4f"
					}break;
				}
				ctx.arc((m.x-mapX)/mapXScale,(m.y-mapY)/mapYScale,5,0,2*Math.PI);
				ctx.fill();
				ctx.stroke();
			}
			ctx.font = "22px Verdana";
			for (var loc of locations) {
				if (mousePos.x>=(loc.x-mapX)/mapXScale-7 && mousePos.x<=(loc.x-mapX)/mapXScale+7 &&
				mousePos.y>=(loc.y-mapY)/mapYScale-7 && mousePos.y<=(loc.y-mapY)/mapYScale+7) {
					var size = ctx.measureText(loc.name)
					ctx.fillStyle="#eef"
					ctx.fillRect(mousePos.x-size.width/2-2,mousePos.y-30,size.width+4,24)
					ctx.fillStyle="#000"
					ctx.fillText(loc.name,mousePos.x-size.width/2,mousePos.y-8)
				}
			}
			var labelY = -30;
			ctx.font = "14px Verdana";
			for (var m of members) {
				if (mousePos.x>=(m.x-mapX)/mapXScale-7 && mousePos.x<=(m.x-mapX)/mapXScale+7 &&
				mousePos.y>=(m.y-mapY)/mapYScale-7 && mousePos.y<=(m.y-mapY)/mapYScale+7) {
					var size = ctx.measureText(m.firstName+" "+m.lastName)
					ctx.fillStyle="#eef"
					ctx.fillRect(mousePos.x-size.width/2-2,mousePos.y-20+labelY,size.width+4,14)
					ctx.fillStyle="#000"
					ctx.fillText(m.firstName+" "+m.lastName,mousePos.x-size.width/2,mousePos.y-8+labelY)
					labelY-=20;
				}
			}
		}
	} else {
		ctx=null;
	}
},50)

function Map(p) {
	const [familyId,setFamilyId] = useState(0)
	const [family,setFamily] = useState([])
	
	useEffect(()=>{
		p.setActive("/map")
	})
	useEffect(()=>{
		if (p.family[familyId]) {
			members=p.family[familyId].members
			//console.log(members)
		}
	},[p.family])
	useEffect(()=>{
		if (family[familyId]) {
			members=family[familyId].members
			//console.log(members)
		}
	},[family])
	useEffect(()=>{
		
		const interval = setInterval(()=>{
			axios.get("http://localhost:8080/family")
			.then((data)=>{setFamily(data.data)})
		},50)
		return () => clearInterval(interval);
	},[requiresUpdate])
	return (
		<>
		<h1>Map View</h1>
		<select id="family" name="family" value={familyId} onChange={(e)=>{setFamilyId(e.target.value);
		members=p.family[e.target.value].members}}>
			{p.family.map((fam)=><option key={fam.id-1} value={fam.id-1}>{fam.name}</option>)}
		</select>
		<canvas id="map" width="800" height="600" onMouseMove={(e)=>{
			mousePos=getMousePos(e.target,e)
		}
		}/>
		</>
	);
}

function Message(p) {
	const [familyId,setFamilyId] = useState(0)
	const [memberId,setMemberId] = useState(null)
	const [notifications,setNotifications] = useState([])
	const [timestamp,setTimestamp] = useState("")
	
	function displayTimestamp(date) {
		var seconds = Math.floor((Date.now()-new Date(date))/1000);
		seconds-=32400
		var minutes = Math.floor(seconds/60);
		//console.log(Date.now()-new Date(date))
		//console.log(Date.now())
		
		var hours = Math.floor(minutes/60)
		if (seconds<60) {return <>{seconds} {"second"+((seconds!==1)?"s":"")} ago</>} else 
		if (minutes<60) {return <>{minutes} {"minute"+((minutes!==1)?"s":"")} ago</>} else {
			return <>{hours} {"hour"+((hours!==1)?"s":"")} ago</>
		}
	}
	
	useEffect(()=>{
		p.setActive("/messages")
	})
	
	useEffect(()=>{
		if (memberId!==null) {
			axios.get("http://localhost:8080/notification/"+memberId)
			.then((data)=>{
				setNotifications(data.data)
			})
		} else {
			setNotifications([])
		}
		const interval = setInterval(()=>{
			if (memberId!==null) {
				axios.get("http://localhost:8080/notification/"+memberId)
				.then((data)=>{
					setNotifications(data.data)
					//console.log(data.data)
				})
			}
		},50)
		return () => clearInterval(interval);
	},[memberId,familyId])
	
	useEffect(()=>{
		if (p.family[0]) {
			setMemberId(p.family[0].members[0].id)
		}
	},[p.family])
	
	return (
		<>
			<h1>Message View</h1>
		<select id="family" name="family" value={familyId} onChange={(e)=>{setFamilyId(e.target.value);
		members=p.family[e.target.value].members;
		if (p.family[e.target.value].members.length>0){setMemberId(p.family[e.target.value].members[0].id)} else {setMemberId(null)}}}>
			{p.family.map((fam)=><option key={fam.id-1} value={fam.id-1}>{fam.name}</option>)}
		</select>
		<select id="member" name="member" value={memberId} onChange={(e)=>{setMemberId(e.target.value)}}>
		{(p.family[familyId] && p.family[familyId].members)?p.family[familyId].members.map((member)=><option key={member.id} value={member.id}>{member.firstName+" "+member.lastName}</option>):<></>}
		</select>
		<br/>
		<br/>
		{notifications.map((notification)=><React.Fragment key={notification.id}>{(notification.notificationType===0)?<div className="fadein alert alert-info">{notification.message}<span className="tinytime">{displayTimestamp(notification.date)}</span></div>:(notification.message.includes("You are"))?<div className="badfadein alert alert-danger">{notification.message}<span className="tinytime">{displayTimestamp(notification.date)}</span></div>:<div className="badchildfadein alert alert-warning">{notification.message}<span className="tinytime">{displayTimestamp(notification.date)}</span></div>}</React.Fragment>)}
		</>
	);
}


function Member(p) {
	
	const [familyId,setFamilyId] = useState(0)
	const [firstName,setFirstName] = useState("")
	const [lastName,setLastName] = useState("")
	const [relationship,setRelationship] = useState("")
	const [mobileDevice,setMobileDevice] = useState("")
	const [disabled,setDisabled] = useState(false)
	
	const options = [{display:"Father",value:"Father"},
	{display:"Mother",value:"Mother"},
	{display:"Son",value:"Brother"},
	{display:"Daughter",value:"Sister"},]
	
	useEffect(()=>{
		p.setActive("/members")
	})
	
	return (
		<>
		<h1>Member View</h1>
			<div className="row">
				<div className="col-3 text-right">
					<label htmlFor="family">Family: </label>
				</div>
				<div className="col-9 text-left">
					<select id="family" name="family" value={familyId} onChange={(e)=>{setFamilyId(e.target.value)}}>
						{p.family.map((fam)=><option key={fam.id-1} value={fam.id-1}>{fam.name}</option>)}
					</select>
				</div>
			</div>
			<div className="row">
				<div className="col-12">
					{(p.family[familyId])?p.family[familyId].members.map((member)=><Mem key={member.id} member={member} update={p.update} setUpdate={p.setUpdate}/>):<></>}
				</div>
			</div>
			<div className="row">
				<div className="col-12">
					<label htmlFor="name">Add Member:</label>
					<br/><div className="form-group"><label htmlFor="firstName">First Name</label><input type="text" className="form-control form-control-sm" name="firstName" disabled={disabled} onChange={(e)=>{setFirstName(e.target.value);}} value={firstName} id="firstName"/></div><div className="form-group"><label htmlFor="lastName">Last Name</label><input type="text" className="form-control form-control-sm" name="lastName" disabled={disabled} onChange={(e)=>{setLastName(e.target.value);}} value={lastName} id="lastName"/></div><div className="form-group"><label htmlFor="mobileDevice">Mobile Device</label><input type="text" name="mobileDevice" className="form-control form-control-sm" disabled={disabled} onChange={(e)=>{setMobileDevice(e.target.value);}} value={mobileDevice} id="mobileDevice"/></div><div className="form-group"><label htmlFor="relationship">Relationship</label><select className="form-control form-control-sm" name="relationship" disabled={disabled} onChange={(e)=>{setRelationship(e.target.value);}} id="relationship" value={relationship}>
						{options.map((option)=><option value={option.value} key={option.value}>{option.display}</option>)}
					</select></div><button disabled={disabled} style={{width:"10%"}} onClick={()=>{
						setDisabled(true)
						axios.post("http://localhost:8080/member/create",{firstName:firstName,lastName:lastName,mobileId:mobileDevice})
						.then((data)=>{
						return axios.post("http://localhost:8080/relationship/"+(Number(familyId)+1)+"/"+(data.data.id)+"/"+(relationship))})
						.then((data)=>{
						p.setUpdate(!p.update)
						setFirstName("")
						setLastName("")
						setMobileDevice("")
						setRelationship("")
						setDisabled(false)
						})
						.catch(setDisabled(false))}}>Add</button>
				</div>
			</div>
		</>
	);
}

function Mem(p) {
	
	return (
		<>
		<div className="row mouseover">
			<div className="col-4 text-right">
				<Editable update={p.update} setUpdate={p.setUpdate} endpoint={"http://localhost:8080/member/"+p.member.id} field="firstName" value={p.member.firstName}/>
			</div>
			<div className="col-4">
				<Editable update={p.update} setUpdate={p.setUpdate} endpoint={"http://localhost:8080/member/"+p.member.id} field="lastName" value={p.member.lastName}/>
			</div>
			<div className="col-2">
				<Editable update={p.update} setUpdate={p.setUpdate} endpoint={"http://localhost:8080/member/"+p.member.id} field="mobileId" value={p.member.mobileDeviceId}/>
			</div>
		</div>
		</>
	);
}

function Editable(p) {
	const [value,setValue] = useState((typeof(p.value)==="string"&&p.value.length>0||typeof(p.value)==="number")?p.value:"-");
	const [editing,setEditing] = useState(false);
	const [disabled,setDisabled] = useState(false);
	const [style,setStyle] = useState({});
	
	return (
	<>
	{(editing)?<><input type="text" style={style} value={value} disabled={disabled} onChange={(e)=>{setValue(e.target.value);setStyle({})}} onKeyPress={(e)=>{
		if (e.key==='Enter') {
			if (value.length===0) {
				setStyle({border:"1px solid red",background:"#fdd"})
				return new Error("Cannot accept 0 length string.")
			}
			setDisabled(true)
			var obj = {}
			obj[p.field] = value
			axios.patch(p.endpoint,obj)
			.then((data)=>{
				p.setUpdate(!p.update)
				setDisabled(false)
				setEditing(false)
			})
			.catch((err)=>{
				setDisabled(false)
				setStyle({border:"1px solid red",background:"#fdd"})
			})
		}
	}
	}/></>:<>
	<span className="editable" onClick={()=>{setEditing(true)}}>{value}</span>
	</>}
	</>
	);
}

function Fam(p) {
	return (<>
		<div className="row">
			<div className="col-1 border-left border-top border-bottom">
				{p.family.id}
			</div>
			<div className="col-2 border-top border-bottom">
				<Editable update={p.update} setUpdate={p.setUpdate} endpoint={"http://localhost:8080/family/"+p.family.id} field="name" value={p.family.name}/>
			</div>
			<div className="col-9 border-left border-top border-bottom border-right">
				{p.family.members.map((member)=><Mem key={member.id} member={member} update={p.update} setUpdate={p.setUpdate}/>)}
			</div>
		</div>
	</>);
}

function Family(p) {
	const [family,setFamily] = useState("");
	
	useEffect(()=>{
	p.setActive("/")
	})
	
	return (
		<>
		<h1>Family View</h1>
		{p.family.map((family)=><Fam key={family.id} family={family} update={p.update} setUpdate={p.setUpdate}/>)}
		<br/>
		<div className="row">
			<div className="col-12">
				<label htmlFor="name">Add Family Name:</label>
				<br/><input type="text" onChange={(e)=>{setFamily(e.target.value);}} value={family} id="name" style={{width:"90%"}}/><button style={{width:"10%"}} onClick={()=>{
					axios.post("http://localhost:8080/family",{name:family})
					.then((data)=>{
					p.setUpdate(!p.update)
					setFamily("")})}}>Add</button>
			</div>
		</div>
		</>
	);
}

function L(p) {
	return (
		<>
			<div className="row">
				<div className="col-12">
					{(p.highlight===p.active)?<b>{p.name}</b>:<>{p.name}</>}
				</div>
			</div>
		</>
	)
}

const darkTheme = createMuiTheme({
    palette: {
      type: 'dark',
    },
});

function App() {
	const [pageView,setPageView] = useState(null);
	const [active,setActive] = useState("/");
	const [family,setFamily] = useState([]);
	const [update,setUpdate] = useState(false);
	const [value,setValue] = useState("Family")
	
	useEffect(()=>{
		axios.get("http://localhost:8080/family")
		.then((data)=>{setFamily(data.data)})
	},[update])
	
	  return (
	  <ThemeProvider theme={darkTheme}>
			<Router>
				<MuiBottomNavigation value={value} showLabels className="navibar">
							<Link to="/" onClick={()=>{setActive("/")}}>
							<MuiBottomNavigationAction label="Family" value="Family" icon={<MuiGroupAddTwoToneIcon />} /></Link>
							<Link to="/members" onClick={()=>{setActive("/members")}}><MuiBottomNavigationAction label="Members" value="Members" icon={<FavoriteTwoToneIcon />} /></Link>
							<Link to="/messages" onClick={()=>{setActive("/messages")}}><MuiBottomNavigationAction label="Notifications" value="Notifications" icon={<AnnouncementTwoToneIcon />} /></Link>
							<Link to="/map" onClick={()=>{setActive("/map")}}><MuiBottomNavigationAction label="Map" value="Map" icon={<RoomTwoToneIcon />} /></Link>
							</MuiBottomNavigation>
		<Container maxWidth="md">
				<div className="container">
					<div className="row">
						<div className="col-12 order-1 order-sm-1 order-md-2">
							<Switch>
								<Route path="/map">
									<Map setActive={setActive} family={family}/>
								</Route>
								<Route path="/messages">
									<Message setActive={setActive} family={family}/>
								</Route>
								<Route path="/members">
									<Member setActive={setActive} family={family} update={update} setUpdate={setUpdate}/>
								</Route>
								<Route path="/">
									<Family setActive={setActive} family={family} update={update} setUpdate={setUpdate}/>
								</Route>
							</Switch>
						</div>
					</div>
				</div>
			</Container>
			</Router>
		</ThemeProvider>
	  );
}

export default App;
