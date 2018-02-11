package nt.faceauth.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nt.faceauth.database.ClaimsDBHandler;
import nt.faceauth.database.FraudClaimDBHandler;
import nt.faceauth.database.PartyFaceDBHandler;
import nt.faceauth.to.Claim;
import nt.faceauth.to.PartyFace;
import nt.faceauth.to.RekognitionResult;
import nt.faceauth.util.AWSRekognitionUtil;

@Service("RegistrationService")
public class RegistrationService {

	@Autowired
	ClaimsDBHandler claimsDBHandler;
	
	@Autowired
	PartyFaceDBHandler partyFaceDBHandler;
	
	@Autowired
	FraudClaimDBHandler fraudClaimDBHandler;
	
	public Claim registerAndProcess(Claim claim, String bucket, String collectionId, Float matchThresholdFloat, int maxFacesToReturn) throws SQLException {
		
		AWSRekognitionUtil awsRekognitionUtil = new AWSRekognitionUtil(bucket, collectionId);
		List<RekognitionResult> rekognitionResultsList = new ArrayList<RekognitionResult>();
		
		RekognitionResult rekognitionResultDriver = new RekognitionResult();
		rekognitionResultDriver.setCustomerImageName(claim.getDriverImageName());
		rekognitionResultDriver.setParticipantName(claim.getDriverName());
		rekognitionResultsList.add(
processClaimParticipantImage(awsRekognitionUtil, rekognitionResultDriver, bucket, collectionId, matchThresholdFloat, maxFacesToReturn)
				);
		
		RekognitionResult rekognitionResultPedestrian = new RekognitionResult();
		rekognitionResultPedestrian.setCustomerImageName(claim.getPedestrianImageName());
		rekognitionResultPedestrian.setParticipantName(claim.getPedestrianName());
		rekognitionResultsList.add(
		processClaimParticipantImage(awsRekognitionUtil, rekognitionResultPedestrian, bucket, collectionId, matchThresholdFloat, maxFacesToReturn)
				);
		
		RekognitionResult rekognitionResultPassenger = new RekognitionResult();
		rekognitionResultPassenger.setCustomerImageName(claim.getPassengerImageName());
		rekognitionResultPassenger.setParticipantName(claim.getPassengerName());
		rekognitionResultsList.add(
		processClaimParticipantImage(awsRekognitionUtil, rekognitionResultPassenger, bucket, collectionId, matchThresholdFloat, maxFacesToReturn)
				);
		
		RekognitionResult rekognitionResultWitness = new RekognitionResult();
		rekognitionResultWitness.setCustomerImageName(claim.getWitnessImageName());
		rekognitionResultWitness.setParticipantName(claim.getWitnessName());
		rekognitionResultsList.add(
		processClaimParticipantImage(awsRekognitionUtil, rekognitionResultWitness, bucket, collectionId, matchThresholdFloat, maxFacesToReturn)
				);
		
		claim = processClaimParticipantData(awsRekognitionUtil, claim, rekognitionResultDriver, rekognitionResultPedestrian, rekognitionResultPassenger, rekognitionResultWitness);
		
		return claim;
//		request.setAttribute("faceMatches", rekognitionResults);
		
	}

	private RekognitionResult processClaimParticipantImage(AWSRekognitionUtil awsRekognitionUtil, RekognitionResult rekognitionResult, String bucket, String collectionId, Float matchThresholdFloat, int maxFacesToReturn) {

		//awsRekognitionUtil.uploadToAmazonS3(rekognitionResult.getCustomerImageName(), rekognitionResult.getCustomerImageName());

		return awsRekognitionUtil.storeFace(rekognitionResult, matchThresholdFloat, maxFacesToReturn);
		
	}

	private Claim processClaimParticipantData(AWSRekognitionUtil awsRekognitionUtil, Claim claim, RekognitionResult rekognitionResultDriver, RekognitionResult rekognitionResultPedestrian, RekognitionResult rekognitionResultPassenger, RekognitionResult rekognitionResultWitness) throws SQLException {
		PartyFace partyFaceDriver = processClaimPartyFaceDataMapping(rekognitionResultDriver);
		PartyFace partyFacePedestrian = processClaimPartyFaceDataMapping(rekognitionResultPedestrian);
		PartyFace partyFacePassenger = processClaimPartyFaceDataMapping(rekognitionResultPassenger);
		PartyFace partyFaceWitness = processClaimPartyFaceDataMapping(rekognitionResultWitness);

		claim.setDriverName(partyFaceDriver.getParticipantName());
		claim.setDriverParty(partyFaceDriver.getPartyId());
		claim.setPedestrianName(partyFacePedestrian.getParticipantName());
		claim.setPedestrianParty(partyFacePedestrian.getPartyId());
		claim.setPassengerName(partyFacePassenger.getParticipantName());
		claim.setPassengerParty(partyFacePassenger.getPartyId());
		claim.setWitnessName(partyFaceWitness.getParticipantName());
		claim.setWitnessParty(partyFaceWitness.getPartyId());
		
		claim = claimsDBHandler.createClaimRecord(claim);
		
/*		//new method to call search by faceid service
		findFraudMatch(partyFaceDriver);
		
		List<FraudClaim> fraudClaimList = faceFraudFaceMatchesPresent(rekognitionResultDriver, partyFaceDriver, 
				rekognitionResultPedestrian, partyFacePedestrian, 
				rekognitionResultPassenger, partyFacePassenger, 
				rekognitionResultWitness, partyFaceWitness);
		
		if(!fraudClaimList.isEmpty()) {
			createFraudRecordsForClaim();
		}*/
		
		return claim;
	}	

/*	private void findFraudMatch(AWSRekognitionUtil awsRekognitionUtil, PartyFace partyFaceDriver) {
		searchFaceAgainstImage
		
	}

	private List<FraudClaim> faceFraudFaceMatchesPresent(RekognitionResult rekognitionResultDriver, PartyFace partyFaceDriver, 
			RekognitionResult rekognitionResultPedestrian, PartyFace partyFacePedestrian, 
			RekognitionResult rekognitionResultPassenger, PartyFace partyFacePassenger, 
			RekognitionResult rekognitionResultWitness, PartyFace partyFaceWitness) {
		
		List<FraudClaim> fraudClaimList = new ArrayList<FraudClaim>();
		
//		if(rekognitionResultDriver.getMatchedFace())
		FraudClaim fraudClaimDriver = new FraudClaim();
		FraudClaim fraudClaimDriverMatch = new FraudClaim();
		
				
		
		return fraudClaimList;
	}

	private void createFraudRecordsForClaim() {
				
	}*/

	/*this function gets executed once for the face id which is added for claim */
	private PartyFace processClaimPartyFaceDataMapping(RekognitionResult rekognitionResult) throws SQLException {
		PartyFace partyFace = new PartyFace();
		partyFace.setFaceId(rekognitionResult.getMatchedFace().getFaceId());
		partyFace.setParticipantName(rekognitionResult.getParticipantName());
		return partyFaceDBHandler.createPartyFaceRecord(partyFace);		
//		return partyFaceDBHandler.getLastInsertIDForPartyFaceRecord(partyFace);
	}
	
}
