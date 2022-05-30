/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnConnessioneMassima"
    private Button btnConnessioneMassima; // Value injected by FXMLLoader

    @FXML // fx:id="btnCollegamento"
    private Button btnCollegamento; // Value injected by FXMLLoader

    @FXML // fx:id="txtMinuti"
    private TextField txtMinuti; // Value injected by FXMLLoader

    @FXML // fx:id="cmbMese"
    private ComboBox<Month> cmbMese; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM1"
    private ComboBox<Match> cmbM1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM2"
    private ComboBox<Match> cmbM2; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doConnessioneMassima(ActionEvent event) {
    	txtResult.clear();
    	if(model.getGrafo() == null) {
    		txtResult.setText("Creare prima il grafo!");
    		return;
    	}
    	else {
    		txtResult.setText("Coppie con connessione massima:\n\n");
    		for(Adiacenza a : model.getConnessioneMax())
    		txtResult.appendText(a.toString());
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	
    	Integer minuti;
    	Month mese;
    	Integer nMese;
    	
    	try {
    		minuti = Integer.parseInt(txtMinuti.getText());
    		mese = cmbMese.getValue();
    		if(mese == null) {
    			txtResult.setText("Inserire valori");
    			return;
    		}
    		else {
    			nMese = mese.getValue();
    			txtResult.appendText(model.creaGrafo(nMese, minuti));
    		}
    		
    	}catch(NumberFormatException e) {
    		e.printStackTrace();
    		txtResult.setText("Inserire valori");
    		return;
    	}
    	
    	cmbM1.getItems().clear();
    	cmbM1.getItems().addAll(model.getGrafo().vertexSet());
    	cmbM2.getItems().clear();
    	cmbM2.getItems().addAll(model.getGrafo().vertexSet());
    	
    }

    @FXML
    void doCollegamento(ActionEvent event) {
    	txtResult.clear();
    	
    	Match m1 = cmbM1.getValue();
    	Match m2 = cmbM2.getValue();
    	
    	if(model.getGrafo() == null) {
    		txtResult.setText("Creare prima il grafo!");
    		return;

    	}
    	
    	if(m1.equals(m2)) {
    		txtResult.setText("Scegliere due partite diverse");
    		return;
    	}
    	
    	for(Match m : model.calcolaCollegamento(m1, m2)) {
    		txtResult.appendText(m.toString() + "\n");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnConnessioneMassima != null : "fx:id=\"btnConnessioneMassima\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCollegamento != null : "fx:id=\"btnCollegamento\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMinuti != null : "fx:id=\"txtMinuti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbMese != null : "fx:id=\"cmbMese\" was not injected: check your FXML file 'Scene.fxml'.";        assert cmbM1 != null : "fx:id=\"cmbM1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbM2 != null : "fx:id=\"cmbM2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        
        cmbMese.getItems().clear();
        for(int i = 1; i <= 12; i++) {
        	cmbMese.getItems().add(Month.of(i));
        }
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
    
    
}
