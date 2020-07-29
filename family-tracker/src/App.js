import React, {useState,useEffect} from 'react';
import logo from './logo.svg';
import './App.css';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link,
  useRouteMatch,
  useParams
} from "react-router-dom";

const axios = require('axios');

function Map(p) {
	p.setActive("/map")
	return (
		<>
		<h1>Map View</h1>
		</>
	);
}

function Message(p) {
	p.setActive("/messages")
	return (
		<>
			<h1>Message View</h1>
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
				<div className="col-sm-12 col-lg-8">
					<label htmlFor="name">Add Member:</label>
					<br/><input type="text" disabled={disabled} onChange={(e)=>{setFirstName(e.target.value);}} value={firstName} id="firstName" style={{width:"30%"}}/><input type="text" disabled={disabled} onChange={(e)=>{setLastName(e.target.value);}} value={lastName} id="lastName" style={{width:"30%"}}/><input type="text" disabled={disabled} onChange={(e)=>{setMobileDevice(e.target.value);}} value={mobileDevice} id="mobileDevice" style={{width:"30%"}}/><input type="text" disabled={disabled} onChange={(e)=>{setRelationship(e.target.value);}} value={relationship} id="relationship" style={{width:"30%"}}/><button disabled={disabled} style={{width:"10%"}} onClick={()=>{
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
						})}}>Add</button>
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
				setStyle({border:"1px solid red"})
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
			<div className="col-1 border-top border-bottom">
				<Editable update={p.update} setUpdate={p.setUpdate} endpoint={"http://localhost:8080/family/"+p.family.id} field="name" value={p.family.name}/>
			</div>
			<div className="col-sm-12 col-lg-6 mr-3 border-left border-top border-bottom border-right">
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
			<div className="col-sm-12 col-lg-8">
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

function App() {
	const [pageView,setPageView] = useState(null);
	const [active,setActive] = useState("/");
	const [family,setFamily] = useState([]);
	const [update,setUpdate] = useState(false);
	
	useEffect(()=>{
		axios.get("http://localhost:8080/family")
		.then((data)=>{setFamily(data.data)})
	},[update])
	
	  return (
		<Router>
			<div className="container-fluid">
				<div className="row">
					<div className="col-sm-3 text-center pt-5 order-2 order-sm-2 order-md-1">
						<Link to="/" onClick={()=>{setActive("/")}}><L highlight="/" active={active} name="Family"/></Link>
						<Link to="/members" onClick={()=>{setActive("/members")}}><L highlight="/members" active={active} name="Members"/></Link>
						<Link to="/messages" onClick={()=>{setActive("/messages")}}><L highlight="/messages" active={active} name="Notifications"/></Link>
						<Link to="/map" onClick={()=>{setActive("/map")}}><L highlight="/map" active={active} name="Map"/></Link>
					</div>
					<div className="col-sm-9 order-1 order-sm-1 order-md-2">
						<Switch>
							<Route path="/map">
								<Map setActive={setActive}/>
							</Route>
							<Route path="/messages">
								<Message setActive={setActive}/>
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
		</Router>
	  );
}

export default App;
