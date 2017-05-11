package it.polito.tdp.metrodeparis.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import org.jgrapht.graph.WeightedMultigraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;


import it.polito.tdp.metrodeparis.dao.MetroDAO;

public class Model {
 
 private List<Fermata> listaTotFermate;
 private List<SimpleFermata> listaTotSimpleFermate;
 private Map <Integer,Linea>mappaLinee=null;
 private  DirectedWeightedMultigraph<Fermata,DefaultWeightedEdge> grafoPercorso=null;
 private int tempoPercorrenza;
	
	
  
  public List<Fermata> getListaFermate(){
		if(listaTotFermate==null){
         MetroDAO mDAO= new MetroDAO();
		listaTotFermate=  mDAO.getAllFermate();
		}
		return listaTotFermate;
	}
 
  public List<SimpleFermata> getListaSimpleFermate(){
		if(listaTotSimpleFermate==null){
         MetroDAO mDAO= new MetroDAO();
		listaTotSimpleFermate=  mDAO.getAllSimpleFermate();
		}
		return listaTotSimpleFermate;
	}




public List<Fermata> calcolaPercorso(SimpleFermata partenza, SimpleFermata arrivo) {
	if(mappaLinee==null){
		MetroDAO mDAO = new MetroDAO();
		mappaLinee=mDAO.getListaLinee();
	}
	
	if(grafoPercorso==null){
	 generaGrafo();
	 }
	
	List<Fermata> percorso= new ArrayList<Fermata>();
	percorso=getPercorso(partenza,arrivo);
	
	return percorso;
	
}


private List<Fermata> getPercorso(SimpleFermata partenza, SimpleFermata arrivo) {
	if(partenza.equals(arrivo)){
		return null;
	}
	List<DefaultWeightedEdge>percorsoArchi = new ArrayList<DefaultWeightedEdge>();

	List<Fermata>possibiliPartenze=new ArrayList<Fermata>();
	List<Fermata>possibiliArrivi=new ArrayList<Fermata>();
	for(Fermata f: getListaFermate()){
		if(f.getIdFermata()==partenza.getId()){
			possibiliPartenze.add(f);
		}
		if(f.getIdFermata()==arrivo.getId()){
			possibiliArrivi.add(f);
		}	
	}
	List<Fermata>percorsoVerticiOttimale = new ArrayList<Fermata>();
	tempoPercorrenza=999999999;
	
	for(Fermata p: possibiliPartenze){
		for(Fermata a: possibiliArrivi){
	percorsoArchi=DijkstraShortestPath.findPathBetween(grafoPercorso, p, a);
	List<Fermata>percorsoVertici = new ArrayList<Fermata>();
	int temp=0;
	
	percorsoVertici.add(p);
	for(DefaultWeightedEdge dwe: percorsoArchi){
		Fermata f= grafoPercorso.getEdgeTarget(dwe);
		percorsoVertici.add(f);
		temp+=(grafoPercorso.getEdgeWeight(dwe))*3600;  //distanza / velocitaMedia(suppongo 40km/h) * 3600 in secondi
	    temp+=30;
	}
	if(temp<=tempoPercorrenza){
		percorsoVerticiOttimale=percorsoVertici;
		tempoPercorrenza=temp;
	}
	}
	}
	return percorsoVerticiOttimale;

}
	



public void generaGrafo() {
	MetroDAO mDAO= new MetroDAO();
	
	 grafoPercorso= new DirectedWeightedMultigraph<Fermata,DefaultWeightedEdge>(DefaultWeightedEdge.class);
	 Graphs.addAllVertices(grafoPercorso,getListaFermate());
	 
	 for(Fermata f: grafoPercorso.vertexSet()) {
			List<Fermata> adiacenti = mDAO.getListaFermateAdiacenti(f) ;
			for(Fermata f2: adiacenti){
			DefaultWeightedEdge e= grafoPercorso.addEdge(f, f2) ;
			grafoPercorso.setEdgeWeight(e, calcolaPeso(f,f2));
			} 
	 }
	 for(Fermata f1: grafoPercorso.vertexSet()){
		 for(Fermata f2: grafoPercorso.vertexSet()){
			 if(!f1.equals(f2)&&f1.getIdFermata()==f2.getIdFermata()){
				 DefaultWeightedEdge e1= grafoPercorso.addEdge(f1, f2);
				 grafoPercorso.setEdgeWeight(e1,calcolaPeso(f1,f2));
				 DefaultWeightedEdge e2= grafoPercorso.addEdge(f2, f1);
				 grafoPercorso.setEdgeWeight(e2,calcolaPeso(f2,f1));	 
			 }
			 
		 }
		 
		 
		 
	 }
}

private double calcolaPeso(Fermata f1, Fermata f2) {
	double result;
	if(f1.getIdFermata()==f2.getIdFermata()){
		result=(mappaLinee.get(f2.getLinea()).getIntervallo())/60;  //trasformo in ore
	}
	else{
	double distanza=LatLngTool.distance(f1.getCoords(),f2.getCoords(), LengthUnit.KILOMETER);
	double velocitaLinea= mappaLinee.get(f1.getLinea()).getVelocita();
	result=distanza/velocitaLinea;
	}
	return  result;
}



public String calcolaTempoPercorrenza(List<Fermata> percorso) {	
int	h= (this.tempoPercorrenza/3600);             //tronca sulle ore
int	min=(this.tempoPercorrenza-(h*3600))/60;     //tronca sui minuti
int	sec= this.tempoPercorrenza-(h*3600)-(min*60); 
	
String hStr=Integer.toString(h);
String minStr=Integer.toString(min);
String secStr=Integer.toString(sec);

if(hStr.length()<2)
	hStr=0+hStr;
if(minStr.length()<2)
	minStr=0+minStr;
if(secStr.length()<2)
	secStr=0+secStr;

return (hStr+":"+minStr+":"+secStr);
}

public String NomeDaIdLinea(int linea) {
	
	return mappaLinee.get(linea).getNome();
}





}
	
	

