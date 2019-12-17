const DeviceType = require('../models/deviceType-model')

createDeviceType = (req, res) => {
    const body = req.body

    if (!body) {
        return res.status(400).json({
            success: false,
            error: 'You must provide a deviceType',
        })
    }

    const deviceType = new DeviceType(body)

    if (!deviceType) {
        return res.status(400).json({ success: false, error: err })
    }

    deviceType
        .save()
        .then(() => {
            return res.status(201).json({
                success: true,
                id: deviceType._id,
                message: 'DeviceType created!',
            })
        })
        .catch(error => {
            return res.status(400).json({
                error,
                message: 'DeviceType not created!',
            })
        })
}

updateDeviceType = async (req, res) => {
    const body = req.body

    if (!body) {
        return res.status(400).json({
            success: false,
            error: 'You must provide a body to update',
        })
    }

    DeviceType.findOne({ _id: req.params.id }, (err, deviceType) => {
        if (err) {
            return res.status(404).json({
                err,
                message: 'DeviceType not found!',
            })
        }
        deviceType.name = body.name
        deviceType
            .save()
            .then(() => {
                return res.status(200).json({
                    success: true,
                    id: deviceType._id,
                    message: 'DeviceType updated!',
                })
            })
            .catch(error => {
                return res.status(404).json({
                    error,
                    message: 'DeviceType not updated!',
                })
            })
    })
}

deleteDeviceType = async (req, res) => {
    await DeviceType.findOneAndDelete({ _id: req.params.id }, (err, deviceType) => {
        if (err) {
            return res.status(400).json({ success: false, error: err })
        }

        if (!deviceType) {
            return res
                .status(404)
                .json({ success: false, error: "DeviceType not found" })
        }

        return res.status(200).json({ success: true, data: deviceType })
    }).catch(err => console.log(err))
}

getDeviceTypeById = async (req, res) => {
    await DeviceType.findOne({ _id: req.params.id }, (err, deviceType) => {
        if (err) {
            return res.status(400).json({ success: false, error: err })
        }

        if (!deviceType) {
            return res
                .status(404)
                .json({ success: false, error: "DeviceType not found" })
        }
        return res.status(200).json({ success: true, data: deviceType })
    }).catch(err => console.log(err))
}

getDeviceTypes = async (req, res) => {
    await DeviceType.find({}, (err, deviceTypes) => {
        if (err) {
            return res.status(400).json({ success: false, error: err })
        }
        if (!deviceTypes.length) {
            return res
                .status(404)
                .json({ success: false, error: "DeviceType not found" })
        }
        return res.status(200).json({ success: true, data: deviceTypes })
    }).catch(err => console.log(err))
}

module.exports = {
    createDeviceType,
    updateDeviceType,
    deleteDeviceType,
    getDeviceTypes,
    getDeviceTypeById,
}
