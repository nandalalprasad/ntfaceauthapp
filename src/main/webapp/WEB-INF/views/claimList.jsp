<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>    
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Claims List</title>
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
		$("#warningNotSelected").hide();
		$("#lengthExceeds").hide();
		
	});
	
	$( document ).ready(function() {
		$('#runRekog').click(function() {
			//alert($.find('#rekog td input[type="checkbox"]:checked').length);
			
			var totalChecked=$.find('#rekog td input[type="checkbox"]:checked').length;
			
			if(totalChecked > 2) {
				$('#lengthExceeds').show();
				//checkedClaims.splice(-1,1);
			} else if(totalChecked == 0){
				//checkedClaims.push($(this).val());
				$('#warningNotSelected').show();
				$('#lengthExceeds').hide();
			} else if(totalChecked == 1) {
				$('#warningNotSelected').show();
				$('#lengthExceeds').hide();
			} else if(totalChecked == 2) {
				$("#rekog").submit();
			}			
		});
	}); 

	$( document ).ready(function() {
		$('#newClaimEntry').click(function() {
			location.href='/ntfaceauthapp/';
		});		
	});
 
</script>

<form method="post" id="rekog" action="compareClaim" >

<div class="container" id="claimContent">
<h3>Claims list for the year 2018</h3>
  <hr>
  
	<c:if test="${not empty success}">
	  <div class="alert alert-success">
	  	 ${success}
	  </div>
	</c:if>
  
  <div class="alert alert-warning" id="warningNotSelected">
  	 <strong>Input needed!</strong> Please select two claims for validation!
  </div>

  <div class="alert alert-warning" id="lengthExceeds">
  	 <strong>Sorry!</strong> This system is currently limited to comparison of two claims!
  </div>

	<table class="table table-condensed table-hover">
	  <thead>
	    <tr>
	      <th scope="col"></th>
	      <th scope="col">#</th>
	      <th scope="col">Claim Id</th>
	      <th scope="col">Driver</th>
	      <th scope="col">Pedestrian</th>
	      <th scope="col">Passenger</th>
	      <th scope="col">Witness</th>

	    </tr>
	  </thead>
	  <tbody>
	  
	  
	  <c:forEach var="claim" items="${claimsList}" varStatus="status">
	    <tr>
	      <td>
                <input class="check" type="checkbox" name="claimSelect" value="${claim.getClaimId()}">
                <!-- <input type="hidden" name="_community" value="on"/> -->
          </td>
          
	      <td>${status.index + 1}</td>	    
	      <td>${claim.getClaimId()}</td>
	      <td>${claim.getDriverName()}</td>
	      <td>${claim.getPedestrianName()}</td>
	      <td>${claim.getPassengerName()}</td>
	      <td>${claim.getWitnessName()}</td>
	      	      
	    </tr>
	  </c:forEach>

	  </tbody>
	</table>        

<div class="row">
	<div class="col-12 col-md-6">
		<button type="button" id="newClaimEntry" class="btn btn-primary">New Claim Data Entry</button>
	</div>
	<div class="col-12 col-md-6">
		<button type="button" id="runRekog" class="btn btn-primary float-right">Run Validation</button>
	</div>
</div>

</div>
<c:remove var="success" scope="session" />
</form>

</body>
</html>