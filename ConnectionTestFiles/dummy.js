
function sendData(){
	data = {"data":"Random" + Math.floor(Math.random() * 100)};
	console.log(data);
	$.ajax({
		type:'POST',
		url: 'http://ec2-54-196-57-67.compute-1.amazonaws.com/node/sendDummyData',
		data: JSON.stringify(data),
		contentType: 'application/json',
		dataType: 'json',
		success: function(data){
			alert('data: ' + data);
		},
		error: function(xhr){
	        alert('Request Status: ' + xhr.status + ' Status Text: ' + xhr.statusText + ' ' + xhr.responseText);
	    }
	});
};

function sayHello(){
	console.log("@string/defual_string");
};