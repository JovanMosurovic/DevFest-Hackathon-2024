package com.devfesthackathon.devfesthackathon.app;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * A class that provides methods to interact with Google's Generative Language API (GEMINI).
 */
public class GeminiAPI {
    private static final Logger logger = Logger.getLogger(GeminiAPI.class.getName());

    private static final String API_KEY = "AIzaSyChQlLOjm8YKdbWQ6cv4sKcWLajxKqhK6s";
    private static final String BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/";

    private static final class Models {
        static final String TEXT = "gemini-1.0-pro";
        static final String VISION = "gemini-1.5-flash";
    }

    private static final class Config {
        static Map<String, Object> getDefaultGenerationConfig() {
            return Map.of(
                    "temperature", 0.9,
                    "topK", 1,
                    "topP", 1,
                    "maxOutputTokens", 2048,
                    "stopSequences", List.of()
            );
        }
    }

    public static String generateText(String prompt) {
        try {
            Map<String, Object> requestBody = createRequestBody(prompt);
            String endpoint = getEndpoint(Models.TEXT);
            return executeRequestAndExtractText(endpoint, requestBody);
        } catch (Exception e) {
            return handleError(e);
        }
    }

    public static String generateImageResponse(String prompt, String imagePath) {
        try {
            Map<String, Object> requestBody = createImageRequestBody(prompt, imagePath);
            String endpoint = getEndpoint(Models.VISION);
            return executeRequestAndExtractText(endpoint, requestBody);
        } catch (Exception e) {
            return handleError(e);
        }
    }

    private static Map<String, Object> createRequestBody(String prompt) {
        Map<String, Object> content = new HashMap<>();
        content.put("role", "user");
        content.put("parts", List.of(Map.of("text", prompt)));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", List.of(content));
        requestBody.put("generationConfig", Config.getDefaultGenerationConfig());

        return requestBody;
    }

    private static Map<String, Object> createImageRequestBody(String prompt, String imagePath) throws Exception {
        String base64Image = encodeImage(imagePath);

        Map<String, Object> content = new HashMap<>();
        content.put("role", "user");
        content.put("parts", List.of(
                Map.of("text", prompt),
                Map.of("inline_data", Map.of(
                        "mime_type", getMimeType(imagePath),
                        "data", base64Image
                ))
        ));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", List.of(content));
        requestBody.put("generationConfig", Config.getDefaultGenerationConfig());

        return requestBody;
    }

    private static String encodeImage(String imagePath) throws Exception {
        byte[] imageBytes = Files.readAllBytes(Path.of(imagePath));
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    private static String getEndpoint(String model) {
        return BASE_URL + model + ":generateContent?key=" + API_KEY;
    }

    private static String executeRequestAndExtractText(String endpoint, Map<String, Object> requestBody) throws Exception {
        String response = executeRequest(endpoint, requestBody);
        return extractTextFromResponse(response);
    }

    private static String executeRequest(String endpoint, Map<String, Object> requestBody) throws Exception {
        HttpRequestFactory requestFactory = new NetHttpTransport()
                .createRequestFactory(request ->
                        request.setParser(new JsonObjectParser(GsonFactory.getDefaultInstance())));

        HttpRequest request = requestFactory.buildPostRequest(
                new GenericUrl(endpoint),
                new JsonHttpContent(GsonFactory.getDefaultInstance(), requestBody)
        );

        return request.execute().parseAsString();
    }

    private static String extractTextFromResponse(String jsonResponse) {
        JsonObject response = JsonParser.parseString(jsonResponse).getAsJsonObject();
        if (!response.has("candidates") || response.getAsJsonArray("candidates").isEmpty()) {
            return "No response found";
        }

        JsonObject candidate = response.getAsJsonArray("candidates").get(0).getAsJsonObject();
        if (!candidate.has("content")) {
            return "No content found";
        }

        JsonObject content = candidate.getAsJsonObject("content");
        if (!content.has("parts") || content.getAsJsonArray("parts").isEmpty()) {
            return "No parts found";
        }

        return content.getAsJsonArray("parts")
                .get(0).getAsJsonObject()
                .get("text").getAsString();
    }

    private static String getMimeType(String filePath) {
        String extension = filePath.substring(filePath.lastIndexOf('.') + 1).toLowerCase();
        return switch (extension) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "webp" -> "image/webp";
            default -> "image/jpeg";
        };
    }

    private static String handleError(Exception e) {
        logger.severe(e.getMessage());
        return "Error: " + e.getMessage();
    }

    public static void main(String[] args) {
        // Test of text generation
        String textResponse = generateText("What is a healthy crop? Explain it in bullet points.");
        System.out.println("Text Response: " + textResponse);

        // Test of image analysis
        String imagePath = "src/main/resources/images/test_image.jpg";
        String imageResponse = generateImageResponse(
                "Do you see healthy or unhealthy crops on this image and why?",
                imagePath
        );
        System.out.println("Image Analysis: " + imageResponse);
    }
}