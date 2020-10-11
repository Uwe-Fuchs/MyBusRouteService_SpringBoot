package com.uwefuchs.demo.goeuro.fileprocessing;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.filefilter.WildcardFileFilter;

public final class FileOperationsHelper {
  private static final String FILE_PREFIX = "testdata";
  private static final String FILE_SUFFIX = ".tmp";

  public static void deleteTmpFiles() {
    final File dir = new File(System.getProperty("java.io.tmpdir"));
    final FileFilter fileFilter = new WildcardFileFilter(FILE_PREFIX + "*" + FILE_SUFFIX);

    File[] listFiles = dir.listFiles(fileFilter);

    if (listFiles != null) {
      Arrays.asList(listFiles).forEach(File::delete);
    }
  }

  public static String createTempDataFile(final List<String> data) throws IOException {
    final Path file = Files.createTempFile(FILE_PREFIX, FILE_SUFFIX);
    Files.write(file, data, StandardCharsets.UTF_8);

    return file.toString();
  }
}
