const Floor = require('../models/floor-model')
const Building = require('../models/building-model')
const fs = require('fs')
const multer = require ('multer')
const busboy = require ('connect-busboy')



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

    Building.findOne({_id : req.body.building}, (err,building) => {
      if (err){
        console.log(err)
        return res.status(400).json({
          err,
          message: 'There is no Building with that Id'
})
}
      floor.building =  building._id
      floor.coordinates = body.coordinates
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
            console.log(error)
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
        floor.geometry = body.geometry
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

updateFloorPlan = async (req, res) => {
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
        floor.refpoints = body.refpoints
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
}


uploadFloorPlan = async (req, res) => {
  
    const body = req.body
  
  
   
   console.log(req.file) 
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
        
      //  floor.image.data = fs.readFileSync(req.file.path);
      //  floor.image.contentType = 'image/jpg';
       // floor
       //     .save()
        //    .then(() => {
                return res.status(200).json({
                    success: true,
                    id: floor._id,
                    message: 'Floor updated!',
                })
          //  })
            .catch(error => {
                return res.status(404).json({
                    error,
                    message: 'Floor not updated!',
                })
            })
})

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
    return res.status(200).json( { success: true, data: floors })
}).catch(err => console.log(err))
}

module.exports = {
    createFloor,
    updateFloor,
    updateFloorPlan,
    uploadFloorPlan,
    deleteFloor,
    getFloors,
    getFloorById,
    getFloorsByBuildingId,
   
}

