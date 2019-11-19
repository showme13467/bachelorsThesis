from datetime import datetime
from nameresolver import db

class Building(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    buildingname = db.Column(db.String(100), unique=True, nullable=False)
    floors = db.relationship('Floor', backref='building', lazy=True)

    def __repr__(self):
        return f"Building('{self.buildingname}')"


class Floor(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    floorname = db.Column(db.String(100), unique=True, nullable=False)
    rooms = db.relationship('Room', backref='floor', lazy=True)
    building_id = db.Column(db.Integer, db.ForeignKey('building.id'), nullable=False)

    def __repr__(self):
        return f"Floor('{self.floorname}')"


class Room(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    roomname = db.Column(db.String(100), unique=True, nullable=False)
    devices = db.relationship('Device', backref='room', lazy=True)
    floor_id = db.Column(db.Integer, db.ForeignKey('floor.id'), nullable=False)

    def __repr__(self):
        return f"Room('{self.roomname}')"

class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(20), unique=True, nullable=False)
    email = db.Column(db.String(120), unique=True, nullable=False)
    image_file = db.Column(db.String(20), nullable=False, default='default.jpg')
    password = db.Column(db.String(60), nullable=False)
    #devices = db.relationship('Device', backref='author', lazy=True)
    #room_id = db.Column(db.Integer, db.ForeignKey('room.id'), nullable=False)

    def __repr__(self):
        return f"User('{self.username}', '{self.email}', '{self.image_file}')"



class Device(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    devicename = db.Column(db.String(100), nullable=False)
    type = db.Column(db.String(120), nullable=False)
    longitude = db.Column(db.Integer, nullable=False, default='0')
    latitude = db.Column(db.Integer, nullable=False, default='0')
    height = db.Column(db.Integer, nullable=False, default='0')
    url = db.Column(db.String(120), nullable=True, default='http://NameofTheIotDevice.com')
    image_file = db.Column(db.String(20), nullable=True, default='default.jpg')
    date_added = db.Column(db.DateTime, nullable=False, default=datetime.utcnow)
    room_id = db.Column(db.Integer, db.ForeignKey('room.id'), nullable=False)


    def __repr__(self):
        return f"Device('{self.devicename}', '{self.type}','{self.url}','{self.image_file}')"

