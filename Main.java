package application;



import java.awt.event.ActionListener;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application implements EventHandler<ActionEvent>, ActionListener {
	// store any command-line arguments that were entered.
	// NOTE: this.getParameters().getRaw() will get these also
	private List<String> args;

	GridPane mainMiddleBox;

	private Scene mainScene;
	private Label title;
	public Alert userAdded;
	public Alert userRemoved;
	private ComboBox<String> userToFind;
	private Button userSearch;
	public Alert friendsAdded;
	public Alert friendsRemoved;
	private File saveFile;
	private Button home;
	private Button showFriendships;
	public static int numFriendships;
	public String friendNum;
	public static int numUsers = 0;
	public String userNum;
	private Scene userScene;
	public VBox centralUser;	
	private BorderPane newUser;
	private SocialNetwork network;
	private GraphicsContext context;
	private Canvas canvas;


	double x = 0;
	private static final int WINDOW_WIDTH = 800;
	private static final int WINDOW_HEIGHT = 500;
	private static final String APP_TITLE = "Friend Book";


	@Override
	public void start(Stage primaryStage) throws Exception {

		// Main layout is Border Pane example (top,left,center,right,bottom)
		BorderPane root = new BorderPane();
		root.setId("background-image");
		mainMiddleBox = new GridPane();
		mainMiddleBox.setHgap(10);
		mainMiddleBox.setVgap(20);

		mainScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
		//mainScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		network = new SocialNetwork();
		args = this.getParameters().getRaw();

		centralUser();
		addUsers();
		removeUsers();
		searchUser(primaryStage);
		loadNetwork(primaryStage);
		addFriends();
		removeFriends();
		saveNetwork();
		clearAndExit(primaryStage);
		numberOfUsers();
		showFriendships();
		showConnectedComponents();


		//ArrayList<String> namesOfUsers = new ArrayList<String>();

		home = new Button("HOME");
		home.getStyleClass().add("home-button-canvas");
		BorderPane.setAlignment(home, Pos.CENTER);
		userSearch.setOnAction(e -> {if (userToFind.getValue() != null) {		    
		changeScene(primaryStage, userToFind.getValue().toString());}});
	
		VBox empty = new VBox();
		empty.getChildren().add(new Label("                                 "));

		title = new Label("Welcome to Freind");
		title.getStyleClass().add("header");
		root.setTop(title);
		BorderPane.setAlignment(title, Pos.CENTER);
		root.setLeft(empty);
		root.setCenter(mainMiddleBox);
		BorderPane.setAlignment(mainMiddleBox, Pos.CENTER);


		// Add the stuff and set the primary stage
		primaryStage.setTitle(APP_TITLE);
		primaryStage.setScene(mainScene);
		primaryStage.show();
	}

	/**
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * 
	 * @param primaryStage
	 * @param input
	 */
	private void changeScene(Stage primaryStage, String input) {
		{
			newUser = new BorderPane();
			canvas = new Canvas(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
			context = canvas.getGraphicsContext2D();
			//drawGraph(context, input);

			HBox searchBox = new HBox();
			ComboBox<String> friends = new ComboBox<String>();
			try {
				if (network.getFriends(input) != null) {
					for (String f : network.getFriends(input)){
						friends.getItems().add(f);
					}
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalNameException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			friends.setValue("Search user friends");
			Button searchFriend = new Button("Search");
			searchBox.getChildren().addAll(friends, searchFriend);
			searchFriend.setOnAction(f -> {if(!friends.getValue().toString().equals("Search user friends")) {
			    
			changeScene(primaryStage, friends.getValue().toString());}});


			newUser.setCenter(canvas);
			BorderPane.setAlignment(canvas, Pos.CENTER);
			newUser.setBottom(home);

			Label userSearchHeader = new Label(input + "'s Connections");
			userSearchHeader.getStyleClass().add("header");
			VBox sceneName = new VBox();
			sceneName.getChildren().addAll(userSearchHeader, searchBox);
			BorderPane.setAlignment(sceneName, Pos.CENTER);
			BorderPane.setAlignment(searchBox, Pos.CENTER);

			Label filler = new Label("                   ");
			Label filler2 = new Label("                   ");

			newUser.setLeft(filler2);
			newUser.setRight(filler);
	        newUser.setTop(sceneName);
			userToFind.setValue("");


			newUser.setId("background-image");
			userScene = new Scene(newUser, WINDOW_WIDTH, WINDOW_HEIGHT);
			//userScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());;
			home.setOnAction(f -> {
				primaryStage.setScene(mainScene);
				userScene = null;
				newUser = null;

			});
			primaryStage.setScene(userScene);
		}
	}

	

	/**
	 * 
	 */
	private void addUsers() {

		mainMiddleBox.add(new Label("Add new user: "), 0, 0, 1, 1);
		TextField userName = new TextField();
		mainMiddleBox.add(userName, 1, 0, 1, 1);
		Button userAdd = new Button("Submit");
		mainMiddleBox.add(userAdd, 2, 0, 1, 1);
		userAdd.setOnAction(e -> {
			try {
				if (network.addUser(userName.getText())) {
					userToFind.getItems().add(userName.getText());
					mainMiddleBox.add(
						transitionCreator(
							"User \"" + userName.getText() + "\" was added to the network!", true),
						3, 0, 1, 1);
				} else if (userName.getText().equals("")){
				    mainMiddleBox.add(transitionCreator(
				        "Invalid input", false), 3, 0, 1, 1);
				}
				    else {

					mainMiddleBox.add(transitionCreator(
						"User \"" + userName.getText() + "\" is already added!", false), 3, 0, 1, 1);
				}
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalNameException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			userName.setText("");
		});

	}

	/**
	 * 
	 */
	private void removeUsers() {
		mainMiddleBox.add(new Label("Remove user: "), 0, 1, 1, 1);
		TextField removeUserName = new TextField();
		mainMiddleBox.add(removeUserName, 1, 1, 1, 1);
		Button userRemove = new Button("Remove");
		mainMiddleBox.add(userRemove, 2, 1, 1, 1);
		userRemove.getStyleClass().add("button-blue");
		userRemove.setOnAction(e -> {
			try {
				if (network.removeUser(removeUserName.getText())) {
					userToFind.getItems().remove(removeUserName.getText());
					mainMiddleBox.add(transitionCreator(
						"User \"" + removeUserName.getText() + "\" was removed!", true), 3, 1, 1, 1);
				} else if (removeUserName.getText().equals("")){
				     
				        mainMiddleBox.add(transitionCreator(
				            "Invalid input", false), 3, 1, 1, 1);
				    
				}else {
				
					mainMiddleBox.add(
						transitionCreator(
							"User \"" + removeUserName.getText() + "\" is not in the network!", false),
						3, 1, 1, 1);
				}
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalNameException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			removeUserName.setText("");
		});

	}

	/**
	 * 
	 * @param primaryStage
	 */
	private void searchUser(Stage primaryStage) {

		mainMiddleBox.add(new Label("Find certain user: "), 0, 4, 1, 1);
		userToFind = new ComboBox<String>();
		mainMiddleBox.add(userToFind, 1, 4, 1, 1);
		userSearch = new Button("Search");
		mainMiddleBox.add(userSearch, 2, 4, 1, 1);
		userSearch.getStyleClass().add("button-blue");
		
		userSearch.setOnAction(e -> {
					
			String chosen = userToFind.getValue();
			try {
				String[] net = network.getFriends(chosen);
				VBox vb = new VBox();
				for(int i=0;i<net.length;i++) {
					Button b = new Button(net[i]);
					vb.getChildren().add(b);
				}
				mainMiddleBox.add(vb, 2, 2, 1, 1);
			} catch (IllegalArgumentException | IllegalNameException e1) {
				e1.printStackTrace();
			}
			
			
		});

	}

	/**
	 * 
	 * @param primaryStage
	 */
	private void loadNetwork(Stage primaryStage) {
		Button loadNetwork = new Button("Load from a txt file");
		mainMiddleBox.add(loadNetwork, 0, 6, 1, 1);

//		loadNetwork.getStyleClass().add("button-red");
//		loadNetwork.getStyleClass().add("button-red-large-width");

		loadNetwork.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			File selectedFile = fileChooser.showOpenDialog(primaryStage);
			try {
				network.loadFromFile(selectedFile);
				for (String x : network.getAllUsers()) {
					if (!userToFind.getItems().contains(x)){
						userToFind.getItems().add(x);

					}
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
	}

	/**
	 * 
	 */
	private void addFriends() {

		mainMiddleBox.add(new Label("Add friendship: "), 0, 2, 1, 1);

		VBox twoFriends = new VBox();
		TextField add1 = new TextField();
		TextField add2 = new TextField();
		twoFriends.getChildren().add(add1);
		twoFriends.getChildren().add(add2);
		twoFriends.setSpacing(5);

		mainMiddleBox.add(twoFriends, 1, 2, 1, 1);

		Button enterFriend = new Button("Add");
		mainMiddleBox.add(enterFriend, 2, 2, 1, 1);

//		enterFriend.getStyleClass().add("button-blue");

		enterFriend.setOnAction(d -> {
			try {
				if (network.addFriends(add1.getText(), add2.getText())) {
					if (!userToFind.getItems().contains(add1.getText())) {
						userToFind.getItems().add(add1.getText());
					}
					if (!userToFind.getItems().contains(add2.getText())) {
						userToFind.getItems().add(add2.getText());
					}
					mainMiddleBox.add(transitionCreator(
						"\"" + add1.getText() + "\" and \"" + add2.getText() + "\" are now friends!",
						true), 3, 3, 1, 1);

				} else if (add1.getText().equals("")|| add2.getText().equals("")) { 
				    mainMiddleBox.add(transitionCreator(
				        "Invalid input", false), 3, 3, 1, 1);
				}
				else {
					mainMiddleBox.add(transitionCreator("\"" + add1.getText() + "\" and \""
						+ add2.getText() + "\" are already friends!", false), 3, 3, 1, 1);
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalNameException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DuplicateNameException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			add1.setText("");
			add2.setText("");
		});

	}

	/**
	 * 
	 */
	private void removeFriends() {

		mainMiddleBox.add(new Label("Remove friendship: "), 0, 3, 1, 1);

		VBox twoFriends2 = new VBox();
		TextField remove1 = new TextField();
		TextField remove2 = new TextField();
		twoFriends2.getChildren().add(remove1);
		twoFriends2.getChildren().add(remove2);
		twoFriends2.setSpacing(5);


		mainMiddleBox.add(twoFriends2, 1, 3, 1, 1);

		Button removeFriend = new Button("Remove");
		mainMiddleBox.add(removeFriend, 2, 3, 1, 1);
		//removeFriend.getStyleClass().add("button-blue");

		removeFriend.setOnAction(e -> {
			try {
				if (network.removeFriends(remove1.getText(), remove2.getText())) {
					mainMiddleBox.add(transitionCreator("Friendship successfully removed!", true), 3, 4,
						1, 1);
				} else if (remove1.getText().equals("") || (remove2.getText().equals(""))) {
				    mainMiddleBox.add(transitionCreator(
				        "Invalid input", false), 3, 4, 1, 1);
				}
				
				else {
					mainMiddleBox.add(transitionCreator(
						remove1.getText() + " and " + remove2.getText() + " are not friends!", false),
						3, 4, 1, 1);
				}
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalNameException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (DuplicateNameException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			remove1.setText("");
			remove2.setText("");
		});

	}

	/**
	 * 
	 */
	private void saveNetwork() {
		mainMiddleBox.add(new Label("Save to file: "), 0, 5, 1, 1);
		TextField save = new TextField();

		mainMiddleBox.add(save, 1, 5, 1, 1);
		Button saveEnter = new Button("Save");
		mainMiddleBox.add(saveEnter, 2, 5, 1, 1);
		saveEnter.getStyleClass().add("button-blue");

		saveEnter.setOnAction(e -> {
			saveFile = new File(save.getText());
			network.saveToFile(saveFile);
		});

	}

	/**
	 * 
	 * @param primaryStage
	 */
	private void clearAndExit(Stage primaryStage) {

		Button clearNetwork = new Button("Clear current graph");
		Button exit = new Button("Exit");

		mainMiddleBox.add(clearNetwork, 1, 6, 1, 1);
		mainMiddleBox.add(exit, 3, 5, 1, 1);

		
		clearNetwork.setOnAction(c -> {
			network = new SocialNetwork();
			numUsers = 0;
			try {
				start(primaryStage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		exit.setOnAction(e -> {
			TextInputDialog td = new TextInputDialog("");
			td.setTitle("Exit");
			td.setHeaderText("Enter filename to save network and press OK to exit. \nIf "
					+ "no filename is entered, the network will not be saved.");
			td.setContentText("Save as:");
			Optional<String> result = td.showAndWait();
			result.ifPresent(name -> {
				saveFile = new File(name);
				network.saveToFile(saveFile);
				primaryStage.close();
			});
		});
	}

	
	/**
	 * 
	 */
	private void numberOfUsers() {
		Button showNumUsers = new Button("Number of Users");
		mainMiddleBox.add(showNumUsers, 1, 7, 1, 1);

		showNumUsers.setOnAction(r -> {
			Alert useNums = new Alert(AlertType.INFORMATION, (Integer.toString(network.getTotalFriends())));
			useNums.showAndWait();
		});
	}
	
	/**
	 * 
	 */
	private void showFriendships() {
		numFriendships = network.getTotalFriendships();
		showFriendships = new Button("Total Friendships");

		mainMiddleBox.add(showFriendships, 0, 7, 1, 1);
		showFriendships.setOnAction(r -> {
			Alert friendNums = new Alert(AlertType.INFORMATION, (Integer.toString(network.getTotalFriendships()/2)));
			friendNums.showAndWait();
		});
	}

	 /**
		 * 
		 */
		private void showConnectedComponents() {
			numFriendships = network.getTotalFriends();
			showFriendships = new Button("Connected Components");
			mainMiddleBox.add(showFriendships, 2, 7, 1, 1);
			showFriendships.setOnAction(r -> {
				Alert connectedNums = new Alert(AlertType.INFORMATION, (Integer.toString(network.getConnectedComponents().size())));
				connectedNums.showAndWait();
			});
		}
	/**
	 * 
	 */
	private void centralUser() {
		numFriendships = network.getTotalFriends();
		showFriendships = new Button("Central User:");
		mainMiddleBox.add(showFriendships, 3, 1, 1, 1);
		showFriendships.setOnAction(r -> {
			Alert centeruser = new Alert(AlertType.INFORMATION, network.getCentralUser());
			centeruser.showAndWait();
		});

	}

	/**
	 * 
	 * @param context
	 * @param user
	 */
	private void drawGraph(GraphicsContext context, String user) {
		double x = WINDOW_WIDTH / 4;
		double y = WINDOW_HEIGHT / 4;
		double r = 100;
		double theta = 0;
		drawNode(context, user, user, x, y);
		try {
			if (network.getFriends(user) != null) {
				//System.out.println(network.getFriends(user).length);
				
				double thetaIncrement = 360 / 1000;

				for (String e : network.getFriends(user)) {
					double xPolar = x + r * Math.cos(theta*Math.PI/180);
					double yPolar = y + r * Math.sin(theta*Math.PI/180);
					drawEdge(context, x, y, xPolar, yPolar);

					drawNode(context, e, user, xPolar, yPolar);
					drawNode(context, user, user,  x, y);

					theta += thetaIncrement;

				}
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	/**
	 * 
	 * @param context
	 * @param name
	 * @param user
	 * @param x
	 * @param y
	 */
	private void drawNode(GraphicsContext context, String name, String user, double x, double y) {
		context.setFill(Color.LIGHTBLUE);
		if (name.equals(user)) {
		    context.fillOval(x - 50, y - 50, 100, 100);
		} else {
		context.fillOval(x - 30, y - 30, 60, 60);
		}
		context.setTextAlign(TextAlignment.CENTER);
		context.setFill(Color.BLACK);
		context.setFont(new Font(16));
		context.fillText(name, x, y + 5);
	}

	/**
	 * 
	 * @param context
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	private void drawEdge(GraphicsContext context, double x1, double y1, double x2, double y2) {
		context.setStroke(Color.LIGHTBLUE);
		context.setLineWidth(2);
		context.strokeLine(x1, y1, x2, y2);
	}

	/**
	 * 
	 * @param node
	 * @return
	 */
	private FadeTransition createFader(Node node) {
		FadeTransition fade = new FadeTransition(Duration.seconds(.3), node);
		fade.setFromValue(1);
		fade.setToValue(0);
		return fade;
	}

	/**
	 * 
	 * @param string
	 * @param success
	 * @return
	 */
	private Node transitionCreator(String string, boolean success) {

		if (success) {

			Label alertText = new Label(string);
			alertText.getStyleClass().add("success-text");
			FadeTransition fader = createFader(alertText);
			PauseTransition pauseTransition = new PauseTransition(Duration.millis(2000));
			SequentialTransition sequentialTransition =
				new SequentialTransition(pauseTransition, fader);
			sequentialTransition.play();
			return alertText;
		} else {

			Label alertText = new Label(string);
			alertText.getStyleClass().add("error-text");
			FadeTransition fader = createFader(alertText);
			PauseTransition pauseTransition = new PauseTransition(Duration.millis(2000));
			SequentialTransition sequentialTransition =
				new SequentialTransition(pauseTransition, fader);
			sequentialTransition.play();
			return alertText;
		}


	}
	

	public void MouseEvent() {}

	@Override
	public void handle(ActionEvent event) {}

	@Override
	public void actionPerformed(java.awt.event.ActionEvent e) {}

}
