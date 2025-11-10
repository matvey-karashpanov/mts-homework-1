package com.mipt.ailanakaramchakova.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileProcessor {

  public List<Path> splitFile(String sourcePath, String outputDir, int partSize)
      throws IOException {
    Path source = Paths.get(sourcePath);
    String fileName = source.getFileName().toString();
    Path outputDirectory = Paths.get(outputDir);
    Files.createDirectories(outputDirectory);

    List<Path> parts = new ArrayList<>();

    try (FileChannel sourceChannel = FileChannel.open(source, StandardOpenOption.READ)) {
      long fileSize = sourceChannel.size();
      long position = 0;
      int partIndex = 1;

      while (position < fileSize) {
        Path partFile = outputDirectory.resolve(fileName + ".part" + partIndex);

        try (FileChannel partChannel = FileChannel.open(partFile, StandardOpenOption.CREATE,
            StandardOpenOption.WRITE)) {
          ByteBuffer buffer = ByteBuffer.allocate((int) Math.min(partSize, fileSize - position));
          sourceChannel.position(position);
          sourceChannel.read(buffer);
          buffer.flip();
          partChannel.write(buffer);
        }
        parts.add(partFile);
        position += partSize;
        partIndex++;
      }
    }
    return parts;
  }

  public void mergeFiles(List<Path> partPaths, String outputPath) throws IOException {
    Path output = Paths.get(outputPath);
    Files.createDirectories(output.getParent());

    try (FileChannel outChannel = FileChannel.open(output, StandardOpenOption.CREATE,
        StandardOpenOption.WRITE)) {
      for (Path part : partPaths) {
        if (!Files.exists(part)) {
          throw new IOException("Part file not found: " + part);
        }
        try (FileChannel inChannel = FileChannel.open(part, StandardOpenOption.READ)) {
          ByteBuffer buffer = ByteBuffer.allocate(8192);

          while (inChannel.read(buffer) != -1) {
            buffer.flip();
            outChannel.write(buffer);
            buffer.clear();
          }
        }
      }
    }
  }
}
