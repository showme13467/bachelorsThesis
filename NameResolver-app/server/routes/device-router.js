const express = require('express')

const DeviceCtrl = require('../controllers/device-ctrl')

const router = express.Router()

router.post('/device', DeviceCtrl.createDevice)
router.put('/device/:id', DeviceCtrl.updateDevice)
router.delete('/device/:id', DeviceCtrl.deleteDevice)
router.get('/device/:id', DeviceCtrl.getDeviceById)
router.get('/devices', DeviceCtrl.getDevices)
router.post('/devicesByRoom', DeviceCtrl.getDevicesByRoom)

module.exports = router
