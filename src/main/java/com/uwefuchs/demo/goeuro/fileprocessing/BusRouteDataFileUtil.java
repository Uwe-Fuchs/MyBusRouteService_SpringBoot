package com.uwefuchs.demo.goeuro.fileprocessing;

import com.uwefuchs.demo.goeuro.exceptions.DataConstraintViolationException;
import com.uwefuchs.demo.goeuro.exceptions.InconsistentDataException;
import com.uwefuchs.demo.goeuro.model.domain.BusRoute;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.apache.commons.lang3.Validate;

/**
 * reads bus-route-data from a given file. Caches the data into a given {@link Map}.
 *
 * @author info@uwefuchs.com
 */
public class BusRouteDataFileUtil {

  public static final int MIN_NUMBER_OF_BUS_ROUTES = 1;
  public static final long MAX_NUMBER_OF_BUS_ROUTES = 100000;
  public static final long MAX_NUMBER_OF_STATIONS = 1000000;
  public static final int MAX_NUMBER_OF_STATIONS_PER_ROUTE = 1000;
  public static final int MIN_NUMBER_OF_STATIONS_PER_ROUTE = 2;

  /**
   * reads the data from the file with given pathname.
   *
   * @param pathname the pathname to the data-file.
   */
  public static List<BusRoute> createBusRouteDataCache(final String pathname) {
    Validate.notBlank(pathname, "path-name mus not be blank!");

    try (final Scanner scanner = new Scanner(Paths.get(pathname))) {
      if (!scanner.hasNext()) {
        throw new InconsistentDataException(String.format("No data found in given file [%s]", pathname));
      }

      final int numberOfBusRoutes = scanner.nextInt();
      final List<BusRoute> busRouteList = new ArrayList<>(numberOfBusRoutes);

      while (scanner.hasNextLine()) {
        processSingleLine(scanner.nextLine(), busRouteList);
      }

      assertNumberOfBusRoutesWithinBounds(busRouteList.size());
      assertNumberOfBusRoutesAsAnnounced(busRouteList, numberOfBusRoutes);

      return busRouteList;
    } catch (final java.io.IOException ex) {
      throw new com.uwefuchs.demo.goeuro.exceptions.IOException("IOException when reading data-file!", ex);
    } catch (final InputMismatchException ex) {
      throw new InconsistentDataException("InputMismatchException when reading data-file!", ex);
    }
  }

  private static void processSingleLine(final String aLine, final List<BusRoute> busRouteList) {
    // use a second Scanner to parse the content of each line
    try (final Scanner scanner = new Scanner(aLine)) {
      scanner.useDelimiter(" ");

      if (!scanner.hasNext()) {
        return;
      }

      final long busRouteId = scanner.nextLong();
      assertUniqueBusRouteId(busRouteList, busRouteId);

      final List<Integer> stationIdList = new ArrayList<>();

      while (scanner.hasNext()) {
        final int stationId = scanner.nextInt();
        assertUniqueStationId(stationIdList, stationId);
        stationIdList.add(stationId);
      }

      assertStationsPerRouteWithinBounds(stationIdList.size());
      busRouteList.add(new BusRoute(busRouteId, stationIdList));
    }
  }

  private static void assertNumberOfBusRoutesWithinBounds(final int numberOfBusRoutes) {
    if (numberOfBusRoutes < MIN_NUMBER_OF_BUS_ROUTES || numberOfBusRoutes > MAX_NUMBER_OF_BUS_ROUTES) {
      throw new DataConstraintViolationException(
          String.format("Number of bus-routes not within bounds. Minimum is %d, maximum is %d, given was %d",
              MIN_NUMBER_OF_BUS_ROUTES, MAX_NUMBER_OF_BUS_ROUTES, numberOfBusRoutes));
    }
  }

  private static void assertNumberOfBusRoutesAsAnnounced(final List<BusRoute> busRouteList,
      final int numberOfBusRoutes) {
    if (numberOfBusRoutes != busRouteList.size()) {
      throw new InconsistentDataException(
          String.format("Real number [%d] of bus-routes differs from announced number [%d]", busRouteList.size(),
              numberOfBusRoutes));
    }
  }

  private static void assertStationsPerRouteWithinBounds(final int numberOfStations) {
    if (numberOfStations < MIN_NUMBER_OF_STATIONS_PER_ROUTE || numberOfStations > MAX_NUMBER_OF_STATIONS_PER_ROUTE) {
      throw new DataConstraintViolationException(
          String.format("Number of stations not within bounds. Minimum is %d, maximum is %d, given was %d",
              MIN_NUMBER_OF_STATIONS_PER_ROUTE, MAX_NUMBER_OF_STATIONS_PER_ROUTE, numberOfStations));
    }
  }

  private static void assertUniqueStationId(final List<Integer> stationIdList, final int stationId) {
    if (stationIdList.contains(stationId)) {
      throw new DataConstraintViolationException(
          String.format("double occurrence of station-id [%d]", stationId));
    }
  }

  private static void assertUniqueBusRouteId(final List<BusRoute> busRouteList, final long busRouteId) {
    busRouteList.forEach(b -> {
      if (b.getBusRouteId().equals(busRouteId)) {
        throw new DataConstraintViolationException(
            String.format("Number of stations not within bounds. Minimum is %d, maximum is %d, given was %d",
                MIN_NUMBER_OF_STATIONS_PER_ROUTE, MAX_NUMBER_OF_STATIONS_PER_ROUTE, busRouteId));
      }
    });
  }
}
