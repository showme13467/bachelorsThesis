const mongoose = require('mongoose')
const Schema = mongoose.Schema
const ObjectId = Schema.Types.ObjectId


const Type = new Schema(
{
name : {type: String, require: true, default: "Type"}
}
);


module.exports = mongoose.model('deviceTypes', Type)

