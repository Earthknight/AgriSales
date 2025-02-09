/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrisales;

import DB.DBConnection;
import DB.DeleteDatabase;
import DB.DisplayDatabase;
import DB.QueryDatabase;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author tanzeem
 */
public class PurchaseSceneController implements Initializable {
@FXML
    private TextField pName;
@FXML
    
private TextField sName;
@FXML
private ComboBox<String> pType;
@FXML
private TextField quantity;
@FXML
private TextField pRate;
@FXML
private TextField sRate;
@FXML
private DatePicker pDate;
@FXML
private Label warnMsg;
@FXML
private TableView<?> purchaseTable;
  
    ObservableList<String> pList = FXCollections.observableArrayList(); 
    ObservableList<String> typeList = FXCollections.observableArrayList(); 
    DisplayDatabase pData = new DisplayDatabase();
    

    /**
     * Initializes the controller class.
     */
ObservableList<String> sList=  FXCollections.observableArrayList(); 
   
String nameP = "";
String nameS = "";
String productType = "";
String qty = "";
LocalDate dateP;
double rateP = 0;
double rateS = 0;
    @FXML
    private Button btnAdd;
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        ResultSet rs = QueryDatabase.query("Select Name from SellerTable;");
        if(rs!=null){
            try {
                while(rs.next()){
                    sList.add(rs.getString(1));
                }
            } catch (SQLException ex) {
                Logger.getLogger(PurchaseSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        }
      
      AutoCompletionBinding<String> auto = TextFields.bindAutoCompletion(sName,sList); 
        
        typeList.add("Floriculture");
        typeList.add("Processed food");
        typeList.add("Fresh Vegitables");
        typeList.add("Fresh fruits");
        typeList.add("Cereals");
        typeList.add("Animal food");
        pType.setItems(typeList);
       
        pData.buildData(purchaseTable, "Select * from purchaseTable;");
        
        // TODO
    }    


@FXML
    private void addPurchase(ActionEvent event) {
        Connection c;
        boolean val =  GetPurchaseFields();
        String query;
       if(!val){
       return;
       }
       try{
           if(!update){
           c = DBConnection.connect();
             query = "INSERT INTO purchaseTable (ProductName,SellerName,ProductType,Quantity,PurchaseDate,PurchaseRate,SellingRate)VALUES("+
"'"+nameP+"',\n" +
"'"+nameS+"',\n" +
"'"+productType+"',\n" + 
"'"+qty+"',\n" +
"'"+dateP+"',\n" + 
"'"+rateP+"',\n" +                     
"'"+rateS+"');";            
         
            c.createStatement().execute(query);
              }else{
              c = DBConnection.connect();
            query = "Update purchaseTable set ProductName='"+nameP+"',SellerName='"+nameS+"',ProductType='"+productType+"',"
                   + "Quantity='"+qty+"',PurchaseDate='"+dateP+"',PurchaseRate='"+rateP+"',SellingRate='"+rateS+"'Where Id='"+id+"';";
                  System.out.println(query);
           c.createStatement().executeUpdate(query);
          
            }
            
            if(update){
                
                
                qty=String.valueOf(Double.parseDouble(qty)-Double.parseDouble(oldQty));
                query = "Update inventoryTable set Quantity=Quantity+"+qty+" where ProductName='"+nameP+"' And ProductType='"+productType+"';";
                c.createStatement().executeUpdate(query);
                       
             
            }
            ResultSet rs = QueryDatabase.query("Select * from InventoryTable where ProductName='"+nameP+"' And ProductType='"+productType+"';");
            if(rs!=null){
            if(rs.next()){
                        query = "Update inventoryTable set Quantity=Quantity+"+qty+",PurchaseRate="+rateP+" ,SellingRate="+rateS+"  where ProductName='"+nameP+"' And ProductType='"+productType+"';";
                    }else{
                        query = "INSERT INTO inventoryTable (ProductName,ProductType,Quantity,PurchaseRate,SellingRate)VALUES("+
                                "'"+nameP+"',\n" +
                                "'"+productType+"',\n" + 
                                "'"+qty+"',\n" +
                                "'"+rateP+"',\n" +                     
                                "'"+rateS+"');";      
                    }
            } else{
                        query = "INSERT INTO inventoryTable (ProductName,ProductType,Quantity,PurchaseRate,SellingRate)VALUES("+
                                "'"+nameP+"',\n" +
                                "'"+productType+"',\n" + 
                                "'"+qty+"',\n" +
                                "'"+rateP+"',\n" +                     
                                "'"+rateS+"');";      
                    }
                   
         
            c.createStatement().execute(query);
            
            
            
             c.close();
       
       } catch (SQLException ex) {
            Logger.getLogger(PurchaseSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       clearAllFields();
         pData.buildData(purchaseTable, "Select * from PurchaseTable;");
        
        
        
        
        
    }

    private boolean GetPurchaseFields() {
         nameP = pName.getText();
        
 nameS = sName.getText();
 productType = pType.getValue();
 qty = quantity.getText();
 dateP = pDate.getValue();
 
 String purchaseRate = pRate.getText();
 if(purchaseRate==null || purchaseRate.isEmpty()){
 pRate.requestFocus();
 warnMsg.setText("Please enter Purchase rate.");
 return false;
 }
 
 String saleRate = sRate.getText();
 if(saleRate==null || saleRate.isEmpty()){
 sRate.requestFocus();
 warnMsg.setText("Please enter Selling rate.");
 return false;
 }
 
 rateP = Double.parseDouble(pRate.getText());
 rateS = Double.parseDouble(sRate.getText());
 
 
 if(nameP==null || nameP.isEmpty()){
           pName.requestFocus();
            warnMsg.setText("Pls enter Product Name.");
            return false;
 }
 
 if(dateP==null ){
           pDate.requestFocus();
            warnMsg.setText("Pls enter Date of purchase.");
            return false;
 }
 
  if(nameS==null || nameS.isEmpty()){
           sName.requestFocus();
            warnMsg.setText("Pls enterSellers Name.");
            return false;
 }
   if(productType==null || productType.isEmpty()){
           pType.requestFocus();
            warnMsg.setText("Pls enter Batch Number.");
            return false;
 }
    if(qty==null || qty.isEmpty()){
           quantity.requestFocus();
            warnMsg.setText("Pls enter Quantity.");
            return false;
 }
     
    

 
 return true;
    }

    private void clearAllFields() {
      pName.clear();
     sName.clear();
      
      quantity.clear();
      pRate.clear();
      sRate.clear();
    pName.requestFocus();
    update = false;
    btnAdd.setText("Add Product");
    }

@FXML
    private void DeletePurchcase(ActionEvent event) {
        int index = purchaseTable.getSelectionModel().getSelectedIndex();
         ObservableList data = pData.getData();
         ObservableList<String> items = (ObservableList) data.get(index);
         DeleteDatabase.deleteRecord(Integer.parseInt(items.get(0)), "PurchaseTable");
         
       
         
         String  query = "Update inventoryTable set Quantity=Quantity-"+items.get(4)+"  where ProductName='"+items.get(2)+"' And ProductType='"+items.get(1)+"';";
                   
                   
          Connection c;
      
       try{
           c = DBConnection.connect();
            c.createStatement().execute(query);
             c.close();
       
       } catch (SQLException ex) {
            Logger.getLogger(PurchaseSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
          pData.buildData(purchaseTable, "Select * from PurchaseTable;");
    }

    
    @FXML
    private void getProducts(ActionEvent event) {
        String type = pType.getValue();
        if(type==null || type.isEmpty()){
         return;
        }
        pList.clear();
          ResultSet rs = QueryDatabase.query("Select ProductName from inventorytable WHERE ProductType= '"+type+"';");
        if(rs!=null){
            try {
                while(rs.next()){
                    pList.add(rs.getString(1));
                }
            } catch (SQLException ex) {
                Logger.getLogger(PurchaseSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        }
      
      AutoCompletionBinding<String> auto1 = TextFields.bindAutoCompletion(pName,pList); 
       
    }

    int id;
boolean update = false;
    String oldQty="0";
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @FXML
    private void UpdatePurchcase(ActionEvent event) {
        int index = purchaseTable.getSelectionModel().getFocusedIndex();
      ObservableList<ObservableList> data = pData.getData();
      ObservableList<String> itemData = data.get(index);
      
      oldQty = itemData.get(4);
      id = Integer.parseInt(itemData.get(0));
      pName.setText(itemData.get(2));
        sName.setText(itemData.get(3));
        pType.setValue(itemData.get(1));
        quantity.setText(oldQty);
        
        pDate.setValue(LocalDate.parse(itemData.get(5)));
        pRate.setText(itemData.get(6));
        sRate.setText(itemData.get(7));
        
        update=true;
        btnAdd.setText("Update");
        
    }

 
}
