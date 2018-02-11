<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>    
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Claim fraud results</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>


<style type="text/css">


 .check {
  margin-top: 6px;
  position: relative;
}

</style>

</head>
<body>

<script type="text/javascript">
	
 	$( document ).ready(function() {
		$('#newClaimEntry').click(function() {
			location.href='/ntfaceauthapp/';
		});

		$('#claimsList').click(function() {
			location.href='/ntfaceauthapp/claimListView';
		});
 	}); 
 
</script>

<form method="get" id="rekog">

<div class="container" id="claimContent">
<h3>Claim fraud results</h3>
 <hr>
  
	<c:if test="${not empty success}">
	  <div class="alert alert-success">
	  	 ${success}
	  </div>
	</c:if>

	<c:if test="${not empty alert}">
	  <div class="alert alert-danger">
	  	 ${alert}
	  </div>
	</c:if>
  
	<table class="table table-condensed table-hover">
	  <thead>
	    <tr>
	      <th scope="col">#</th>
	      <th scope="col">Claim Id</th>
	      <th scope="col">Driver</th>
	      <th scope="col">Pedestrian</th>
	      <th scope="col">Passenger</th>
	      <th scope="col">Witness</th>

	    </tr>
	  </thead>
	  <tbody>
	  
	  
	  <c:forEach var="claim" items="${claimsCheckList}" varStatus="status">
	    <tr>
          
	      <td>${status.index + 1}</td>	    
	      <td>${claim.getClaimId()}</td>

		  <c:choose>
		  	<c:when test="${claim.fraudDriver}">
			      <td id="driver" class="table-warning">${claim.getDriverName()}</td>
		  	</c:when>
		  	<c:otherwise>
			      <td>${claim.getDriverName()}</td>
		  	</c:otherwise>
		  </c:choose>

		  <c:choose>
		  	<c:when test="${claim.fraudPedestrian}">
	      		<td id="pedestrian" class="table-warning">${claim.getPedestrianName()}</td>
		  	</c:when>
		  	<c:otherwise>
	      		<td>${claim.getPedestrianName()}</td>
		  	</c:otherwise>
		  </c:choose>

		  <c:choose>
		  	<c:when test="${claim.fraudPassenger}">
			      <td id="passenger" class="table-warning">${claim.getPassengerName()}</td>
		  	</c:when>
		  	<c:otherwise>
			      <td>${claim.getPassengerName()}</td>
		  	</c:otherwise>
		  </c:choose>
		  
		  <c:choose>
		  	<c:when test="${claim.fraudWitness}">
		      <td id="witness" class="table-warning">${claim.getWitnessName()}</td>
		  	</c:when>
		  	<c:otherwise>
		      <td>${claim.getWitnessName()}</td>
		  	</c:otherwise>
		  </c:choose>
	      	      
	    </tr>
	  </c:forEach>

	  </tbody>
	</table>        

<div class="row">
	<div class="col-12 col-md-6">
		<button type="button" id="newClaimEntry" class="btn btn-primary">New Claim Data Entry</button>
	</div>
	<div class="col-12 col-md-6">
		<button type="button" id="claimsList" class="btn btn-primary float-right">Claims List</button>
	</div>
</div>

</div>
<c:remove var="success" scope="session" />
<c:remove var="alert" scope="session" />

</form>

</body>
</html>