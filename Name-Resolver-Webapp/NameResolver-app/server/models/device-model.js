const mongoose = require('mongoose')
const Schema = mongoose.Schema
const ObjectId = Schema.Types.ObjectId



const Device = new Schema(
    {
        name: { type: String, require: true },
        type: { type: ObjectId, require: true},
        room: {type: ObjectId, require: true, default: "5de417b6fac8781e2f121f64"},
        height: { type: String, require: true, default: "8 m"},
        url:{type: String, require: true, default: "http://test.com" },
        author:{type: ObjectId, require: true, default: "5de417b6fac8781e2f121f64"},
        location: {type: {
                     type: String,
                     enum: ['Point'],
                     required: true
      },
                   coordinates: {
                     type: [Number],
                     required: true
}
}
    },
    { timestamps: true },
);

module.exports = mongoose.model('devices', Device)
