const mongoose = require('mongoose')
const Schema = mongoose.Schema
const ObjectId = Schema.Types.ObjectId



const Room = new Schema(
    {
        name: { type: String, require: true },
        floor: {type: ObjectId, require: true, default: "5de417b6fac8781e2f121f64"},
        geometry: {type: {
                     type: String,
                     enum: ['Polygon'],
                     required: true
      },
                   coordinates: {
                     type:[[[Number]]],
                     required: true
}
}
    },
    { timestamps: true },
);

module.exports = mongoose.model('rooms', Room)

