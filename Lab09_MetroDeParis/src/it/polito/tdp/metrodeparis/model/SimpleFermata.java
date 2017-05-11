package it.polito.tdp.metrodeparis.model;

public class SimpleFermata {
String nome;
int id;
public SimpleFermata(String nome, int id) {
	super();
	this.nome = nome;
	this.id = id;
}
@Override
public String toString() {
	return nome;
}
public String getNome() {
	return nome;
}
public void setNome(String nome) {
	this.nome = nome;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + id;
	result = prime * result + ((nome == null) ? 0 : nome.hashCode());
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
	SimpleFermata other = (SimpleFermata) obj;
	if (id != other.id)
		return false;
	if (nome == null) {
		if (other.nome != null)
			return false;
	} else if (!nome.equals(other.nome))
		return false;
	return true;
}


}
