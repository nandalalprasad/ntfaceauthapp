package nt.faceauth.to;

public class ClaimsCheckResult {

	private String driverMatch;
	private String pedestrianMatch;
	private String passengerMatch;
	private String witnessMatch;
	
	public String getDriverMatch() {
		return driverMatch;
	}
	public void setDriverMatch(String driverMatch) {
		this.driverMatch = driverMatch;
	}
	public String getPedestrianMatch() {
		return pedestrianMatch;
	}
	public void setPedestrianMatch(String pedestrianMatch) {
		this.pedestrianMatch = pedestrianMatch;
	}
	public String getPassengerMatch() {
		return passengerMatch;
	}
	public void setPassengerMatch(String passengerMatch) {
		this.passengerMatch = passengerMatch;
	}
	public String getWitnessMatch() {
		return witnessMatch;
	}
	public void setWitnessMatch(String witnessMatch) {
		this.witnessMatch = witnessMatch;
	}
	
	
	
}
