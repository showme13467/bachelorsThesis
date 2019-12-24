import React from 'react'
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom'


import { NavBar } from '../components'
import { DevicesList, DevicesInsert, DevicesUpdate, DevicesDetail, DevicesGet, HomePage, UploadPlan, DevicesRoom, DevicesInsertCoordinates, DevicesUpdateCoordinates, DevicesUpdateAllCoordinates} from '../pages'

import 'bootstrap/dist/css/bootstrap.min.css'

function App() {
   
    return (
        <Router>
            <NavBar />
            <Switch>
                <Route path = "/" exact component={HomePage}/>
                <Route path="/devices/detail/:id"
                       exact
                       component={DevicesDetail}
                />
                <Route path="/uploadFloorPlan" exact component={UploadPlan}/>
                <Route path="/room/:id" exact component={DevicesRoom}/>
                <Route path="/devices/get" exact component={DevicesGet} />
                <Route path="/devices/list" exact component={DevicesList} />
                <Route path="/devices/create" exact component={DevicesInsert} />
                <Route path="/devices/create/coords/:id" exact component={DevicesInsertCoordinates}/>
                <Route path="/devices/update/coords/:id" exact component={DevicesUpdateCoordinates}/>
                <Route path="/devices/update/coords" exact component={DevicesUpdateAllCoordinates}/>
                <Route
                    path="/devices/update/:id"
                    exact
                    component={DevicesUpdate}
                />
            </Switch>
        </Router>
    )
}

export default App
