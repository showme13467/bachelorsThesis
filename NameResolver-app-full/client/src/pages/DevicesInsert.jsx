import React, { Component } from 'react'
import api from '../api'
import {Dropdown} from 'semantic-ui-react'
import styled from 'styled-components'
import 'semantic-ui-css/semantic.min.css'

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
const Coordinates = styled.div`
    color: #0000FF;
    cursor: pointer;
`

const InputText = styled.input.attrs({
    className: 'form-control',
})`
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


class DevicesInsert extends Component {
    constructor(props) {
        super(props)

        this.state = {
            id: '',
            name: '',
            types: [],
            buildings: [],      
            rooms: [],
            room: '',
            location: { type: 'Point',
                        coordinates: [203,201]
                        },
            floors: [],
            building: '',
            type: '',
            floor: '',
            height: '',
            floorLoading: true,
            roomLoading: true,
            buildingOptions: [],
            floorOptions: [],
            typeOptions: [],
            roomOptions:[],
        }
    }

    componentDidMount = async () => {
        await api.getAllTypes().then(types => {
        this.setState({
                types: types.data.data,
                typeOptions: [],
            })
        })
       const typeOptions = this.state.typeOptions
       this.state.types.map((type) => (
typeOptions.push({text: type.name, key: type._id, value: type._id})
))
        
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
                    typeOptions: typeOptions,
})
    }

        

    handleChangeInputName = async event => {
        const name = event.target.value
        this.setState({ name })
    }

   handleChangeInputHeight = async event => {
        const height = event.target.value
        this.setState({ height })
    }

   
    handleChangeValueType = async (event,data) =>{
    const {value} = data
    this.setState({ type : value,})
    }


   handleChangeValueBuilding = async (event,data) =>{
     const {value} = data
     const payload = {building : value} 
     await api.getFloorsByBuilding(payload).then(res => {
     this.setState({floors: res.data.data,
                    rooms: [],
                    floorOptions: [],
                    roomOptions: [],
                    building: value , 
                    floorLoading: true,
                    roomLoading: true,});
      const floorOptions = []
     this.state.floors.map((floor) => (
floorOptions.push({text: floor.name, key: floor._id, value: floor._id})
))
     this.setState({ floorOptions : floorOptions, floorLoading: false, roomOptions: [],})
}).catch(err => {
 this.setState({floors: [],
                    rooms: [],
                    floorOptions: [],
                    roomOptions: [],
                    building: value ,
                    floorLoading: true,
                    roomLoading: true,});
                    console.log(err);
})

}


    handleChangeValueFloor = async (event,data) =>{
     const {value} = data
     const payload = {floor : value}
     await api.getRoomsByFloor(payload).then(res => {
     this.setState({rooms: res.data.data,
                    devices: [],
                    roomOptions: [],
                    floor: value,
                    roomLoading: true, });
           const roomOptions = []
     this.state.rooms.map((room) => (
roomOptions.push({text: room.name, key: room._id, value: room._id})
))
     this.setState({roomOptions: roomOptions, roomLoading: false,})

                                           
}).catch(err => { this.setState({
                    rooms: [],
                    devices: [],
                    roomOptions: [],
                    floor: value,
                    roomLoading: true,});console.log(err) })
     
}
    
    handleChangeValueRoom = async (event,data) =>{
    const {value} = data
    this.setState({ room : value,})
}
    handleIncludeDevice = async () => {
        const { name, type, height, room, floor, building, location } = this.state
        const payload = { name, type, height ,room, floor, building, location }
        await api.insertDevice(payload).then(res => {
            window.alert(`Device inserted successfully`)
            this.setState({
                id: res.data.id,
                name: '',
                height: '',
                floorLoading: true,
                roomLoading: true,
      })
    }).catch(err => {window.alert(`Required fields are missing`); console.log(err)})
       const {id} = this.state
     window.location.href=`/devices/create/coords/${id}` 
}


    render() {
        const { name , height , floorLoading, roomLoading} = this.state
       // console.log(this.state.floorOptions)
       // console.log(this.state.floorOptions)
        return (
            <Wrapper>
                <Title>Create Device</Title>

                <Label>Name: </Label>
                <InputText
                    type="text"
                    value={name}
                    onChange={this.handleChangeInputName}
                />

                <Label> Type </Label>
                <Dropdown fluid selection onChange={this.handleChangeValueType} options={this.state.typeOptions} placeholder="Select a Type"
                />

                <Label> Height above floor level (in meter) </Label>
                <InputText
                    type="number"
                    value={height}
                    step="0.01"
                    min="0"
                    max="10"
                    onChange={this.handleChangeInputHeight}
                />

                <Label>Building</Label>
                <Dropdown id="BuildingDropdown" fluid selection onChange={this.handleChangeValueBuilding} options={this.state.buildingOptions} placeholder="Select a Building"
                />
                
                <Label>Floor</Label>
                <Dropdown fluid selection  disabled={floorLoading} onChange={this.handleChangeValueFloor} noResultsMessage={'Please select a Building at first.'}options={this.state.floorOptions} placeholder="Select a Floor"
                />
           
               <Label>Room</Label>
                <Dropdown fluid selection disabled={roomLoading} onChange={this.handleChangeValueRoom} options={this.state.roomOptions} noResultsMessage={'Please select a Floor at first.'} placeholder="Select a Room"
                />

                <Label>Coordinates</Label>
                <Coordinates>Add the Position of the Device after creating</Coordinates>
                
                <Button onClick={this.handleIncludeDevice}>Add Device</Button>
			<CancelButton href={'/devices/list'}>Cancel</CancelButton>
            </Wrapper>
        )
    }
}

export default DevicesInsert
