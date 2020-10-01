package com.uwefuchs.demo.goeuro.fileprocessing;

import com.uwefuchs.demo.goeuro.exceptions.DataContraintViolationException;
import com.uwefuchs.demo.goeuro.exceptions.InconsistentDataException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.Validate;

/**
 * reads bus-route-data from a given file. Caches the data into a given {@link Map}.
 *
 * @author info@uwefuchs.com
 */
public class BusRouteDataFileUtil {

  public static final int MIN_NUMBER_OF_BUS_ROUTES = 1;
  public static final long MAX_NUMBER_OF_BUS_ROUTES = 100000L;
  public static final long MAX_NUMBER_OF_STATIONS = 1000000L;
  public static final int MAX_NUMBER_OF_STATIONS_PER_ROUTE = 1000;
  public static final int MIN_NUMBER_OF_STATIONS_PER_ROUTE = 2;

  /**
   * reads the data from the file with given pathname.
   *
   * @param pathname the pathname to the data-file.
   */
  public static Map<Integer, Map<Integer, Integer>> createBusRouteDataCache(final String pathname) {
    Validate.notBlank(pathname, "path-name mus not be blank!");

    try (final Scanner scanner = new Scanner(Paths.get(pathname))) {
      if (!scanner.hasNext()) {
        throw new InconsistentDataException(String.format("No data found in given file [%s]", pathname));
      }

      final int numberOfBusRoutes = scanner.nextInt();
      final Map<Integer, Map<Integer, Integer>> dataMap = new ConcurrentHashMap<>(numberOfBusRoutes);

      while (scanner.hasNextLine()) {
        processSingleLine(scanner.nextLine(), dataMap);
      }

      assertNumberOfBusRoutesWithinBounds(dataMap.size());
      assertNumberOfBusRoutesAsAnnounced(dataMap, numberOfBusRoutes);

      return dataMap;
    } catch (final java.io.IOException ex) {
      throw new com.uwefuchs.demo.goeuro.exceptions.IOException("IOException when reading data-file!", ex);
    } catch (final InputMismatchException ex) {
      throw new InconsistentDataException("InputMismatchException when reading data-file!", ex);
    }
  }

  private static void processSingleLine(final String aLine, final Map<Integer, Map<Integer, Integer>> dataMap) {
    // use a second Scanner to parse the content of each line
    try (final Scanner scanner = new Scanner(aLine)) {
      scanner.useDelimiter(" ");

      if (!scanner.hasNext()) {
        return;
      }

      final int busRouteId = scanner.nextInt();
      if (dataMap.containsKey(busRouteId)) {
        throw new InconsistentDataException(String.format("non-unique bus-route-id: [%d]", busRouteId));
      }

      final Map<Integer, Integer> busRouteMap = new HashMap<>();

      for (int counter = 0; scanner.hasNext(); counter++) {
        final int stationId = scanner.nextInt();
        if (busRouteMap.containsKey(stationId)) {
          throw new InconsistentDataException(
              String.format("double occurrence of station-id [%d] in bus-route [%d]", stationId, busRouteId));
        }

        busRouteMap.put(stationId, counter);
      }

      assertStationsPerRouteWithinBounds(busRouteMap.size());

      dataMap.put(busRouteId, busRouteMap);
    }
  }

  private static void assertNumberOfBusRoutesWithinBounds(final int numberOfBusRoutes) {
    if (numberOfBusRoutes < MIN_NUMBER_OF_BUS_ROUTES || numberOfBusRoutes > MAX_NUMBER_OF_BUS_ROUTES) {
      throw new DataContraintViolationException(
          String.format("Number of bus-routes not within bounds. Minimum is %d, maximum is %d, given was %d",
              MIN_NUMBER_OF_BUS_ROUTES, MAX_NUMBER_OF_BUS_ROUTES, numberOfBusRoutes));
    }
  }

  private static void assertNumberOfBusRoutesAsAnnounced(final Map<Integer, Map<Integer, Integer>> dataMap,
      final int numberOfBusRoutes) {
    if (numberOfBusRoutes != dataMap.size()) {
      throw new InconsistentDataException(
          String.format("Real number [%d] of bus-routes differs from announced number [%d]", dataMap.size(),
              numberOfBusRoutes));
    }
  }

  private static void assertStationsPerRouteWithinBounds(final int numberOfStations) {
    if (numberOfStations < MIN_NUMBER_OF_STATIONS_PER_ROUTE || numberOfStations > MAX_NUMBER_OF_STATIONS_PER_ROUTE) {
      throw new DataContraintViolationException(
          String.format("Number of stations not within bounds. Minimum is %d, maximum is %d, given was %d",
              MIN_NUMBER_OF_STATIONS_PER_ROUTE, MAX_NUMBER_OF_STATIONS_PER_ROUTE, numberOfStations));
    }
  }
}
