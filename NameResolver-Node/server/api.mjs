import express from 'express';
import multer from 'multer';
import mongodb from 'mongodb';
import path from 'path';
import app from './main.js';

const { ObjectID } = mongodb;
const db = app.get('mongo_db').db();

     

let api;
export default api = express.Router();


api.get('/table', (req, res) => {
  
});

api.get('/table_detail/:id', (req, res) => {

});

api.get('/get_devices', (req, res) => {

});


api.get('/get_floors', (req, res) => {

});

api.get('/login', (req, res) => {

});

api.get('/register', (req, res) => {

});

api.get('/admin', (req, res) => {

});

api.get('/building/:id', (req, res) => {

});


api.get('/room/:id', (req, res) => {

});

api.get('/floor/:id', (req, res) => {

});

api.get('/table/search', (req, res) => {

});

api.get('/back', (req, res) => {

});



// api to handle all post requests
api.post('/create_device/', (req, res) => {
  console.log(req.body);
 let col_name = db.collection('devices');
col_name.findOne({$and:[{"name": req.body.name},{"room": req.body.room}]}, function(err,doc){
if(err){
console.log(err);
res.send(error.message);
}
else{
if(doc==null){
insert_device(req,res);
}
else{
console.log("Device already exists in that room");
res.send(doc);
}
}
});
});

function insert_device(req,res){
let col_name = db.collection('devices');
let obj = {"name": req.body.name, "type": req.body.type, "building": req.body.building, "floor": req.body.floor, "room": req.body.floor, "height": req.body.height, "url": req.body.url, "date_added": new Date() , "author": req.body.author, "image": req.body.image, "location": { "type": "Point", "coordinates": req.body.coordinates}};
  col_name.insertOne(obj, function(error, result) {
    if (error) {
      console.error(error.message);
      res.send(error.message);
    } else {
      update_room;
      res.send(result);
    }
  });
};

function update_room(req,res){
      let col_name = db.collection('rooms');
      let room_name = req.body.room;
      col_name.findOneAndUpdate({"name": room_name}, {$push: {devices: {[req.body.name]: res.ops[0]._id}}} , function(err, doc){
        if(err){
          console.log(err);
}
        else{
          console.log(doc);
}
});
};

api.post('/create_floor/', (req, res) => {
  console.log(req.body);
 let col_name = db.collection('floors');
col_name.findOne({"name": req.body.name}, function(err,doc){
if(err){
console.log(er);
res.send(err.message);
}
else{
if(doc==null){
insert_floor(req,res);
}
else{
console.log("Floor already exists!");
res.send(doc);
}
}
});
});

function insert_floor(req,res){
let col_name = db.collection('floors');
let obj = {"name": req.body.name, "coordinates": req.body.coordinates, "rooms":[] };
  col_name.insertOne(obj, function(err, result) {
    if (err) {
      console.error(err.message);
      res.send(err.message);
    } else {
      update_building(req,result);
      res.send(result);
    }
  });
};

function update_building(req,res){
      let col_name = db.collection('buildings');
      let building_name = req.body.building;
      col_name.findOneAndUpdate({"name": building_name}, {$push: {floors: {[req.body.name]: res.ops[0]._id}}} , function(err, doc){
        if(err){
          console.log(err);
}
        else{
          console.log(doc);
}
});
};



api.post('/create_room/', (req, res) => {
  console.log(req.body);
    let col_name = db.collection('rooms');
 col_name.findOne({"name": req.body.name}, function(err,doc){
if(err){
console.log(err);
res.send(error.message);
}
else{
if(doc==null){
insert_room(req,res);
}
else{
console.log("Room already exists!");
res.send(doc);
}
}
});
});

function insert_room(req,res){
let col_name = db.collection('rooms');
let obj = {"name": req.body.name, "coordinates": req.body.coordinates, "devices":[] }
  col_name.insertOne(obj, function(error, result) {
    if (error) {
      console.error(error.message);
      res.send(error.message);
    } else {
      update_floor(req,result);
      res.send(result)
    }
  });
};

function update_floor(req,res){
      let col_name = db.collection('floors');
      let floor_name = req.body.floor;	
      col_name.findOneAndUpdate({"name": floor_name}, {$push: {rooms: {[req.body.name]: res.ops[0]._id}}} , function(err, doc){
        if(err){
          console.log(err);
}
        else{
          console.log(doc);
}
});
};

api.post('/get_rooms/', (req,res) => {
  console.log(req.body);
  let col_name = db.collection('buildings')
  col_name.findOne({"name": req.body.building}, function(err, result){
if(err){
console.log(err);
res.send(err);
}
else{
console.log(result.floors);
res.send(result.floors);
}
});
});




api.post('/create_building/', (req, res) => {
 console.log(req.body);
 let col_name = db.collection('buildings');
col_name.findOne({"name": req.body.name}, function(err,doc){
if(err){
console.log(err);
res.send(error.message);
}
else{
if(doc==null){
insert_building(req,res);
}
else{
console.log("Building already exists!");
res.send(doc);
}
}
});
});

function insert_building(req,res){
let col_name = db.collection('buildings');
let obj = {"name": req.body.name, "floors":[] }
  col_name.insertOne(obj, function(error, result) {
    if (error) {
      console.error(error.message);
      res.send(error.message);
    } else {
      res.send(result)
    }
  });
};

api.post('/create_user/', (req, res) => {
  console.log(req.body);
  res.send(req.body);

});




//
// Returns the current state of UT devices as stored in the MongoDB
//

api.get('/', (req, res) => {
  // TODO
});

api.post('/', async (req, res) => {
  // TODO
});

api.get('*', (req, res) => {
  res.status(404).json({
    code: 404,
    reason: 'Not Found'
  });
});

