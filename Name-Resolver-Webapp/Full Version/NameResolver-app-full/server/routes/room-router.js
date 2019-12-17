const express = require('express')

const RoomCtrl = require('../controllers/room-ctrl')

const router = express.Router()

router.post('/room', RoomCtrl.createRoom)
router.put('/room/:id', RoomCtrl.updateRoom)
router.delete('/room/:id', RoomCtrl.deleteRoom)
router.get('/room/:id', RoomCtrl.getRoomById)
router.get('/rooms', RoomCtrl.getRooms)
router.post('/roomsByFloor', RoomCtrl.getRoomsByFloorId)
router.post('/roomsByPolygon', RoomCtrl.getRoomsByPolygon)
router.post('/roomsByMouse', RoomCtrl.getRoomsByMouse)


module.exports = router

