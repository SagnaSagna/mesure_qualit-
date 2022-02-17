package org.demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import javafx.application.Application;

@SpringBootApplication
public class ChaterApplication {

	public static void main(String[] args) {   
		Application.launch ( JMSChat.class , args ) ;
	
	}

}
