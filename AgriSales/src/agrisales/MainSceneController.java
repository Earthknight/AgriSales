/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrisales;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author Sameer
 */
public class MainSceneController implements Initializable {
    
    @FXML
    private BorderPane rootLayout;
    @FXML
    private ToggleGroup group1;
    
  
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
         changeScene("SaleCounter.fxml");
         
    }    


    @FXML
    private void setSaleCounter(ActionEvent event) {
        changeScene("SaleCounter.fxml");
    }

    
      public  void changeScene(String scenePath){
        
        FXMLLoader loader;
        loader = new FXMLLoader(getClass().getResource(scenePath));
        AnchorPane pane = new AnchorPane();
           
    try{
            pane = (AnchorPane) loader.load();
            rootLayout.setCenter(pane);
        }
        catch(Exception e){

        }
     
    }

   
    @FXML
    private void setPurchaseScene(ActionEvent event) {
         changeScene("PurchaseScene.fxml");
    }

    @FXML
    private void setSellerScene(ActionEvent event) {
         changeScene("SellerScene.fxml");
    }

    @FXML
    private void setCustomerScene(ActionEvent event) {
        changeScene("CustomerScene.fxml");
    }
}
