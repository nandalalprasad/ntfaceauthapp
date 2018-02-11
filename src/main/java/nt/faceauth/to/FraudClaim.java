package nt.faceauth.to;

public class FraudClaim {

	private int fraudId;
	private int claimId;
	private String fraudFaceId;
	private int fraudPartyId;
	private String fraudIdMerge;
	
	public String getFraudIdMerge() {
		return fraudIdMerge;
	}
	public void setFraudIdsMerge(String fraudIdMerge) {
		this.fraudIdMerge = fraudIdMerge;
	}
	public int getFraudId() {
		return fraudId;
	}
	public void setFraudId(int fraudId) {
		this.fraudId = fraudId;
	}
	public String getFraudFaceId() {
		return fraudFaceId;
	}
	public void setFraudFaceId(String fraudFaceId) {
		this.fraudFaceId = fraudFaceId;
	}
	public int getFraudPartyId() {
		return fraudPartyId;
	}
	public void setFraudPartyId(int fraudPartyId) {
		this.fraudPartyId = fraudPartyId;
	}
	public int getClaimId() {
		return claimId;
	}
	public void setClaimId(int claimId) {
		this.claimId = claimId;
	}
		
}
