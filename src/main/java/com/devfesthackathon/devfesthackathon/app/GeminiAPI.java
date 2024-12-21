package com.devfesthackathon.devfesthackathon.app;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class GeminiAPI {

    private static final String API_KEY = "AIzaSyChQlLOjm8YKdbWQ6cv4sKcWLajxKqhK6s"; // Replace with your actual API key!
    private static final String TEXT_ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash-exp:generateContent";
    private static final String VISION_ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash-exp-vision:generateContent";
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    public static String generateTextContent(String prompt) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            Map<String, Object> contents = new HashMap<>();
            Map<String, String> parts = new HashMap<>();

            parts.put("text", prompt);
            contents.put("parts", new Object[]{parts});
            requestBody.put("contents", contents);
            String jsonResponse = executeRequest(TEXT_ENDPOINT, requestBody);

            JsonObject response = JsonParser.parseString(jsonResponse).getAsJsonObject();

            if (response.has("candidates") && !response.getAsJsonArray("candidates").isEmpty()) {
                JsonObject candidate = response.getAsJsonArray("candidates").get(0).getAsJsonObject();
                if (candidate.has("content")) {
                    JsonObject content = candidate.getAsJsonObject("content");
                    if (content.has("parts") && !content.getAsJsonArray("parts").isEmpty()) {
                        JsonObject part = content.getAsJsonArray("parts").get(0).getAsJsonObject();
                        if (part.has("text")) {
                            return part.get("text").getAsString();
                        }
                    }
                }
            }
            return "No text response found.";
        } catch (Exception e) {
            System.err.println("Error generating text: " + e.getMessage());
            return "Error generating text: " + e.getMessage();
        }
    }

    public static String generateImageContent(String prompt, String imagePath) {
        try {
            File imageFile = new File(imagePath);
            if (!imageFile.exists()) {
                throw new Exception("Image file not found: " + imagePath);
            }

            byte[] imageBytes = Files.readAllBytes(Path.of(imagePath));
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            Map<String, Object> requestBody = new HashMap<>();
            Map<String, Object> contents = new HashMap<>();
            Map<String, Object> textPart = new HashMap<>();
            Map<String, Object> imagePart = new HashMap<>();
            Map<String, String> imageData = new HashMap<>();

            textPart.put("text", prompt);

            String mimeType = getMimeType(imagePath);
            System.out.println(mimeType);
            imageData.put("mime_type", mimeType);
            imageData.put("data", base64Image);
            imagePart.put("inline_data", imageData);

            contents.put("parts", new Object[]{textPart, imagePart});
            requestBody.put("contents", contents);

            String jsonResponse = executeRequest(VISION_ENDPOINT, requestBody);

            JsonObject response = JsonParser.parseString(jsonResponse).getAsJsonObject();
            if (response.has("candidates") && !response.getAsJsonArray("candidates").isEmpty()) {
                JsonObject candidate = response.getAsJsonArray("candidates").get(0).getAsJsonObject();
                if (candidate.has("content")) {
                    JsonObject content = candidate.getAsJsonObject("content");
                    if (content.has("parts") && !content.getAsJsonArray("parts").isEmpty()) {
                        JsonObject part = content.getAsJsonArray("parts").get(0).getAsJsonObject();
                        if (part.has("text")) {
                            return part.get("text").getAsString();
                        }
                    }
                }
            }
            return "No image analysis response found."; // More specific message
        } catch (Exception e) {
            System.err.println("Error analyzing image: " + e.getMessage());
            return "Error analyzing image: " + e.getMessage();
        }
    }

    private static String getMimeType(String filePath) {
        String extension = filePath.substring(filePath.lastIndexOf('.') + 1).toLowerCase();
        return switch (extension) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "webp" -> "image/webp";
            default -> "image/jpeg"; // Default to JPEG if unknown
        };
    }

    private static String executeRequest(String endpoint, Map<String, Object> requestBody) throws Exception {
        HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(
                request -> request.setParser(new JsonObjectParser(JSON_FACTORY)));

        HttpRequest request = requestFactory.buildPostRequest(
                new GenericUrl(endpoint + "?key=" + API_KEY),
                new JsonHttpContent(JSON_FACTORY, requestBody));

        return request.execute().parseAsString();
    }

    public static void main(String[] args) {
//        System.out.println("Generating text response...");
//        String textResponse = generateTextContent("What is your version?");
//
//        System.out.println("Text Response:\n" + textResponse);

        System.out.println("\nAnalyzing image...");
        String imagePath = "D:\\DevFEst\\cum\\src\\main\\resources\\images\\test_image.jpg"; // Use a more robust way to get the path
        String imageResponse = generateImageContent(
                "What's in this image?",
                imagePath
        );

        System.out.println("Image Analysis:\n" + imageResponse);
    }
}