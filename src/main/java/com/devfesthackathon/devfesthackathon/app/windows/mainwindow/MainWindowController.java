package com.devfesthackathon.devfesthackathon.app.windows.mainwindow;

import com.devfesthackathon.devfesthackathon.app.ControllerBase;
import com.devfesthackathon.devfesthackathon.app.GeminiAPI;
import com.devfesthackathon.devfesthackathon.app.WeatherTrack;
import com.devfesthackathon.devfesthackathon.app.Window;
import com.devfesthackathon.devfesthackathon.app.util.MarkdownParser;
import com.devfesthackathon.devfesthackathon.app.Location;

import javafx.animation.TranslateTransition;
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
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.List;
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
    private HBox promptCloudContainer;
    @FXML
    private TextField messageInput;
    @FXML
    private Button attachButton;
    @FXML
    private Button weatherButton;
    @FXML
    private Button sendButton;

    private File selectedImage = null;
    private Boolean weatherModeEnabled = false;
    private WeatherTrack weatherTracker;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Window.getWindowAt(Window.MAIN_WINDOW).setController(this);
        chatScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        chatScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        generateWelcomeMessage();
        setupMessageHandling();
        setupAttachmentHandling();
        setupPromptCloud();
        setupWeatherMode();

        weatherTracker = new WeatherTrack();
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

    private void setupPromptCloud() {
        promptCloudContainer.lookupAll(".prompt-button").forEach(node -> {
            if (node instanceof Button button) {
                button.setOnAction(e -> {
                    messageInput.setText(button.getText());
                    sendMessage();
                    promptCloudContainer.setVisible(false);
                    promptCloudContainer.setManaged(false);
                });

                button.getStyleClass().add("prompt-cloud-button");
            }
        });
    }

    private void setupWeatherMode() {
        weatherButton.setOnAction(e -> toggleWeatherMode());
        updateWeatherButtonStyle();
    }

    private void toggleWeatherMode() {
        weatherModeEnabled = !weatherModeEnabled;
        updateWeatherButtonStyle();
    }

    private void updateWeatherButtonStyle() {
        if (weatherModeEnabled) {
            weatherButton.getStyleClass().add("weather-enabled");
        } else {
            weatherButton.getStyleClass().remove("weather-enabled");
        }
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
        addMessageToChat(message, true, null);
        messageInput.clear();

        String modifiedPrompt = message;

        if (weatherModeEnabled) {
            try {
                Location location = weatherTracker.getLocationService().getCurrentLocation();
                List<Double> temperatures = weatherTracker.getWeatherService().getDailyAverageTemperatures(location);

                modifiedPrompt += "\n\nCurrent location: " + location.toString() +
                        "\nWeather data for the next " + temperatures.size() + " days:\n";

                for (int i = 0; i < temperatures.size(); i++) {
                    modifiedPrompt += String.format("Day %d: %.2f°C\n", i + 1, temperatures.get(i));
                }
            } catch (Exception e) {
                logger.warning("Could not fetch weather data: " + e.getMessage());
            }
        }

        modifiedPrompt += "\n\nNote: You are an AI assistant specifically designed for agricultural and crop-related topics. " +
                "If the question is not related to agriculture, farming, crops, or plant care, or it is just chatting, " +
                "politely inform the user that you are specialized in agricultural topics and can only assist with those kinds of questions.";

        HBox loadingBox = createLoadingBox();
        chatArea.getChildren().add(loadingBox);
        GeminiAPI.generateTextAsync(modifiedPrompt).thenAcceptAsync(response -> {
            if (response != null && !response.isEmpty()) {
                Platform.runLater(() -> addMessageToChat(response, false, loadingBox));
            }
        });
    }

    private void handleImageMessage(String message) {
        promptCloudContainer.setVisible(false);
        promptCloudContainer.setManaged(false);

        String defaultPrompt = "Analyze this image and provide insights on its contents.";
        String finalMessage = message.isEmpty() ? defaultPrompt : message;

        if(!message.isEmpty()) {
            addMessageToChat(message, true, null);
        }

        File tempImage = selectedImage;
        messageInput.clear();

        try {
            String modifiedPrompt = finalMessage + "\n\nNote: You are an AI assistant specifically designed for agricultural and crop-related topics. " +
                    "If the image is not related to agriculture, farming, crops, or plant care, " +
                    "politely inform the user that you are specialized in agricultural topics and can only assist with those kinds of questions." +
                    "Dont generate responses which contains markdown which is not bold, italic, code or citation.";


            String response = GeminiAPI.generateImageResponse(modifiedPrompt, tempImage.getAbsolutePath());
            HBox loadingBox = createLoadingBox();
            addMessageToChat(response, false, loadingBox);
        } catch (Exception e) {
            logger.severe("Error occurred while generating image response: " + e.getMessage());
        }

        selectedImage = null;
    }

    private HBox createLoadingBox() {
        HBox loadingBox = new HBox();
        loadingBox.getStyleClass().add("message-box");
        loadingBox.getStyleClass().add("message-box-assistant");

        HBox loader = new HBox();
        loader.getStyleClass().add("loader");
        loader.setSpacing(5);

        for (int i = 0; i < 3; i++) {
            Circle circle = new Circle(3);
            circle.getStyleClass().add("loader-circle");

            TranslateTransition transition = new TranslateTransition(Duration.seconds(0.6), circle);
            transition.setByY(10);
            transition.setCycleCount(TranslateTransition.INDEFINITE);
            transition.setAutoReverse(true);
            transition.setDelay(Duration.seconds(i * 0.2));
            transition.play();

            loader.getChildren().add(circle);
        }

        loadingBox.getChildren().add(loader);
        return loadingBox;
    }

    private void addMessageToChat(String message, boolean isUser, HBox loadingBox) {
        HBox messageBox = new HBox();
        messageBox.getStyleClass().add("message-box");
        messageBox.getStyleClass().add(isUser ? "message-box-user" : "message-box-assistant");

        if (isUser) {
            Label messageLabel = new Label(message);
            messageLabel.setWrapText(true);
            messageLabel.getStyleClass().addAll("message-bubble", "message-bubble-user");
            messageBox.getChildren().add(messageLabel);
            chatArea.getChildren().add(messageBox);
        } else {
            CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(1000);
                    return MarkdownParser.parseMarkdownToText(message);
                } catch (Exception e) {
                    logger.severe("Error parsing markdown: " + e.getMessage());
                    return new Label(message);
                }
            }).thenAccept(parsedText -> Platform.runLater(() -> {
                if (loadingBox != null)
                    chatArea.getChildren().remove(loadingBox);

                VBox container = new VBox(parsedText);
                container.getStyleClass().add("markdown-container");
                messageBox.getChildren().add(container);
                chatArea.getChildren().add(messageBox);

                // Scroll to bottom
                chatScrollPane.setVvalue(1.0);
            }));
        }
    }

    private void addImageToChat(File imageFile) {
        try {
            Image image = new Image(imageFile.toURI().toString());
            ImageView imageView = new ImageView(image);

            imageView.setFitWidth(300);  // Increased width for better visibility
            imageView.setPreserveRatio(true);

            VBox imageContainer = new VBox();
            imageContainer.getStyleClass().add("image-message-container");
            imageContainer.setAlignment(Pos.CENTER_RIGHT);

            HBox imageWrapper = new HBox(imageView);
            imageView.getStyleClass().add("image-wrapper");
            imageWrapper.setAlignment(Pos.CENTER_RIGHT);

            Label imageInfo = new Label(imageFile.getName());
            imageInfo.getStyleClass().add("image-info-label");

            imageContainer.getChildren().addAll(imageWrapper, imageInfo);

            imageContainer.setSpacing(5);
            imageContainer.setPadding(new javafx.geometry.Insets(10));

            chatArea.getChildren().add(imageContainer);

            // Scroll to bottom
            chatScrollPane.setVvalue(1.0);

        } catch (Exception e) {
            logger.severe("Error occurred while adding image to chat: " + e.getMessage());
        }
    }

}
