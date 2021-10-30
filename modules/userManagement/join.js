var uuidv4 = require('uuid/v4');
var crypto = require('crypto');
var bodyParser=require('body-parser');



module.exports.join = function(sqlClient, userData, response){

	console.log('module join called');
	console.log(userData);

        sqlClient.query('SELECT uuid FROM userTBL WHERE email=?', [userData.email], function(err, result){
                var resultLength = Object.keys(result).length
                if(resultLength==0){	//해당 정보로 등록된 회원이 없음 

        		var hash = crypto.createHash('md5').update(userData.password).digest('base64'); // 회원정보 비밀번호 암호화 
			var uuid = uuidv4(); 	// 회원 고유 식별자 생성
                        sqlClient.query('INSERT INTO userTBL VALUES(?,?,?,null)', [userData.email, uuid, hash], function(err, result){
                                console.log('insert complete');
                                console.log(result);
				response.json({
                                        'code' : 200,
                                        'message' : 'join success',
                                        'uuid' : uuid
				});


                                        
                        });

                }else{
                        console.log('join fail exist email');
                        
			response.json({
                                'code' : 412,
                                'message' : 'exist email',
                                'uuid' : null
                        });


                }
        });






}



