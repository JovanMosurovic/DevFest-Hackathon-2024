package com.devfesthackathon.devfesthackathon.app.windows.mainwindow;

import com.devfesthackathon.devfesthackathon.app.ControllerBase;
import com.devfesthackathon.devfesthackathon.app.GeminiAPI;
import com.devfesthackathon.devfesthackathon.app.Window;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Duration;

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
    private ImageView avatarImage;
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

    private Timeline avatarAnimation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Window.getWindowAt(Window.MAIN_WINDOW).setController(this);
        chatScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        chatScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setupAvatarAnimation();
        setupMessageHandling();
        setupAttachmentHandling();
        generateWelcomeMessage();
    }

    private void generateWelcomeMessage() {
        welcomeLabel.setText("Welcome to CropSense!");

        CompletableFuture.runAsync(() -> {
            try {
                String prompt = "Generate a short, inspiring message for farmers about crop care and growth. " +
                        "The message should be motivational and under 40 characters. " +
                        "Make it personal and caring.";

                String generatedMessage = GeminiAPI.generateText(prompt);

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

    private void setupAvatarAnimation() {
        avatarAnimation = new Timeline(
                new KeyFrame(Duration.seconds(2),
                        new KeyValue(avatarImage.rotateProperty(), 10),
                        new KeyValue(avatarImage.scaleXProperty(), 1.1),
                        new KeyValue(avatarImage.scaleYProperty(), 1.1)
                ),
                new KeyFrame(Duration.seconds(4),
                        new KeyValue(avatarImage.rotateProperty(), -10),
                        new KeyValue(avatarImage.scaleXProperty(), 1),
                        new KeyValue(avatarImage.scaleYProperty(), 1)
                )
        );
        avatarAnimation.setCycleCount(Timeline.INDEFINITE);
        avatarAnimation.play();
    }

    private void setupMessageHandling() {
        sendButton.setOnAction(e -> sendMessage());
        messageInput.setOnAction(e -> sendMessage());
    }

    private void setupAttachmentHandling() {
        attachButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
            );
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                addImageToChat(selectedFile);
            }
        });
    }

    private void sendMessage() {
        String message = messageInput.getText().trim();
        if (!message.isEmpty()) {
            addMessageToChat(message, true);
            messageInput.clear();
            // Simulation of the assistant's response
            Platform.runLater(() -> {
                try {
                    Thread.sleep(1000);
                    String response = GeminiAPI.generateText(message);
                    addMessageToChat(response, false);
                } catch (InterruptedException e) {
                    logger.severe("Error occurred while generating response: " + e.getMessage());
                }
            });
        }
    }

    private void addMessageToChat(String message, boolean isUser) {
        HBox messageBox = new HBox(10);
        messageBox.setAlignment(isUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(300);
        messageLabel.setStyle(
                String.format("-fx-background-color: %s; -fx-padding: 10; -fx-background-radius: 15;",
                        isUser ? "#0084ff" : "#e4e6eb")
        );
        messageLabel.setTextFill(isUser ? Color.WHITE : Color.BLACK);

        messageBox.getChildren().add(messageLabel);
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
