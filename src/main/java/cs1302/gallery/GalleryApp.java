package cs1302.gallery;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.scene.shape.Circle;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;

import java.net.*;
import java.io.*;

import com.google.gson.*;

import java.util.Arrays;
import java.util.stream.Stream;

/** this class is a display App for itunes search API. It allows the user to enter input into a text field.
 * Then the app takes this input, loads, and displays images related. The Images are rotated through with other 
 * results pulled from the search. It allows the user to pause and play the random refreshment of images.
 *
 * @author Michael Runyan
*/
public class GalleryApp extends Application {

    String searchTerm = "Chainsmokers";
    private Stage stage;
    private GridPane pane = new GridPane();
    private int index = 21;
    private int numImg =0;
    private ImageView[] ivArray;
    private int[][] record = new int[4][5];
    ProgressBar pBar  = new ProgressBar();
    private ImageView[] temp;

    // Handler to be run with the timeline. Randomly replaces the images with ones that aren't currently being displayed
    EventHandler<ActionEvent> handler = event -> {int row=0;int col=0;
						      row = getRandomNum(3);col = getRandomNum(4);
						      int abort = 0;
						  while(inArray(getIndex(false)))
							getIndex(true);
						  //System.out.println("row:" + row + "                       col: " +col);
						  //displayArray();
						  pane.getChildren().remove(getIvArray(getRecord(row,col)));
						  pane.setConstraints(getIvArray(getIndex(false)),col,row+3,1,1);
						      
						      //if (NotInArray)
						  pane.getChildren().add(ivArray[getIndex(false)]);
						  setRecord(row,col,getIndex(false));
						  //displayArray();
						  getIndex(true);
						  stage.show();
    };//end of lamda

