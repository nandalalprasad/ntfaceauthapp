package nt.faceauth.to;

public class Claim {
	
	private int claimId;
	public void setClaimId(int claimId) {
		this.claimId = claimId;
	}
	
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	private String driverName;
	private int driverParty;
	private String pedestrianName;
	private int pedestrianParty;
	private String passengerName;
	private int passengerParty;
	private String witnessName;
	private int witnessParty;
	private String driverImageName;
	private String pedestrianImageName;
	private String passengerImageName;
	private String witnessImageName;

	private boolean isFraudDriver;
	private boolean isFraudPedestrian;
	private boolean isFraudPassenger;
	private boolean isFraudWitness;

	
	public boolean isFraudDriver() {
		return isFraudDriver;
	}
	public void setFraudDriver(boolean isFraudDriver) {
		this.isFraudDriver = isFraudDriver;
	}
	public boolean isFraudPedestrian() {
		return isFraudPedestrian;
	}
	public void setFraudPedestrian(boolean isFraudPedestrian) {
		this.isFraudPedestrian = isFraudPedestrian;
	}
	public boolean isFraudPassenger() {
		return isFraudPassenger;
	}
	public void setFraudPassenger(boolean isFraudPassenger) {
		this.isFraudPassenger = isFraudPassenger;
	}
	public boolean isFraudWitness() {
		return isFraudWitness;
	}
	public void setFraudWitness(boolean isFraudWitness) {
		this.isFraudWitness = isFraudWitness;
	}
	
	
	public String getDriverImageName() {
		return driverImageName;
	}
	public void setDriverImageName(String driverImageName) {
		this.driverImageName = driverImageName;
	}
	public String getPedestrianImageName() {
		return pedestrianImageName;
	}
	public void setPedestrianImageName(String pedestrianImageName) {
		this.pedestrianImageName = pedestrianImageName;
	}
	public String getPassengerImageName() {
		return passengerImageName;
	}
	public void setPassengerImageName(String passengerImageName) {
		this.passengerImageName = passengerImageName;
	}
	public String getWitnessImageName() {
		return witnessImageName;
	}
	public void setWitnessImageName(String witnessImageName) {
		this.witnessImageName = witnessImageName;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public int getDriverParty() {
		return driverParty;
	}
	public void setDriverParty(int driverParty) {
		this.driverParty = driverParty;
	}
	public String getPedestrianName() {
		return pedestrianName;
	}
	public void setPedestrianName(String pedestrianName) {
		this.pedestrianName = pedestrianName;
	}
	public int getPedestrianParty() {
		return pedestrianParty;
	}
	public void setPedestrianParty(int pedestrianParty) {
		this.pedestrianParty = pedestrianParty;
	}
	public String getPassengerName() {
		return passengerName;
	}
	public void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}
	public int getPassengerParty() {
		return passengerParty;
	}
	public void setPassengerParty(int passengerParty) {
		this.passengerParty = passengerParty;
	}
	public String getWitnessName() {
		return witnessName;
	}
	public void setWitnessName(String witnessName) {
		this.witnessName = witnessName;
	}
	public int getWitnessParty() {
		return witnessParty;
	}
	public void setWitnessParty(int witnessParty) {
		this.witnessParty = witnessParty;
	}
	public int getClaimId() {
		return claimId;
	}
}
