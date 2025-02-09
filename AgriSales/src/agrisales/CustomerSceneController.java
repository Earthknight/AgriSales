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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author tanzeem
 */
public class CustomerSceneController implements Initializable {

    @FXML
    private TextField cName;
   
    @FXML
    private Label warnMsg;
    
   
DisplayDatabase cData = new DisplayDatabase();
    @FXML
    private TableView<?> customerTable;
    @FXML
    private TextField cContact;
    @FXML
    private TextArea cAdd;
   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         cData.buildData(customerTable, "Select * from customerTable;");
        // TODO
    }    
String nameC = "";
String contactC = "";
String addC = "";
    @FXML
    private void addCustomer(ActionEvent event) {
         Connection c;
        boolean val =  GetCustomerFields();
       if(!val){
       return;
       }
       try{
           c = DBConnection.connect();
            String query = "INSERT INTO customerTable (Name,Contact,Address)VALUES("+
"'"+nameC+"',\n" +
"'"+contactC+"',\n" +                  
"'"+addC+"');";            
         
            c.createStatement().execute(query);
             c.close();
       
       } catch (SQLException ex) {
            Logger.getLogger(CustomerSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       clearAllFields();
         cData.buildData(customerTable, "Select * from customerTable;");
        
        
    }

    private void clearAllFields() {
      cName.clear();
      cContact.clear();
      cAdd.clear();
    }
        

 
 
 

    @FXML
    private void DeleteCustomer(ActionEvent event) {
        int index = customerTable.getSelectionModel().getSelectedIndex();
         ObservableList data = cData.getData();
         ObservableList<String> items = (ObservableList) data.get(index);
         DeleteDatabase.deleteRecord(Integer.parseInt(items.get(0)), "CustomerTable");
         
         cData.buildData(customerTable, "Select * from CustomerTable;");
    }

    private boolean GetCustomerFields() {
       nameC = cName.getText();
        
 contactC = cContact.getText();

 addC = cAdd.getText();
if(nameC==null || nameC.isEmpty()){
           cName.requestFocus();
            warnMsg.setText("Pls enter Customer's Name.");
            return false;
 }
  if(contactC==null || contactC.isEmpty()){
           cContact.requestFocus();
            warnMsg.setText("Pls Customer's Contact.");
            return false;
 }
   if(addC==null || addC.isEmpty()){
           cAdd.requestFocus();
            warnMsg.setText("Pls enter Customer's Address.");
            return false;
}
return true;

    }


   
    
}
