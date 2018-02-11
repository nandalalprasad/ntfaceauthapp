package nt.faceauth.to;

import java.util.List;

public class ClaimCompare {

	private Claim claimPrimary;
	private Claim claimSecondary;
	private List<PartyFace> partyFacePrimaryList;
	private List<PartyFace> partyFaceSecondaryList;
	
	public Claim getClaimPrimary() {
		return claimPrimary;
	}
	public void setClaimPrimary(Claim claimPrimary) {
		this.claimPrimary = claimPrimary;
	}
	public Claim getClaimSecondary() {
		return claimSecondary;
	}
	public void setClaimSecondary(Claim claimSecondary) {
		this.claimSecondary = claimSecondary;
	}
	public List<PartyFace> getPartyFacePrimaryList() {
		return partyFacePrimaryList;
	}
	public void setPartyFacePrimaryList(List<PartyFace> partyFacePrimaryList) {
		this.partyFacePrimaryList = partyFacePrimaryList;
	}
	public List<PartyFace> getPartyFaceSecondaryList() {
		return partyFaceSecondaryList;
	}
	public void setPartyFaceSecondaryList(List<PartyFace> partyFaceSecondaryList) {
		this.partyFaceSecondaryList = partyFaceSecondaryList;
	}
	
}
