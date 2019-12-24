const express = require('express')
const fileUpload = require ('express-fileupload')
const bodyParser = require('body-parser')
const cors = require('cors')
const db = require('./db')
const deviceRouter = require('./routes/device-router')
const roomRouter = require('./routes/room-router')
const floorRouter = require('./routes/floor-router')
const buildingRouter = require('./routes/building-router')
const deviceTypeRouter = require ('./routes/deviceType-router')
const userRouter = require('./routes/user-router')
const path = require('path')
const multer = require('multer')
const passport = require ('passport')


const app = express()
const apiPort = 80
const apiHost = '0.0.0.0'

const storage = multer.diskStorage({
  destination: function(req,file,cb){
    cb(null,   __dirname + '/build/images')
  },
  filename: function (req,file,cb){
    cb(null , file.originalname)
  }
})

app.use(multer({storage: storage}).single('file'));
app.use(bodyParser.urlencoded({ extended: true }))
app.use(cors())
app.use(bodyParser.json())
app.options('*', cors())
app.use(passport.initialize())
require('./config/passport')(passport);


db.on('error', console.error.bind(console, 'MongoDB connection error:'))


const api = new express.Router()
api.use('/', deviceRouter)
api.use('/', roomRouter)
api.use('/', floorRouter)
api.use('/', buildingRouter)
api.use('/', deviceTypeRouter)
api.use('/', userRouter)

app.use('/api', api)






app.use(express.static(path.join(__dirname, 'build')));
app.get('/*', function(req, res) {
  res.sendFile(path.join(__dirname, 'build', 'index.html'));
});




app.listen(apiPort, apiHost, () => console.log(`Server running on ${apiHost}:${apiPort}`))
