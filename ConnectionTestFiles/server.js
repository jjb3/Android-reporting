//Server side script
//run with "node server.js"

var express = require('express');
var cors = require('cors');
var bodyParser = require('body-parser');
var app = express();
app.use(cors());
var fs = require("fs");
var path = require('path');

var mysql = require('mysql');
var message = 'Server listening at http://%s:%s';
var host = "localhost"
var connection;

// app.use(express.static(__dirname));
//app.set('views', __dirname);
//app.engine('html', require('ejs').renderFile);
//app.set('view engine', 'html');
//app.use(bodyParser.urlencoded({
//   extended: true
// }));
//app.use(bodyParser.json());

// parse application/x-www-form-urlencoded
app.use(bodyParser.urlencoded({ extended: true }));

// parse application/json
app.use(bodyParser.json());


//Get all rows from tablets tables
app.get('//listDevices', function (req, res) {
var queryString = 'SELECT * FROM Device';
 
   connection.query(queryString, function(err, rows, fields) {
       if (err) throw err;

       data = JSON.stringify(rows);
       res.end(data);
   });
})

//Print hello world 
app.get('//hello', function (req, res) {
       data = "Hello world!";
       res.end(data);
})

// app.get('//cartoDBTest', function (req, res) {
//        app.use(express.static(path.join(__dirname, 'public')));
//        res.render('index.html');
//      })


app.get('/', function (req, res) {
       data = "Try:\n/node/hello \n/node/listDevices";
       res.end(data);
})

//Print hello world 
app.get('//', function (req, res) {
       data = "Try:\n/node/hello \n/node/listDevices";
       res.end(data);
})

app.post('//sendDummyData', function(req, res){
    //console.log('POST /');
    //console.dir(req.body);
    //var jsonObject = JSON.parse(req.body);
    console.log(req.body["data"]);
    res.writeHead(200, {'Content-Type': 'text/html'});
    res.end('thanks');

    var queryString = 'INSERT INTO Dummy(`data`) VALUES ("' + req.body["data"]+'")';
 
    connection.query(queryString, function(err, rows, fields) {
         if (err) throw err;

         data = JSON.stringify(rows);
         res.end(data);
     });
});


//Open the server and listen on 8081 
var server = app.listen(8081, function () {

  var host = server.address().address;
  var port = server.address().port;
  console.log(message, host, port);
})


//auto reconnect
function handleDisconnect() {

  //Authentications used to creat the connection
  connection = mysql.createConnection(
      {
        host     : 'localhost',
        user     : 'root',
        password : 'We011241',
        database : 'Tablets_Tracking',
        insecureAuth: 'true',
      }   
  );

  connection.connect(function(err) {              // The server is either down
    if(err) {                                     // or restarting (takes a while sometimes).
      console.log('error when connecting to db:', err);
      setTimeout(handleDisconnect, 2000); // We introduce a delay before attempting to reconnect,
    }                                     // to avoid a hot loop, and to allow our node script to
  });                                     // process asynchronous requests in the meantime.
                                          // If you're also serving http, display a 503 error.
  connection.on('error', function(err) {
    console.log('db error', err);
    if(err.code === 'PROTOCOL_CONNECTION_LOST') { // Connection to the MySQL server is usually
      handleDisconnect();                         // lost due to either server restart, or a
    } else {                                      // connnection idle timeout (the wait_timeout
      throw err;                                  // server variable configures this)
    }
  });
}

handleDisconnect();
