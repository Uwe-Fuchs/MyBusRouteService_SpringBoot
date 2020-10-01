package com.uwefuchs.demo.goeuro.fileprocessing;

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

  /**
   * reads the data from the file with given pathname.
   *
   * @param pathname the pathname to the data-file.
   */
  public static Map<Integer, Map<Integer, Integer>> createBusRouteDataCache(final String pathname) {
    Validate.notBlank(pathname, "path-name mus not be blank!");

    try (final Scanner scanner = new Scanner(Paths.get(pathname))) {
      BusRouteDataValidationUtil.assertFileNotEmpty(scanner, pathname);
      final int numberOfBusRoutes = scanner.nextInt();
      final Map<Integer, Map<Integer, Integer>> dataMap = new ConcurrentHashMap<>(numberOfBusRoutes);

      while (scanner.hasNextLine()) {
        processSingleLine(scanner.nextLine(), dataMap);
      }

      BusRouteDataValidationUtil.assertNumberOfBusRoutesWithinBounds(dataMap);
      BusRouteDataValidationUtil.assertNumberOfBusRoutesAsAnnounced(dataMap, numberOfBusRoutes);

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
      BusRouteDataValidationUtil.assertUniqueBusRouteIds(dataMap, busRouteId);

      final Map<Integer, Integer> busRouteMap = new HashMap<>();

      for (int counter = 0; scanner.hasNext(); counter++) {
        final int stationId = scanner.nextInt();
        BusRouteDataValidationUtil.assertUniqueStationIdsInBusRoute(busRouteMap, stationId, busRouteId);
        busRouteMap.put(stationId, counter);
      }

      BusRouteDataValidationUtil.assertNumberOfStationsWithinBounds(busRouteMap, busRouteId);

      dataMap.put(busRouteId, busRouteMap);
    }
  }
}
