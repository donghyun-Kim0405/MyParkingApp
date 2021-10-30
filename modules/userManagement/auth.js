var tokenManager = require('./tokenManager');

module.exports.auth=function(sqlClient, userData, response){
	var result = tokenManager.verify(userData.token);
	console.log('the code of verify is ..');
	console.log(result);

	if(result==500){
		response.json({
			'code':result});
	}else{
		response.json({
			'code':200
		});
	}

}
