import secrets
import os
from PIL import Image
from flask import render_template, url_for, flash, redirect, request
from nameresolver import *
from nameresolver.forms import *
from nameresolver.models import *
from flask_login import login_user, current_user, logout_user, login_required




@app.route('/home')
@app.route('/')
def home():
    return render_template('home.html')



@app.route('/table')
@login_required
def table_full():
    devices = Device.query.all()
   # buildings = Building.query.all()
    #floors = Floor.query.all()
    #rooms = Room.query.all()
    return render_template('table_full.html', title= 'Table full view', devices=devices )#buildings=buildings, floors=floors, rooms=rooms)

@app.route('/table/<device_id>')
@login_required
def deviceDetail(device_id):
    device = Device.query.get_or_404(device_id)
    return render_template('device_detail.html',title=device.devicename, device=device)



@app.route('/register', methods=['GET', 'POST'])
def register():
    if current_user.is_authenticated:
        return redirect(url_for('home'))
    form = RegistrationForm()
    if form.validate_on_submit():
        hashed_password = bcrypt.generate_password_hash(form.password.data).decode('utf-8')
        user = User(username=form.username.data, email=form.email.data, password=hashed_password)
        user.save()
        flash(f'Your Account has been created! You are now able to log in !', 'success')
        return redirect(url_for('home'))
    return render_template('register.html', form=form, title= 'Register Page')

@app.route('/login', methods=['GET', 'POST'])
def login():
    if current_user.is_authenticated:
        return redirect(url_for('home'))
    form = LoginForm()
    if form.validate_on_submit():
        user = User.query.filter(User.email==form.email.data).first()
        if user and bcrypt.check_password_hash(user.password, form.password.data):
            login_user(user, remember=form.remember.data)
            next_page = request.args.get('next')
            if next_page:
                return redirect(next_page)
            else:
                return redirect(url_for('home'))
        else:
            flash('Login Unsuccessful, Please check email and password' , 'danger')
    return render_template('login.html', form=form, title= 'Login Page')

@app.route('/logout')
def logout():
    logout_user()
    return redirect(url_for('home'))

def save_picture(form_picture):
    random_hex = secrets.token_hex(8)
    _, f_ext = os.path.splitext(form_picture.filename)
    picture_fn = random_hex + f_ext
    picture_path = os.path.join(app.root_path, 'static/profile_pics', picture_fn)

    output_size = (125,125)
    i = Image.open(form_picture)
    i.thumbnail(output_size)
    i.save(picture_path)

    return picture_fn

@app.route('/account', methods=['GET', 'POST'])
@login_required
def account():
    form = UpdateAccountForm()
    if form.validate_on_submit():
        if form.picture.data:
            picture_file = save_picture(form.picture.data)
            current_user.image_file = picture_file
        current_user.username = form.username.data
        current_user.email = form.email.data
        current_user.save()
        flash('Your account has been updated!', 'success')
        return redirect(url_for('account'))
    elif request.method == 'GET':
        form.username.data = current_user.username
        form.email.data = current_user.email
    image_file = url_for('static', filename='profile_pics/' + current_user.image_file)
    return render_template('account.html', title='Account Info', image_file=image_file, form=form)

@app.route('/createPlan', methods=['GET', 'POST'])
@login_required
def createPlan():
    if current_user.admin == True:
        return render_template('createPlan.html',  title='Site Plan Creation')
    return redirect(url_for('home'))

@app.route('/createDevice', methods=['GET', 'POST'])
@login_required
def createDevice():
    if current_user.admin == True:
        form = CreateDeviceForm()
        if form.validate_on_submit():
            device = Device(devicename=form.devicename.data, type=form.type.data, height=form.height.data ,longitude=form.longitude.data, latitude=form.latitude.data, url=form.url.data, image_file=form.picture.data)
            device.save()
            flash('Your Device has been created!', 'success')
            return redirect(url_for('table_full'))
        return render_template('create_device.html', title= 'New Device', form=form )
    else:
        flash('You are not authorized to access this site', 'warning')
        return redirect(url_for('home'))