<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Tweet Map</title>

<script src="//code.jquery.com/jquery-1.10.2.js"></script>
<script src="javascript/updateMap.js"></script>
<script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
<script>
  function initialize() {
  var mapOptions = {
    zoom: 2,
    center: new google.maps.LatLng(29.340755, 154.290208)
  };
  map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
}
google.maps.event.addDomListener(window, 'load', initialize);
</script>

</head>
<body>
  Select a keyword 
  <select id="keywordselect">
    <option value="% a %">a</option>
    <option value="% the %">the</option>
    <option value="% you %">you</option>
    <option value="% good %">good</option>
    <option value="% like %">like</option>
  </select>
  <form id="keyword">
    <input type="submit" value="Show on Map"/>
    <button onclick="stopMap()">stop</button>
  </form>
  <div id="map-canvas" style="height:600px; width:1000px"></div>
</body>
</html>