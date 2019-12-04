const mongoose = require('mongoose')
const Schema = mongoose.Schema
const ObjectId = Schema.Types.ObjectId


const Type = new Schema(
{
type : {type: String, require: true, default: "5de6caf2ae66285bfb8d58e0"}
}
);


module.exports = mongoose.model('deviceTypes', Type)

