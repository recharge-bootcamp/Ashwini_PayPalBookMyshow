<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<h1>Book my Show</h1>

<h2>Select City</h2>
<form method="get" id="form_id" action="movieList">
<input type="text" name="cities">Select City</input>
<div class="dropdown">
  <button id="cityDropdown" onclick="myFunction()">Select a City</button>
</div>
			

<button type="submit">Submit</button>


</body>
</html>