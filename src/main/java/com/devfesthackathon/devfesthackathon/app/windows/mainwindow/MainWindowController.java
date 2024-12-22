package com.devfesthackathon.devfesthackathon.app.windows.mainwindow;

import com.devfesthackathon.devfesthackathon.app.ControllerBase;
import com.devfesthackathon.devfesthackathon.app.GeminiAPI;
import com.devfesthackathon.devfesthackathon.app.Window;
import com.devfesthackathon.devfesthackathon.app.util.MarkdownParser;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

/**
 * Controller for the Main Window in the application.
 * <p>This class handles the user interactions within the Main Window,
 * such as executing SQL queries, importing databases, saving changes,
 * and displaying the results in the console and result tabs.</p>
 *
 * @see ControllerBase
 * @see Window
 */
public class MainWindowController extends ControllerBase {

    private static final Logger logger = Logger.getLogger(MainWindowController.class.getName());

    @FXML
    private Label welcomeLabel;
    @FXML
    private ScrollPane chatScrollPane;
    @FXML
    private VBox chatArea;
    @FXML
    private TextField messageInput;
    @FXML
    private Button attachButton;
    @FXML
    private Button sendButton;

    private File selectedImage = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Window.getWindowAt(Window.MAIN_WINDOW).setController(this);
        chatScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        chatScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        generateWelcomeMessage();
        setupMessageHandling();
        setupAttachmentHandling();
    }

    private void generateWelcomeMessage() {
        welcomeLabel.setText("Welcome to CropSense!");

        CompletableFuture.runAsync(() -> {
            try {
                String prompt = "Generate a short, inspiring message for farmers about crop care and growth. " +
                        "The message should be motivational and under 40 characters. " +
                        "Make it personal and caring.";

                String generatedMessage = GeminiAPI.generateText(prompt);

                Thread.sleep(6000);

                Platform.runLater(() -> {
                    if (generatedMessage != null && !generatedMessage.isEmpty()) {
                        welcomeLabel.setText(generatedMessage);
                    }
                });
            } catch (Exception e) {
                logger.severe("Error occurred while generating welcome message: " + e.getMessage());
            }
        });
    }

    private void setupMessageHandling() {
        sendButton.setOnAction(e -> sendMessage());
        messageInput.setOnAction(e -> sendMessage());
    }

    private void setupAttachmentHandling() {
        attachButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.webp", "*.gif")
            );
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                selectedImage = selectedFile;
                addImageToChat(selectedFile);
            }
        });
    }

    private void sendMessage() {
        String message = messageInput.getText().trim();

        if(message.isEmpty() && selectedImage == null) {
            return;
        }

        if(selectedImage != null) {
            handleImageMessage(message);
        } else {
            handleTextMessage(message);
        }
    }

    private void handleTextMessage(String message) {
        addMessageToChat(message, true);
        messageInput.clear();

        try {
            String modifiedPrompt = message + "\n\nNote: You are an AI assistant specifically designed for agricultural and crop-related topics. " +
                    "If the question is not related to agriculture, farming, crops, or plant care, or it is just chatting, " +
                    "politely inform the user that you are specialized in agricultural topics and can only assist with those kinds of questions.";

            String response = GeminiAPI.generateText(modifiedPrompt);
            addMessageToChat(response, false);
        } catch (Exception e) {
            logger.severe("Error occurred while generating text response: " + e.getMessage());
        }
    }

    private void handleImageMessage(String message) {
        String defaultPrompt = "Analyze this image and provide insights on its contents.";
        String finalMessage = message.isEmpty() ? defaultPrompt : message;

        if(!message.isEmpty()) {
            addMessageToChat(message, true);
        }

        File tempImage = selectedImage;
        messageInput.clear();

        try {
            String modifiedPrompt = finalMessage + "\n\nNote: You are an AI assistant specifically designed for agricultural and crop-related topics. " +
                    "If the image is not related to agriculture, farming, crops, or plant care, " +
                    "politely inform the user that you are specialized in agricultural topics and can only assist with those kinds of questions.";

            String response = GeminiAPI.generateImageResponse(modifiedPrompt, tempImage.getAbsolutePath());
            addMessageToChat(response, false);
        } catch (Exception e) {
            logger.severe("Error occurred while generating image response: " + e.getMessage());
        }

        selectedImage = null;
    }

    private void addMessageToChat(String message, boolean isUser) {
        HBox messageBox = new HBox();
        messageBox.getStyleClass().add("message-box");
        messageBox.getStyleClass().add(isUser ? "message-box-user" : "message-box-assistant");

        if (isUser) {
            Label messageLabel = new Label(message);
            messageLabel.setWrapText(true);
            messageLabel.getStyleClass().addAll("message-bubble", "message-bubble-user");
            messageBox.getChildren().add(messageLabel);
        } else {
            Node parsedText = MarkdownParser.parseMarkdownToText(message);
            VBox container = new VBox(parsedText);
            container.getStyleClass().add("markdown-container");
            messageBox.getChildren().add(container);
        }

        chatArea.getChildren().add(messageBox);
    }

    private void addImageToChat(File imageFile) {
        try {
            Image image = new Image(imageFile.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(200);
            imageView.setPreserveRatio(true);

            HBox imageBox = new HBox(imageView);
            imageBox.setAlignment(Pos.CENTER_RIGHT);
            chatArea.getChildren().add(imageBox);
        } catch (Exception e) {
            logger.severe("Error occurred while adding image to chat: " + e.getMessage());
        }
    }

}
