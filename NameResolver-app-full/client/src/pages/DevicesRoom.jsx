import React, { Component } from 'react'
import ReactTable from 'react-table'
import api from '../api'

import styled from 'styled-components'

import 'react-table/react-table.css'

const Wrapper = styled.div`
    padding: 0 40px 40px 40px;
`

const Update = styled.div`
    color: #ef9b0f;
    cursor: pointer;
`

const Delete = styled.div`
    color: #ff0000;
    cursor: pointer;
`

const Detail = styled.div`
    color: #0000FF;
    cursor: pointer;
`

const Normal = styled.div`
    color: #000000;
`


class UpdateDevice extends Component {
    updateUser = event => {
        event.preventDefault()

        window.location.href = `/devices/update/${this.props.id}`
    }

    render() {
        return <Update onClick={this.updateUser}>Update</Update>
    }
}

class DeleteDevice extends Component {
    deleteUser = event => {
        event.preventDefault()

        if (
            window.confirm(
                `Do tou want to delete the device ${this.props.id} permanently?`,
            )
        ) {
            api.deleteDeviceById(this.props.id)
            window.location.reload()
        }
    }

    render() {
        return <Delete onClick={this.deleteUser}>Delete</Delete>
    }
}

class DetailDevice extends Component {
    detailUser = event => {
   event.preventDefault()
   window.location.href= `/devices/detail/${this.props.id}`
}
    render() {
        return <Detail onClick={this.detailUser}>{this.props.name}</Detail>
    }
}

class GoToDevice extends Component {
    detailUser = event => {
   event.preventDefault()
   window.location.href= `${this.props.url}`
}
    render() {
        return <Detail onClick={this.detailUser}>{this.props.url}</Detail>
    }
}



class GetRooms extends Component {
 constructor(props){
   super(props)
   this.state={
      room: '',
      id: this.props.id,
}
} 
  getRooms =  async ()  =>  {
    const id = this.state.id
    await api.getRoomById(id).then(room  =>{
      this.setState({ room: room.data.data.name});
}).catch(err => {console.log(err); return "" })
}
render(){
  this.getRooms()
  const {room} = this.state
  return(
   <Normal>{room}</Normal>  
);
}
}


class GetFloors extends Component {
 constructor(props){
   super(props)
   this.state={
      floor: '',
      id: this.props.id,
}
}
  getFloors =  async ()  =>  {
    const id = this.state.id
await api.getFloorById(id).then(floor => {
             this.setState({floor: floor.data.data.name})
             }).catch(err => {console.log(err); return ""})

}

render(){
  this.getFloors()
  const {floor} = this.state
  return(
   <Normal>{floor}</Normal>
);
}
}



class GetBuildings extends Component {
 constructor(props){
   super(props)
   this.state={
      building: '',
      id: this.props.id,
}
}
  getBuildings =  async ()  =>  {
   const id = this.state.id
await api.getBuildingById(id).then(building => {
             this.setState({building: building.data.data.name,})
}).catch(err => {console.log(err); return ""})
}

render(){
  this.getBuildings()
  const {building} = this.state
  return(
   <Normal>{building}</Normal>
);
}
}



class GetTypes extends Component {
 constructor(props){
   super(props)
   this.state={
      type: '',
      id: this.props.id,
}
}
  getTypes =  async ()  =>  {
    const id = this.state.id
    await api.getTypeById(id).then(type  =>{
      this.setState({ type: type.data.data.name});
}).catch(err => {console.log(err); return "" })
}
render(){
  this.getTypes()
  const {type} = this.state
  return(
   <Normal>{type}</Normal>
);
}
}

class GetCoordinates extends Component {
render(){
const locs = this.props.location.coordinates
const erg = "x: " + locs[0].$numberDecimal + " y: " + locs[1].$numberDecimal
return(
 <Normal>{erg}</Normal>
);
}
}




class DevicesRoom extends Component {
    constructor(props) {
        super(props)
        this.state = {
            id: this.props.match.params.id,
            devices: [],
            columns: [],
            isLoading: false,
            room:'',
            type: '',
        }
    }
    


    componentDidMount = async () => {
        this.setState({ isLoading: true })
        const payload = {room : this.state.id}
        await api.getDevicesByRoom(payload).then(devices => {
            this.setState({
                devices: devices.data.data,
                isLoading: false,
            })
        })
    }

    getTypeName = async (id) => {
        await api.getTypeById(id).then(type => {
           this.setState({
               type: type.data.data.name,
           }).catch(err => {console.log(err)})
})

    } 

    render() {
        const { devices, isLoading} = this.state
        console.log('TCL: DevicesList -> render -> devices', devices)

        const columns = [
            {
                Header: 'ID',
                accessor: '_id',
                filterable: true,
            },
            {
                Header: 'Name',
                accessor: 'name',
                filterable: true,
		Cell: props => <span> <DetailDevice id={props.original._id} name={props.original.name}/> </span>
            },
            {
                Header: 'Type',
                accessor: 'type',
                filterable: true,
               /* filterMethod: (filter, rows) => {
                this.getTypeName(props.original.type);
                matchsorter(rows, filter.value, this.state.room);
                },*/
                Cell: props => <span><GetTypes id={props.original.type}/></span>
            },
            {
                Header: 'Room',
                accessor: '',
                filterable: true,
               Cell: props => <span> <GetRooms id={props.original.room}/> </span>
            },
            {
                Header: 'Floor',
                accessor: '',
                filterable: true,
               Cell: props => <span> <GetFloors id={props.original.floor}/> </span>
            },
            {
                Header: 'Building',
                accessor: '',
                filterable: true,
               Cell: props => <span> <GetBuildings id={props.original.building}/> </span>
            },
            {
                Header: 'URL',
                accessor: 'url',
                filterable: true,
               Cell: props => <span> <GoToDevice url={props.original.url}/> </span>
            },
            {
             Header: 'Coordinates',
                accessor: 'location',
                filterable: false,
                Cell: props => <span> <GetCoordinates location={props.original.location}/> </span>
            },
            {
                Header: '',
                accessor: '',
                Cell: function(props) {
                    return (
                        <span>
                            <DeleteDevice id={props.original._id} />
                        </span>
                    )
                },
            },
            {
                Header: '',
                accessor: '',
                Cell: function(props) {
                    return (
                        <span>
                            <UpdateDevice id={props.original._id} />
                        </span>
                    )
                },
            },
        ]

        let showTable = true
        if (!devices.length) {
            showTable = false
        }

        return (
            <Wrapper>
                {showTable && (
                    <ReactTable
                        data={devices}
                        columns={columns}
                        loading={isLoading}
                        defaultPageSize={10}
                        showPageSizeOptions={true}
                        minRows={0}
                    />
                )}
            </Wrapper>
        )
    }
}

export default DevicesRoom
