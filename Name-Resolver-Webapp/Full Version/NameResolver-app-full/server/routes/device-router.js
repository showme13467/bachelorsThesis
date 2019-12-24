const express = require('express')

const DeviceCtrl = require('../controllers/device-ctrl')

const router = express.Router()

router.post('/device', DeviceCtrl.createDevice)
router.put('/device/:id', DeviceCtrl.updateDevice)
router.put('/device/pixel/:id', DeviceCtrl.updateDevicePixel)
router.put('/device/name/:id', DeviceCtrl.updateDeviceName)
router.delete('/device/:id', DeviceCtrl.deleteDevice)
router.get('/device/:id', DeviceCtrl.getDeviceById)
router.get('/devices', DeviceCtrl.getDevices)
router.post('/devicesByRoom', DeviceCtrl.getDevicesByRoom)
router.post('/devicesByPolygon', DeviceCtrl.getDevicesByPolygon)
router.post('/devicesByFloor', DeviceCtrl.getDevicesByFloor)

module.exports = router
