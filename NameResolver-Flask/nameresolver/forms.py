from flask_wtf import FlaskForm
from flask_wtf.file import FileAllowed, FileField
from flask_login import current_user
from wtforms import StringField, PasswordField, SubmitField, BooleanField, FloatField, IntegerField, DateTimeField
from wtforms.validators import DataRequired, Length, Email, EqualTo, ValidationError
from nameresolver.models import *


class RegistrationForm(FlaskForm):
    username = StringField('Username', validators=[DataRequired(), Length(min=2, max=20)])
    email = StringField('Email', validators=[DataRequired(), Email() ])
    password = PasswordField('Password', validators=[DataRequired()])
    confirm_password = PasswordField('Confirm Password', validators=[DataRequired(), EqualTo('password')])
    submit = SubmitField('Sign Up')

    def validate_username(self, username):
        user= User.query.filter(User.username==username.data).first()
        if user:
            raise ValidationError('That username is taken. Please choose a different one.')

    def validate_email(self, email):
        email= User.query.filter(User.email==email.data).first()
        if email:
            raise ValidationError('That email is taken. Please choose a different one.')

class LoginForm(FlaskForm):
    email = StringField('Email', validators=[DataRequired(), Email() ])
    password = PasswordField('Password', validators=[DataRequired()])
    remember = BooleanField('Remember Me')
    submit = SubmitField('Login')

class UpdateAccountForm(FlaskForm):
    username = StringField('Username', validators=[DataRequired(), Length(min=2, max=20)])
    email = StringField('Email', validators=[DataRequired(), Email()])
    picture = FileField('Update Profile Picture', validators=[FileAllowed(['jpg', 'png'])])
    submit = SubmitField('Update')

    def validate_username(self, username):
        if username.data != current_user.username:
            user= User.query.filter(User.username==username.data).first()
            if user:
                raise ValidationError('That username is taken. Please choose a different one.')

    def validate_email(self, email):
        if email.data != current_user.email:
            email= User.query.filter(User.email==email.data).first()
            if email:
                raise ValidationError('That email is taken. Please choose a different one.')

class CreateDeviceForm(FlaskForm):
    devicename = StringField('Name', validators=[DataRequired()])
    type = StringField('Type', validators=[DataRequired()])
    #room = IntegerField('Room', validators=[DataRequired()])
    longitude = IntegerField('Longitude', default='0')
    latitude = IntegerField('Latitude', default='0')
    height = IntegerField('Height', default='0')
    url = StringField('URL', default='www.google.com')
    date = DateTimeField('Date', default=datetime.utcnow())
    picture = FileField('Device Picture', validators=[FileAllowed(['jpg', 'png'])], default='defaultDevice.jpg')
    submit = SubmitField('Post')

   # def validate_room(self, room):
    #    rooms = Room.query.get(room.data)
     #   if rooms==None :
      #      raise ValidationError('That Room is not taken. Please Choose another one.')

