package org.demo;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.util.ByteArrayInputStream;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class JMSChat extends Application {
	private MessageProducer messageProducer;
	private Session session;
	private String idUser;

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Bienvenue sur l'application chat de communication entre les clients ");
	    BorderPane borderPane=new BorderPane();
	    Text text1=new Text("Bienvenue sur l'application chat de communication entre les clients");
	    text1.setStyle("-fx-font-weight: bold");
	   
	    
	    HBox hBox=new HBox();
	    
	    hBox.setPadding(new Insets(20)); 
	    hBox.setSpacing(5);
	    hBox.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
	  
	    
	    Label labelId=new Label("ID");
	    labelId.setFont(new Font("Arial", 16)); // La taille et la police du text du label
	    labelId.setPadding(new Insets(5));
	    TextField textFieldId = new TextField("Client 1");
	    textFieldId.setPromptText("ID");
	    
	    Label labelHost=new Label("Host");
	    labelHost.setFont(new Font("Arial", 16));
	    labelHost.setPadding(new Insets(5));
	    TextField textFieldHost = new TextField("localhost");
	    textFieldHost.setPromptText("Host");
	    
	    Label labelPort=new Label("Port");
	    labelPort.setFont(new Font("Arial", 14));
	    labelPort.setPadding(new Insets(5));
	    TextField textFieldPort = new TextField("61616");
	    textFieldPort.setPromptText("Port");
	    
	    Button btnConnect = new Button("Connecter");
	    btnConnect.setStyle("-fx-font: 14 arial; -fx-base: #A2E41D;");
	    
	    
	    hBox.getChildren().add(labelId);
	    hBox.getChildren().add(textFieldId); 
	    hBox.getChildren().add(labelHost);
	    hBox.getChildren().add(textFieldHost);
	    hBox.getChildren().add(labelPort);
	    hBox.getChildren().add(textFieldPort);
	    hBox.getChildren().add(btnConnect);
	    
	    borderPane.setTop(hBox);
	    
	    VBox vBox=new VBox();
	    GridPane gridPane=new GridPane();
	    HBox hBox2=new HBox();
	    vBox.getChildren().add(gridPane);
	    vBox.getChildren().add(hBox2);
	    borderPane.setCenter(vBox);   // Pour centrer le vBox
	        
	    
	    Label labelDestinataire=new Label("Destinataire :");
	    TextField textFieldDestinataire=new TextField("Client 1");
	    textFieldDestinataire.setPrefWidth(250); 
	    textFieldDestinataire.setFont(new Font("Arial", 16));
	    
	    Label labelMessage=new Label("Message :");
	    TextArea textAreaMessage=new TextArea("Le message");
	    textAreaMessage.setPrefWidth(250);
	    textAreaMessage.setFont(new Font("Arial", 16));
	    textAreaMessage.setPrefRowCount(3); // Le nombre de ligne dans la zone du message 
	    
	    Button btnEnvoyer=new Button("Envoyer");
	    btnEnvoyer.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");
	    
	    
	    Label labelImage=new Label("Envoyer image :");	// Partie image
	    File file=new File("images");
	    ObservableList<String> observableListImg=FXCollections.observableArrayList(file.list());
	    ComboBox<String> comboBoxImage=new ComboBox<String>(observableListImg); //Selection de l'image
	    comboBoxImage.getSelectionModel().select(0);
	    
	    Button btnEnvoyerImage=new Button("Envoyer");
	    btnEnvoyerImage.setStyle("-fx-font: 22 arial; -fx-base: #E4871D;");
	    
	    
	    //Ajout de ces élément dans le gridPabe
	    gridPane.setPadding(new Insets(10));
	    gridPane.setVgap(10); // Pour le spacement vertical
	    gridPane.setHgap(10);// Pour le spacement horizontal
	    
	    
	    
	    gridPane.add(labelDestinataire, 0, 0); //le premier 0 indique le colone et le deuxième la ligne
	    gridPane.add(textFieldDestinataire, 1, 0);
	    gridPane.add(labelMessage, 0, 1);
	    gridPane.add(textAreaMessage, 1, 1);
	    gridPane.add(btnEnvoyer, 2, 1);
	    gridPane.add(labelImage, 0, 2);
	    gridPane.add(comboBoxImage, 1, 2);
	    gridPane.add(btnEnvoyerImage, 2, 2);
	    
	    
	    ObservableList<String> observableListMg=FXCollections.observableArrayList();
	    ListView<String> listViewMg=new ListView<String>(observableListMg);
		/*
		 * listViewMg.setMaxWidth(180); 
		 * listViewMg.maxHeight(150);
		 */
	    
	    File file1=new File("images/"+comboBoxImage.getSelectionModel().getSelectedItem());
	    Image image=new Image(file1.toURI().toString());
	    ImageView imageView=new ImageView(image);
	    
	    
	    hBox2.getChildren().add(listViewMg);
	    hBox2.getChildren().add(imageView);
	    hBox2.setPadding(new Insets(10));
	    hBox2.setSpacing(20);
	    
	    
        
	    Scene scene=new Scene(borderPane,800,500); 
	    stage.setScene(scene);
	    stage.show();
	    
	    comboBoxImage.getSelectionModel().selectedItemProperty().addListener( new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				File file2=new File("images/"+newValue);
				Image image=new Image(file2.toURI().toString());
				imageView.setImage(image);
				
			}
	    	
		});
	    
	    btnEnvoyer.setOnAction(e->{
	    	try {
				TextMessage textMessage=session.createTextMessage();
				textMessage.setText(textAreaMessage.getText());
				textMessage.setStringProperty("code", textFieldDestinataire.getText());
				messageProducer.send(textMessage);
			} catch (JMSException f) {
				// TODO Auto-generated catch block
				f.printStackTrace();
			}
	    	
	    });
	    
	    btnEnvoyerImage.setOnAction(e->{
	    	try {
				StreamMessage streamMessage=session.createStreamMessage();
				streamMessage.setStringProperty("code", textFieldDestinataire.getText());
				File file2=new File("images/"+comboBoxImage.getSelectionModel().getSelectedItem());
				FileInputStream fis=new FileInputStream(file2);
				byte[] data=new byte[(int)file2.length()];
				fis.read(data);
				streamMessage.writeString(comboBoxImage.getSelectionModel().getSelectedItem());
				streamMessage.writeInt(data.length);
				streamMessage.writeBytes(data);
				messageProducer.send(streamMessage);
				
				
				
				
			} catch (JMSException f) {
				// TODO Auto-generated catch block
				f.printStackTrace();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    	
	    });
	    
	    
	    btnConnect.setOnAction(new EventHandler<ActionEvent>() {
		  
		  @Override
		  public void handle(ActionEvent event) { 
			
		  try { 
			  
			  idUser=textFieldId.getText();
			  String host=textFieldHost.getText();
			  int port=Integer.parseInt(textFieldPort.getText());
			  ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://"+host+":"+port);
			  Connection connection=connectionFactory.createConnection();
			  connection.start();
			  
			  session=connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
			  //session2=connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
			  Destination destination=session.createTopic("uasz.chat");
			  MessageConsumer messageConsumer=session.createConsumer(destination,"code='"+idUser+"'");
			  messageProducer = session.createProducer(destination);
			  messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			  messageConsumer.setMessageListener(message->{
				  
			  try {
				  if(message instanceof TextMessage) {
					  TextMessage textMessage=(TextMessage)message;
					  observableListMg.add(textMessage.getText());
				  }else if(message instanceof StreamMessage) {
					  StreamMessage streamMessage=(StreamMessage)message;
					  String nomPhoto=streamMessage.readString();
					  observableListMg.add("Réception de l'image : "+nomPhoto);
					  int size=streamMessage.readInt();
					  byte[] data=new byte[size];
					  streamMessage.readBytes(data);
					  ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(data);
					  Image image=new Image(byteArrayInputStream);
					  imageView.setImage(image);
				  }
						  
			  } 
			  catch (Exception e) {
				e.printStackTrace();
			 }
			  });
			  
			  hBox.setDisable(true);
		  } catch (JMSException e) { 
				 
				  e.printStackTrace();
		  }
		  
		  } }); 
		
	}

}


// 34mn - 34s Part 4


