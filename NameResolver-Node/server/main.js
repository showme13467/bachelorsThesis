const http      = require('http'),
      stdio     = require('stdio'),
      debug     = require('debug'),
      fs        = require('fs'),
      mongodb   = require('mongodb'),
      cors      = require('cors'),
      morgan    = require('morgan'),
      path      = require('path'),
      express   = require('express'),
      nunjucks  = require('nunjucks'),
      socket_io = require('socket.io');

const dbg = debug('lmr:main');

const DEFAULT_MONGODB_URL = 'mongodb://127.0.0.1:27017/mongoDB_Node',
      DEFAULT_SOCK = '3000',
      devMode = process.env.NODE_ENV === "development",
      ui_dir = path.resolve(__dirname, '..'),
      static_dir = path.resolve(ui_dir, 'static');


async function main() {
  dbg(`Starting LMR emulator backend server in ${devMode ? 'development' : 'production'} mode`);

  global.opts = stdio.getopt({
    sock: {
      key: 's',
      args: 1,
      description: 'UNIX domain socket or port number to listen on for HTTP requests',
      default: process.env.SOCK || DEFAULT_SOCK
    },
    db: {
      key: 'd',
      args: 1,
      description: 'MongoDB database URL',
      default: process.env.MONGODB_URL || DEFAULT_MONGODB_URL
    },
  });

  const app = express();

  module.exports = app;

  nunjucks.configure(path.resolve(ui_dir, 'NameResolver-Node/views'), {
    autoescape: true,
    express: app
  });

  app.use(cors());
  app.options('*', cors());

  app.use(morgan(devMode ? 'dev' : 'combined'));

  app.use(express.json());


  if (devMode) {
    //
    // If we are running in development mode, dynamically load the
    // webpack-dev-middleware plugin and generate bundles for the
    // frontend on the fly. Re-generate the bundle if any of its
    // dependencies change.
    //
    const webpack_config = require('../webpack.config.js'),
          // eslint-disable-next-line import/no-extraneous-dependencies
          webpack        = require('webpack'),
          compiler       = webpack(webpack_config),
          // eslint-disable-next-line import/no-extraneous-dependencies
          middleware     = require('webpack-dev-middleware');

    app.use(middleware(compiler, {
      publicPath: webpack_config.output.publicPath,
      logLevel: process.env.WEBPACK_LOGLEVEL || 'info'
    }));
    // eslint-disable-next-line import/no-extraneous-dependencies
    app.use(require("webpack-hot-middleware")(compiler));
  } else {
    //
    // If we're running in production mode, serve pre-generated bundle
    // files from the folder 'bundle' with express.static module. If
    // the client requests a file from the bundle folder that does not
    // exist, generate an error rather than continuing to other
    // plugins.
    //
    app.use('/bundle', express.static('bundle', {
      fallthrough: false
    }));
  }


  dbg('Connecting to MongoDB at %s', opts.db);
  app.set('mongo_db', await mongodb.MongoClient.connect(opts.db, {
      useNewUrlParser: true,
      useUnifiedTopology: true,
      autoReconnect: true,
      reconnectInterval: 1000,
      reconnectTries: Number.MAX_VALUE
  }));
  dbg('Connected to MongoDB');


  // Create a HTTP server for the API and a socket.io based
  // WebSocket server.
  dbg(`Creating HTTP API server`);
  const http_server = http.createServer(app);

  dbg(`Creating socket.io WebSocket server`);
  const ws_server = socket_io(http_server);
  app.set('socketio_server', ws_server);



  // Automatically handle requests to join/leave a particular room.
  ws_server.on('connection', s => {
    dbg(`WebSocket client connected: ${s.id}`);
    setup_pubsub(s);
    s.on('disconnect', () => {
      dbg(`${s.id} disconnected`);
    });
  });



  // The following import generates a cyclic dependency, however, that's not
  // a problem in this particular case, because modules imported from api.mjs
  // import this module for for the express application, which we assigned to
  // module.exports before the import is invoked.
  //
  // Note: Pay attention to the order in which various subsystems are
  // initialized. The api.mjs module serves as the main entry point to our
  // backend application and we should make sure that the server is
  // reasonably initialized. In particular, make sure that the Mongo
  // database is connected, the WebSocket server has been created, and that
  // the ZeroConf discovery server has been started. All these "singleton"
  // objects should be set on the Express application before the api.mjs
  // module is imported.
  //
  dbg(`Loading the main application module api.mjs`);
  // eslint-disable-next-line import/no-cycle

 const api_router = (await import('./api.mjs')).default;
  app.use('/api', api_router);

  //
  // Serve static files from the "static" folder. This folder contains
  // the main index.html file, favicon.ico, and service-worker.js.
  //
  app.use('/', express.static('static'));

  //
  // The following route is here to support browser-side routing. When
  // react-router (or a similar framework) changes the HTTP URL of the
  // site and the user reload the page, we need to serve the main
  // index.html file. react-router will take care of navigating the
  // user to the correct component within the site.
  //

  const cfg = fs.readFileSync(path.resolve(static_dir, 'config.json'));

  app.get('*', (req, res) => {
    res.render('index.njk', { config: cfg });
  });


  const sock = parseInt(opts.sock, 10);
  if (Number.isNaN(sock)) {
    dbg(`Starting HTTP server on UNIX domain socket ${opts.sock}`);
    fs.stat(opts.sock, error => {
      if (!error) fs.unlinkSync(opts.sock);
      http_server.listen(opts.sock, () => {
        fs.chmodSync(opts.sock, '666');
      });
    });
  } else {
    if (sock <= 0 || sock >= 65536)
      throw new Error(`Error: Invalid port number ${opts.sock}`);
    dbg(`Starting HTTP server on TCP port ${opts.sock}`);
    http_server.listen(sock);
  }
}


main().catch(err => {
  // eslint-disable-next-line no-console
  console.error(err);
  process.exit(1);
});

