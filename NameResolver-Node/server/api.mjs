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

api.get('/get_rooms', (req, res) => {

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
	res.status(200).send();
});

api.post('/create_floor/', (req, res) => {

});

api.post('/create_room/', (req, res) => {

});

api.post('/create_building/', (req, res) => {

});

api.post('/create_user/', (req, res) => {

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

