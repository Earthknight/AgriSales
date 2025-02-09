/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrisales;

import DB.DBConnection;
import DB.DeleteDatabase;
import DB.DisplayDatabase;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author tanzeem
 */
public class SellerSceneController implements Initializable {

    @FXML
    private TextField sName;
    @FXML
    private TextField sContact;
    @FXML
    private Label warnMsg;
    @FXML
    private TextArea sAdd;
    @FXML
    private TableView<?> sellerTable;
DisplayDatabase sData = new DisplayDatabase();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         sData.buildData(sellerTable, "Select * from SellerTable;");
        // TODO
    }    
String nameS = "";
String contactS = "";
String addS = "";  

    private boolean GetCustomerFields() {
       nameS = sName.getText();
        
 contactS = sContact.getText();

 addS = sAdd.getText();
if(nameS==null || nameS.isEmpty()){
           sName.requestFocus();
            warnMsg.setText("Pls enter Seller's Name.");
            return false;
 }
  if(contactS==null || contactS.isEmpty()){
           sContact.requestFocus();
            warnMsg.setText("Pls Seller's Contact.");
            return false;
 }
   if(addS==null || addS.isEmpty()){
           sAdd.requestFocus();
            warnMsg.setText("Pls enter Seller's Address.");
            return false;
}
return true;

    }

      

    @FXML
    private void addSeller(ActionEvent event) {
        Connection c;
        boolean val =  GetCustomerFields();
       if(!val){
       return;
       }
       try{
           c = DBConnection.connect();
            String query = "INSERT INTO SellerTable (Name,Contact,Address)VALUES("+
"'"+nameS+"',\n" +
"'"+contactS+"',\n" +                  
"'"+addS+"');";            
         
            c.createStatement().execute(query);
             c.close();
       
       } catch (SQLException ex) {
            Logger.getLogger(SellerSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       clearAllFields();
         sData.buildData(sellerTable, "Select * from SellerTable;");
    }

    @FXML
    private void DeleteCustomer(ActionEvent event) {
        int index = sellerTable.getSelectionModel().getSelectedIndex();
         ObservableList data = sData.getData();
         ObservableList<String> items = (ObservableList) data.get(index);
         DeleteDatabase.deleteRecord(Integer.parseInt(items.get(0)), "SellerTable");
         
         sData.buildData(sellerTable, "Select * from SellerTable;");
    }

    private void clearAllFields() {
       sName.clear();
       sContact.clear();
       sAdd.clear();
    }
    
}
