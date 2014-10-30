/**
 * 
 */
var markers = [];

$(document).ready (
  function() {
    $('#keyword').submit(
    	function keywordsubmit() {
    		addMarkers();
    		return false;
    });
});

function addMarkers() {
	clearMarkers();
	var keywordselect = document.getElementById("keywordselect");
	var keyword = keywordselect.options[keywordselect.selectedIndex].value;
	$.ajax({
	  url: 'getTweet',
	  type: 'POST',
	  data: {"keyword": keyword},
	  dataType: 'json',
	  success: function(data) {
		var latlngarrray, pointarray, heatmap;
		// add marker for each lat-lng pair to the map
		for (var i=0, len=data.length; i < len; i++) {
		  var latlng = data[i];
		  lat = latlng[0];
	      lng = latlng[1];
	      var image = 'javascript/reddot.png';
	      var marker = new google.maps.Marker({
        	position: new google.maps.LatLng(lat,lng),
        	title:"",
        	map: map,
        	icon: image
          });
	      markers.push(marker);
        }
	  },
	  complete: function() {
	    setTimeout(addMarkers, 5000);
	  }
	});
}

function stopMap() {
	clearMarkers();
	window.location.replace("index.jsp");
}

function clearMarkers() {
	for (var i = 0; i < markers.length; i++) {
	    markers[i].setMap(null);
	}  
}

