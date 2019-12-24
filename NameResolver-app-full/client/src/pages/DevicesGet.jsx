import React, { Component } from 'react'
import api from '../api'

//import styled from 'styled-components'

/*
const Wrapper = styled.div`
    padding: 0 40px 40px 40px;
` 
*/
class DevicesGet extends Component {

     constructor(props) {
        super(props)
        this.state = {
            devices: [],
            columns: [],
            isLoading: false,
        }
    }



    componentDidMount = async () => {
        this.setState({ isLoading: true })

        await api.getAllDevices().then(devices => {      
        this.setState({
                devices: devices.data.data,
                isLoading: false,
            })
        })
    }



    render() {
       const {devices} = this.state
       console.log({devices})
       JSON.stringify({devices})
        return(
<div>
{this.state.devices.map((device)=> (
 <p>{device.name} </p>
))}
</div>
)
    }
}

export default DevicesGet
