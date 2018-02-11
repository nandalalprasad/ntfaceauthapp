package nt.faceauth.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nt.faceauth.database.ClaimsDBHandler;
import nt.faceauth.database.FraudClaimDBHandler;
import nt.faceauth.database.PartyFaceDBHandler;
import nt.faceauth.to.Claim;
import nt.faceauth.to.ClaimCompare;
import nt.faceauth.to.FraudClaim;
import nt.faceauth.to.PartyFace;
import nt.faceauth.to.RekognitionResult;
import nt.faceauth.util.AWSRekognitionUtil;

@Service("ClaimsFraudCheckService")
public class ClaimsFraudCheckService {

	@Autowired
	FraudClaimDBHandler fraudClaimDBHandler;
	
	@Autowired
	ClaimsDBHandler claimsDBHandler;
	
	@Autowired
	PartyFaceDBHandler partyFaceDBHandler;
	
	public List<Claim> findFraudClaimsMatch(String bucket, String collectionId, 
			Float matchThresholdFloat, int maxFacesToReturn, List<Claim> claimsListCheckResult, Map<Integer, List<Integer>> claimFraudMap) {
		
		AWSRekognitionUtil awsRekognitionUtil = new AWSRekognitionUtil(bucket, collectionId);
		
		//Using party id, find faceid of each member from party face id table - construct list of partyface objects
		//create claimcompare list with each claim and partyface
		ClaimCompare claimCompare = constructPartyFaceIdForMembersOfClaim(claimsListCheckResult);
		
		//For each party face object, send face id to AWS to get single match of the face id
		
		//find matching face for each party id in claim compare partyFaceId list
		List<FraudClaim> fraudClaims = findMatchingFace(claimCompare, awsRekognitionUtil, matchThresholdFloat, maxFacesToReturn);
		
			
		//Query party face id table passing on the fraud match face id and construct Fraud party face match object list
		//WRONG!Use the party id from previous step to get the respective 'fraud' claim id from claim table
		//Use the fraud claim and fraud party face id, create records on fraud claim table for the original claim and the matched fraud claim
				
		fraudClaimDBHandler.createFraudClaimRecord(fraudClaims);
		
		//Claim will be considered fraud only if there is a chain of operatives resulting in match of faces more than 2 across claims.
		//If same person is part of two claims and rest of members of both claims are unique, a "fraud chain" event will not trigger be triggered.
		if(fraudClaims.size()>2) {
			List<Integer> fraudList = null;
			
			for (Claim claim : claimsListCheckResult) {
				fraudList = new ArrayList<Integer>();
				for (FraudClaim fraudClaim : fraudClaims) {
					if(claim.getClaimId()==fraudClaim.getClaimId()) {					
						if(claim.getDriverParty()==fraudClaim.getFraudPartyId()) {
							claim.setFraudDriver(true);
							fraudList.add(claim.getDriverParty());
							claimFraudMap.put(claim.getClaimId(), fraudList);
						} else if(claim.getPedestrianParty()==fraudClaim.getFraudPartyId()) {
							claim.setFraudPedestrian(true);	
							fraudList.add(claim.getPedestrianParty());
							claimFraudMap.put(claim.getClaimId(), fraudList);
						} else if(claim.getPassengerParty()==fraudClaim.getFraudPartyId()) {
							claim.setFraudPassenger(true);
							fraudList.add(claim.getPassengerParty());
							claimFraudMap.put(claim.getClaimId(), fraudList);
						} else if(claim.getWitnessParty()==fraudClaim.getFraudPartyId()) {
							claim.setFraudWitness(true);
							fraudList.add(claim.getWitnessParty());
							claimFraudMap.put(claim.getClaimId(), fraudList);
						}
					}				
				}
			}			
		}
		
		if(fraudClaims.size() > 2) {
			return claimsListCheckResult;
		} else {
			return null;
		}
				
	}

	private List<FraudClaim> findMatchingFace(ClaimCompare claimCompare, AWSRekognitionUtil awsRekognitionUtil, Float matchThreshold, int maxFacesToReturn) {
		
		List<PartyFace> partyFacesPrimary = claimCompare.getPartyFacePrimaryList();
		List<PartyFace> partyFacesSecondary = claimCompare.getPartyFaceSecondaryList();
		
		String driverFaceIdPrimary = partyFacesPrimary.get(0).getFaceId();
		String pedestrainFaceIdPrimary = partyFacesPrimary.get(1).getFaceId();
		String passengerFaceIdPrimary = partyFacesPrimary.get(2).getFaceId();
		String witnessFaceIdPrimary = partyFacesPrimary.get(3).getFaceId();

		int driverPartyIdPrimary = partyFacesPrimary.get(0).getPartyId();
		int pedestrainPartyIdPrimary = partyFacesPrimary.get(1).getPartyId();
		int passengerPartyIdPrimary = partyFacesPrimary.get(2).getPartyId();
		int witnessPartyIdPrimary = partyFacesPrimary.get(3).getPartyId();
		
		List<FraudClaim> fraudClaimsList = new ArrayList<FraudClaim>();
		
		RekognitionResult rekognitionResultDriver = checkForMatch(driverFaceIdPrimary, awsRekognitionUtil, matchThreshold, maxFacesToReturn);
		RekognitionResult rekognitionResultPedestrian = checkForMatch(pedestrainFaceIdPrimary, awsRekognitionUtil, matchThreshold, maxFacesToReturn);
		RekognitionResult rekognitionResultPassenger = checkForMatch(passengerFaceIdPrimary, awsRekognitionUtil, matchThreshold, maxFacesToReturn);
		RekognitionResult rekognitionResultWitness = checkForMatch(witnessFaceIdPrimary, awsRekognitionUtil, matchThreshold, maxFacesToReturn);
	
		if(null != rekognitionResultDriver) {
			fraudClaimsList.addAll(checkAndSetFraudClaim(driverFaceIdPrimary, driverPartyIdPrimary, rekognitionResultDriver.getMatchedFace().getFaceId(), 
					partyFacesSecondary, claimCompare.getClaimPrimary(),claimCompare.getClaimSecondary(), new ArrayList<FraudClaim>()));
		}

		if(null != rekognitionResultPedestrian) {
			fraudClaimsList.addAll(checkAndSetFraudClaim(pedestrainFaceIdPrimary, pedestrainPartyIdPrimary, rekognitionResultPedestrian.getMatchedFace().getFaceId(), 
					partyFacesSecondary, claimCompare.getClaimPrimary(),claimCompare.getClaimSecondary(), new ArrayList<FraudClaim>()));
		}

		if(null != rekognitionResultPassenger) {
			fraudClaimsList.addAll(checkAndSetFraudClaim(passengerFaceIdPrimary, passengerPartyIdPrimary, rekognitionResultPassenger.getMatchedFace().getFaceId(), 
					partyFacesSecondary, claimCompare.getClaimPrimary(),claimCompare.getClaimSecondary(), new ArrayList<FraudClaim>()));
		}
	
		if(null != rekognitionResultWitness) {
			fraudClaimsList.addAll(checkAndSetFraudClaim(witnessFaceIdPrimary, witnessPartyIdPrimary, rekognitionResultWitness.getMatchedFace().getFaceId(), 
					partyFacesSecondary, claimCompare.getClaimPrimary(),claimCompare.getClaimSecondary(), new ArrayList<FraudClaim>()));
		}
	
		return fraudClaimsList;
	}			

