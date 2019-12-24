# bachelorsThesis
Name Resolver for IoT Devices

[![npm Version](https://img.shields.io/badge/npm-6.13.4-brightgreen.svg)](https://www.npmjs.com/)
[![Node Version](https://img.shields.io/badge/node-12.14.0-brightgreen.svg)](https://nodejs.org)
[![MongoDb Version](https://img.shields.io/badge/mongodb-4.2-brightgreen.svg)](https://www.mongodb.com)


![Homepage Example Screenshot](https://raw.githubusercontent.com/showme13467/bachelorsThesis/master/Website.png)


## Running the Project Locally

First, clone the repository to your local machine:

```bash
git clone https://github.com/showme13467/bachelorsThesis.git
```

Install the requirements :

```bash
cd Name-Resolver-Webapp/Full\ Version/NameResolver-app-full/client && rm package-lock.json && npm install && cd ../server/ && rm package-lock.json && npm install
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
