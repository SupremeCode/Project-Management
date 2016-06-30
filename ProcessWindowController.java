package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Optional;



import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ProcessWindowController {
	protected Main main;
	protected Stage secondaryStage;
	@FXML Label projectName;
	final ToggleGroup group1 = new ToggleGroup();
	final ToggleGroup group2 = new ToggleGroup();
	final ToggleGroup group3 = new ToggleGroup();
	final ToggleGroup group4 = new ToggleGroup();
	final ToggleGroup group5 = new ToggleGroup();
	final ToggleGroup group6 = new ToggleGroup();
	final ToggleGroup group7 = new ToggleGroup();
	final ToggleGroup group8 = new ToggleGroup();
	@FXML RadioButton yes1 = new RadioButton();
	@FXML RadioButton no1 = new RadioButton();
	@FXML RadioButton yes2 = new RadioButton();
	@FXML RadioButton no2 = new RadioButton();
	@FXML RadioButton yes3 = new RadioButton();
	@FXML RadioButton no3 = new RadioButton();
	@FXML RadioButton yes4 = new RadioButton();
	@FXML RadioButton no4 = new RadioButton();
	@FXML RadioButton yes5 = new RadioButton();
	@FXML RadioButton no5 = new RadioButton();
	@FXML RadioButton yes6 = new RadioButton();
	@FXML RadioButton no6 = new RadioButton();
	@FXML RadioButton yes7 = new RadioButton();
	@FXML RadioButton no7 = new RadioButton();
	@FXML RadioButton yes8 = new RadioButton();
	@FXML RadioButton no8 = new RadioButton();
	private boolean ifSaved = false;
	@FXML Label receiptNumber;
	@FXML TextField d1,d2,d3,d4,d5,d6,d7,d8,w1,w2,w3,w4,w5,w6,w7,w8;
	String url = "jdbc:mysql://localhost:3306/yourSchemaName";
	String username = "root";
	String password = "yourDbPassword";
	private int cut, machine, setup, stamp, inspect, photos, ship, completion, idwo;
	private String pd1, pd2, pd3, pd4, pd5, pd6, pd7, pd8, pw1, pw2, pw3, pw4, pw5, pw6, pw7, pw8;
	private boolean hasPreviousRecord = false;
	
	public void setMain(Main main, Stage secondaryStage){
		this.main = main;
		this.secondaryStage = secondaryStage;
		if (main.projectName[0] == null){ //empty (i.e. method was ran) is different from null (i.e. method was NOT ran)
			projectName.setText(main.projectNm);//if  FirstWindowController was ran
		}else{
			projectName.setText(main.projectName[0]); //else, then SecondWindowController was ran
		}
		receiptNumber.setText(main.orderNum);
		System.out.println("main.orderNum = " + main.orderNum);
		idwo = Integer.parseInt(main.orderNum);
		yes1.setToggleGroup(group1);
		no1.setToggleGroup(group1);
		yes2.setToggleGroup(group2);
		no2.setToggleGroup(group2);
		yes3.setToggleGroup(group3);
		no3.setToggleGroup(group3);
		yes4.setToggleGroup(group4);
		no4.setToggleGroup(group4);
		yes5.setToggleGroup(group5);
		no5.setToggleGroup(group5);
		yes6.setToggleGroup(group6);
		no6.setToggleGroup(group6);
		yes7.setToggleGroup(group7);
		no7.setToggleGroup(group7);
		yes8.setToggleGroup(group8);
		no8.setToggleGroup(group8);
		checkIfSavedBefore();
	}
	public void checkIfSavedBefore(){
		//this method will run if there is already a record of process in mysql; otherwise, it will just display all no
		try{
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(url, username, password);
			String query = "SELECT * FROM process WHERE idwo = " + idwo;
			Statement firstStatement = con.createStatement();
			ResultSet rs = firstStatement.executeQuery(query);
			System.out.println("rs.next() is equal to = " + rs.next());

				//populate the radiobuttons from mySQL record with respect to idwo
				Statement stmnt1 = con.createStatement();
				String query1 = "SELECT cut, machine, setup, stamp, inspect, photos, ship, completion, d1, w1,"
						+ "d2, w2, d3, w3, d4, w4, d5, w5, d6, w6, d7, w7, d8, w8" + 
						" FROM process WHERE idwo = "	+ idwo;
				ResultSet rs1 = stmnt1.executeQuery(query1);
				
				while(rs1.next()){
					hasPreviousRecord = true;
					cut = rs1.getInt(1);
					machine = rs1.getInt(2);
					setup = rs1.getInt(3);
					stamp = rs1.getInt(4);
					inspect = rs1.getInt(5);
					photos = rs1.getInt(6);
					ship = rs1.getInt(7);
					completion = rs1.getInt(8);
					pd1 = rs1.getString(9);
					pw1 = rs1.getString(10);
					pd2 = rs1.getString(11);
					pw2 = rs1.getString(12);
					pd3 = rs1.getString(13);
					pw3 = rs1.getString(14);
					pd4 = rs1.getString(15);
					pw4 = rs1.getString(16);
					pd5 = rs1.getString(17);
					pw5 = rs1.getString(18);
					pd6 = rs1.getString(19);
					pw6 = rs1.getString(20);
					pd7 = rs1.getString(21);
					pw7 = rs1.getString(22);
					pd8 = rs1.getString(23);
					pw8 = rs1.getString(24);
				}
				if(cut == 1){
					yes1.setSelected(true);
				}else{
					no1.setSelected(true);
				}
				if(machine == 1){
					yes2.setSelected(true);
				}else{
					no2.setSelected(true);
				}
				if(setup == 1){
					yes3.setSelected(true);
				}else{
					no3.setSelected(true);
				}
				if(stamp == 1){
					yes4.setSelected(true);
				}else{
					no4.setSelected(true);
				}
				if(inspect == 1){
					yes5.setSelected(true);
				}else{
					no5.setSelected(true);
				}
				if(photos == 1){
					yes6.setSelected(true);
				}else{
					no6.setSelected(true);
				}
				if(ship == 1){
					yes7.setSelected(true);
				}else{
					no7.setSelected(true);
				}
				if(completion == 1){
					yes8.setSelected(true);
				}else{
					no8.setSelected(true);
				}
				if(pd1.equals(null)){
					d1.setText("");
				}else{
				d1.setText(pd1);
				}
				if(pw1.equals(null)){	//if null, set blank on GUI
					w1.setText("");
				}else{
					w1.setText(pw1);
				}
				if(pd2.equals(null)){
					d2.setText("");
				}else{
					d2.setText(pd2);
				}
				if(pw2.equals(null)){
					w2.setText("");
				}else{
					w2.setText(pw2);
				}
				if(pd3.equals(null)){
					d3.setText("");
				}else{
					d3.setText(pd3);
				}
				if(pw3.equals(null)){
					w3.setText("");
				}else{
					w3.setText(pw3);
				}
				if(pd4.equals(null)){
					d4.setText("");
				}else{
					d4.setText(pd4);
				}
				if(pw4.equals(null)){
					w4.setText("");
				}else{
					w4.setText(pw4);
				}
				if(pd5.equals(null)){
					d5.setText("");
				}else{
					d5.setText(pd5);
				}
				if(pw5.equals(null)){
					w5.setText("");
				}else{
					w5.setText(pw5);
				}if(pd6.equals(null)){
					d6.setText("");
				}else{
					d6.setText(pd6);
				}
				if(pw6.equals(null)){
					w6.setText("");
				}else{
					w6.setText(pw6);
				}
				if(pd7.equals(null)){
					d7.setText("");
				}else{
					d7.setText(pd7);
				}
				if(pw7.equals(null)){
					w7.setText("");
				}else{
					w7.setText(pw7);
				}
				if(pd8.equals(null)){
					d8.setText("");
				}else{
					d8.setText(pd8);
				}
				if(pw8.equals(null)){
					w8.setText("");
				}else{
					w8.setText(pw8);
				}
			con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		if(hasPreviousRecord == false){
			System.out.println("has previous record = " + hasPreviousRecord);
			checkSelected();
		}
	}		
	public void checkSelected(){	//set user input to the radio buttons.
		if(yes1.isSelected()){
			cut = 1;
		}else{
			cut = 0;
		}
		if(yes2.isSelected()){
			machine = 1;
		}else{
			machine = 0;
		}
		if(yes3.isSelected()){
			setup = 1;
		}else{
			setup = 0;
		}
		if(yes4.isSelected()){
			stamp = 1;
		}else{
			stamp = 0;
		}
		if(yes5.isSelected()){
			inspect = 1;
		}else{
			inspect = 0;
		}
		if(yes6.isSelected()){
			photos = 1;
		}else{
			photos = 0;
		}
		if(yes7.isSelected()){
			ship = 1;
		}else{
			ship = 0;
		}
		if(yes8.isSelected()){
			completion = 1;
		}else{
			completion = 0;
		}
		pd1 = d1.getText().toString();
		pw1 = w1.getText().toString();
		pd2 = d2.getText().toString();
		pw2 = w2.getText().toString();
		pd3 = d3.getText().toString();
		pw3 = w3.getText().toString();
		pd4 = d4.getText().toString();
		pw4 = w4.getText().toString();
		pd5 = d5.getText().toString();
		pw5 = w5.getText().toString();
		pd6 = d6.getText().toString();
		pw6 = w6.getText().toString();
		pd7 = d7.getText().toString();
		pw7 = w7.getText().toString();
		pd8 = d8.getText().toString();
		pw8 = w8.getText().toString();
	}
	@FXML
	public void close(){
		secondaryStage.close();
	}
	@FXML
	public void save(){
		ifSaved = true;
		//Connect to DB
		//save data to process Table CHECK IF idwo IS NOT EXISTING YET BEFORE THE INSERT
		checkSelected();
		try{
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(url, username, password);
			System.out.println("Connected database successfully...");
			
			String query1 = "SELECT idwo FROM process WHERE idwo = " + idwo;
			Statement statementSave1 = con.createStatement();
			ResultSet rsSave1 = statementSave1.executeQuery(query1);
			
			if(rsSave1.next()){
				String query2 = "UPDATE process SET cut = '" + cut + "', machine = '" + machine +
						"', setup = '" + setup + "', stamp = '" + stamp + "', inspect = '" + inspect +
						"', photos = '" + photos + "', ship = '" + ship + "', completion = '" +
						completion + "', d1 = '" + pd1 + "', w1 = '" + pw1 + "', d2 = '" + pd2 + 
						"', w2 = '" + pw2 + "', d3 = '" + pd3 + "', w3 = '" + pw3 +
						"', d4 = '" + pd4 + "', w4 = '" + pw4 + "', d5 = '" + pd5 +
						"', w5 = '" + pw5 + "', d6 = '" + pd6 + "', w6 = '" + pw6 +
						"', d7 = '" + pd7 + "', w7 = '" + pw7 + "', d8 = '" + pd8 + 
						"' WHERE idwo = " + idwo;
				System.out.println("After String query2");
				PreparedStatement stmnt2 = con.prepareStatement(query2);
				System.out.println("After PreparedStatement stmnt2");
				stmnt2.executeUpdate();
				System.out.println("After stmnt2.executeUpdate");
			}else{
				String insertQuery = "INSERT INTO process (cut, machine, setup, stamp, inspect, photos, ship, " +
						"completion, idwo, d1, w1, d2 ,w2, d3, w3, d4, w4, d5, w5, d6, w6, d7, w7, d8, w8)" +
						" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement insertStatement = con.prepareStatement(insertQuery);
				insertStatement.setInt(1, cut);
				insertStatement.setInt(2, machine);
				insertStatement.setInt(3, setup);
				insertStatement.setInt(4, stamp);
				insertStatement.setInt(5, inspect);
				insertStatement.setInt(6, photos);
				insertStatement.setInt(7, ship);
				insertStatement.setInt(8, completion);
				insertStatement.setInt(9, idwo);
				insertStatement.setString(10, pd1);
				insertStatement.setString(11, pw1);
				insertStatement.setString(12, pd2);
				insertStatement.setString(13, pw2);
				insertStatement.setString(14, pd3);
				insertStatement.setString(15, pw3);
				insertStatement.setString(16, pd4);
				insertStatement.setString(17, pw4);
				insertStatement.setString(18, pd5);
				insertStatement.setString(19, pw5);
				insertStatement.setString(20, pd6);
				insertStatement.setString(21, pw6);
				insertStatement.setString(22, pd7);
				insertStatement.setString(23, pw7);
				insertStatement.setString(24, pd8);
				insertStatement.setString(25, pw8);
				insertStatement.executeUpdate();
			}
			con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		Alert alert = new Alert(AlertType.INFORMATION);	//Alert pop up window
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Updates were Saved!");

		alert.showAndWait();
	}
}
