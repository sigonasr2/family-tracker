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

function Map() {
	return (
		<>
		<h1>Map View</h1>
		</>
	);
}

function Message() {
	return (
		<>
		<h1>Message View</h1>
		</>
	);
}

function Member() {
	return (
		<>
		<h1>Member View</h1>
		</>
	);
}

function Family() {
	return (
		<>
		<h1>Family View</h1>
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
	  return (
		<Router>
			<div className="container-fluid">
				<div className="row">
					<div className="col-sm-3 text-center pt-5">
						<Link to="/" onClick={()=>{setActive("/")}}><L highlight="/" active={active} name="Family"/></Link>
						<Link to="/members" onClick={()=>{setActive("/members")}}><L highlight="/members" active={active} name="Members"/></Link>
						<Link to="/messages" onClick={()=>{setActive("/messages")}}><L highlight="/messages" active={active} name="Notifications"/></Link>
						<Link to="/map" onClick={()=>{setActive("/map")}}><L highlight="/map" active={active} name="Map"/></Link>
					</div>
					<div className="col-sm-9">
						<Switch>
							<Route path="/map">
								<Map/>
							</Route>
							<Route path="/messages">
								<Message/>
							</Route>
							<Route path="/members">
								<Member/>
							</Route>
							<Route path="/">
								<Family/>
							</Route>
						</Switch>
					</div>
				</div>
			</div>
		</Router>
	  );
}

export default App;
