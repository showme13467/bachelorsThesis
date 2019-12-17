from datetime import datetime
from nameresolver import *
from flask_login import UserMixin



@login_manager.user_loader
def load_user(user_id):
    return User.query.get(user_id)





class Building(db.Document):
   # id = db.IntField(primary_key=True)
    buildingname = db.StringField()
  #  floors = db.relationship('Floor', backref='building', lazy=True)

    def __repr__(self):
        return f"Building('{self.buildingname}')"


    def get_id(self):
        return str(self.mongo_id)


class Floor(db.Document):
   # id = db.IntField(primary_key=True)
    floorname = db.StringField()
  #  rooms = db.relationship('Room', backref='floor', lazy=True)
 #   building_id = db.StringField(db.Integer, db.ForeignKey('building.id'), nullable=False)

    def __repr__(self):
        return f"Floor('{self.floorname}')"


    def get_id(self):
        return str(self.mongo_id)


class Room(db.Document):
    #id = db.IntField( primary_key=True)
    roomname = db.StringField()
    #devices = db.relationship('Device', backref='room', lazy=True)
   # floor_id = db.IntField( db.ForeignKey('floor.id'), nullable=False)

    def __repr__(self):
        return f"Room('{self.roomname}')"

    def get_id(self):
        return str(self.mongo_id)

class User(db.Document, UserMixin):
    username = db.StringField()
    email = db.StringField()
    image_file = db.StringField(default='default.jpg')
    password = db.StringField()
    admin = db.BoolField(default=False)
    #devices = db.relationship('Device', backref='author', lazy=True)
    #room_id = db.StringField(db.Integer, db.ForeignKey('room.id'), nullable=False)

    def __repr__(self):
        return f"User('{self.username}', '{self.email}', '{self.image_file}')"

    def get_id(self):
        return str(self.mongo_id)

class Device(db.Document):
   #id = db.IntField(primary_key=True)
    devicename = db.StringField()
    type = db.StringField()
    longitude = db.IntField( default='0')
    latitude = db.IntField( default='0')
    height = db.IntField( default='0')
    url = db.StringField(default='http://NameofTheIotDevice.com')
    image_file = db.StringField(default='defaultDevice.jpg')
    date_added = db.DateTimeField(default=datetime.utcnow())
  #  room_id = db.StringField(db.Integer, db.ForeignKey('room.id'), nullable=False)


    def __repr__(self):
        return f"Device('{self.devicename}', '{self.type}','{self.url}','{self.image_file}')"

    def get_id(self):
        return str(self.mongo_id)