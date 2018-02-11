package nt.faceauth.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.stereotype.Component;

import nt.faceauth.to.PartyFace;

@Component
public class PartyFaceDBHandler {

	public PartyFace createPartyFaceRecord(PartyFace partyFace) {
		
		Connection connection = DatabaseUtil.createConnection();
		PreparedStatement preparedStatement = null;

		String insertSql = "insert into party_faceid (participantName, faceId) values (?,?)";
		
		try {
			preparedStatement = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, partyFace.getParticipantName());
			preparedStatement.setString(2, partyFace.getFaceId());
			
			preparedStatement.executeUpdate();

			ResultSet rs = preparedStatement.getGeneratedKeys();
			
	        if (rs.next()) {
	        	partyFace.setPartyId(rs.getInt(1));
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
		return partyFace;	
	}

	public PartyFace getLastInsertIDForPartyFaceRecord(PartyFace partyFace) {
		
		Connection connection = DatabaseUtil.createConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		String sql = "SELECT LAST_INSERT_ID()";
		
		try {
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			
			partyFace.setPartyId(resultSet.getInt("partyId"));
			
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
		return partyFace;				
	}

	public List<PartyFace> getFacesForPartyIds(List<PartyFace> partyFaceListForAClaim) {
		
		Connection connection = DatabaseUtil.createConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		PartyFace partyFace = null;
		
		StringBuilder partyIdBuilder = new StringBuilder();
		for (int i = 0; i < partyFaceListForAClaim.size(); i++) {
			partyIdBuilder.append(partyFaceListForAClaim.get(i).getPartyId());
			partyIdBuilder.append(",");
		}
		
		String selectQuery =
	        "select partyId, participantName, faceId " +
	        "from " + "facerecog" + ".party_faceid " + "where partyId in (" + 
	        partyIdBuilder.deleteCharAt(partyIdBuilder.length() -1).toString() + 
	        ") order by field(" + partyIdBuilder.toString() + ")";

		System.out.println("selectQuery ********************** " + selectQuery);
		
	    try {
	    	preparedStatement = connection.prepareStatement(selectQuery);
	        resultSet = preparedStatement.executeQuery(selectQuery);
	        int index = 0;
	        while (resultSet.next()) {
	        	partyFace = partyFaceListForAClaim.get(index);
	        	partyFace.setPartyId(resultSet.getInt("partyId"));
	        	partyFace.setParticipantName(resultSet.getString("participantName"));
	        	partyFace.setFaceId(resultSet.getString("faceId"));

	            index ++;
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
	    return partyFaceListForAClaim;
		
	}

	
	
	
}
