const eventemitter = require('events');

var url = 'http://mylogger.io/log';


class Logger extends eventemitter{
    log(message) {

        console.log(message);

        this.emit('messageLogged', {id:1 , url: 'http://'});
    }

}

module.exports =Logger;