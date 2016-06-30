package application;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.Optional;

import javax.imageio.ImageIO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.sql.Blob;
import java.sql.*;

public class FirstWindowController {
	protected Main main;
	protected Stage primaryStage;
	@FXML protected Label workOrderDate; //this is the date
	protected Date date = new Date();
	@FXML ComboBox<String> workDescription = new ComboBox();
	@FXML TextField projectName, clientName, PO, itemDescription, drawingNumber,
					traceNumber, BM1, TN1, BM2, TN2, BM3, TN3, cost, approvedBy;
	@FXML Label orderNumber;
	//---------
	String projectNm, clientNm, POnum, workDesc, itemDesc, drawingNum, traceNum,
			bm1, tn1, bm2, tn2, bm3, tn3, startDate, approved;
	String actualCost;
	Image imageProduct;
	int  orderNum, orderNum2;
	//---------
	private boolean ifSaved = false;
	private boolean isOld = false;
	@FXML ImageView finishedProduct;
	String url = "jdbc:mysql://localhost:3306/yourSchemaName"; //your database address
	String uname ="root";
	String pword = "yourpassword";
	
	BufferedImage bufferedImage;
	Image image;
	
	public void setMain(Main main, Stage primaryStage){
		this.main = main;
		this.primaryStage = primaryStage;
		workOrderDate.setText(date.toString());
		File initPhoto = new File("noImage.jpg");
		try{	//to initialize buffered image with noImage.jpg
		bufferedImage = ImageIO.read(initPhoto);
		}catch(Exception e){
			e.printStackTrace();
		}
		workDescription.getItems().addAll( //you can change the items below in accordance to your company's products
			    "Machining/Rolling/Forming-In-House",
			    "Machining/Rolling/Forming-Sub-Contract",
			    "Welding/Fabrication/Re-Furbishment-Sub-Contract",
			    "Equipment Installation",
			    "Equipment Installation(Sub-Contractor)",
			    "Sale of Scrap Iron, Plates, Chips",
			    "Authorized Rental"
			);
		try{ //Connect to db then retrieve the next blank receipt number.
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(url, uname, pword);
			System.out.println("Connected database successfully...");
			Statement stmnt = con.createStatement();
		
			ResultSet rs = stmnt.executeQuery("SELECT id FROM yourSchemaName.yourTableName");
	
			int i = 1;
			while(rs.next()){
				orderNum = rs.getInt(1);
				System.out.println(orderNum);
				i++;
			}
			main.receiptNum[0] = 1000 + i -1; //this is the official receipt number
			System.out.println("main.receipt[0] = " + main.receiptNum[0]);
			orderNumber.setText(Integer.toString(main.receiptNum[0]));
			main.orderNum = main.receiptNum[0].toString(); //main.orderNum needs a copy of order number for Process Window
			con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@FXML
	public void exit(){
	 if(projectName.getText().equals("") || clientName.getText().equals("")){
		 main.logWindow();
	 }else{
		 if(ifSaved==true){
				main.logWindow();
			}else{
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirm Action");
				alert.setHeaderText("Your update has NOT been saved, yet");
				alert.setContentText("Are you sure you want to Exit?");

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK){
					main.logWindow();
				} else {
				    alert.close();
				}
			}
	 }	
	}
	@FXML
	public void gotoProcess(){
		main.projectName[0] = projectName.getText().toString();
		main.process();
	}
	@FXML
	public void save(){
	  //work order cannot be saved if project name and client name is blank	
	  if(projectName.getText().equals("") || clientName.getText().equals("")){
		 main.logWindow();
	   }else{
		ifSaved = true; //to prompt if not yet saved when exit is clicked
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("File Saved");
		alert.setHeaderText(null);
		alert.setContentText("Updates were saved!");
		alert.showAndWait();
		orderNum2 = main.receiptNum[0];
		
		if(isOld==false){ //if it hasn't been saved before
			saveList();
		}else if(isOld==true){ //if save button has already been clicked at least once
			updateDB(); //updates DB without incrementing the receipt number
		}
	   }
	}
	public void updateDB(){
		startDate = workOrderDate.getText().toString();
		try{
		if(projectName != null){
		projectNm = projectName.getText().toString();
		}
		if(clientName != null){
		clientNm = clientName.getText().toString();
		}
		if(PO != null){
		POnum = PO.getText().toString();
		}
		if(itemDescription != null){
		itemDesc = itemDescription.getText().toString();
		}
		if(drawingNumber != null){
		drawingNum = drawingNumber.getText().toString();
		}
		if(traceNumber != null){
		traceNum = traceNumber.getText().toString();
		}
		if(BM1 != null){
		bm1 = BM1.getText().toString();
		}
		if(TN1 != null){
		tn1 = TN1.getText().toString();
		}
		if(BM2 != null){
		bm2 = BM2.getText().toString();
		}
		if(TN2 != null){
		tn2 = TN2.getText().toString();
		}
		if(BM3 != null){
		bm3 = BM3.getText().toString();
		}
		if(TN3 != null){
		tn3 = TN3.getText().toString();
		}
		if(cost != null){
		actualCost = cost.getText().toString();
		}
		if(approvedBy != null){
		approved = approvedBy.getText().toString();
		}
		if(workDescription.getValue().toString().isEmpty()){
				workDesc = null;
				}else{
					workDesc = workDescription.getValue().toString();
				}
		}catch(Exception e){
			e.printStackTrace();
		}

		try{	//Updates db with the most recent changes made by the user.
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(url, uname, pword);
			System.out.println("Connected database successfully...");
			String updateStatement ="UPDATE workorder SET projectName = '" + projectNm + "', clientName = '" + clientNm +
					"', PO = '" + POnum + "', workDescription = '" + workDesc + "', drawingNumber = '" + drawingNum + 
					"', itemDescription = '" + itemDesc + "', traceNumber = '" + traceNum + "', BM1 = '" + bm1 +
					"', TN1 = '" + tn1 + "', BM2 = '" + bm2 + "', TN2 = '" + tn2 + "', BM3 = '" + bm3 + 
					"', TN3 = '" + tn3 + "', actualCost = '" + actualCost + "', approved = '" + approved + 
					"' WHERE id = " + orderNum2;
			PreparedStatement stmnt = con.prepareStatement(updateStatement);
			
			stmnt.executeUpdate();
			
			
			//update image
			if(bufferedImage!=null){
				//prepare another statement for blob photo	
					String newQuery = "UPDATE workorder SET photo = ? WHERE id = " + main.receiptNum[0];
					PreparedStatement photoStatement = con.prepareStatement(newQuery);
					System.out.println("blob photo statement executed...");
				//Get the byte array of the bufferedImage:
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(bufferedImage, "jpg", baos );
					byte[] imageInByte = baos.toByteArray();
					System.out.println("byte array of the buffered image executed...");
				//turn byte array into input stream
		            InputStream photoStream = new ByteArrayInputStream(imageInByte);
		            System.out.println("byte array into input stream executed...");
		 		//update DB with the binary photoFile
		            photoStatement.setBinaryStream(1, photoStream);
		            photoStatement.executeUpdate();
		            System.out.println("binary photo file uploaded...");
					System.out.println("Inserted all records into the table...");
				}
			con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void saveList(){
		isOld = true;
		//get the textField values from user input
		startDate = workOrderDate.getText().toString();
		try{
		if(projectName != null){
		projectNm = projectName.getText().toString();
		}
		if(clientName != null){
		clientNm = clientName.getText().toString();
		}
		if(PO != null){
		POnum = PO.getText().toString();
		}
		if(itemDescription != null){
		itemDesc = itemDescription.getText().toString();
		}
		if(drawingNumber != null){
		drawingNum = drawingNumber.getText().toString();
		}
		if(traceNumber != null){
		traceNum = traceNumber.getText().toString();
		}
		if(BM1 != null){
		bm1 = BM1.getText().toString();
		}
		if(TN1 != null){
		tn1 = TN1.getText().toString();
		}
		if(BM2 != null){
		bm2 = BM2.getText().toString();
		}
		if(TN2 != null){
		tn2 = TN2.getText().toString();
		}
		if(BM3 != null){
		bm3 = BM3.getText().toString();
		}
		if(TN3 != null){
		tn3 = TN3.getText().toString();
		}
		if(cost != null){
		actualCost = cost.getText().toString();
		}
		if(approvedBy != null){
		approved = approvedBy.getText().toString();
		}
		if(workDescription.getValue().toString().isEmpty()){
				workDesc = null;
				}else{
					workDesc = workDescription.getValue().toString();
				}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("getText success");

		//connect to DB then insert the values except for the photo yet
		try{
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(url, uname, pword);
			System.out.println("Connected database successfully...");
			String query = "INSERT INTO workorder " +
					"(projectName, clientName, PO, workDescription, itemDescription, "
					+ "drawingNumber, traceNumber, BM1, TN1, BM2, TN2, BM3, TN3, actualCost, "
					+ "date, approved) " + "VALUES " + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement stmnt = con.prepareStatement(query);
			
			stmnt.setString(1, projectNm);
			stmnt.setString(2, clientNm);
			stmnt.setString(3, POnum);
			stmnt.setString(4, workDesc);
			stmnt.setString(5, itemDesc);
			stmnt.setString(6, drawingNum);
			stmnt.setString(7, traceNum);
			stmnt.setString(8, bm1);
			stmnt.setString(9, tn1);
			stmnt.setString(10, bm2);
			stmnt.setString(11, tn2);
			stmnt.setString(12, bm3);
			stmnt.setString(13, tn3);
			stmnt.setString(14, actualCost);
			stmnt.setString(15, startDate);
			stmnt.setString(16, approved);
			
			stmnt.executeUpdate();
		if(bufferedImage!=null){
		//prepare another statement for blob photo	
			String newQuery = "UPDATE workorder SET photo = ? WHERE id = " + main.receiptNum[0];
			PreparedStatement photoStatement = con.prepareStatement(newQuery);
			System.out.println("blob photo statement executed...");
		//Get the byte array of the bufferedImage:
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "jpg", baos );
			byte[] imageInByte = baos.toByteArray();
			System.out.println("byte array of the buffered image executed...");
		//turn byte array into input stream
            InputStream photoStream = new ByteArrayInputStream(imageInByte);
            System.out.println("byte array into input stream executed...");
 		//update DB with the binary photoFile
            photoStatement.setBinaryStream(1, photoStream);
            photoStatement.executeUpdate();
            System.out.println("binary photo file uploaded...");
			System.out.println("Inserted all records into the table...");
		}else{
			try{
				//convert noImage.jpg to buffered image for the SwingFXUtils
				BufferedImage noImageBuffered = ImageIO.read(getClass().getResource("noImage.jpg"));
				imageProduct = SwingFXUtils.toFXImage(noImageBuffered, null);
				finishedProduct.setImage(imageProduct);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
			con.close();
		}catch(Exception e){
			e.printStackTrace();;
		}
	}
	@FXML
	public void upload(){
		FileChooser fileChooser = new FileChooser();
		
		//Set extension filter
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
        
        //Show open file dialog
        File file = fileChooser.showOpenDialog(null);
        try {
        	//select image then upload to imageview
            bufferedImage = ImageIO.read(file);
            //convert bufferedImage to image for upload in GUI
            image = SwingFXUtils.toFXImage(bufferedImage, null);
            finishedProduct.setImage(image);
 
		} catch (IOException ex) {
            Logger.getLogger(FirstWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
     
	}
}
