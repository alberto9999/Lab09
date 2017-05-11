package it.polito.tdp.metrodeparis.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jgrapht.graph.DefaultWeightedEdge;

import com.javadocmd.simplelatlng.LatLng;

import it.polito.tdp.metrodeparis.model.Fermata;

public class MetroDAO {

	public List<Fermata> getAllFermate() {

		final String sql = "SELECT id_fermata, nome, coordx, coordy FROM fermata ORDER BY nome ASC";
		List<Fermata> fermate = new ArrayList<Fermata>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Fermata f = new Fermata(rs.getInt("id_Fermata"), rs.getString("nome"), new LatLng(rs.getDouble("coordx"), rs.getDouble("coordy")));
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

	public List<Fermata> getListaFermateAdiacenti(Fermata fermata) {
		final String sql = "SELECT * "+
                           "FROM fermata f, connessione c "+
                           "WHERE f.id_fermata = c.id_stazA AND c.id_stazP=?";
		List<Fermata> fermate = new ArrayList<Fermata>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1,fermata.getIdFermata());
			ResultSet rs = st.executeQuery();
			

			while (rs.next()) {
				Fermata f = new Fermata(rs.getInt("id_fermata"), rs.getString("nome"), new LatLng(rs.getDouble("coordx"), rs.getDouble("coordy")));
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
}
