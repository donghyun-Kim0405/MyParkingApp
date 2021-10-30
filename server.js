var express=require('express');
var bodyParser=require('body-parser');
var multer = require('multer');
var path = require('path');
var mysql=require('mysql');
var uuidv4 = require('uuid/v4');
var crypto = require('crypto');
var joinUtil = require('./modules/userManagement/join.js');
var loginUtil = require('./modules/userManagement/login.js');
var parkingArea = require('./modules/service/parkingArea.js');
var board = require('./modules/service/board.js');
var authUtil = require('./modules/userManagement/auth.js');
var storage;
var app=express();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

var client=mysql.createConnection({
	user: 
	password: 
	database: 'parkingAppDB'
});


app.post('/board/get', function(request, response){
	board.getData(client, request.body, response);
})
app.post('/board/write', function(request, response){
	console.log('boardwrite called');
	board.write(client, request.body, response);
})
app.post('/user/auth', function(request, response){
	authUtil.auth(client, request.body, response);
});
app.post('/user/join', function(request, response){
	joinUtil.join(client, request.body, response);
});
app.post('/user/login', function(request, response){
	loginUtil.login(client, request.body, response);
});
app.post('/download/parkingAreaData', function(request, response){
	parkingArea.getData(client, request.body, response)
});
app.post('/download/bookmark/parkinglot', function(request, response){
        console.log(request.body);
	parkingArea.getBookmarkParkingLot(client, request.body, response)
});


app.listen(80, function(){
	console.log('server running :80');
});
