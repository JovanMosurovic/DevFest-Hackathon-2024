package com.devfesthackathon.devfesthackathon.app;

import com.microsoft.azure.cognitiveservices.vision.computervision.*;
import com.microsoft.azure.cognitiveservices.vision.computervision.models.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class ComputerVisionTest {

    private final ComputerVisionClient client;

    public ComputerVisionTest(String endpoint, String key) {
        this.client = ComputerVisionManager.authenticate(key).withEndpoint(endpoint);
    }

//    public void analyzeImageFromUrl(String imageUrl) {
//        try {
//            ImageAnalysis analysis = client.computerVision().analyzeImage().withUrl(imageUrl)
//                    .withVisualFeatures(List.of(VisualFeatureTypes.CATEGORIES, VisualFeatureTypes.DESCRIPTION,
//                            VisualFeatureTypes.TAGS, VisualFeatureTypes.OBJECTS, VisualFeatureTypes.ADULT)).execute();
//
//            printAnalysis(analysis);
//
//        } catch (Exception e) {
//            System.err.println("Error analyzing image from URL: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }

    public void analyzeImageFromFile(String imagePath) {
        try (FileInputStream inputStream = new FileInputStream(new File(imagePath))) {
            byte[] imageBytes = inputStream.readAllBytes();

            ImageAnalysis analysis = client.computerVision().analyzeImageInStream().withImage(imageBytes)
                    .withVisualFeatures(List.of(VisualFeatureTypes.CATEGORIES, VisualFeatureTypes.DESCRIPTION,
                            VisualFeatureTypes.TAGS, VisualFeatureTypes.OBJECTS, VisualFeatureTypes.ADULT,VisualFeatureTypes.BRANDS)).execute();

            printAnalysis(analysis);

        } catch (IOException e) {
            System.err.println("Error reading image file: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error analyzing image from file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void printAnalysis(ImageAnalysis analysis) {
        if (analysis == null) {
            System.out.println("No analysis results received.");
            return;
        }

        System.out.println("Categories:");
        if (analysis.categories() != null) {
            for (Category category : analysis.categories()) {
                System.out.println("- " + category.name() + " (score: " + category.score() + ")");
            }
        }

        System.out.println("\nDescription:");
        if (analysis.description() != null && analysis.description().captions() != null) {
            for (ImageCaption caption : analysis.description().captions()) {
                System.out.println("- " + caption.text() + " (confidence: " + caption.confidence() + ")");
            }
        }

        System.out.println("\nTags:");
        if (analysis.tags() != null) {
            for (ImageTag tag : analysis.tags()) {
                System.out.println("- " + tag.name() + " (confidence: " + tag.confidence() + ")");
            }
        }
        System.out.println("\nObjects:");
        if (analysis.objects() != null) {
            for (DetectedObject detectedObject : analysis.objects()) {
                System.out.println("- " + detectedObject.objectProperty() + " (confidence: " + detectedObject.confidence() + ")");
            }
        }
        System.out.println("\nObjects:");
        if (analysis.brands() != null) {
            for (DetectedBrand detectedObject : analysis.brands()) {
                System.out.println("- " + detectedObject.name() + " (confidence: " + detectedObject.confidence() + ")");
            }
        }
        System.out.println("\nAdult:");
        if (analysis.adult() != null) {
            System.out.println("Is Adult Content: " + analysis.adult().isAdultContent());
            System.out.println("Adult Score: " + analysis.adult().adultScore());
            System.out.println("Is Racy Content: " + analysis.adult().isRacyContent());
            System.out.println("Racy Score: " + analysis.adult().racyScore());


        }
        System.out.println("\n------------------------------------");
    }

    public static void main(String[] args) {

        String endpoint = "https://hakaton.cognitiveservices.azure.com/";
        String key = "CGtgpWkuljsHnZXKlY0u1jC682Q1f4Qm8pyamMK4Rg9GfV8OYWVbJQQJ99ALAC5RqLJXJ3w3AAAFACOG9re3";

        if (endpoint == null || key == null) {
            System.err.println("Error: COMPUTER_VISION_ENDPOINT and COMPUTER_VISION_KEY environment variables must be set.");
            return;
        }

        ComputerVisionTest tester = new ComputerVisionTest(endpoint, key);


        String imagePath = "D:\\DevFEst\\cum\\src\\main\\resources\\images\\test_image.png";
        tester.analyzeImageFromFile(imagePath);
    }
}