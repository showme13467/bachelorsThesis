const mongoose = require('mongoose')
const Schema = mongoose.Schema
const ObjectId = Schema.Types.ObjectId
const Decimal = Schema.Types.Decimal128



const Device = new Schema(
    {
        name: { type: String, require: true },
        type: { type: ObjectId, require: true},
        room: {type: ObjectId, require: true, default: "5de417b6fac8781e2f121f64"},
        floor: {type: ObjectId, require: true, default: "5de417b6fac8781e2f121f64"},
        building: {type: ObjectId, require: true, default: "5de417b6fac8781e2f121f64"},
        height: { type: Number, require: true, default: 8},
        url:{type: String, require: true, default: "http://test.com" },
        author:{type: ObjectId, require: true, default: "5de417b6fac8781e2f121f64"},
        pixel: {type: [Number],  require: true, default: [100,100]},
        location: {type: {
                     type: String,
                     enum: ['Point'],
                     required: true
      },
                   coordinates: {
                     type: [Decimal],
                     required: true
}
}
    },
    { timestamps: true },
);

module.exports = mongoose.model('devices', Device)
