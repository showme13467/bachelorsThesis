import express from 'express';
import multer from 'multer';
import mongodb from 'mongodb';
import path from 'path';
import app from './main.js';

const { ObjectID } = mongodb;
const db = app.get('mongo_db').db();


let api;
export default api = express.Router();


api.get('/your_get_endpoint/', (req, res) => {
  // TODO
});

api.post('/your_post_endpoint/', async (req, res) => {
  // TODO
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

