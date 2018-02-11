<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%> 
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>New Claim Participant Data Entry</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

</head>
<body>
<script type="text/javascript">

$(function() {
	$('#validationFill').hide();
});

function previewImage(inputId, imageId) {
    var oFReader = new FileReader();
    oFReader.readAsDataURL(document.getElementById(inputId).files[0]);

    oFReader.onload = function (oFREvent) {
        document.getElementById(imageId).src = oFREvent.target.result;
    };
};

function claimSubmit() {
	
	var valid = true;
	
	var driver = $("#imgDriver").val() != '';
	var pedestrian = $("#imgPedestrian").val()!= '';
	var passenger = $("#imgPassenger").val()!= '';
	var witness = $("#imgWitness").val()!= '';
	
	var driverName = $("#driverName").val() != '';
	var pedestrianName = $("#pedestrianName").val()!= '';
	var passengerName = $("#passengerName").val()!= '';
	var witnessName = $("#witnessName").val()!= '';


	if(!(driver && driverName && pedestrian && pedestrianName && passenger && passengerName && witness && witnessName)) {
		valid = false;
		$('#validationFill').show();
	} else {
		$("#createClaim").submit();
	}
}


$(document).ready(function() {
	$('#claimsList').click(function() {
		location.href='/ntfaceauthapp/claimListView';
	});
});

</script>

<script type="text/css">

</script>


	<div class="container">
		<h2>Claim Participant Data Entry</h2>
		<hr>

		<form:form action="processRekognitionOnClaimUsers" method="post" id="createClaim" modelAttribute="claim">

			<div class="alert alert-warning" id="validationFill">
			  <strong>Validation error!</strong> Please complete data entry for all participant images and names!
			</div>

			<div class="row">

				<div class="col-md-5">
					<div class="card" style="width: 25rem;">
						<img id="driverImg" name="driverImg" class="card-img-top" src="" alt="Driver's Image" >
						<div class="card-body">
							<h4 class="card-title">Driver</h4>
							<p class="card-text">Select the image and enter name.</p>

							<div class="form-group row">
							  <form:label for="driverName" class="col-3 col-form-label" path="driverName">Name</form:label>
							  <div class="col-8">
							    <form:input required="required" class="form-control" type="text" id="driverName" name="driverName" path="driverName"></form:input>
							  </div>
							</div>

							<form:input class="btn btn-primary" type="file" id="imgDriver" onchange="previewImage('imgDriver','driverImg');" name="imgDriver" path="driverImageName"/>
							<!-- <a href="#" class="btn btn-primary">Select Driver</a> -->
						</div>
					</div>
				</div>

				<div class="col-md-5">
					<div class="card" style="width: 25rem;">
						<img id="pedestrianImg" name="pedestrianImg" class="card-img-top"
							src=""
							alt="Pedestrian's Image">
						<div class="card-body">
							<h4 class="card-title">Pedestrian</h4>
							<p class="card-text">Select the image and enter name.</p>

							<div class="form-group row">
							  <form:label for="pedestrianName" class="col-3 col-form-label" path="pedestrianName">Name</form:label>
							  <div class="col-8">
							    <form:input class="form-control" type="text" id="pedestrianName" name="pedestrianName" path="pedestrianName"></form:input>
							  </div>
							</div>

							<form:input class="btn btn-primary" type="file" id="imgPedestrian" name="imgPedestrian" path="pedestrianImageName" onchange="previewImage('imgPedestrian','pedestrianImg');"/>
							
							<!-- <a href="#" class="btn btn-primary">Select Pedestrian</a> -->
						</div>
					</div>
				</div>

				<div class="col-md-5">
					<div class="card" style="width: 25rem;">
						<img id="passengerImg" name="passengerImg" class="card-img-top"
							src=""
							alt="Passenger's Image">
						<div class="card-body">
							<h4 class="card-title">Passenger</h4>
							<p class="card-text">Select the image and enter name.</p>

							<div class="form-group row">
							  <form:label for="passengerName" class="col-3 col-form-label" path="passengerName">Name</form:label>
							  <div class="col-8">
							    <form:input class="form-control" type="text" id="passengerName" name="passengerName" path="passengerName"></form:input>
							  </div>
							</div>


							<form:input class="btn btn-primary" type="file" id="imgPassenger" name="imgPassenger" path="passengerImageName" onchange="previewImage('imgPassenger','passengerImg');"/>
							
							<!-- <a href="#" class="btn btn-primary">Select Passenger</a> -->
						</div>
					</div>
				</div>

				<div class="col-md-5">
					<div class="card" style="width: 25rem;">
						<img id="witnessImg" name="witnessImg" class="card-img-top"
							src=""
							alt="Witness's Image">
						<div class="card-body">
							<h4 class="card-title">Witness</h4>
							<p class="card-text">Select the image and enter name.</p>

							<div class="form-group row">
							  <form:label for="witnessName" class="col-3 col-form-label" path="witnessName">Name</form:label>
							  <div class="col-8">
							    <form:input class="form-control" type="text" value="" id="witnessName" name="witnessName" path="witnessName"></form:input>
							  </div>
							</div>

							<form:input class="btn btn-primary" type="file" id="imgWitness" name="imgWitness" path="witnessImageName" onchange="previewImage('imgWitness','witnessImg');"/>
							
							<!-- <a href="#" class="btn btn-primary">Go somewhere</a> -->
						</div>
					</div>
				</div>

			</div>
<br>

	<div class="row">
		<div class="col-12 col-md-6">
			<button type="button" id="claimsList" class="btn btn-primary">Claims List</button>
		</div>
		<div class="col-12 col-md-4">
			<button type="button" class="btn btn-primary float-right" onclick="claimSubmit()">Submit Claim Data Entry</button>
		</div>
	</div>

		</form:form>
	</div>

</body>
</html>