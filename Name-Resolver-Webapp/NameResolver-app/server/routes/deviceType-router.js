const express = require('express')

const DeviceTypeCtrl = require('../controllers/deviceType-ctrl')

const router = express.Router()

router.post('/deviceType', DeviceTypeCtrl.createDeviceType)
router.put('/deviceType/:id', DeviceTypeCtrl.updateDeviceType)
router.delete('/deviceType/:id', DeviceTypeCtrl.deleteDeviceType)
router.get('/deviceType/:id', DeviceTypeCtrl.getDeviceTypeById)
router.get('/deviceTypes', DeviceTypeCtrl.getDeviceTypes)


module.exports = router

