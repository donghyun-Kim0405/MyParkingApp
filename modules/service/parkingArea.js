module.exports.getData = function(sqlClient, userData, response){
	var lat = parseFloat(userData.lat);
	var lon = parseFloat(userData.lon);
	var query = 'SELECT * FROM parkingArea WHERE (6371*acos(cos(radians(?))* cos( radians(위도))* cos( radians(경도)-radians(?))+ sin ( radians(?))* sin( radians(위도))))<5';
	sqlClient.query(query, [lat, lon, lat], function(error, results){
                response.send(results);
				
        });

}

module.exports.getBookmarkParkingLot = function(sqlClient, bookmarkData, response){
	var query = 'SELECT * FROM parkingArea WHERE 소재지지번주소=?';
	sqlClient.query(query, [bookmarkData.address], function(error, results){
		response.send(results[0]);
	});
}



