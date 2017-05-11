package it.polito.tdp.metrodeparis;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.metrodeparis.model.Fermata;
import it.polito.tdp.metrodeparis.model.Model;
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
    private ComboBox<Fermata> cmbPartenza;

    @FXML
    private ComboBox<Fermata> cmbArrivo;

    @FXML
    private Button btnCalcolaPercorso;

    Model model;
 	public void setModel(Model model) {
 		this.model=model;
 		cmbPartenza.getItems().addAll(model.getListaFermate());
 		cmbArrivo.getItems().addAll(model.getListaFermate());
 		
 	}
    
    
    
    @FXML
    void doCalcola(ActionEvent event) {
     if(cmbPartenza.getValue()!=null&&cmbArrivo.getValue()!=null&&cmbPartenza.getValue()!=cmbArrivo.getValue()){
       txtResult.setText("");	
       Fermata partenza= cmbPartenza.getValue();
       Fermata arrivo= cmbArrivo.getValue();  
       List<Fermata> percorso= model.calcolaPercorso(partenza,arrivo);
       
       for(Fermata f: percorso){
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