    private KeyFrame keyFrame = new KeyFrame(Duration.seconds(2), handler);
    private Timeline timeline = new Timeline();
   
    
    /**
	 * The start method for the javaFX GUI. This method sets the scene and stages, and starts to retreive the intial images
	 * 
	 * 
	 * @param Stage stage the stage in which contains the other elements
	 */
    @Override
    public void start(Stage stage) {
	this.stage = stage;
       
        Scene scene = new Scene(pane);
	stage.setMaxWidth(640);
	stage.setMaxHeight(510);
        stage.setTitle("Gallery!");
	stage.sizeToScene();
    	pane.setPrefSize(500, 480 );   

	pBar.setProgress(0.0F);
	ToolBar toolBar = addToolBar();
	MenuBar menuBar = addMenuBar();

	pane.setConstraints(pBar,0,7,5,1);
	pane.setConstraints(toolBar, 0, 2, 5,1);
	pane.setConstraints(menuBar, 0, 1, 5, 1);
	pane.getChildren().addAll(menuBar,toolBar,pBar);
     
	stage.setScene(scene);
	//Intial stage is now set
	stage.show();

	// this thread is run so the progress bar can be updated as the images are gathered to be shown
	Runnable r = () -> {
		    temp = retrieveImages();
		    Platform.runLater(() -> {showImages(); // Added so this can be done on the javaFX thread so it can interact with the stage
			pane.setConstraints(pBar,0,7,5,1);
			stage.sizeToScene();
			});//run later
	};// Runnable
		Thread t = new Thread(r);
		t.setDaemon(true);
		t.start();
    
    } // start
    /**
	 * Main method. Automatically launches. Launch the javaFX
	 *   
	 */
    public static void main(String[] args) {
	try {
			//Launches the javaFX GUI
		    Application.launch(args);
		} catch (UnsupportedOperationException e) {
		    System.out.println(e);
		    System.err.println("If this is a DISPLAY problem, then your X server connection");
		    System.err.println("has likely timed out. This can generally be fixed by logging");
		    System.err.println("out and logging back in.");
		    System.exit(1);
		} // try
	
    } // main
    /**
	 * Getter for stage object
	 * 
	 * @return  stage the stage of the GUI    
	 */
    public Stage getStage() {
	return stage;
    } // getStage
    /**
   	 * Creates a Menu bar and sets the buttons, lables and constraints
   	 * 
   	 * @return MenuBar menuBar a set up menu bar    
   	 */
    private MenuBar addMenuBar() {
    	MenuBar menuBar = new MenuBar();
    	final Menu menu1 = new Menu("File"); 
    	
    	
    	
    	MenuItem exitSelec = new MenuItem("Exit");
    	exitSelec.setOnAction(e -> System.exit(0));
    	
    	final Menu help = new Menu("Help");
    	MenuItem about = new MenuItem("About");
    	about.setOnAction(new EventHandler<ActionEvent>() {
    		 
	    		public void handle(ActionEvent event) {
	    			
	    			File imageFile = new File("/home/ugrads/runyan/P3/cs1302-gallery/src/main/java/cs1302/gallery/picme.png");
	    			String fileLocation = imageFile.toURI().toString();
	    			Image fxImage = new Image(fileLocation);   
	    			ImageView picOfMe = new ImageView(fxImage);
	    			
	    			Label secondLabel = new Label("Michael Runyan\n"
	            		+ "Msr88812@uga.edu\n"
	            		+ "Version: .45");
	
	            GridPane secondaryLayout = new GridPane();
	            secondaryLayout.getChildren().add(picOfMe);
	            secondaryLayout.getChildren().add(secondLabel);
	
	            secondaryLayout.setConstraints(picOfMe,0,0,1,1);
	            secondaryLayout.setConstraints(secondLabel,0,2,1,1);
	            
	            Scene secondScene = new Scene(secondaryLayout, 200, 285);
	
	            // New window (Stage)
	            Stage newWindow = new Stage();
	            newWindow.setTitle("About Michael Runyan");
	            newWindow.setScene(secondScene);
	
	            // Set position of second window, related to primary window.
	            newWindow.setX(stage.getX() + 200);
	            newWindow.setY(stage.getY() + 100);
	
	            newWindow.show();
	    		}//handle
	    });
    	
    	menu1.getItems().add(exitSelec);
    	menuBar.getMenus().add(menu1);
    	
    	help.getItems().add(about);
    	menuBar.getMenus().add(help);
    	
    	return menuBar;
    }// addMenuBar
    /**
   	 * Creates a Tool bar. Adds buttons, labels, and constraints
   	 * 
   	 * @return  ToolBar toolBar a fully set up tool bar
   	 */
    private ToolBar addToolBar() {
    	
    	//set intial labels and text
    	Button update = new Button("Update Images");
    	Button pause = new Button("Pause");
    	Label searchLabel =new Label("Search Query");
    	TextField textField = new TextField("Chainsmokers");
	
    	//Handler for update image button
		update.setOnAction(e -> { searchTerm = textField.getText();
			searchTerm = (searchTerm.trim()).replaceAll(" ","+");
			
			//creates a thread to be run
			Runnable r = () -> {
			    temp = retrieveImages();
			    Platform.runLater(() -> {showImages();}); // Added so this can be done on the javaFX thread so it can interact with the stage
			};
			Thread t = new Thread(r);
			t.setDaemon(true);
			t.start();// begins the new thread
		
		    });
		// handles the pause button with lambdas
		pause.setOnAction(e ->{ 
			if (pause.getText() == "Pause"){
			    timeline.pause();
			    pause.setText("Play");
			}//if
			else{
			    timeline.play();
			    pause.setText("Pause");
			}//else
		    });
    	ToolBar toolBar = new ToolBar(pause,searchLabel, textField, update);
    	
    	return toolBar;
    }
    /**
   	 * Searches the itunes search API, retrieve objects. Converts those objects into and Array of image view and returns them.
   	 * 
   	 * @return  ImageView[] ivArray Array of image views 
   	 */
    private ImageView[] retrieveImages() {// gotta catch a few things here
	
	Platform.runLater(() -> {pBar.setProgress(0F);});
	String[] imgAsString = new String[3];
	ImageView[] ivArray = new ImageView[3];
		
	try{
	    URL url = new URL("https://itunes.apple.com/search?term="+ searchTerm + "&limit=100" );// create the proper search URL
	    Platform.runLater(() -> {pBar.setProgress(.03F);});// updates progress bar
	    try{
		InputStreamReader reader = new InputStreamReader(url.openStream());// creates reader to be parsed
		Platform.runLater(() -> {pBar.setProgress(.06F);});
		JsonParser jp = new JsonParser();
		Platform.runLater(() -> {pBar.setProgress(.09F);});
		JsonElement je = jp.parse(reader);
		Platform.runLater(() -> {pBar.setProgress(.12F);});
		JsonObject root = je.getAsJsonObject();                      // root of response
		Platform.runLater(() -> {pBar.setProgress(.15F);});
		JsonArray results = root.getAsJsonArray("results");          // "results" array
		Platform.runLater(() -> {pBar.setProgress(.18F);});
		int numResults = results.size();                             // "results" array size
		Platform.runLater(() -> {pBar.setProgress(.21F);});	
		imgAsString = new String[numResults];
		// working code ivArray = new ImageView[numResults];
		Platform.runLater(() -> {pBar.setProgress(.24F);});
		for (int i = 0; i < numResults; i++) {                       
		    JsonObject result = results.get(i).getAsJsonObject();    // object i in array
		    JsonElement artworkUrl100 = result.get("artworkUrl100"); // artworkUrl100 member
		    if (artworkUrl100 != null) {                             // member might not exist
			String artUrl = artworkUrl100.getAsString();        // get member as string
			//			System.out.println(artUrl);                         // print the string
			imgAsString[i] = artUrl;
			Platform.runLater(() -> {pBar.setProgress(.27F);});
		    } // if
		} // for
			
	    }//reader try statement

	    catch(IOException e){// Catches possible errors when getting images
		System.out.println("The search results could not be read");
	    }//reader catch statement
	}//URL try statement
	catch(MalformedURLException e){
	    System.out.println("The URL could not be read");
	}// end of URL Catch

	imgAsString = Arrays.stream(imgAsString).distinct().toArray(String[]::new); // Takes out any repeated images
	Platform.runLater(() -> {pBar.setProgress(.30F);});
	
	//Handles if not enough images are retrieved
	if(imgAsString.length <= 20){
	    Platform.runLater(() -> {
		    Alert alert = new Alert(AlertType.INFORMATION);
		    alert.setTitle("ERROR");
		    alert.setHeaderText("An error has ocurred:");
		    alert.setContentText("Not enough results");
		    alert.showAndWait();
		});
	    ivArray  = null;
		}//if
	else{
	    ivArray = new ImageView[imgAsString.length];
	    for (int i = 0; i < imgAsString.length;i++){//puts results into an array
	    	Image imageNum = new Image(imgAsString[i]);
	    	ivArray[i] = new ImageView(imageNum);
	    	Platform.runLater(() -> {pBar.setProgress(.01F +pBar.getProgress());});
	    }//for
	 }//else
	return ivArray;
    }//retrieveImages
    /**
   	 * Displays the images in ivArray. Rotates through the images with a timeline randomly replaceing them
   	 *   
   	 */
    private void showImages(){
	index = 20; //resets the index
	//Insures that the returned array is not empty
	if (temp != null) //insures the images are not changed if not enough were gathered
	    ivArray =  temp;
	else{
	    for (int i = 0; ivArray.length >i;i++)
	    	pane.getChildren().remove(ivArray[i]);
	}//else
	numImg = ivArray.length;
	
	for (int r = 0; r < 4; r++){
	    for (int c = 0; c < 5; c++){
		record[r][c]= c+r*5;
		pane.setConstraints(ivArray[c+r*5],c,3+r,1,1);
		pane.getChildren().add(ivArray[c+r*5]);
		pBar.setProgress(.95F);
	    }//inner for 
	}//Outer for
	pane.setConstraints(pBar,0,7,5,1);
	stage.show();
	pBar.setProgress(1F);
	//System.out.println("Height of the window" + pane.getHeight());

	timeline.setCycleCount(Timeline.INDEFINITE);
	timeline.getKeyFrames().add(keyFrame);
	timeline.play();
	
    }//showImages

