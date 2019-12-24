const Floor = require('../models/floor-model')
const Building = require('../models/building-model')



createFloor = (req, res) => {
    const body = req.body
    console.log(req.body);
    if (!body) {
        return res.status(400).json({
            success: false,
            error: 'You must provide a floor',
        })
    }

    const floor = new Floor(body)

    if (!floor) {
        return res.status(400).json({ success: false, error: err })
    }

    floor.geometry = {"type": "Polygon",
                       "coordinates" :  [req.body.coordinates]}
    Building.findOne({_id : req.body.building}, (err,building) => {
      if (err){
        return res.status(400).json({
          err,
          message: 'There is no Building with that Id'
})
}
      floor.building =  building._id
      console.log(floor);
      floor
        .save()
        .then(() => {
            return res.status(201).json({
                success: true,
                id: floor._id,
                message: 'Floor created!',
            })
        })
        .catch(error => {
            return res.status(400).json({
                error,
                message: 'Floor not created!',
            })
        })

});
}

updateFloor = async (req, res) => {
    const body = req.body

    if (!body) {
        return res.status(400).json({
            success: false,
            error: 'You must provide a body to update',
        })
    }

    Floor.findOne({ _id: req.params.id }, (err, floor) => {
        if (err) {
            return res.status(404).json({
err,
                message: 'Floor not found!',
            })
        }
        floor.name = body.name
        Building.findOne({_id : req.body.building}, (err,building) => {
      if (err){
        return res.status(400).json({
          err,
          message: 'There is no Building with that name'
})
}
        floor.building = building._id
        floor.geometry = {"type": "Polygon",
                       "coordinates" :  [body.coordinates]}
        floor
            .save()
            .then(() => {
                return res.status(200).json({
                    success: true,
                    id: floor._id,
                    message: 'Floor updated!',
                })
            })
            .catch(error => {
                return res.status(404).json({
                    error,
                    message: 'Floor not updated!',
                })
            })
    })
});
}

deleteFloor = async (req, res) => {
    await Floor.findOneAndDelete({ _id: req.params.id }, (err, floor) => {
        if (err) {
            return res.status(400).json({ success: false, error: err })
        }

        if (!floor) {
            return res
                .status(404)
                .json({ success: false, error: "Floor not found" })
        }

        return res.status(200).json({ success: true, data: floor })
    }).catch(err => console.log(err))
}

getFloorById = async (req, res) => {
    await Floor.findOne({ _id: req.params.id }, (err, floor) => {
 if (err) {
 return res.status(400).json({ success: false, error: err })
        }

        if (!floor) {
            return res
                .status(404)
                .json({ success: false, error: "Floor not found" })
        }
        return res.status(200).json({ success: true, data: floor })
    }).catch(err => console.log(err))
}

getFloors = async (req, res) => {
    await Floor.find({}, (err, floors) => {
        if (err) {
            return res.status(400).json({ success: false, error: err })
        }
        if (!floors.length) {
            return res
                .status(404)
                .json({ success: false, error: "Floor not found" })
        }
        return res.status(200).json({ success: true, data: floors })
    }).catch(err => console.log(err))
}
//TODO because of alex = stupid
getFloorsByBuildingId = async (req,res) => {
   await Floor.find({building : req.body.building}, (err, floors) => {
     if(err) {
        return res.status(400).json({succes : false, error: err})
}
    if(!floors.length){
       return res
            .status(404)
            .json({success : false, error: "Floor not found"})
}
    return res.status(200).json({floors})
}).catch(err => console.log(err))
}

module.exports = {
    createFloor,
    updateFloor,
    deleteFloor,
    getFloors,
    getFloorById,
    getFloorsByBuildingId,
}
