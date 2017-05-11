package it.polito.tdp.metrodeparis.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.javadocmd.simplelatlng.LatLng;

import it.polito.tdp.metrodeparis.model.Fermata;
import it.polito.tdp.metrodeparis.model.Linea;
import it.polito.tdp.metrodeparis.model.SimpleFermata;


public class MetroDAO {

	public List<Fermata> getAllFermate() {

		final String sql = "SELECT id_fermata, nome, coordx, coordy, id_linea"+
				" FROM fermata f, connessione c"+
				 " WHERE f.id_fermata=c.id_stazP"+ 
				 " ORDER BY nome ASC";
		List<Fermata> fermate = new ArrayList<Fermata>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Fermata f = new Fermata(rs.getInt("id_Fermata"), rs.getString("nome"), new LatLng(rs.getDouble("coordx"), rs.getDouble("coordy")),rs.getInt("id_linea"));
				if(!fermate.contains(f)){
				fermate.add(f);
				}
			}

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}

		return fermate;
	}

	
	public List<SimpleFermata> getAllSimpleFermate() {

		final String sql = "SELECT  nome, id_fermata "+
				" FROM fermata f, connessione c"+
				 " WHERE f.id_fermata=c.id_stazP"+ 
				 " ORDER BY nome ASC";
		List<SimpleFermata> fermate = new ArrayList<SimpleFermata>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				SimpleFermata f = new SimpleFermata( rs.getString("nome"),rs.getInt("id_fermata"));
				if(!fermate.contains(f)){
				fermate.add(f);
				}
			}

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}

		return fermate;
	}
	
	
	
	
	
	public List<Fermata> getListaFermateAdiacenti(Fermata fermata) {
		final String sql = "SELECT * "+
                           "FROM fermata f, connessione c "+
                           "WHERE f.id_fermata = c.id_stazA AND c.id_stazP=? AND c.id_linea=?";
		List<Fermata> fermate = new ArrayList<Fermata>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1,fermata.getIdFermata());
			st.setInt(2, fermata.getLinea());
			ResultSet rs = st.executeQuery();
			

			while (rs.next()) {
				Fermata f = new Fermata(rs.getInt("id_fermata"), rs.getString("nome"), new LatLng(rs.getDouble("coordx"), rs.getDouble("coordy")),rs.getInt("id_linea"));
				fermate.add(f);
			}

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}

		return fermate;
	}


	public double getVelocitaLinea(Fermata f1, Fermata f2) {
		final String sql = "SELECT  velocita "+
                "FROM linea l, connessione c "+
                "WHERE l.id_linea = c.id_linea AND c.id_stazP=? AND c.id_stazA=?";
		double velocita=0;

try {
	Connection conn = DBConnect.getInstance().getConnection();
	PreparedStatement st = conn.prepareStatement(sql);
	st.setInt(1,f1.getIdFermata());
	st.setInt(2,f2.getIdFermata());
	ResultSet rs = st.executeQuery();
	

	if (rs.next()) {
		velocita=rs.getDouble("velocita");
	}

	st.close();
	conn.close();

} catch (SQLException e) {
	e.printStackTrace();
	throw new RuntimeException("Errore di connessione al Database.");
}

return velocita;
	}


	
	
	
	public Map<Integer, Linea> getListaLinee() {
		final String sql = "SELECT  * "+
                "FROM linea";
    Map<Integer,Linea>listaLinee=new HashMap<Integer,Linea>();            
try {
	Connection conn = DBConnect.getInstance().getConnection();
	PreparedStatement st = conn.prepareStatement(sql);
	
	ResultSet rs = st.executeQuery();
	

	while(rs.next()) {
		listaLinee.put(rs.getInt("id_linea"), new Linea(rs.getInt("id_linea"),rs.getString("nome"),rs.getDouble("velocita"),rs.getDouble("intervallo"),rs.getString("colore")));
	}

	st.close();
	conn.close();

} catch (SQLException e) {
	e.printStackTrace();
	throw new RuntimeException("Errore di connessione al Database.");
}

return listaLinee;
	}



}
