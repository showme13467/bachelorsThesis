const Device = require('../models/device-model')
const Room = require('../models/room-model')
const Floor = require('../models/floor-model')
const Building = require('../models/building-model')


createDevice = (req, res) => {
    const body = req.body
    console.log(req.body);
    if (!body) {
        return res.status(400).json({
            success: false,
            error: 'You must provide a device',
        })
    }

    const device = new Device(body)

    if (!device) {
        return res.status(400).json({ success: false, error: err })
    }
        Room.findOne({_id: req.body.room}, (err,room) => {
      if (err){
        return res.status(400).json({
          err,
          message: 'There is no Room with that ID'
})
}
         console.log(room);
         device.room = room._id
         device.type = body.type
    device
        .save()
        .then(() => {
            return res.status(201).json({
                success: true,
                id: device._id,
                message: 'Device created!',
            })
        })
        .catch(error => {
            return res.status(400).json({
                error,
                message: 'Device not created!',
            })
        })

});
}

updateDevice = async (req, res) => {
    const body = req.body

    if (!body) {
        return res.status(400).json({
            success: false,
            error: 'You must provide a body to update',
        })
    }

    Device.findOne({ _id: req.params.id }, (err, device) => {
        if (err) {
            return res.status(404).json({
                err,
                message: 'Device not found!',
            })
        }
         console.log(floor);
         device.floor = floor._id
        Room.findOne({_id: req.body.room}, (err,room) => {
      if (err){
        return res.status(400).json({
          err,
          message: 'There is no Room with that ID'
})
}
         console.log(room);
        device.room = room._id
        device.name = body.name
        device.type = body.type
        device.height = body.height
        device.url = body.url
        device.author = body.author
        device.location = {"type": "Point",
                       "coordinates" :  body.coordinates}

        device
            .save()
            .then(() => {
                return res.status(200).json({
                    success: true,
                    id: device._id,
                    message: 'Device updated!',
                })
            })
            .catch(error => {
                return res.status(404).json({
                    error,
                    message: 'Device not updated!',
                })
            })
    })

});
}

deleteDevice = async (req, res) => {
    await Device.findOneAndDelete({ _id: req.params.id }, (err, device) => {
        if (err) {
            return res.status(400).json({ success: false, error: err })
        }

        if (!device) {
            return res
                .status(404)
                .json({ success: false, error: `Device not found` })
        }

        return res.status(200).json({ success: true, data: device })
    }).catch(err => console.log(err))
}

getDeviceById = async (req, res) => {
    await Device.findOne({ _id: req.params.id }, (err, device) => {
        if (err) {
            return res.status(400).json({ success: false, error: err })
        }

        if (!device) {
            return res
                .status(404)
                .json({ success: false, error: `Device not found` })
        }
        return res.status(200).json({ success: true, data: device })
    }).catch(err => console.log(err))
}

getDevices = async (req, res) => {
    await Device.find({}, (err, devices) => {
        if (err) {
            return res.status(400).json({ success: false, error: err })
        }
        if (!devices.length) {
            return res
                .status(404)
                .json({ success: false, error: `Device not found` })
        }
        return res.status(200).json({ success: true, data: devices })
    }).catch(err => console.log(err))
}

getDevicesByRoom = async (req, res) => {
    await Device.find({room : req.body.room}, (err, devices) => {
        if (err) {
            return res.status(400).json({ success: false, error: err })
        }
        if (!devices.length) {
            return res
                .status(404)
                .json({ success: false, error: `Device not found` })
        }
        return res.status(200).json({ success: true, data: devices })
    }).catch(err => console.log(err))
}


module.exports = {
    createDevice,
    updateDevice,
    deleteDevice,
    getDevices,
    getDeviceById,
    getDevicesByRoom,
}