    /**
   	 * Keeps track of an index. Resets the index if it is higher then number of images. Increases it if true is entered
   	 * 
   	 * @param boolean inc determines whether 
   	 * @return int index index of kept track   
   	 */
    public int getIndex(boolean inc){
	if(index >  numImg)index = 0;
	if (inc){
	    if(index < numImg-1)
		index++;
	    else index = 0;
	}//if
	//System.out.println("index:"+index+ "                 numImg:" + numImg);
	return index;
    }// get index
    /**
   	 * Returns a random number between 0 and the parameter
   	 * 
   	 * @param int max the range of the returned number
   	 * @return  stage the stage of the GUI    
   	 */
    public int getRandomNum(int max){
	max++;
	return (int)Math.floor(Math.random()*max);
    }//getRandomNum
    /**
   	 * sets the index of the record keeping colum. Keeps track of what Images are where.
   	 * 
   	 * @param int row desired row to change
   	 * @param int col desired col to change
   	 * @param int index the index to change it too    
   	 */
    public void setRecord(int row,int col,int index){
	record[row][col]=index;
    }// end setRecord
    /**
   	 * Returns the index of the location specified in parameters
   	 * 
   	 * @return  stage the stage of the GUI    
   	 */
    public int getRecord(int row,int col){
	return record[row][col];
    }//gerRecord
    /**
   	 * A method to display the Array to help in debugging
   	 * 
   	 */
    public void displayArray(){
	for (int r = 0; r < 4;r++){
	    for(int c = 0; c < 5;c++){
		System.out.print(" " + record[r][c]+ " ");
	    }//c for
	    System.out.println();
	}//r for
    }//display array
    /**
   	 * Returns the ImageView at the index of the parameter
   	 * 
   	 * @param int index the index of the array desired to be retrieved
   	 * @return  ImageView the Image view at the index of ivArray
   	 */
    public ImageView getIvArray(int index){
	return ivArray[index];
    }//getIvArray
    /**
   	 * Checks to see if the index is in the record array
   	 * 
   	 * @param int index The index to be checked
   	 * @return  boolean InArray whether of not it is in the array 
   	 */
    public boolean inArray(int index){
	boolean InArray =false;
	for (int r = 0; r < 4;r++){
	    for(int c = 0; c <5;c++){
		if(getRecord(r,c) == index){
		    InArray = true;
		}//if
	    }//c for
	}//r for
	return InArray;
    }//inArray
 
 
} // GalleryApp
