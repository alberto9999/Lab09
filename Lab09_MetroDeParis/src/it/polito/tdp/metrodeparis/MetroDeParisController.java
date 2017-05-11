package it.polito.tdp.metrodeparis;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.metrodeparis.model.Fermata;
import it.polito.tdp.metrodeparis.model.Model;
import it.polito.tdp.metrodeparis.model.SimpleFermata;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class MetroDeParisController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea txtResult;

    @FXML
    private ComboBox<SimpleFermata> cmbPartenza;

    @FXML
    private ComboBox<SimpleFermata> cmbArrivo;

    @FXML
    private Button btnCalcolaPercorso;

    Model model;
 	public void setModel(Model model) {
 		this.model=model;
 		cmbPartenza.getItems().addAll(model.getListaSimpleFermate());
 		cmbArrivo.getItems().addAll(model.getListaSimpleFermate());
 		
 	}
    
    
    
    @FXML
    void doCalcola(ActionEvent event) {
     if(cmbPartenza.getValue()!=null&&cmbArrivo.getValue()!=null&&cmbPartenza.getValue()!=cmbArrivo.getValue()){
       txtResult.setText("");	
       SimpleFermata partenza= cmbPartenza.getValue();
       SimpleFermata arrivo= cmbArrivo.getValue();  
       List<Fermata> percorso= model.calcolaPercorso(partenza,arrivo);
       int lineaPrec=0;
       for(Fermata f: percorso){
    	   if(lineaPrec!=f.getLinea()){
    		   txtResult.appendText("LINEA: "+f.getLinea()+"\n");
    		   lineaPrec=f.getLinea();
    	   }
    	   txtResult.appendText(f+"\n");
       }
       txtResult.appendText("\nTEMPO DI PERCORRENZA TOTALE: "+model.calcolaTempoPercorrenza(percorso));
     }
     else{
    	 txtResult.setText("INSERIRE I CAMPI CORRETTAMENTE");
     }
    }

    @FXML
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'MetroDeParis.fxml'.";
        assert cmbPartenza != null : "fx:id=\"cmbPartenza\" was not injected: check your FXML file 'MetroDeParis.fxml'.";
        assert cmbArrivo != null : "fx:id=\"cmbArrivo\" was not injected: check your FXML file 'MetroDeParis.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'MetroDeParis.fxml'.";

    }

 
}
