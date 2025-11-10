package com.mipt.ailanakaramchakova.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TextFileAnalyzer {

  public static class AnalysisResult {

    private final long lineCount;
    private final long wordCount;
    private final long charCount;
    private final Map<Character, Integer> charFrequency;

    public AnalysisResult(long lineCount, long wordCount, long charCount,
        Map<Character, Integer> charFrequency) {
      this.lineCount = lineCount;
      this.wordCount = wordCount;
      this.charCount = charCount;
      this.charFrequency = new HashMap<>(charFrequency);
    }

    public long getLineCount() {
      return lineCount;
    }

    public long getWordCount() {
      return wordCount;
    }

    public long getCharCount() {
      return charCount;
    }

    public Map<Character, Integer> getCharFrequency() {
      return new HashMap<>(charFrequency);
    }

    @Override
    public String toString() {
      return "AnalysisResult{" +
          "lineCount=" + lineCount +
          ", wordCount=" + wordCount +
          ", charCount=" + charCount +
          ", charFrequency=" + charFrequency +
          '}';
    }
  }

  public AnalysisResult analyzeFile(String filePath) throws IOException {
    long lineCount = 0;
    long wordCount = 0;
    long charCount = 0;
    Map<Character, Integer> charFrequency = new HashMap<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = reader.readLine()) != null) {
        lineCount++;
        charCount += line.length();

        for (char c : line.toCharArray()) {
          charFrequency.merge(c, 1, Integer::sum);
        }

        String[] words = line.trim().split("\\s+");
        if (!line.trim().isEmpty()) {
          wordCount += words.length;
        }
      }
    }

    return new AnalysisResult(lineCount, wordCount, charCount, charFrequency);
  }

  public void saveAnalysisResult(AnalysisResult result, String outputPath) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
      writer.write("Lines: " + result.getLineCount());
      writer.newLine();
      writer.write("Words: " + result.getWordCount());
      writer.newLine();
      writer.write("Characters: " + result.getCharCount());
      writer.newLine();
      writer.write("Character Frequencies:");
      writer.newLine();

      for (Map.Entry<Character, Integer> entry : result.getCharFrequency().entrySet()) {
        writer.write("'" + entry.getKey() + "': " + entry.getValue());
        writer.newLine();
      }
    }
  }
}
