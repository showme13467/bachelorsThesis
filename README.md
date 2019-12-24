# bachelorsThesis
Name Resolver for IoT Devices

[![npm Version](https://img.shields.io/badge/npm-6.13.4-brightgreen.svg)](https://www.npmjs.com/)
[![Node Version](https://img.shields.io/badge/node-12.14.0-brightgreen.svg)](https://nodejs.org)
[![MongoDb Version](https://img.shields.io/badge/mongodb-4.2-brightgreen.svg)](https://www.mongodb.com)


![Homepage Example Screenshot](https://raw.githubusercontent.com/showme13467/bachelorsThesis/master/Website.png)

## Live Demo

you can see a live demo at [NameResolver Page](http://irt-beagle.cs.columbia.edu) 

## Running the Project Locally

First, clone the repository to your local machine:

```bash
git clone https://github.com/showme13467/bachelorsThesis.git
```

Install the requirements :

```bash
cd NameResolver-app-full/client && npm install && ../server/ && npm install
```

Create MongoDB Database and name it *'NameResolver-Full'* or update index.js in db Folder:

`mongoose
    .connect('mongodb://127.0.0.1:27017/your-db-name', { useNewUrlParser: true, useUnifiedTopology: true })
    .catch(e => {
        console.error('Connection error', e.message)
    })`


Finally, run the development server in server folder:

```bash
npm run dev
```

The project will be available at **0.0.0.0:80**.
Change it by editing index.js:

`
const apiPort = 80
const apiHost = '0.0.0.0'
`

You can edit the frontend by running the client:
```bash
cd client/ && npm start
```

After editing all stuff type in your console:

```bash
 npm run build && cd ../server/ && rm build && cd ../client && cp -R /build ../server/
```
## Manual
If you want to use our program you can see all IoT devices without registration and access or connect to them as you like.

To unlock all features you have to register first and then log in on the website. 

#### How to get started
First, log in to the page and navigate to the tab *"create new plans "* . 

![Homepage Example Screenshot](https://raw.githubusercontent.com/showme13467/bachelorsThesis/master/example-images/Webpage1.PNG)

Afterwards you have the possibility to create either new buildings or floors for the respective buildings.

![Homepage Example Screenshot](https://raw.githubusercontent.com/showme13467/bachelorsThesis/master/example-images/Webpage2.PNG)

After creating a new floor, an image must be uploaded to have the matching floor plan for the floor. Make sure that the floor plan is not in blue or red, otherwise it would be counterproductive for the clarity. It is also recommended to upload an image with the dimensions 1200 x 800 pixels. If this should not be possible, the image will be automatically stretched or shrunk to 1200 x 800.

![Homepage Example Screenshot](https://raw.githubusercontent.com/showme13467/bachelorsThesis/master/example-images/Webpage3.PNG)

Now comes the most important part of the whole web application. On the floor plan you can set a point by pressing once with the left mouse button. After that you can set another point by pressing the left mouse button again. Pressing the mouse button again will delete all points again. Here it is important to make a square of the points as big as possible to get exact results. So choose a point as far as possible at the edge of your picture and see if it is physically possible to reach it. When you have set all the points you can either use Google Maps or your mobile phone to find out the coordinates of these points and enter them next to your floor plan. Note that google maps shows the coordinates in latitude and then longitude.

![Homepage Example Screenshot](https://raw.githubusercontent.com/showme13467/bachelorsThesis/master/example-images/Webpage4.PNG)

If you are satisfied with the points you have set, you can save them by clicking on the submit button. Make sure that the reference points you have set match the coordinates from google as closely as possible. This data is then used to convert all other pixels into coordinates.

After creation of the floor, the individual rooms can be created. This is where the Admin Panel comes into play.
The program is written in java 8, so make sure you have the latest version installed. 
In the panel, the tips for creating a room are shown below. So if you need help you only have to go through the tips and look at them. Nevertheless here is a quick introduction to the admin panel.

![Admin Example Screenshot](https://raw.githubusercontent.com/showme13467/bachelorsThesis/master/example-images/Admin1.PNG)

To create a room quickly, it is only necessary to draw the room with the help of individual points. To do so, select a point from the room and press the left mouse button. At the position of the cursor a letter is shown. If you click on another point of the room a line to the cursor will be drawn. You can do this until the room is drawn.

![Admin Example Screenshot](https://raw.githubusercontent.com/showme13467/bachelorsThesis/master/example-images/Admin2.PNG)

After you have created a room you only have to press Enter and name the room. After saving the room, it will be automatically transferred to the database and thanks to the created reference points, the pixels will be converted into coordinates.

The created room can be viewed only by selecting the appropriate room on the website. 
Now it is also possible to create a Device for this room. Just click on "create Device" to add a new Device.

![Homepage Example Screenshot](https://raw.githubusercontent.com/showme13467/bachelorsThesis/master/example-images/Webpage5.PNG)

Here it is not important how it is named. An algorithm names the corresponding Device according to its position. But it is important that you give the device the correct position. To do this, you can simply select the appropriate locations in the dropdown menus.

![Homepage Example Screenshot](https://raw.githubusercontent.com/showme13467/bachelorsThesis/master/example-images/Webpage6.PNG)

After confirming the entered data, it is now possible to move your device to the appropriate place in your room. First the new device is displayed in the upper left corner of the screen. By keeping the left mouse button pressed, you can move the device and place it in your room. The room where the Device should be is named and even if you place the device outside the room a query will intercept this and place it back into the room. (when you move the device a blue dot is placed at the old position. This symbolizes the previous position of the device and plays an important role later during the update).
It is possible that other blue dots are already in the room. These signalize already existing devices in the specific room.

![Homepage Example Screenshot](https://raw.githubusercontent.com/showme13467/bachelorsThesis/master/example-images/Webpage7.PNG)

Now you can see all devices either on the homepage or go to devices List. You can also click on the devices within the homepage and also go to the rooms to see the devices within the room in a list. 
