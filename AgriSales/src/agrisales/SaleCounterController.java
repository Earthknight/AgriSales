/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agrisales;

import DB.DBConnection;
import DB.DisplayDatabase;
import DB.QueryDatabase;
import Model.Items;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

/**
 *
 * @author Sameer
 */
public class SaleCounterController implements Initializable {

    @FXML
    private AnchorPane SaleCounterPage;
    @FXML
    private AnchorPane addInvoPage;
    @FXML
    private DatePicker rDate;
    @FXML
    private Label rNum1;
    @FXML
    private AnchorPane qcustomerAnchor;
    @FXML
    private TextField rCustName;
    @FXML
    private TableView<Items> rTableView;
    @FXML
    private ContextMenu productModifyMenu;
    @FXML
    private MenuItem pDelete;
    @FXML
    private TextField itemQty;
    @FXML
    private Label itemRate;
    @FXML
    private Label itemAmount;
    @FXML
    private Button addItemBtn;
    @FXML
    private Label rTotal;
    @FXML
    private Button sReceiptBtn;
    @FXML
    private Button resetBtn;
    @FXML
    private ComboBox<String> pType;
    @FXML
    private TextField pName;

    
    ObservableList<String> pList = FXCollections.observableArrayList(); 
    ObservableList<String> typeList = FXCollections.observableArrayList(); 
     ObservableList<String> cList = FXCollections.observableArrayList(); 
    @FXML
    private Label warnMsg;
    @FXML
    private TableView<?> sTableView;
   
    DisplayDatabase sData = new DisplayDatabase();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sData.buildData(sTableView, "Select * from salesItemTable;");
        createSaleTable();
        typeList.add("Floriculture");
        typeList.add("Processed food");
        typeList.add("Fresh Vegitables");
        typeList.add("Fresh fruits");
        typeList.add("Cereals");
        typeList.add("Animal food");
        pType.setItems(typeList);
       
