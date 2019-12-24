import React, { Component } from 'react'
import api from '../api'
import {Dropdown} from 'semantic-ui-react'
import styled from 'styled-components'
import 'semantic-ui-css/semantic.min.css'
import ImageMapper from 'react-image-mapper'





const Wrapper = styled.div.attrs({
    className: 'form-group',
})`
    margin: 0 30px;
`

const Label = styled.label`
    margin: 5px;
`

const Divdrop = styled.div.attrs({
      className: 'form-group'
})`
      margin: 0 30px;
      position: fixed;
      margin-left: -300px;
      left: 50%;
      top: 20%; 
      margin-top: -100px;
`

const Divimg = styled.div.attrs({
       className: 'form-group'
})`
      margin: 0 30px;
      position: fixed;
      margin-left: -180px;
      left: 30%;
      margin-top: -100px;
      top: 30%;
`

 
const Tooltip = styled.a.attrs({
      className: 'tooltip'
})`
   position: absolute;
   color: #fff;
   padding: 10px;
   background: rgba(0,0,0,0.8);
   transform: translated3d(-50%, -50%, 0);
   border-radius: 5px;
   pointer-events: none;
   z-index: 1000;
 
`

class HomePage extends Component {
    constructor(props) {
        super(props)

         this.state = {
            name: '',
            buildings: [],
            rooms: [],
            room: '',
            floors: [],
            building: '',
            floor: '',
            floorLoading: true,
            buildingOptions: [],
            floorOptions: [],
            devices: [],
            url: window.location.origin + "/images/default.jpg",
            hoveredArea: null,
        }
       

    }

    componentDidMount = async () => {
        
       await api.getAllBuildings().then(buildings =>{
       this.setState({
              buildings: buildings.data.data,
              buildingOptions: [],
})
 })
      const buildingOptions = this.state.buildingOptions
      this.state.buildings.map((building) => (
buildingOptions.push({text: building.name, key: building._id, value: building._id})
))

      this.setState({
                    buildingOptions: buildingOptions,
})
    }


    handleChangeValueBuilding = async (event,data) =>{
     const {value} = data
     const payload = {building : value}
     await api.getFloorsByBuilding(payload).then(res => {
     this.setState({floors: res.data.data,
                    floorOptions: [],
                    building: value ,
                    floorLoading: true,
                    });
      const floorOptions = []
     this.state.floors.map((floor) => (
floorOptions.push({text: floor.name, key: floor._id, value: floor._id})
))
     this.setState({ floorOptions : floorOptions, floorLoading: false, roomOptions: [],})
}).catch(err => {
 this.setState({floors: [],
                    floorOptions: [],
                    building: value ,
                    floorLoading: true,
                    });
                    console.log(err);
})

}


     handleChangeValueFloor = async (event,data) =>{
     const {value} = data
     const payload = {floor : value}
     await api.getRoomsByFloor(payload).then(res => {
     this.setState({rooms: res.data.data,
                    devices:[],
                    floor: value,
                    roomLoading: true, 
                    url: window.location.origin + "/images/" + value + ".jpg",});

}).catch(err => { this.setState({
                    rooms: [],
                    devices: [],
                    floor: value,
                    url: window.location.origin + "/images/" + value + ".jpg",
                     });console.log(err) })
     const rooms = await api.getRoomsByFloor(payload)
     const devices = await api.getDevicesByFloor(payload)
     this.setState({
            devices: devices.data.data,
            rooms: rooms.data.data,
})
}

    
enterArea = (area) =>{
 this.setState({hoveredArea: area,});
}

leaveArea = (area) => {
 this.setState({hoveredArea: null,});
}

getTipPosition = (area) =>{
 return{top: `${area.center[1]}px`, left: `${area.center[0]}px`};
}


    render(){
      const {floorLoading, devices, rooms} = this.state
      const Areas  = [];
    
       
      devices.map((device) => (Areas.push({_id: device._id, name: device.name, shape: "circle" , coords: [device.pixel[0], device.pixel[1], 7] , href: "/devices/detail/"+ device._id, preFillColor: "blue" })))
      rooms.map((room) => (Areas.push({_id: room._id, name: room.name, shape: "poly" , coords: room.pixel , href: "/room/"+ room._id })))
      
     
      var MAP = {name: "Area", areas: Areas}
      console.log(MAP)
      const URL = this.state.url
      console.log(URL)
      return (
           <Wrapper>
            <Divimg>
                   <ImageMapper onMouseenter={area => this.enterArea(area)} onMouseLeave={area => this.leaveArea(area)}  src={URL} map={MAP} width={1211} height={800}  imgWidth={1211} imgHeight={800} alt="Floorplan"/>
                 {this.state.hoveredArea &&  <Tooltip style={{...this.getTipPosition(this.state.hoveredArea)}}> {this.state.hoveredArea && this.state.hoveredArea.name}   </Tooltip>}
            </Divimg>
               <Divdrop>
            <Label>Building</Label>
                <Dropdown id="BuildingDropdown" selection onChange={this.handleChangeValueBuilding} options={this.state.buildingOptions} placeholder="Select a Building"
                />

                <Label>Floor</Label>
                <Dropdown selection  disabled={floorLoading} onChange={this.handleChangeValueFloor} noResultsMessage={'Please select a Building at first.'} options={this.state.floorOptions} placeholder="Select a Floor"
                />
            </Divdrop>

            </Wrapper>
        )
    }
}   

export default HomePage

