package nt.faceauth.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.stereotype.Component;

import nt.faceauth.to.FraudClaim;

@Component
public class FraudClaimDBHandler {

	public List<FraudClaim> createFraudClaimRecord(List<FraudClaim> fraudClaimsList) {
		
		Connection connection = DatabaseUtil.createConnection();
		PreparedStatement preparedStatement = null;
		
		String insertSql = "insert into fraudclaim "
				+ "(claimId, fraudFaceId, fraudPartyId, fraudIdMerge) Values ("
				+ "?,?,?,?)";
		
		try {
			preparedStatement = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
			
			for (FraudClaim fraudClaim : fraudClaimsList) {
				preparedStatement.setInt(1, fraudClaim.getClaimId());
				preparedStatement.setString(2, fraudClaim.getFraudFaceId());
				preparedStatement.setInt(3, fraudClaim.getFraudPartyId());
				preparedStatement.setString(4, fraudClaim.getFraudIdMerge());
				preparedStatement.executeUpdate();
				
				ResultSet rs = preparedStatement.getGeneratedKeys();
		        
				if (rs.next()) {
					fraudClaim.setFraudId(rs.getInt(1));
		        }

			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		
		return fraudClaimsList;
	}
	
	
	
}
