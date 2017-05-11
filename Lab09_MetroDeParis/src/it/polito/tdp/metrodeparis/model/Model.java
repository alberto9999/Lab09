package it.polito.tdp.metrodeparis.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;


import it.polito.tdp.metrodeparis.dao.MetroDAO;

public class Model {
 
private List<Fermata> listaTotFermate;
 private  WeightedGraph<Fermata,DefaultWeightedEdge> grafoPercorso;
  private int tempoPercorrenza;
	
	
  
  public List<Fermata> getListaFermate(){
		if(listaTotFermate==null){
         MetroDAO mDAO= new MetroDAO();
		listaTotFermate=  mDAO.getAllFermate();
		}
		return listaTotFermate;
	}



public List<Fermata> calcolaPercorso(Fermata partenza, Fermata arrivo) {
	if(grafoPercorso==null){
	 generaGrafo();
	 }
	List<Fermata> percorso= new ArrayList<Fermata>();
	percorso=getPercorso(partenza,arrivo);
	
	return percorso;
	
}



private List<Fermata> getPercorso(Fermata partenza, Fermata arrivo) {
	if(partenza.equals(arrivo)){
		return null;
	}
	List<DefaultWeightedEdge>percorsoArchi = new ArrayList<DefaultWeightedEdge>();
	percorsoArchi=DijkstraShortestPath.findPathBetween(grafoPercorso, partenza, arrivo);
	List<Fermata>percorsoVertici = new ArrayList<Fermata>();
	tempoPercorrenza=0;
	
	percorsoVertici.add(partenza);
	for(DefaultWeightedEdge dwe: percorsoArchi){
		Fermata f1= grafoPercorso.getEdgeSource(dwe); 
		Fermata f2= grafoPercorso.getEdgeTarget(dwe);

		if(percorsoVertici.get(percorsoVertici.size()-1).equals(f1)){   //il grafo non è orientato perciò source e target del grafo sono a caso
			percorsoVertici.add(f2);
		}
		else if(percorsoVertici.get(percorsoVertici.size()-1).equals(f2)){   //il grafo non è orientato perciò source e target del grafo sono a caso
			percorsoVertici.add(f1);
		}
		tempoPercorrenza+=(grafoPercorso.getEdgeWeight(dwe)/50)*3600;  //distanza / velocitaMedia(suppongo 50km/h) * 3600 in secondi
	    tempoPercorrenza+=30;
	}

	return percorsoVertici;
	

}



public void generaGrafo() {
	MetroDAO mDAO= new MetroDAO();
	
	 grafoPercorso= new WeightedMultigraph<Fermata,DefaultWeightedEdge>(DefaultWeightedEdge.class);
	 Graphs.addAllVertices(grafoPercorso,getListaFermate());
	 
	 for(Fermata f: grafoPercorso.vertexSet()) {
			List<Fermata> adiacenti = mDAO.getListaFermateAdiacenti(f) ;
			for(Fermata f2: adiacenti){
			DefaultWeightedEdge e= grafoPercorso.addEdge(f, f2) ;
			grafoPercorso.setEdgeWeight(e, CalcolaDistanza(f.getCoords(),f2.getCoords()));
			}
	 
	 }
}



private double CalcolaDistanza(LatLng coords, LatLng coords2) {
	return LatLngTool.distance(coords,coords2, LengthUnit.KILOMETER) ;
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
}
	
	
	

