package nt.faceauth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nt.faceauth.database.ClaimsDBHandler;
import nt.faceauth.to.Claim;

@Service("ClaimsListService")
public class ClaimsListService {

	@Autowired
	ClaimsDBHandler claimsDBHandler;
	
	public List<Claim> retrieveClaims() {
		
		return claimsDBHandler.retrieveClaims();
	}

	public List<Claim> retrieveClaimById(int[] claimIds) {
		
		return claimsDBHandler.retrieveClaimById(claimIds);
	}


}
