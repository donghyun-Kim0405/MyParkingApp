var uuidv4 = require('uuid/v4');
var crypto = require('crypto');
var bodyParser=require('body-parser');
var tokenManager = require('./tokenManager');



module.exports.login = function(sqlClient, userData, response){


	sqlClient.query('SELECT password, uuid FROM userTBL WHERE email=?', [userData.email], function(err, result){

                if(Object.keys(result).length != 0){

                var passFromDB=result[0].password;
                var uuidFromDB=result[0].uuid;
		var hash = crypto.createHash('md5').update(userData.password).digest('base64'); // 회원정보 비밀번호 암호화

                if(hash == passFromDB){

                        console.log('login permitted');
			
			var token = tokenManager.generate(result[0]);//동기식 처리 고려!

                        response.json({
                                'code' : 200,
                                'message' : 'login permitted',
                                'uuid' : uuidFromDB,
				'token' : token,
				'email' : userData.email
                        });
                }else{

                        console.log('login denied');
                        response.json({
                                'code' : 412,
                                'message' : '잘못된 비밀번호 입니다.',
                                'uuid' : null,
				'token':null

                        });
                }
                }else{
                        response.json({
                                        'code' : 412,
                                        'message' : '잘못된 이메일 입니다.',
                                        'uuid' : null,
					'token': null
                                 });

                        console.log('login denied for wrong email');
                }
        });



}

