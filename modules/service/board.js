var tokenManager = require('/home/ubuntu/NODEJS/parkingApp/modules/userManagement/tokenManager.js');



module.exports.getData = function(sqlClient, userData, response){

	var identifier = userData.parkingLotNumber;
	sqlClient.query('SELECT * FROM boardTBL WHERE parkingLotNumber=? ORDER BY time DESC', [identifier], function(error, results){
                response.send(results);
        });

}


module.exports.write = function(sqlClient, userData, response){
	console.log(userData);
	var tokenResult=tokenManager.verify(userData.token);
	
	if(tokenResult!=500){
		sqlClient.query('SELECT email FROM userTBL WHERE uuid=?', [tokenResult.uuid], function(err, result){

                var email = result[0].email;

                sqlClient.query('INSERT INTO boardTBL VALUES(?,?,?,?,?,?)', [userData.title, userData.content, tokenResult.uuid, email, userData.time, userData.parkingLotNumber], function(error, result){

                        response.json({
                                'code' : 200,
                                'message' : 'insert complete',
                                'uuid' : null
                        });
                })

        })
	}else{
		response.json({
			'code':500,
			'message':'login error'
		});
	}
	

}