       ResultSet rs = QueryDatabase.query("Select Name from customertable;");
        if(rs!=null){
            try {
                while(rs.next()){
                    cList.add(rs.getString(1));
                }
            } catch (SQLException ex) {
                Logger.getLogger(SaleCounterController.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        }
      
      AutoCompletionBinding<String> autocust = TextFields.bindAutoCompletion(rCustName,cList); 
      
        
        // TODO
    }   
    
    @FXML
    private void deleteProduct(ActionEvent event) {
        int index = rTableView.getSelectionModel().getSelectedIndex();
        Items i = saleList.get(index);
        total-=Double.parseDouble(i.getAmount());
        rTotal.setText(String.format("%.2f", total));
        saleList.remove(index);
        rTableView.refresh();
    }

    

   
    String cName = "";
    LocalDate date;


   
    @FXML
    private void sReceipt(ActionEvent event) {
       
        cName = rCustName.getText();
        date = rDate.getValue();
        
        if(cName==null || cName.isEmpty()){
            warnMsg.setText("Please Enter customer Name.");
            rCustName.requestFocus();
            return;
        }
        
        if(date==null){
            warnMsg.setText("Please Enter Date.");
            rDate.requestFocus();
            return;
        }
        
       Connection c;
       try{
           c = DBConnection.connect();
           
        String query = "INSERT INTO SalesTable (Date,CustomerName,Amount)VALUES("+
                            "'"+date+"',\n" +
                            "'"+cName+"',\n" +
                            "'"+total+"');";                    
         
         PreparedStatement ps =  c.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);
         ps.execute();
         System.out.println(query);
         ResultSet rs = ps.getGeneratedKeys();
         rs.next();
         String saleId = rs.getString(1);
         
         for(Items i: saleList){
             query = "INSERT INTO salesitemtable (SaleId,ProductType,ProductName,Qty,Rate)VALUES("+
                "'"+saleId+"',\n" +
                "'"+i.getpType()+"',\n" +
                "'"+i.getpName()+"',\n" +
                "'"+i.getQty()+"',\n" +
                "'"+i.getAmount()+"');";                    
         
         c.createStatement().execute(query);
            System.out.println(query);
            String iQuery = "Update inventoryTable set Quantity=Quantity-"+i.getQty()+" where ProductName='"+i.getpName()+"' And ProductType='"+i.getpType()+"';";
            c.createStatement().execute(iQuery);
            
            }
            
            
            
            c.close();
       
       } catch (SQLException ex) {
            Logger.getLogger(SaleCounterController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
         sData.buildData(sTableView, "Select * from salesItemTable;");
        resetR();
        
    }


    @FXML
    private void resetR() {
        total=0;
        rDate.setValue(LocalDate.now());
        rCustName.clear();
        rTotal.setText("0.0");
        resetItemFields();
        saleList.clear();
        rTableView.refresh();
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
       auto1.setOnAutoCompleted(
       e -> {
           getRate();
       });
    }
    
    double rate = 0;
    double iQty = 0;
    double amount = 0;
    double total = 0;
    @FXML
    private void getRate() {
        
        
        String sQty = itemQty.getText();
        
        if(sQty==null || sQty.isEmpty()){
        iQty=1;
        itemQty.setText("1");
        }else{
        iQty = Double.parseDouble(sQty);
        }  
       
        ResultSet rs = QueryDatabase.query("Select SellingRate from inventoryTable where ProductType='"+pType.getValue()+"' AND ProductName='"+pName.getText()+"';");
        if(rs!=null){
            try {
               if(rs.next()){
                    rate = Double.parseDouble(rs.getString(1));
                    itemRate.setText(rs.getString(1));
                    amount = rate*iQty;
                    itemAmount.setText(String.valueOf(amount));
                   
                }
            } catch (SQLException ex) {
                Logger.getLogger(SaleCounterController.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        }
    }

   

    @FXML
    private void calcAmount() {
       
         name = pName.getText();
        String qty = itemQty.getText();
        
        if(name==null || name.isEmpty()){
            warnMsg.setText("Pls enter product details.");
            pName.requestFocus();
            return;
        }
        
        if(qty==null || qty.isEmpty()){
            warnMsg.setText("Pls enter Qty.");
            itemQty.requestFocus();
            return;
        }
        
        if(rate<=0){
        warnMsg.setText("Product not in database.");
        return;
        }
        
        iQty = Double.parseDouble(qty);
        amount = iQty*rate;
        itemAmount.setText(String.format("%.2f", amount));
        
        
    }
ObservableList<Items> saleList = FXCollections.observableArrayList();
    @FXML
    private void addItem(ActionEvent event) {
        getSaleFields();
                
        saleList.add(new Items(name,type,iQty,String.valueOf(amount)));
        rTableView.setItems(saleList);
        total = total+amount;
        rTotal.setText(String.format("%.2f", total));
        
        resetItemFields();
    }

    private void resetItemFields() {
        pName.clear();
        rate=0;
        itemRate.setText("0.0");
        amount = 0;
        itemQty.setText("1");
        itemAmount.setText("0.0");
    }

    String name;
    String type;
   
   
    private void getSaleFields() {
       
         
        type = pType.getValue();
       
        if(type==null || type.isEmpty()){
            warnMsg.setText("Pls Select Product Type.");
            pType.requestFocus();
            return;
         }
        
       calcAmount();
        
         
        
        
        
    }

    private void createSaleTable() {
   
       
           TableColumn col_Type=  new TableColumn("Type");
        col_Type.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Items,String>,ObservableValue<String>>(){            
           public ObservableValue<String> call(TableColumn.CellDataFeatures<Items, String> param) {                                                 
             return new SimpleStringProperty(param.getValue().getpType());              
           }            
         });  
        rTableView.getColumns().addAll(col_Type);
        
        
        TableColumn pro_name = new TableColumn("ProductName");
        pro_name.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Items,String>,ObservableValue<String>>(){            
           public ObservableValue<String> call(TableColumn.CellDataFeatures<Items, String> param) {                                                 
             return new SimpleStringProperty(param.getValue().getpName());              
           }            
         });  
        rTableView.getColumns().addAll(pro_name);
        
        TableColumn col_qty = new TableColumn("Quantity");
        col_qty.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Items,String>,ObservableValue<String>>(){            
           public ObservableValue<String> call(TableColumn.CellDataFeatures<Items, String> param) {                                                 
             return new SimpleStringProperty(String.valueOf(param.getValue().getQty()));              
           }            
         }); 
        
        rTableView.getColumns().addAll(col_qty);
        
        TableColumn col_amount = new TableColumn("Amount");
        col_amount.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Items,String>,ObservableValue<String>>(){            
           public ObservableValue<String> call(TableColumn.CellDataFeatures<Items, String> param) {                                                 
             return new SimpleStringProperty(param.getValue().getAmount());              
           }            
         });  
        rTableView.getColumns().addAll(col_amount);
        
        
    
    }
    
}
