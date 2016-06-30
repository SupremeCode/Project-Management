package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Optional;

import com.mysql.jdbc.PreparedStatement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class SearchWindowController {
	protected Main main;
	protected Stage primaryStage;
	@FXML TextField orderNumber;
	String url = "jdbc:mysql://localhost:3306/yourSchemaName"; 
	String uname ="root";
	String pword = "yourDbPassword"; 
	String projectNm, clientNm, POnum, workDesc, itemDesc, drawingNum, traceNum,
	bm1, tn1, bm2, tn2, bm3, tn3, startDate, approved;
	String actualCost, orderNum;
	Image imageProduct;
	
	public void setMain(Main main, Stage primaryStage){
		this.main = main;
		this.primaryStage = primaryStage;
	}
	public void search(){

		try{
			orderNum = orderNumber.getText().toString();
			main.orderNum = orderNum;
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(url, uname, pword);
			System.out.println("Connected database successfully...");
			
			//if orderNum is equal to one of the id's:
			String initQuery = "SELECT * FROM workorder WHERE id = ?";
			PreparedStatement initStatement = (PreparedStatement) con.prepareStatement(initQuery);
			initStatement.setString(1, orderNum);
			ResultSet initRs = initStatement.executeQuery();
			if (initRs.next()){
			
			Statement stmnt = con.createStatement();
			String query = "SELECT projectName, clientName, PO, workDescription, itemDescription, drawingNumber, "
				+ "traceNumber, BM1, TN1, BM2, TN2, BM3, TN3, actualCost, photo, approved FROM workorder WHERE id "
				+ "= " + orderNum;
			ResultSet rs = stmnt.executeQuery(query);
			
			while(rs.next()){ //save a copy from MySQL record to main variables for SecondWindowController use.
			projectNm = rs.getString(1);
			main.projectNm = projectNm;
			clientNm = rs.getString(2);
			main.clientNm = clientNm;
			POnum = rs.getString(3);
			main.POnum = POnum;
			workDesc = rs.getString(4);
			main.workDesc = workDesc;
			itemDesc = rs.getString(5);
			main.itemDesc = itemDesc;
			drawingNum = rs.getString(6);
			main.drawingNum = drawingNum;
			traceNum = rs.getString(7);
			main.traceNum = traceNum;
			bm1 = rs.getString(8);
			main.bm1 = bm1;
			tn1 = rs.getString(9);
			main.tn1 = tn1;
			bm2 = rs.getString(10);
			main.bm2 = bm2;
			tn2 = rs.getString(11);
			main.tn2 = tn2;
			bm3 = rs.getString(12);
			main.bm3 = bm3;
			tn3 = rs.getString(13);
			main.tn3 = tn3;
			actualCost = rs.getString(14);
			main.actualCost = actualCost;
			approved = rs.getString(16);
			main.approved = approved;
			}
			System.out.println("Project name is: " + projectNm);
			fillForm();
			
			}else{
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialog");
				alert.setHeaderText("Work Order # does not exist");
				alert.setContentText("Try Again");
				alert.showAndWait();	
			}
			
			con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void fillForm(){
		main.secondWindow();
	}
	public void goBack(){
		main.logWindow();
	}
}
