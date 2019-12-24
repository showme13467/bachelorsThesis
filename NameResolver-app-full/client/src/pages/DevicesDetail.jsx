import React, { Component } from 'react'
import api from '../api'
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
    margin: 8px;
    font-weight: 700;
    text-decoration: underline solid rgb(68,68,68);
`
const Normal = styled.div`
    margin: 8px;
    font-style: italic;
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


class DevicesDetail extends Component {
    constructor(props) {
        super(props)

        this.state = {
            id: this.props.match.params.id,
            name: '',    
            room: '',
            location: '',
            building: '',
            type: '',
            floor: '',
            height: '',
        }
    }

    componentDidMount = async () => {
       const {id} = this.state
       const device = await api.getDeviceById(id)
       const room = await api.getRoomById(device.data.data.room)
       const floor = await api.getFloorById(device.data.data.floor)
       const building = await api.getBuildingById(device.data.data.building)
       const type = await api.getTypeById(device.data.data.type)
       const location = "x: " + device.data.data.location.coordinates[0].$numberDecimal + " y: " + device.data.data.location.coordinates[1].$numberDecimal
        this.setState({
            name: device.data.data.name,
            height: device.data.data.height,
            type: type.data.data.name,
            url: device.data.data.url,
            room: room.data.data.name,
            floor: floor.data.data.name,
            building: building.data.data.name,
            location: location,
        })

    }

    handleUpdateDevice = ()  =>  {
        const { id } = this.state
        window.location.href= `/devices/update/${id}`
}
   
    handleConnectDevice = () => {
        const {url} = this.state
        window.location.href = `${url}`
}

    
    render() {
        const { name , height , floor, room, type, building, url, location} = this.state
       
        return (
            <Wrapper>
                <Title>{name}</Title>

                <Label>Height:</Label>
                <Normal>{height}m above Floor Level</Normal>

                <Label>Type:</Label>
                <Normal>{type}</Normal>
 

                <Label>Building: </Label>
                <Normal>{building}</Normal>

                <Label>Floor:</Label>
                <Normal>{floor}</Normal>
                
                <Label>Room: </Label>
                <Normal>{room}</Normal>
           
                <Label>Device URL:</Label>
                <Normal>{url}</Normal>
                <Label>Coordinates of the Device:</Label>
                <Normal>{location}</Normal>
                <Button onClick ={this.handleUpdateDevice}>Update Device </Button>
                <Button onClick ={this.handleConnectDevice}>Connect </Button>
			<CancelButton href={'/devices/list'}>Cancel</CancelButton>
            </Wrapper>
        )
    }
}

export default DevicesDetail
