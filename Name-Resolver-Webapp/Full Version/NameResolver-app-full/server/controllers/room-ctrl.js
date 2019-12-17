const Room = require('../models/room-model.js')
const Floor = require('../models/floor-model')
const Building = require('../models/building-model')



createRoom = (req, res) => {
    const body = req.body
    console.log(req.body);
    if (!body) {
        return res.status(400).json({
            success: false,
            error: 'You must provide a room',
        })
    }

    const room = new Room(body)

    if (!room) {
        return res.status(400).json({ success: false, error: err })
    }                         

          Floor.findOne({ _id : req.body.floor}, (err,floor) => {
      if (err){
        return res.status(400).json({
          err,
          message: 'There is no Floor with that name'
})
}
         console.log(floor);
         room.floor = floor._id
         console.log(room);
    room
        .save()
        .then(() => {
            return res.status(201).json({
                success: true,
                id: room._id,
                message: 'Room created!',
            })
        })
        .catch(error => {
            return res.status(400).json({
                error,
                message: 'Room not created!',
            })
        })

});
}

updateRoom = async (req, res) => {
    const body = req.body

    if (!body) {
        return res.status(400).json({
            success: false,
            error: 'You must provide a body to update',
        })
    }

    Room.findOne({ _id: req.params.id }, (err, room) => {
        if (err) {
            return res.status(404).json({
 err,
                message: 'Room not found!',
            })
        }
          Floor.findOne({ _id : req.body.floor}, (err,floor) => {
      if (err){
        return res.status(400).json({
          err,
          message: 'There is no Floor with that name'
})
}
         console.log(floor);
         room.floor = floor._id
        room.name = body.name
        room.geometry = body.geometry
        room.pixel = body.pixel
        console.log(room);

        room
            .save()
            .then(() => {
                return res.status(200).json({
                    success: true,
                    id: room._id,
                    message: 'Room updated!',
                })
            })
            .catch(error => {
                return res.status(404).json({
                    error,
                    message: 'Room not updated!',
                })
            })
    })
});
}

deleteRoom = async (req, res) => {
    await Room.findOneAndDelete({ _id: req.params.id }, (err, room) => {
        if (err) {
            return res.status(400).json({ success: false, error: err })
        }  

        if (!room) {
            return res
                .status(404)
                .json({ success: false, error: `Room not found` })
        }

        return res.status(200).json({ success: true, data: room })
    }).catch(err => console.log(err))
}

getRoomById = async (req, res) => {
    await Room.findOne({ _id: req.params.id }, (err, room) => {
        if (err) {
 return res.status(400).json({ success: false, error: err })
        }

        if (!room) {
            return res
                .status(404)
                .json({ success: false, error: `Room not found` })
        }
        return res.status(200).json({ success: true, data: room })
    }).catch(err => console.log(err))
}

getRooms = async (req, res) => {
    await Room.find({}, (err, rooms) => {
        if (err) {
            return res.status(400).json({ success: false, error: err })
        }
        if (!rooms.length) {
            return res
                .status(404)
                .json({ success: false, error: `Room not found` })
        }
        return res.status(200).json({ success: true, data: rooms })
    }).catch(err => console.log(err))
}

getRoomsByFloorId = async (req, res) => {
    await Room.find({floor : req.body.floor}, (err, rooms) => {
        if (err) {
            return res.status(400).json({ success: false, error: err })
        }
        if (!rooms.length) {
            return res
                .status(404)
                .json({ success: false, error: `Room not found` })
        }
        return res.status(200).json({ success: true, data: rooms })
    }).catch(err => console.log(err))
}

getRoomsByPolygon = async (req,res ) => {
  const geometry = req.body.geometry
  await Room.find({ geometry: { $geoWithin: { $geometry:  geometry }}}, (err, rooms) => {
      if(err){
          return res.status(400).json({ success: false, error: err })
      }
      if (!rooms.length) {
            return res
                .status(404)
                .json({ success: false, error: `Room not found` })
       }
  return res.status(200).json({ success: true, data: rooms })
 }).catch(err => console.log(err))
}


getRoomsByMouse = async (req,res) => {
   console.log(req.body)
   const location =  req.body.location
   await Room.findOne({geometry: {$geoIntersects: {$geometry: location}}}, (err, room) => {
      if(err){
          return res.status(400).json({ success: false, error: err })
      }
      if (!room) {
            return res
                .status(404)
                .json({ success: false, error: `Room not found` })
       } 
  console.log(room)  
  return res.status(200).json({ success: true, data: room })
 }).catch(err => console.log(err))
}


module.exports = {
    createRoom,
    updateRoom,
    deleteRoom,
    getRooms,
    getRoomById,
    getRoomsByFloorId,
    getRoomsByPolygon,
    getRoomsByMouse,
}
                                                           
