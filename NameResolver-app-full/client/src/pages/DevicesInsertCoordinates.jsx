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
            id: this.props.match.params.id,
            x: '',
            y: '',
            name: '',
            html: '',
            floor: '',
            room: '',
            device: '',
            devices: [],
            isDragging: false,
            url: window.location.origin + "/images/default.jpg",
            hoveredArea: null,
        }
       

    }


  componentDidMount = async() => {
      const {id} = this.state
      const device = await api.getDeviceById(id)
      const floor = device.data.data.floor
      const payload = {room :  device.data.data.room}
      const devices = await api.getDevicesByRoom(payload)
      const room = await api.getRoomById(device.data.data.room)
      const url = window.location.origin + "/images/"+ floor +".jpg"
      this.setState({
      device : device.data.data,
      floor : floor,
      devices : devices.data.data,
      url : url,
      room : room.data.data,
})
}

saveNewPixel = async() => {
const {id} = this.state
const payload ={
                pixel: [this.state.x, this.state.y]
                }
await api.updateDevicePixelById(id, payload).then(res => {
console.log(res);
}).catch(err =>{  window.alert(`Device could not save new Coordinates... Pls try again later.`); console.log(err)})
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
      const {floor, devices, room, device} = this.state
      const URL = this.state.url
      let showDevices = true
      if(!devices.length){
      showDevices = false
      }
      const Areas  = [];
      if(devices.length){
      devices.map((device) => (Areas.push({_id: device._id, name: device.name, shape: "circle" , coords: [device.pixel[0], device.pixel[1], 7] , href: "/devices/detail/"+ device._id, preFillColor: "blue" })))
      Areas.push({_id: room._id, name: room.name, shape: "poly" , coords: room.pixel , href: "/room/"+ room._id })
      }
        
      var MAP = {name: "Area", areas: Areas}
      console.log(MAP)
      return (
           <Wrapper>
         
                  <Divimg>
                <ImageMapper onMouseenter={area => this.enterArea(area)} onMouseLeave={area => this.leaveArea(area)}  src={URL} map={MAP} width={1211} height={800}  imgWidth={1211} imgHeight={800} alt="Floorplan"/>

                </Divimg>                

              { showDevices && ( <Stage opacity={1} offsetX ={-366} offsetY ={-121} width={1211} height={800}>
        <Layer>
               <Circle x={device.pixel[0]} y={device.pixel[1]} width={15} height={15} 
                fill= {this.state.isDragging ? 'green' : 'red'}
                draggable
                 onDragStart={() => {
              this.setState({
                isDragging: true,
              });
            }}
            onDragEnd={e => {
              this.setState({
                isDragging: false,
                x: e.target.x(),
                y: e.target.y(), 
              });
              this.saveNewPixel();
              }}  />

         
 
             </Layer>
              
            </Stage> 
             
                )}
              
            </Wrapper>
        )
    }
}   

export default DevicesInsertCoordinates

