package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Optional;
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
import javafx.stage.Stage;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.PasswordAuthentication;
import java.io.BufferedWriter;

public class LogWindowController {
	protected Main main;
	protected Stage primaryStage;
	protected Date dateNow = new Date();
	final String username = "yourEmail@domainName.com";
	final String password = "yourEmailPassword";
	@FXML Label date;
	final String TO = "yourEmail@domainName.com";
	String host = "smtp.gmail.com";
    String port = "587";
    String message = "BMW sent you a copy of Work Order lists";
    String subject ="Work Order Summary Report";
    final String url = "jdbc:mysql://localhost:3306/yourSchemaName"; 
    final String dbUsername = "root";  
    final String dbPassword = "yourDbPassword";
    String projectName, clientName, PO, workDescription, itemDescription, drawingNumber, traceNumber, BM1,
    		TN1, BM2, TN2, BM3, TN3, actualCost, photo, startDate, approved;
    int idwo;
    ArrayList<Integer> idwoList = new ArrayList<Integer>();
    ArrayList<String> projectNameList = new ArrayList<String>();
    ArrayList<String> clientNameList = new ArrayList<String>();
    ArrayList<String> POList = new ArrayList<String>();
    ArrayList<String> workDescriptionList = new ArrayList<String>();
    ArrayList<String> itemDescriptionList = new ArrayList<String>();
    ArrayList<String> drawingNumberList = new ArrayList<String>();
    ArrayList<String> traceNumberList = new ArrayList<String>();
    ArrayList<String> BM1List = new ArrayList<String>();
    ArrayList<String> TN1List = new ArrayList<String>();
    ArrayList<String> BM2List = new ArrayList<String>();
    ArrayList<String> TN2List = new ArrayList<String>();
    ArrayList<String> BM3List = new ArrayList<String>();
    ArrayList<String> TN3List = new ArrayList<String>();
    ArrayList<String> actualCostList = new ArrayList<String>();
    ArrayList<String> startDateList = new ArrayList<String>();
    ArrayList<String> approvedList = new ArrayList<String>();
    
    ArrayList<Integer> cut = new ArrayList<Integer>();
    ArrayList<Integer> machine = new ArrayList<Integer>();
    ArrayList<Integer> setup = new ArrayList<Integer>();
    ArrayList<Integer> stamp = new ArrayList<Integer>();
    ArrayList<Integer> inspect = new ArrayList<Integer>();
    ArrayList<Integer> photos = new ArrayList<Integer>();
    ArrayList<Integer> ship = new ArrayList<Integer>();
    ArrayList<Integer> completion = new ArrayList<Integer>();
    ArrayList<Integer> pIdwo = new ArrayList<Integer>();
    
    ArrayList<String> d1 = new ArrayList<String>();
    ArrayList<String> w1 = new ArrayList<String>();
    ArrayList<String> d2 = new ArrayList<String>();
    ArrayList<String> w2 = new ArrayList<String>();
    ArrayList<String> d3 = new ArrayList<String>();
    ArrayList<String> w3 = new ArrayList<String>();
    ArrayList<String> d4 = new ArrayList<String>();
    ArrayList<String> w4 = new ArrayList<String>();
    ArrayList<String> d5 = new ArrayList<String>();
    ArrayList<String> w5 = new ArrayList<String>();
    ArrayList<String> d6 = new ArrayList<String>();
    ArrayList<String> w6 = new ArrayList<String>();
    ArrayList<String> d7 = new ArrayList<String>();
    ArrayList<String> w7 = new ArrayList<String>();
    ArrayList<String> d8 = new ArrayList<String>();
    ArrayList<String> w8 = new ArrayList<String>();
    
