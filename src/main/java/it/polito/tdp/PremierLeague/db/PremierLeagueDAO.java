package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {
	
	public List<Player> listAllPlayers(){
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void listAllMatches(Map<Integer, Match> allMatchMap){
		String sql = "SELECT m.MatchID, m.TeamHomeID, m.TeamAwayID, m.teamHomeFormation, m.teamAwayFormation, m.resultOfTeamHome, m.date, t1.Name, t2.Name   "
				+ "FROM Matches m, Teams t1, Teams t2 "
				+ "WHERE m.TeamHomeID = t1.TeamID AND m.TeamAwayID = t2.TeamID";
//		List<Match> result = new ArrayList<Match>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				
				Match match = new Match(res.getInt("m.MatchID"), res.getInt("m.TeamHomeID"), res.getInt("m.TeamAwayID"), res.getInt("m.teamHomeFormation"), 
							res.getInt("m.teamAwayFormation"),res.getInt("m.resultOfTeamHome"), res.getTimestamp("m.date").toLocalDateTime(), res.getString("t1.Name"),res.getString("t2.Name"));
				
				
//				result.add(match);
				allMatchMap.put(match.getMatchID(), match);

			}
			conn.close();
//			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
//			return null;
		}
	}
	
	public List<Match> getVertici(int mese) {
		String sql = "SELECT m.MatchID, m.TeamHomeID, m.TeamAwayID, m.teamHomeFormation, m.teamAwayFormation, m.resultOfTeamHome, m.date, t1.Name, t2.Name "
				+ "FROM Matches m, Teams t1, Teams t2 "
				+ "WHERE m.TeamHomeID = t1.TeamID AND m.TeamAwayID = t2.TeamID AND MONTH(DATE) = ?";
		
		List<Match> result = new ArrayList<Match>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, mese);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				
				Match match = new Match(res.getInt("m.MatchID"), res.getInt("m.TeamHomeID"), res.getInt("m.TeamAwayID"), res.getInt("m.teamHomeFormation"), 
						res.getInt("m.teamAwayFormation"),res.getInt("m.resultOfTeamHome"), res.getTimestamp("m.date").toLocalDateTime(), res.getString("t1.Name"),res.getString("t2.Name"));
				
				
				result.add(match);

			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Adiacenza> getAdiacenti(int minuti, Map<Integer, Match> allMatchMap) {
		final String sql = "SELECT a1.playerID, a1.MatchID, a1.timeplayed, a2.PlayerID, a2.MatchID, COUNT(DISTINCT a1.playerID) AS peso "
				+ "FROM actions a1, actions a2 "
				+ "WHERE a1.MatchID > a2.MatchID AND a1.PlayerID = a2.PlayerID AND a1.timeplayed >= ? AND a2.timeplayed >= ? "
				+ "GROUP BY a1.MatchID, a2.MatchID";
		
		//Altro metodo: da query (non serve iterare sui vertici nel model)
//		final String sql = "SELECT a1.playerID, a1.MatchID, a1.timeplayed, a2.PlayerID, a2.MatchID, COUNT(DISTINCT a1.playerID) AS peso "
//				+ "FROM actions a1, actions a2 "
//				+ "WHERE a1.MatchID > a2.MatchID AND a1.PlayerID = a2.PlayerID AND a1.timeplayed >= ? AND a2.timeplayed >= ? "
//				+ "AND (a1.MatchID, a2.MatchID) IN ( "
//				+ "SELECT m1.MatchID, m2.MatchID "
//				+ "FROM matches m1, matches m2 "
//				+ "WHERE m1.MatchID != m2.MatchID AND MONTH(m1.date) = MONTH(m2.date) AND MONTH(m1.date) = ?) "
//				+ "GROUP BY a1.MatchID, a2.MatchID";
		
		List<Adiacenza> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, minuti);
			st.setInt(2, minuti);
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				Adiacenza adiacenza = new Adiacenza(allMatchMap.get(res.getInt("a1.MatchID")), allMatchMap.get(res.getInt("a2.MatchID")), res.getInt("peso"));
				
				result.add(adiacenza);
			}
			
			conn.close();
			return result;
			
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}	
	
	//Altro metodo per gli archi
//	public List<Adiacenza> getAdiacenti(int minuti, Match m1, Match m2) {
//		final String sql = "SELECT a1.playerid, a1.matchid, a2.playerid, a2.matchid, COUNT(*) AS peso "
//				+ "FROM actions a1, actions a2 "
//				+ "WHERE a1.playerid = a2.playerid AND a1.TimePlayed >= ? AND a2.timeplayed >= ? "
//				+ "AND a1.MatchID = ? "
//				+ "AND a2.matchid = ?";
//		
//		List<Adiacenza> result = new ArrayList<>();
//		Connection conn = DBConnect.getConnection();
//		try {
//			PreparedStatement st = conn.prepareStatement(sql);
//			st.setInt(1, minuti);
//			st.setInt(2, minuti);
//			st.setInt(3, m1.getMatchID());
//			st.setInt(4, m2.getMatchID());
//			ResultSet res = st.executeQuery();
//			
//			while(res.next()) {
//				Adiacenza adiacenza = new Adiacenza(m1, m2, res.getInt("peso"));
//				
//				result.add(adiacenza);
//			}
//			
//			conn.close();
//			return result;
//			
//		}catch(SQLException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}	

}
