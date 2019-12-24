import React, { Component } from 'react'
import api from '../api'
import {Dropdown} from 'semantic-ui-react'
import styled from 'styled-components'
import 'semantic-ui-css/semantic.min.css'
import ImageUploader from 'react-images-upload'
import axios from 'axios'


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
`

const Divimg = styled.div.attrs({
       className: 'form-group'
})`
      margin: 0 30px;
      position: fixed;
      margin-left: -150px;
      left: 50%;
      margin-top: -150px;
      top: 40%;
`


const InputText = styled.input.attrs({
    className: 'form-control',
})`
    margin: 5px;
`

 

class UploadPlan extends Component {
    constructor(props) {
        super(props)

         this.state = {
            name: '',
            buildings: [],
            floors: [],
            building: '',
            floor: '',
            floorLoading: true,
            buildingOptions: [],
            floorOptions: [],
            pictures: [],
            longA: '',
            longB: '',
            latA: '',
            latB: '',
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
     this.setState({ floorOptions : floorOptions, floorLoading: false,})
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
        this.setState({
            floor: value,
})
}

  

  handleChangeInputLongA = async event => {
        const longA = event.target.value
        this.setState({ longA })
    }

   handleChangeInputLatA = async event => {
        const latA = event.target.value
        this.setState({ latA })
    }
    handleChangeInputLongB = async event => {
        const longB = event.target.value
        this.setState({ longB })
    }
    handleChangeInputLatB = async event => {
        const latB = event.target.value
        this.setState({ latB })
    }

handleIncludeFloor = async () => {
        const id = this.state.floor
        console.log(id)
     
        const {latA, longA, latB, longB} = this.state
        const payload = {refpoints : { pixel : [0,0,1211,800], geocoord : [latA, longA, latB, longB]}}  
        await api.updateFloorPlanById(id, payload).then(res => {
            window.alert(`Floor inserted successfully`)
            this.setState({
                name: '',
                floorLoading: true,
                pictures: [],
      })
    }).catch(err => {window.alert(`Required fields are missing`); console.log(err)})
     window.location.href=`/`
}

fileSelectedHandler = event => {
    this.setState({pictures: event.target.files[0]})
}

    render(){
      const {floor, floorLoading, longA, latA, longB, latB} = this.state
      return (
           <Wrapper>
            <Divdrop>
            <Label>Building</Label>
                <Dropdown id="BuildingDropdown" selection onChange={this.handleChangeValueBuilding} options={this.state.buildingOptions} placeholder="Select a Building"
                />

                <Label>Floor</Label>
                <Dropdown selection  disabled={floorLoading} onChange={this.handleChangeValueFloor} noResultsMessage={'Please select a Building at first.'}options={this.state.floorOptions} placeholder="Select a Floor"
                />
            </Divdrop>
            <Divimg>
                   <ImageUploader withPreview={true} withIcon={true} buttonText='Choose image' name={this.state.floor} singleImage={true} label='Max file size: 5mb, accepted: jpg' onChange={this.fileSelectedHandler} imgExtensions={['.jpg']} maxFileSize={5242880} />
                   
           <input type="file" onChange={this.fileSelectedHandler}/> 
             <Label> Longitude A: </Label>
                <InputText
                    type="number"
                    value={longA}
                    step="0.01"
                    min="0"
                    max="10"
                    onChange={this.handleChangeInputLongA}
                />
               <Label> Latitude A: </Label>
                <InputText
                    type="number"
                    value={latA}
                    step="0.01"
                    min="0"
                    max="10"
                    onChange={this.handleChangeInputLatA}
                />
                <Label> Longitude B: </Label>
                <InputText
                    type="number"
                    value={longB}
                    step="0.01"
                    min="0"
                    max="10"
                    onChange={this.handleChangeInputLongB}
                />
 
                 <Label> Latitude B: </Label>
                <InputText
                    type="number"
                    value={latB}
                    step="0.01"
                    onChange={this.handleChangeInputLatB}
                />
                  <Button onClick={this.handleIncludeFloor}>Add Floor</Button>
                        <CancelButton href={'/devices/list'}>Cancel</CancelButton>

          </Divimg>
            </Wrapper>
        )
    }
}   

export default UploadPlan

