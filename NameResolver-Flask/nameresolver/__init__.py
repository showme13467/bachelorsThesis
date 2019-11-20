from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from flask_bcrypt import Bcrypt
from flask_login import LoginManager
from flask_mongoalchemy import MongoAlchemy





app = Flask(__name__)
app.config['SECRET_KEY'] = 'e10d5d0e3c836677eeeb8a24ae7403b1'
app.config['MONGOALCHEMY_DATABASE'] ='mongoDB_Flask'
app.config['MONGOALCHEMY_CONNECTION_STRING'] ='mongodb://127.0.0.1:27017/mongoDB_Flask'
#app.config['SQLALCHEMY_DATABASE_URI'] ='sqlite:///site.db'
db = MongoAlchemy(app)
#db = SQLAlchemy(app)

bcrypt = Bcrypt(app)
login_manager = LoginManager(app)
login_manager.login_view = 'login'
login_manager.login_message_category = 'info'


from nameresolver import routes
