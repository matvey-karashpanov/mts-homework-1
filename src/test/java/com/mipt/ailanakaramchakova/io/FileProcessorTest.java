package com.mipt.ailanakaramchakova.io;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

class FileProcessorTest {

  @TempDir
  Path temporaryDir;

  @Test
  void testSplitAndMergeFile() throws IOException {
    FileProcessor processor = new FileProcessor();

    Path testFile = temporaryDir.resolve("test.dat");
    byte[] testData = new byte[1500];
    new Random().nextBytes(testData);
    Files.write(testFile, testData);

    String outputDir = temporaryDir.resolve("parts").toString();
    List<Path> parts = processor.splitFile(testFile.toString(), outputDir, 500);

    assertEquals(3, parts.size());
    for (Path part : parts) {
      assertTrue(Files.exists(part));
    }

    Path mergedFile = temporaryDir.resolve("merged.dat");
    processor.mergeFiles(parts, mergedFile.toString());

    assertArrayEquals(Files.readAllBytes(testFile), Files.readAllBytes(mergedFile));
  }
}
