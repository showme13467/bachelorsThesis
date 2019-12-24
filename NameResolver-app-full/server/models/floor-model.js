const mongoose = require('mongoose')
const Schema = mongoose.Schema
const ObjectId = Schema.Types.ObjectId
const Decimal = Schema.Types.Decimal128



const Floor = new Schema(
    {
        name: { type: String, require: true },
        building: {type: ObjectId, require: true, default: "5de417b6fac8781e2f121f64"},
       /* image: { data :{
                         type: Buffer,
                         default: "none",
                         required: true
                            },
                 contentType: {
                            type: String,
                            default: "image/jpg",
                            required: true
                            }
               },*/
        refpoints: {pixel: {
                       type: [Number],
                       required: true
                           },
                    geocoord: {
                       type: [Decimal],
                       required: true
                              }
                   },
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

module.exports = mongoose.model('floors', Floor)

