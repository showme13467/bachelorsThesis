from flask import Flask, render_template, url_for, flash, redirect
from nameresolver import *
from nameresolver.forms import *
from nameresolver.models import *




device =[
    {
        'author': 'schaumi',
        'name': 'smartTv',
        'location': 'shapiro',
        'date_added': 'heute'
    },
    {
        'author': 'schaumi',
        'name': 'router',
        'location': 'shapiro',
        'date_added': 'heute'
    }
]

@app.route('/home')
@app.route('/')
def home():
    return render_template('home.html')


@app.route('/table')
def table_full():
    return render_template('table_full.html', devices=device, title= 'Table full view')

@app.route('/register', methods=['GET', 'POST'])
def register():
    form = RegistrationForm()
    if form.validate_on_submit():
        hashed_password = bcrypt.generate_password_hash(form.password.data).decode('utf-8')
        user = User(username=form.username.data, email=form.email.data, password=hashed_password)
        db.session.add(user)
        db.session.commit()
        flash(f'Your Account has been created! You are now able to log in !', 'success')
        return redirect(url_for('home'))
    return render_template('register.html', form=form, title= 'Register Page')

@app.route('/login', methods=['GET', 'POST'])
def login():
    form = LoginForm()
    if form.validate_on_submit():
        if form.email.data == 'admin@test.com' and form.password.data == '13467':
            flash('You have been logged in!', 'success')
            return redirect(url_for('home'))
        else:
            flash('Login Unsuccessful, Please check username and password' , 'danger')
    return render_template('login.html', form=form, title= 'Login Page')
