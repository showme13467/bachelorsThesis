import React, { Component } from 'react'
import api from '../api'
import {Dropdown} from 'semantic-ui-react'
import styled from 'styled-components'
import 'semantic-ui-css/semantic.min.css'
import ImageMapper from 'react-image-mapper'
import Draggable from 'react-draggable'
import {Layer, Circle, Stage, Group, Image} from 'react-konva'


const Title = styled.h1.attrs({
    className: 'h1',
})``

const Wrapper = styled.div.attrs({
    className: 'form-group',
})`
    margin: 0 30px;
`

const Label = styled.label`
    margin: 5px;
`

const Button = styled.button.attrs({
    className: `btn btn-primary`,
})`
    margin: 15px 15px 15px 5px;
`

const CancelButton = styled.a.attrs({
    className: `btn btn-danger`,
})`
    margin: 15px 15px 15px 5px;
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


class DevicesInsertCoordinates extends Component {
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
            x: '',
            y: '',
            device: '',
            isDragging: false,
        }
       

    }


  componentDidMount = async() => {
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


saveNewPixel = async() => {
const id = this.state.device      

console.log(this.state.x)
console.log(this.state.y)
const device = await api.getDeviceById(id)
const payload ={
                pixel: [this.state.x-365, this.state.y-123]
                }
await api.updateDevicePixelById(id, payload).then(res => {
console.log(res);
//window.location.reload();
}).catch(err =>{  window.alert(`Device could not save new Coordinates... Pls try again later.`); console.log(err)})
}


    render(){
       const {floor, floorLoading, devices, rooms} = this.state
      const URL = this.state.url

      let showDevices = true
      if(!devices.length){
      showDevices = false
      }
      return (
           <Wrapper>
         
                  <Divimg>
              <img src={URL} width={1211} height={800}  alt="Floorplan"/>   
                </Divimg>                

               <Stage width={1211} height={800}>
        <Layer>
         {devices.map((device, i) =>
               <Circle key={device._id} x={device.pixel[0]+365} y={device.pixel[1]+123} width={15} height={15} 
                fill= {this.state.isDragging ? 'green' : 'blue'}
                draggable
                 onDragStart={() => {
              this.setState({
                isDragging: true
              });
            }}
            onDragEnd={e => {
              this.setState({
                isDragging: false,
                x: e.target.x(),
                y: e.target.y(),
               device: device._id 
              });
              this.saveNewPixel();
              }}  />
)}
         
 
             </Layer>
              
            </Stage> 
             
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

export default DevicesInsertCoordinates

