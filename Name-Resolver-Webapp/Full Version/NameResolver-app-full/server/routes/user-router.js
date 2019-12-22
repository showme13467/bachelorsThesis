const express = require('express');

const UserCtrl = require( '../controllers/user-ctrl');

const router = express.Router()

router.post('/register',  UserCtrl.createUser)
router.put('/user/:id', UserCtrl.updateUser)
router.delete('/user/:id', UserCtrl.deleteUser)
router.get('/user/:id', UserCtrl.getUserById)
router.post('/login', UserCtrl.getUsers)

module.exports = router

