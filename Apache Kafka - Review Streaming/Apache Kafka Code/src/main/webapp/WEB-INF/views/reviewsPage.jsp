<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Registration</title>
</head>
<body>
	<div align="center">
		<form:form action="reviews" method="post" commandName="hotelReview">
			<table border="0">
				<tr>
					<td colspan="2" align="center"><h2>Spring MVC Form Demo - Registration</h2></td>
				</tr>
				<tr>
					<td>Hotel Name:</td>
					<td><form:input path="hotelId" /></td>
				</tr>
				<tr>
					<td>Reviews</td>
					<td><form:input path="review" /></td>
				</tr>
				
				<tr>
					<td colspan="2" align="center"><input type="submit" value="submit" /></td>
				</tr>
			</table>
		</form:form>
	</div>
</body>
</html> 