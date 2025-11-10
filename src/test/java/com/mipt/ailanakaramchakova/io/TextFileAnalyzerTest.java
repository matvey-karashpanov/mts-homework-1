package com.mipt.ailanakaramchakova.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;

class TextFileAnalyzerTest {

  @TempDir
  Path temporaryDir;

  @Test
  void testAnalyzeFile() throws IOException {
    TextFileAnalyzer analyzer = new TextFileAnalyzer();

    Path testFile = temporaryDir.resolve("test.txt");
    Files.write(testFile, Arrays.asList("Hello world!", "This is test."));

    TextFileAnalyzer.AnalysisResult result = analyzer.analyzeFile(testFile.toString());

    assertEquals(2, result.getLineCount());
    assertEquals(5, result.getWordCount());
    assertEquals(25, result.getCharCount());

    Map<Character, Integer> frequencies = result.getCharFrequency();
    assertTrue(frequencies.containsKey('H'));
    assertTrue(frequencies.containsKey('.'));
    assertEquals(1, (int) frequencies.get('H'));
    assertEquals(3, (int) frequencies.get(' '));
  }

  @Test
  void testSaveAnalysisResult() throws IOException {
    TextFileAnalyzer analyzer = new TextFileAnalyzer();

    TextFileAnalyzer.AnalysisResult result = new TextFileAnalyzer.AnalysisResult(2, 5, 20,
        Map.of('a', 3, 'b', 2));

    Path outputFile = temporaryDir.resolve("analysis.txt");
    analyzer.saveAnalysisResult(result, outputFile.toString());

    assertTrue(Files.exists(outputFile));
    assertTrue(Files.size(outputFile) > 0);

    String content = Files.readString(outputFile);
    assertTrue(content.contains("Lines: 2"));
    assertTrue(content.contains("Words: 5"));
    assertTrue(content.contains("Characters: 20"));
    assertTrue(content.contains("'a': 3"));
  }
}
