package com.uwefuchs.demo.goeuro;

import com.uwefuchs.demo.goeuro.model.domain.BusRoute;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.junit.After;

public abstract class AbstractBusRouteChallengeTest {

  @After
  public void doAfter() throws IOException {
    final File dir = new File(System.getProperty("java.io.tmpdir"));
    final FileFilter fileFilter = new WildcardFileFilter("testdata*.tmp");
    Arrays.asList(dir.listFiles(fileFilter))
        .forEach(File::delete);
  }

  public static String createTempDataFile(final List<String> data) throws IOException {
    final Path file = Files.createTempFile("testdata", ".tmp");
    Files.write(file, data, StandardCharsets.UTF_8);

    return file.toString();
  }

  public static String generateBusRoute(final long routeId, final int count) {
    final StringBuilder sb = new StringBuilder()
        .append(routeId)
        .append(" ");

    for (int i = 0; i < count; i++) {
      sb.append(i);
      sb.append(" ");
    }

    return sb.toString().trim();
  }

  public static List<String> generateListOfBusRoutes(final long routesCount, final int stationsCount) {
    final List<String> routesList = new ArrayList<>();

    routesList.add(Long.toString(routesCount));

    for (long tmpCounter = 1; tmpCounter <= routesCount; tmpCounter += 1) {
      routesList.add(generateBusRoute(tmpCounter, stationsCount));
    }

    return routesList;
  }

  public static BusRoute generateBusRouteObject(final Long routeId, final int count) {
    final List<Integer> stationIds = new ArrayList<>();

    for (int i = 0; i < count; i++) {
      stationIds.add(i);
    }

    return new BusRoute(routeId, stationIds);
  }

  public static List<BusRoute> generateBusRoutesList(final long routesCount, final int stationsCount) {
    final List<BusRoute> routesList = new ArrayList<>();

    for (long l = 1; l <= routesCount; l += 1) {
      final BusRoute route = generateBusRouteObject(routesCount, stationsCount);
    }

    return routesList;
  }
}
