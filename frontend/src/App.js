import React, { Component } from 'react';
import './App.css';

class App extends Component {

	state = {
			message: 'HI!',
			users: []
	};

	componentDidMount() {
		/* setInterval(this.getMessageFromServer, 2000); */
		this.getMessageFromServer();
		this.getUsersFromServer();
	}

	getMessageFromServer = () => {
		fetch('http://localhost:8080/test')
		.then(response => response.text())
		.then(message => {
			this.setState({message: message});
		});
	};

	getUsersFromServer = () => {
		fetch('http://localhost:8080/users')
		.then(response => response.json())
		.then(data => {
			this.setState({users: data});
		});
	};

	render() {
		/* var users = [1,2,3,4,5]; */
		var users = this.state.users;
		var rows = users.map((u) =>
		  <tr key={u.id}><td>{u.id}</td><td>{u.name}</td></tr>
		);
		var header = <tr><td>ID</td><td>NAME</td></tr>
		return (
				<div className="App">
					<p>{this.state.message}</p>
					<button onClick={this.getMessageFromServer}>Update Server Time</button>
					<table>
						<thead>{header}</thead>
						<tbody>{rows}</tbody>
					</table>
				</div>
		);
	}
}

export default App;
