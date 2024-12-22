package com.devfesthackathon.devfesthackathon.app.util;

import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;
import java.util.List;

public class MarkdownParser {

    private static class TextSegment {
        String text;
        boolean isBold;
        boolean isItalic;
        boolean isCode;

        TextSegment(String text, boolean isBold, boolean isItalic, boolean isCode) {
            this.text = text;
            this.isBold = isBold;
            this.isItalic = isItalic;
            this.isCode = isCode;
        }
    }

    public static Node parseMarkdownToText(String markdown) {
        TextFlow textFlow = new TextFlow();
        List<TextSegment> segments = parseToSegments(markdown);

        for (TextSegment segment : segments) {
            Text text = new Text(segment.text);

            if (segment.isBold) {
                text.getStyleClass().add("markdown-bold");
            }
            if (segment.isItalic) {
                text.getStyleClass().add("markdown-italic");
            }
            if (segment.isCode) {
                text.getStyleClass().add("markdown-code");
            }

            textFlow.getChildren().add(text);
        }

        return textFlow;
    }

    private static List<TextSegment> parseToSegments(String markdown) {
        List<TextSegment> segments = new ArrayList<>();
        StringBuilder currentText = new StringBuilder();
        boolean isBold = false;
        boolean isItalic = false;
        boolean isCode = false;

        for (int i = 0; i < markdown.length(); i++) {
            char c = markdown.charAt(i);

            if (c == '*' || c == '_' || c == '`') {
                // Check for bold (** or __)
                if (i < markdown.length() - 1 && (c == '*' || c == '_') &&
                        markdown.charAt(i + 1) == c) {
                    if (!currentText.isEmpty()) {
                        segments.add(new TextSegment(currentText.toString(), isBold, isItalic, isCode));
                        currentText.setLength(0);
                    }
                    isBold = !isBold;
                    i++;
                    continue;
                }

                // Check for italic (* or _)
                if (c == '*' || c == '_') {
                    if (!currentText.isEmpty()) {
                        segments.add(new TextSegment(currentText.toString(), isBold, isItalic, isCode));
                        currentText.setLength(0);
                    }
                    isItalic = !isItalic;
                    continue;
                }

                // Check for code (`)
                if (!currentText.isEmpty()) {
                    segments.add(new TextSegment(currentText.toString(), isBold, isItalic, isCode));
                    currentText.setLength(0);
                }
                isCode = !isCode;
                continue;
            }

            currentText.append(c);
        }

        if (!currentText.isEmpty()) {
            segments.add(new TextSegment(currentText.toString(), isBold, isItalic, isCode));
        }

        return segments;
    }

}
