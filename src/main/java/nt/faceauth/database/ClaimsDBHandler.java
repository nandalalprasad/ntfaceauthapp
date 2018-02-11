package nt.faceauth.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import nt.faceauth.to.Claim;
import nt.faceauth.to.ClaimCompare;
import nt.faceauth.to.FraudClaim;

@Component
public class ClaimsDBHandler {

	public Claim createClaimRecord(Claim claim) {
		
		Connection connection = DatabaseUtil.createConnection();
		PreparedStatement preparedStatement = null;

		String insertSql = "insert into claims "
				+ "(driverNm, driverParty, pedestNm, pedestParty, passengerNm, passengerParty, witnessNm, witnessParty) values "
				+ "(?,?,?,?,?,?,?,?)";
		
		try {
			preparedStatement = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, claim.getDriverName());
			preparedStatement.setInt(2, claim.getDriverParty());
			preparedStatement.setString(3, claim.getPedestrianName());
			preparedStatement.setInt(4, claim.getPedestrianParty());
			preparedStatement.setString(5, claim.getPassengerName());
			preparedStatement.setInt(6, claim.getPassengerParty());
			preparedStatement.setString(7, claim.getWitnessName());
			preparedStatement.setInt(8, claim.getWitnessParty());
			
			preparedStatement.executeUpdate();

			ResultSet rs = preparedStatement.getGeneratedKeys();
			
	        if (rs.next()) {
	        	claim.setClaimId(rs.getInt(1));
	        }

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  finally {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		return claim;				
	}

	public List<Claim> retrieveClaims() {
		
		List<Claim> claimsListResult = new ArrayList<Claim>();
		
		Connection connection = DatabaseUtil.createConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Claim claim = null;
		
		String selectQuery =
	        "select claimId, drivernm, driverParty, pedestNm, pedestParty, " +
	        "passengerNm, passengerparty, witnessNm, witnessParty " +
	        "from " + "facerecog" + ".claims";

	    try {
	    	preparedStatement = connection.prepareStatement(selectQuery);
	        resultSet = preparedStatement.executeQuery(selectQuery);
	        while (resultSet.next()) {
	        	claim = new Claim();
	            claim.setClaimId(resultSet.getInt("claimId"));
	            claim.setDriverName(resultSet.getString("driverNm"));
	            claim.setDriverParty(resultSet.getInt("driverParty"));
	            claim.setPedestrianName(resultSet.getString("pedestNm"));
	            claim.setPedestrianParty(resultSet.getInt("pedestParty"));

	            claim.setPassengerName(resultSet.getString("passengerNm"));
	            claim.setPassengerParty(resultSet.getInt("passengerparty"));

	            claim.setWitnessName(resultSet.getString("witnessNm"));
	            claim.setWitnessParty(resultSet.getInt("witnessParty"));
	            
	            claimsListResult.add(claim);
	        }
	    } catch (SQLException e ) {
	    	//TODO
	        e.printStackTrace();
	    }  finally {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return claimsListResult;
	}

	public List<Claim> retrieveClaimById(int[] claimIds) {
		List<Claim> claimsListResult = new ArrayList<Claim>();
		
		Connection connection = DatabaseUtil.createConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Claim claim = null;
		
		StringBuilder claimIdBuilder = new StringBuilder();
		for (int i = 0; i < claimIds.length; i++) {
			claimIdBuilder.append(claimIds[i]);
			claimIdBuilder.append(",");
		}
		
		String selectQuery =
	        "select claimId, drivernm, driverParty, pedestNm, pedestParty, " +
	        "passengerNm, passengerparty, witnessNm, witnessParty " +
	        "from " + "facerecog" + ".claims " + "where claimId in ( " + 
	        claimIdBuilder.deleteCharAt(claimIdBuilder.length() -1).toString() + " )" + 
	        " order by field ( " + claimIdBuilder.toString() + " )";

		System.out.println("selectQuery ********************** " + selectQuery);

	    try {
	    	preparedStatement = connection.prepareStatement(selectQuery);
	        resultSet = preparedStatement.executeQuery(selectQuery);
	        while (resultSet.next()) {
	        	claim = new Claim();
	            claim.setClaimId(resultSet.getInt("claimId"));
	            claim.setDriverName(resultSet.getString("driverNm"));
	            claim.setDriverParty(resultSet.getInt("driverParty"));
	            claim.setPedestrianName(resultSet.getString("pedestNm"));
	            claim.setPedestrianParty(resultSet.getInt("pedestParty"));

	            claim.setPassengerName(resultSet.getString("passengerNm"));
	            claim.setPassengerParty(resultSet.getInt("passengerparty"));

	            claim.setWitnessName(resultSet.getString("witnessNm"));
	            claim.setWitnessParty(resultSet.getInt("witnessParty"));
	            
	            claimsListResult.add(claim);
	        }
	    } catch (SQLException e ) {
	    	//TODO
	        e.printStackTrace();
	    }  finally {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	    return claimsListResult;
	}

/*	public List<Claim> retrieveFraudClaimInfo(List<FraudClaim> fraudClaims, int primaryClaimId, int secondaryClaimId) {
		List<Claim> claimsListResult = new ArrayList<Claim>();
		
		Connection connection = DatabaseUtil.createConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Claim claim = null;
		
		String selectQuery =
	        "select claimId, drivernm, driverParty, pedestNm, pedestParty, " +
	        "passengerNm, passengerparty, witnessNm, witnessParty " +
	        "from " + "facerecog" + ".claims where claimId in ( " + primaryClaimId + " , " + secondaryClaimId + " )";

	    try {
	    	preparedStatement = connection.prepareStatement(selectQuery);
	        resultSet = preparedStatement.executeQuery(selectQuery);
	        while (resultSet.next()) {
	        	claim = new Claim();
	            claim.setClaimId(resultSet.getInt("claimId"));
	            claim.setDriverName(resultSet.getString("driverNm"));
	            claim.setDriverParty(resultSet.getInt("driverParty"));
	            claim.setPedestrianName(resultSet.getString("pedestNm"));
	            claim.setPedestrianParty(resultSet.getInt("pedestParty"));

	            claim.setPassengerName(resultSet.getString("passengerNm"));
	            claim.setPassengerParty(resultSet.getInt("passengerparty"));

	            claim.setWitnessName(resultSet.getString("witnessNm"));
	            claim.setWitnessParty(resultSet.getInt("witnessParty"));
	            
	            claimsListResult.add(claim);
	        }
	    } catch (SQLException e ) {
	    	//TODO
	        e.printStackTrace();
	    }  finally {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return claimsListResult;
	}
	*/
}
