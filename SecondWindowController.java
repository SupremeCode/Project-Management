package application;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
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
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

import java.sql.*;

public class SecondWindowController {
	protected Main main;
	protected Stage primaryStage;
	@FXML protected Label workOrder; //this is the date
	protected Date date = new Date();
	@FXML ComboBox<String> workDescription = new ComboBox();
	@FXML TextField projectName, clientName, PO, itemDescription, drawingNumber,
					traceNumber, BM1, TN1, BM2, TN2, BM3, TN3, cost, approvedBy;
	@FXML Label orderNumber;
	String projectNm, clientNm, POnum, workDesc, itemDesc, drawingNum, traceNum,
			bm1, tn1, bm2, tn2, bm3, tn3, dateNow, approved;
	String actualCost;
	private boolean ifSaved = false;
	@FXML ImageView finishedProduct;
	String url = "jdbc:mysql://localhost:3306/yourSchemaName"; 
	String uname ="root";
	String pword = "yourDbPassword"; 
	BufferedImage bufferedImage;
	InputStream photoStream;
	WritableImage writableImage;
	Image imageProduct;
	Image image;
	
	public void setMain(Main main, Stage primaryStage){
		this.main = main;
		this.primaryStage = primaryStage; //fill up form from recorded data on DB:
		workOrder.setText(date.toString());
		orderNumber.setText(main.orderNum);
		if (bufferedImage == null){
			try{
			bufferedImage = ImageIO.read(getClass().getResource("noImage.jpg"));
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		workDescription.getItems().addAll(	//Replace the items below with your company products
			    "Machining/Rolling/Forming-In-House",	//This is the drop down list on FXML
			    "Machining/Rolling/Forming-Sub-Contract",
			    "Welding/Fabrication/Re-Furbishment-Sub-Contract",
			    "Equipment Installation",
			    "Equipment Installation(Sub-Contractor)",
			    "Sale of Scrap Iron, Plates, Chips",
			    "Authorized Rental"
			);
		workDescription.setValue(main.workDesc);
		
		projectName.setText(main.projectNm);
		clientName.setText(main.clientNm);
		PO.setText(main.POnum);
		itemDescription.setText(main.itemDesc);
		drawingNumber.setText(main.drawingNum);
		traceNumber.setText(main.traceNum);
		BM1.setText(main.bm1);
		TN1.setText(main.tn1);
		BM2.setText(main.bm2);
		TN2.setText(main.tn2);
		BM3.setText(main.bm3);
		TN3.setText(main.tn3);
		cost.setText(main.actualCost);
		approvedBy.setText(main.approved);
		displayImage();
	}
	public void displayImage(){
		// read BLOB and display to imageView finishedProduct
		try{
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(url, uname, pword);
			System.out.println("Connected database successfully...");
			Statement stmnt = con.createStatement();
			String query = "SELECT photo FROM workorder WHERE id = ";
			ResultSet rs = stmnt.executeQuery(query + main.orderNum);
			System.out.println("photo has been selected...");
			
			while(rs.next()){ //this rs.next() is VERY IMPORTANT
				//if photo cell is null
				if (rs.getBinaryStream(1)!=null){
				photoStream = rs.getBinaryStream(1);
				bufferedImage = javax.imageio.ImageIO.read(photoStream);
			
					if (bufferedImage != null){	
						imageProduct = SwingFXUtils.toFXImage(bufferedImage, null);
						finishedProduct.setImage(imageProduct);
					}
				}else{//if MySQL photo is null, display noImage.jpg in ImageView
					displayNoImage();
				}
			}
			con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void displayNoImage(){
		try{
			//convert noImage.jpg to buffered image for the SwingFXUtils
			BufferedImage noImageBuffered = ImageIO.read(getClass().getResource("noImage.jpg"));
			imageProduct = SwingFXUtils.toFXImage(noImageBuffered, null);
			finishedProduct.setImage(imageProduct);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@FXML
	public void exit(){
		//if project name, client name, are null, then main.logWindow(); no need to save
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
		ifSaved = true;
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("File Saved");
		alert.setHeaderText(null);
		alert.setContentText("Updates were saved!");
		alert.showAndWait();
		updateDB(); //updates DB without incrementing the receipt number
		
	}
	public void updateDB(){
		//get the textField values from user input
		try{ //these try catch and if are very important in saving the updates. Same applies in First Window
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
		//connect to DB then insert the values except for the photo yet
		try{
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(url, uname, pword);
			System.out.println("Connected database successfully...");
			String update1 = "UPDATE workorder SET projectName = '" + projectNm + "', clientName = '" + clientNm +
					"', PO = '" + POnum + "', workDescription = '" + workDesc + "', drawingNumber = '" + drawingNum + 
					"', itemDescription = '" + itemDesc + "', traceNumber = '" + traceNum + "', BM1 = '" + bm1 +
					"', TN1 = '" + tn1 + "', BM2 = '" + bm2 + "', TN2 = '" + tn2 + "', BM3 = '" + bm3 + 
					"', TN3 = '" + tn3 + "', actualCost = '" + actualCost + "', approved = '" + approved + 
					"' WHERE id = " + main.orderNum;
			PreparedStatement stmnt = con.prepareStatement(update1);
			stmnt.executeUpdate();
			
			System.out.println(main.orderNum + " Saved secondWindow successfully!");
			//update image
			try{//NOTE: bufferedImage is currently pointing to MySQL photo. bufferedImage below is replaced
				//by the uploaded image, so don't put if(bufferedImage!=null)
				//prepare another statement for blob photo	
					String newQuery = "UPDATE workorder SET photo = ? WHERE id = " + main.orderNum;
					PreparedStatement photoStatement = con.prepareStatement(newQuery);
					System.out.println("blob photo statement executed...");
				//assign uploaded image to bufferedImage	
					bufferedImage = SwingFXUtils.fromFXImage(image, null);
				//Remove the pinkish tone:
					BufferedImage imageRGB = 
							  new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.OPAQUE);
							Graphics2D graphics = imageRGB.createGraphics();
							graphics.drawImage(bufferedImage, 0, 0, null);
				//Get the byte array of the bufferedImage:
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(imageRGB, "jpg", baos );
					byte[] imageInByte = baos.toByteArray();
					System.out.println("byte array of the buffered image executed...");
				//turn byte array into input stream
		            InputStream photoStream = new ByteArrayInputStream(imageInByte);
		            System.out.println("byte array into input stream executed...");
		 		//update DB with the binary photoFile
		            photoStatement.setBinaryStream(1, photoStream);
		            photoStatement.executeUpdate();
		            System.out.println("binary photo file uploaded...");
		      //      graphics.dispose(); //you only need to dispose of Graphics when you actually create it yourself.
					//this code works even without graphics.dispose();
				
				}catch(Exception e){
						e.printStackTrace();
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
            BufferedImage bufferedImage = ImageIO.read(file);
            image = SwingFXUtils.toFXImage(bufferedImage, null);
            finishedProduct.setImage(image);
        } catch (IOException ex) {
            Logger.getLogger(FirstWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
}
