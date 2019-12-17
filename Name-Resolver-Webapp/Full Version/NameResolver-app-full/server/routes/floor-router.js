const express = require('express')

const FloorCtrl = require('../controllers/floor-ctrl')

const router = express.Router()

router.post('/floor', FloorCtrl.createFloor)
router.put('/floor/:id', FloorCtrl.updateFloor)
router.put('/floor/refpoints/:id', FloorCtrl.updateFloorPlan)
router.delete('/floor/:id', FloorCtrl.deleteFloor)
router.get('/floor/:id', FloorCtrl.getFloorById)
router.get('/floors', FloorCtrl.getFloors)
router.post('/floorsByBuilding', FloorCtrl.getFloorsByBuildingId)

module.exports = router

