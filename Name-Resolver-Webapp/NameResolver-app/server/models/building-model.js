const mongoose = require('mongoose')
 

const Building =  mongoose.Schema(
    {
        name: { type: String, require: true },
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

module.exports = mongoose.model('buildings', Building)

