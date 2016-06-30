package application;
	
import java.util.ArrayList;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

public class Main extends Application {
	
	private Stage primaryStage;
	String[] projectName = new String[1];
	Integer[] receiptNum = new Integer[1]; //This integer variable is for the receipt number counter on FirstWindowController aside from its string counterpart.
	String projectNm, clientNm, POnum, workDesc, itemDesc, drawingNum, traceNum,
	bm1, tn1, bm2, tn2, bm3, tn3, startDate, approved;
	String actualCost, orderNum;
	Image imageProduct;
	
	public static void main(String[] args) {
        launch(args);
    }
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		logWindow();
	}
	public void logWindow(){
		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("LogWindowView.fxml"));
			AnchorPane pane = loader.load();
			Scene scene = new Scene(pane);
			
			LogWindowController logWindowController = loader.getController();
			logWindowController.setMain(this,primaryStage);
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			primaryStage.setTitle("Your Company Work Order Title Here");
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void firstWindow(){
		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("FirstWindowView.fxml"));
			AnchorPane pane = loader.load();
			Scene scene = new Scene(pane);
			
			FirstWindowController firstWindowController = loader.getController();
			firstWindowController.setMain(this,primaryStage);
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			primaryStage.setTitle("Your Company Work Order Title Here");
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void search(){
		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("SearchWindowView.fxml"));
			AnchorPane pane = loader.load();
			Scene scene = new Scene(pane);
			
			SearchWindowController searchWindowController = loader.getController();
			searchWindowController.setMain(this,primaryStage);
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			primaryStage.setTitle("Your Company Work Order Title Here");
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void process(){
		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("ProcessWindowView.fxml"));
			AnchorPane pane = loader.load();
			Scene scene = new Scene(pane);
			Stage secondaryStage = new Stage();
			
			ProcessWindowController processWindowController = loader.getController();
			processWindowController.setMain(this,secondaryStage);
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			secondaryStage.setTitle("9. Process");
			secondaryStage.setScene(scene);
			secondaryStage.setResizable(false);
			secondaryStage.initModality(Modality.APPLICATION_MODAL);
			secondaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void secondWindow(){ //Form for editing existing Work Order
		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("SecondWindowView.fxml"));
			AnchorPane pane = loader.load();
			Scene scene = new Scene(pane);
			
			SecondWindowController secondWindowController = loader.getController();
			secondWindowController.setMain(this,primaryStage);
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			primaryStage.setTitle("Your Business Title Here");
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