	public void setMain(Main main, Stage primaryStage){
		this.main = main;
		this.primaryStage = primaryStage;
		date.setText(dateNow.toString());
	}
	@FXML
	public void newOrder(){
		main.firstWindow();
	}
	@FXML
	public void searchOrder(){
		main.search();
	}
	@FXML
	public void sendReport(){
		selectWO();
		woToExcel();
		selectProcess();
		processToExcel();
		sendEmail();
	}
	public void processToExcel(){
		try{
			File file = new File("C:/Users/yourPcName/Desktop/process.csv"); //Location to save process.csv
			Writer writer = new BufferedWriter(new FileWriter(file));
			for(int i=0; i<pIdwo.size(); i++){
				String text = cut.get(i)+","+machine.get(i)+","+ setup.get(i)+ ","+ stamp.get(i)+
						inspect.get(i)+","+photos.get(i)+","+ ship.get(i)+ ","+ completion.get(i)+","+ 
						pIdwo.get(i)+","+ "\"" +d1.get(i)+ "\"" +","+ w1.get(i)+ ","+  "\"" +d2.get(i)+ "\""+ ","+ w2.get(i)+ ","+ "\"" +d3.get(i)+ "\""+ 
						"," + w3.get(i)+","+ "\"" +d4.get(i)+ "\"" +","+w4.get(i)+","+ "\"" +d5.get(i)+ "\""+ ","+ w5.get(i)+ ","+ "\"" + d6.get(i)+ "\""+ 
						","+ w6.get(i)+ ","+ "\"" + d7.get(i)+ "\"" +","+ w7.get(i)+","+"\"" +d8.get(i)+ "\""+","+ w8.get(i)+
						"\n";
				writer.write(text);
			}
			writer.flush();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void selectProcess(){
		//connect to db and select all processList: 
		try{
			Connection con = DriverManager.getConnection(url, dbUsername, dbPassword);
			Statement pStmnt = con.createStatement();
			ResultSet pRs = pStmnt.executeQuery("SELECT * FROM process");
			while(pRs.next()){
				cut.add(pRs.getInt(2));
				machine.add(pRs.getInt(3));
				setup.add(pRs.getInt(4));
				stamp.add(pRs.getInt(5));
				inspect.add(pRs.getInt(6));
				photos.add(pRs.getInt(7));
				ship.add(pRs.getInt(8));
				completion.add(pRs.getInt(9));
				pIdwo.add(pRs.getInt(10));
				d1.add(pRs.getString(11));
				w1.add(pRs.getString(12));
				d2.add(pRs.getString(13));
				w2.add(pRs.getString(14));
				d3.add(pRs.getString(15));
				w3.add(pRs.getString(16));
				d4.add(pRs.getString(17));
				w4.add(pRs.getString(18));
				d5.add(pRs.getString(19));
				w5.add(pRs.getString(20));
				d6.add(pRs.getString(21));
				w6.add(pRs.getString(22));
				d7.add(pRs.getString(23));
				w7.add(pRs.getString(24));
				d8.add(pRs.getString(25));
				w8.add(pRs.getString(26));
			}
			con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void woToExcel(){
		try{
			File file = new File("C:/Users/yourPcName/Desktop/wo.csv"); //Location to save wo.csv
			Writer writer = new BufferedWriter(new FileWriter(file));
			for(int i=0; i<idwoList.size(); i++){
				String text = idwoList.get(i)+","+projectNameList.get(i)+"," + "\"" + clientNameList.get(i) + "\"" + ","+
						POList.get(i)+","+ "\"" +workDescriptionList.get(i)+ "\"" + ","+ itemDescriptionList.get(i)+"," +
						drawingNumberList.get(i) +","+ traceNumberList.get(i) +","+ 
						"\"" + BM1List.get(i) + "\"" +","+ TN1List.get(i)+","+"\"" + BM2List.get(i) + "\"" +","+ TN2List.get(i)+","+
						"\"" + BM3List.get(i) + "\"" +","+ TN3List.get(i)+","+ "\"" + actualCostList.get(i) + "\"" +","+ startDateList.get(i)+","+
						approvedList.get(i) + "\n";
				writer.write(text);
			}
			writer.flush();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void selectWO(){
		//connect to db and select all workOrderList:
		try{
			Connection con = DriverManager.getConnection(url, dbUsername, dbPassword);
			Statement woStmnt = con.createStatement();
			ResultSet woRs = woStmnt.executeQuery("SELECT * FROM workorder");
			while(woRs.next()){
				idwo = woRs.getInt(1);
				projectName = woRs.getString(2);
				clientName = woRs.getString(3);
				PO = woRs.getString(4);
				workDescription = woRs.getString(5);
				itemDescription = woRs.getString(6);
				drawingNumber = woRs.getString(7);
				traceNumber = woRs.getString(8);
				BM1 = woRs.getString(9); TN1 = woRs.getString(10);
				BM2 = woRs.getString(11); TN2 = woRs.getString(12);
				BM3 = woRs.getString(13); TN3 = woRs.getString(14);
				actualCost = woRs.getString(15);
				startDate = woRs.getString(17);
				approved = woRs.getString(18);
				idwoList.add(idwo);
				workDescriptionList.add(workDescription);
				projectNameList.add(projectName);
				clientNameList.add(clientName);
				POList.add(PO);
				itemDescriptionList.add(itemDescription);
				drawingNumberList.add(drawingNumber);
				traceNumberList.add(traceNumber);
				BM1List.add(BM1); TN1List.add(TN1); 
				BM2List.add(BM2); TN2List.add(TN2);
				BM3List.add(BM3); TN3List.add(TN3);
				actualCostList.add(actualCost);
				startDateList.add(startDate);
				approvedList.add(approved);
			}
			con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void sendEmail(){
		
		try{	
			 // sets SMTP server properties
	        Properties properties = new Properties();
	        properties.put("mail.smtp.host", host);
	        properties.put("mail.smtp.port", port);
	        properties.put("mail.smtp.auth", "true");
	        properties.put("mail.smtp.starttls.enable", "true");
	        properties.put("mail.user", username);
	        properties.put("mail.password", password);
	 
	        Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Report Transferring");
			alert.setHeaderText(null);
			alert.setContentText("Sending the Report... This might take one minute.");
			alert.show(); //NOT .showAndWait() so it will close later without waiting for user's click on OK button.	        
	        
	        // creates a new session with an authenticator
	        Authenticator auth = new Authenticator() {
	            public PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(username, password);
	            }
	        };
	        Session session = Session.getInstance(properties, auth);
	 
	        // creates a new e-mail message
	        Message msg = new MimeMessage(session);
	 
	        msg.setFrom(new InternetAddress(username));
	        InternetAddress[] toAddresses = { new InternetAddress(TO) };
	        msg.setRecipients(Message.RecipientType.TO, toAddresses);
	        msg.setSubject(subject);
	        msg.setSentDate(new Date());
	 
	        // creates message part
	        MimeBodyPart messageBodyPart = new MimeBodyPart();
	        messageBodyPart.setContent(message, "text/html");
	 
	        // creates multiPart
	        Multipart multipart = new MimeMultipart();
	        multipart.addBodyPart(messageBodyPart);
	         
	    	 String[] attachFiles = new String[2];
	    	 attachFiles[0] = "C:/Users/yourPcName/Desktop/wo.csv"); //Attach wo.csv
	         attachFiles[1] = "C:/Users/yourPcName/Desktop/process.csv"); //Attach process.csv
	         // adds attachments
	         if (attachFiles != null && attachFiles.length > 0) {
	             for (String filePath : attachFiles) {
	                 MimeBodyPart attachPart = new MimeBodyPart();
	  
	                 try {
	                     attachPart.attachFile(filePath);
	                 } catch (IOException ex) {
	                     ex.printStackTrace();
	                 }
	  
	                 multipart.addBodyPart(attachPart);
	             }
	         }
	        // sets the multiPart as e-mail's content
	        msg.setContent(multipart);
	 
	        // sends the e-mail
	        Transport.send(msg);
	    	alert.close();
	        alertWindow();
		}catch(Exception e){
			e.printStackTrace();
		}

	}	

	public void alertWindow(){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText("Summary of Report was sent");
		alert.setContentText("Thank you");
		alert.showAndWait();
	}
	@FXML public void printDoc(){
		//save data to pdf
	}
	@FXML
	public void exit(){
		primaryStage.close();
	}
}
