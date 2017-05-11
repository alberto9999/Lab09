package it.polito.tdp.metrodeparis.model;

import com.javadocmd.simplelatlng.LatLng;

public class Fermata {

	private int idFermata;
	private String nome;
	private LatLng coords;
	private int linea;
	
	public Fermata(int idFermata, String nome, LatLng coords,int linea) {
		this.idFermata = idFermata;
		this.nome = nome;
		this.coords = coords;
		this.linea= linea;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idFermata;
		result = prime * result + linea;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Fermata other = (Fermata) obj;
		if (idFermata != other.idFermata)
			return false;
		if (linea != other.linea)
			return false;
		return true;
	}

	public int getLinea() {
		return linea;
	}

	public void setLinea(int linea) {
		this.linea = linea;
	}

	public Fermata(int idFermata) {
		this.idFermata = idFermata;
	}

	public int getIdFermata() {
		return idFermata;
	}

	public void setIdFermata(int idFermata) {
		this.idFermata = idFermata;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public LatLng getCoords() {
		return coords;
	}

	public void setCoords(LatLng coords) {
		this.coords = coords;
	}

	
	@Override
	public String toString() {
		return nome ;
	}
}
