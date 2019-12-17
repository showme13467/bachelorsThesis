const express = require('express')
const bodyParser = require('body-parser')
const cors = require('cors')
const db = require('./db')
const deviceRouter = require('./routes/device-router')
const roomRouter = require('./routes/room-router')
const floorRouter = require('./routes/floor-router')
const buildingRouter = require('./routes/building-router')
const deviceTypeRouter = require ('./routes/deviceType-router')
const path = require('path')


const app = express()
//const apiPort = 3001
const apiPort = 80
//const apiHost = 'localhost'
const apiHost = '0.0.0.0'

app.use(bodyParser.urlencoded({ extended: true }))
app.use(cors())
app.use(bodyParser.json())


db.on('error', console.error.bind(console, 'MongoDB connection error:'))

app.use('/api', deviceRouter)
app.use('/api', roomRouter)
app.use('/api', floorRouter)
app.use('/api', buildingRouter)
app.use('/api', deviceTypeRouter)


app.use(express.static(path.join(__dirname, 'build')));
app.get('/*', function(req, res) {
  res.sendFile(path.join(__dirname, 'build', 'index.html'));
});


app.listen(apiPort, apiHost, () => console.log(`Server running on ${apiHost}:${apiPort}`))
