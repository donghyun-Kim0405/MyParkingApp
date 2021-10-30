var jwt = require('jsonwebtoken');


//tokenManager -> 동기식 처리 고려할것!
//secretKey & expiresIn 수정 필요

var EXPIRE = '30D';
var SECRET_KEY='TESTKEY';

module.exports.generate = function(userData){

	var token = jwt.sign(
		{
			uuid : userData.uuid
		},
		SECRET_KEY,
		{
			expiresIn : EXPIRE
		}
	);
	return token;
}



module.exports.verify=function(token){

	console.log('verify ...');
	console.log(token);

	try{
		var result = jwt.verify(token, SECRET_KEY);
		return result;
	}
	catch(error){
		console.log(error.name);
		return 500;
	}
}
