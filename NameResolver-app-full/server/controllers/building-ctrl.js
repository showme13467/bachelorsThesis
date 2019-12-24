const  Building = require ('../models/building-model')



createBuilding = (req, res) => {
    const body = req.body
    console.log(req.body);
    if (!body) {
        return res.status(400).json({
            success: false,
            error: 'You must provide a building',
        })
    }

    const building = new Building(body)

    if (!building) {
        return res.status(400).json({ success: false, error: err })
    }
    building
        .save()
        .then(() => {
            return res.status(201).json({
                success: true,
                id: building._id,
                message: 'Building created!',
            })
        })
        .catch(error => {
            return res.status(400).json({
                error,
                message: 'Building not created!',
            })
        })
}

updateBuilding = async (req, res) => {
    const body = req.body

    if (!body) {
        return res.status(400).json({
            success: false,
            error: 'You must provide a body to update',
        })
    }

    Building.findOne({ _id: req.params.id }, (err, building) => {
        if (err) {
            return res.status(404).json({
err,
 message: 'Building not found!',
            })
        }
        building.name = body.name
        building.geometry = body.geometry
        building
            .save()
            .then(() => {
                return res.status(200).json({
                    success: true,
                    id: building._id,
                    message: 'Building updated!',
                })
            })
            .catch(error => {
                return res.status(404).json({
                    error,
                    message: 'Building not updated!',
                })
            })
    })
}

deleteBuilding = async (req, res) => {
    await Building.findOneAndDelete({ _id: req.params.id }, (err, building) => {
        if (err) {
            return res.status(400).json({ success: false, error: err })
        }

        if (!building) {
            return res
                .status(404)
                .json({ success: false, error: `Building not found` })
        }

        return res.status(200).json({ success: true, data: building })
    }).catch(err => console.log(err))
}

getBuildingById = async (req, res) => {
    await Building.findOne({ _id: req.params.id }, (err, building) => {
 if (err) {
 return res.status(400).json({ success: false, error: err })
        }

        if (!building) {
            return res
                .status(404)
                .json({ success: false, error: `Building not found` })
 }
        return res.status(200).json({ success: true, data: building })
    }).catch(err => console.log(err))
}

getBuildings = async (req, res) => {
    await Building.find({}, (err, buildings) => {
        if (err) {
            return res.status(400).json({ success: false, error: err })
        }
        if (!buildings.length) {
            return res
                .status(404)
                .json({ success: false, error: `Building not found` })
        }
        return res.status(200).json({ success: true, data: buildings })
    }).catch(err => console.log(err))
}

module.exports = {
    createBuilding,
    updateBuilding,
    deleteBuilding,
    getBuildings,
    getBuildingById,
}


