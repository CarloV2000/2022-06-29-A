/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import it.polito.tdp.itunes.model.Album;
import it.polito.tdp.itunes.model.BilancioAlbum;
import it.polito.tdp.itunes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnAdiacenze"
    private Button btnAdiacenze; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="cmbA1"
    private ComboBox<Album> cmbA1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbA2"
    private ComboBox<Album> cmbA2; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML
    void doCalcolaAdiacenze(ActionEvent event) {
    	Album album1 = cmbA1.getValue();
    	if(album1==null) {
    		this.txtResult.setText("Selezionare un album dalla box 1");
    		return;
    	}
    	List<BilancioAlbum> bilanci = model.getAdiacenti(album1);
    	this.txtResult.setText("Stampo successori dell'album "+album1+"\n");
    	for(BilancioAlbum b : bilanci) {
    		this.txtResult.appendText(b + "\n");
    	}
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	Album album1 = cmbA1.getValue();//
    	if(album1==null) {
    		this.txtResult.setText("Selezionare un album dalla box 1");
    		return;
    	}
    	
    	Album album2 = cmbA2.getValue();
    	if(album1==null) {
    		this.txtResult.setText("Selezionare un album dalla box 2");
    		return;
    	}
    	
    	String input = this.txtN.getText();
    	int inputNum = 0;
    	if(input == "") {
    		this.txtResult.setText("Input string is empty");
    	}
    	try {
    		inputNum = Integer.parseInt(input);
    		
    		
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Inserire un numero");
    	}
    	List<Album>path = model.getPath(album1, album2, inputNum);
    	if(path.isEmpty()) {
    		this.txtResult.setText("No path between "+ album1+ " and " + album2);
    		return;
    	}
    	
    	this.txtResult.setText("Percorso : \n");
    	for(Album a : path) {
    		this.txtResult.appendText(""+a);
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	String input = this.txtN.getText();
    	int inputNum = 0;
    	if(input == "") {
    		this.txtResult.setText("Input string is empty");
    	}
    	try {
    		inputNum = Integer.parseInt(input);
    		model.buildGraph(inputNum);
    		int numVert = model.getNumVertici();
    		int numEdges = model.getNumEdges();
    		
    		this.txtResult.appendText("Grafo creato correttamente");
    		this.txtResult.appendText("numero vertici = "+model.getNumVertici()+" numero archi = "+model.getNumEdges());
    		
    		this.cmbA1.getItems().setAll(model.getVertices());
    		this.cmbA2.getItems().setAll(model.getVertices());
    		
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Inserire un numero");
    	}
    	
    	
    	
    	
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnAdiacenze != null : "fx:id=\"btnAdiacenze\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbA1 != null : "fx:id=\"cmbA1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbA2 != null : "fx:id=\"cmbA2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";

    }

    
    public void setModel(Model model) {
    	this.model = model;
    }
}
