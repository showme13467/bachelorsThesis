import React, {Component} from 'react';
//import axios from 'axios';

class App extends Component{
  state = {
    data:[],
    type: null,
    name: null,
  }
  getDataFromDb = (url, type, name) => {
  let uri = 'http://irt-ap.cs.columbia.edu:80/api/get_' + url	
  fetch(uri).then((data) => data.json()).then((res)=> this.SetState({data: res.data}));
  };	
  
  createDataToDb = (url) => {
    
  };

  	
  render(){
    const {data} = this.state;
    return <div>
             <ul>
  {data.length <= 0
  ? 'NO DB ENTRIES YET'
  : data.map((dat) => (
<li style = {{padding : '10px'}} >
<span style = {{color: 'gray'}}>data: </span> dat
</li>
))}
</ul>
<div style={{padding : '10px'}}>
<input type="text" onChange={(e) => this.setState({type: e.target.value})} placeholder="Type in what type you wanna get from DB" style={{width: '200px'}} />
<input type="text" onChange={(e) => this.setState({name: e.target.value})} placeholder="Type in what name you wanna get from DB" style={{width: '200px'}} />
<button onClick={() => this.getDataFromDb(this.state.type + 's' , this.state.type, this.state.name  )}>GET</button>
</div>
<h1> {this.state.data}</h1>     
  </div>
  }
}

export default App;
