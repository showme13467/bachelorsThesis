const express = require('express')

const BuildingCtrl = require( '../controllers/building-ctrl')

const router = express.Router()

router.post('/building', BuildingCtrl.createBuilding)
router.put('/building/:id', BuildingCtrl.updateBuilding)
router.delete('/building/:id', BuildingCtrl.deleteBuilding)
router.get('/building/:id', BuildingCtrl.getBuildingById)
router.get('/buildings', BuildingCtrl.getBuildings)

module.exports = router