	private List<FraudClaim> checkAndSetFraudClaim(String primaryFaceId, int primaryPartyId, String matchedFaceId, 
			List<PartyFace> partyFacesSecondary, Claim primaryClaim, Claim secondaryClaim, List<FraudClaim> fraudClaimsList) {
		FraudClaim fraudClaimPrimary = null;
		FraudClaim fraudClaimSecondary = null;
		
		for (int i = 0; i < partyFacesSecondary.size(); i++) {
			PartyFace partyFace = partyFacesSecondary.get(i);
			if(matchedFaceId.equals(partyFace.getFaceId())) {
				fraudClaimPrimary = new FraudClaim();
				fraudClaimSecondary = new FraudClaim();
				
				fraudClaimPrimary.setClaimId(primaryClaim.getClaimId());
				fraudClaimPrimary.setFraudFaceId(primaryFaceId);
				fraudClaimPrimary.setFraudPartyId(primaryPartyId);

				fraudClaimSecondary.setClaimId(secondaryClaim.getClaimId());
				fraudClaimSecondary.setFraudFaceId(matchedFaceId);
				fraudClaimSecondary.setFraudPartyId(partyFace.getPartyId());
				
				fraudClaimPrimary.setFraudIdsMerge(primaryPartyId + "$" + partyFace.getPartyId());
				fraudClaimSecondary.setFraudIdsMerge(primaryPartyId + "$" + partyFace.getPartyId());
				
				fraudClaimsList.add(fraudClaimPrimary);
				fraudClaimsList.add(fraudClaimSecondary);
			}
		}
		return fraudClaimsList;
	}

	private RekognitionResult checkForMatch(String faceId, AWSRekognitionUtil awsRekognitionUtil, Float matchThreshold, int maxFacesToReturn) {
		if(null != faceId) {
			return awsRekognitionUtil.searchFaceWithFaceId(faceId, matchThreshold, maxFacesToReturn);
		} else {
			return null;
		}
	}


	private ClaimCompare constructPartyFaceIdForMembersOfClaim(List<Claim> claimsListCheckResult) {
		
		ClaimCompare claimCompare = new ClaimCompare();
		claimCompare.setClaimPrimary(claimsListCheckResult.get(0));
		claimCompare.setPartyFacePrimaryList(getPartyFaceList(claimsListCheckResult.get(0), new ArrayList<PartyFace>()));
		claimCompare.setClaimSecondary(claimsListCheckResult.get(1));
		claimCompare.setPartyFaceSecondaryList(getPartyFaceList(claimsListCheckResult.get(1), new ArrayList<PartyFace>()));

		return claimCompare;
	}

	private List<PartyFace> getPartyFaceList(Claim claim, List<PartyFace> partyFaceList) {
		PartyFace partyDriverFace = null;
		PartyFace partyPedestrianFace = null;
		PartyFace partyPassengerFace = null;
		PartyFace partyWitnessFace = null;

		partyDriverFace = new PartyFace();
		partyDriverFace.setPartyId(claim.getDriverParty());
		partyDriverFace.setParticipantName(claim.getDriverName());
		
		partyPedestrianFace = new PartyFace();
		partyPedestrianFace.setPartyId(claim.getPedestrianParty());
		partyPedestrianFace.setParticipantName(claim.getPedestrianName());

		partyPassengerFace = new PartyFace();
		partyPassengerFace.setPartyId(claim.getPassengerParty());
		partyPassengerFace.setParticipantName(claim.getPassengerName());

		partyWitnessFace = new PartyFace();
		partyWitnessFace.setPartyId(claim.getWitnessParty());
		partyWitnessFace.setParticipantName(claim.getWitnessName());

		partyFaceList.add(partyDriverFace);
		partyFaceList.add(partyPedestrianFace);
		partyFaceList.add(partyPassengerFace);
		partyFaceList.add(partyWitnessFace);

		return partyFaceList = partyFaceDBHandler.getFacesForPartyIds(partyFaceList);
			
	}
	
}
