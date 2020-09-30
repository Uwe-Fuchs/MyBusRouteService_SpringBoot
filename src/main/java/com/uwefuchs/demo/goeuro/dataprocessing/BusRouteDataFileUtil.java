package com.uwefuchs.demo.goeuro.dataprocessing;

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
   * reads the data in the file with given pathname. Caches the data into the given {@link Map}.
   *
   * @param pathname the pathname to the data-file.
   */
  public static Map<Integer, Map<Integer, Integer>> readAndCacheBusRouteData(final String pathname) {
    Validate.notBlank(pathname, "path-name mus not be blank!");

    try (final Scanner scanner = new Scanner(Paths.get(pathname))) {
      DataValidationUtil.assertFileNotEmpty(scanner, pathname);
      final int numberOfBusRoutes = scanner.nextInt();
      final Map<Integer, Map<Integer, Integer>> dataMap = new ConcurrentHashMap<>(numberOfBusRoutes);

      while (scanner.hasNextLine()) {
        processSingleLine(scanner.nextLine(), dataMap);
      }

      DataValidationUtil.assertNumberOfBusRoutesWithinBounds(dataMap);
      DataValidationUtil.assertNumberOfBusRoutesAsAnnounced(dataMap, numberOfBusRoutes);

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
      DataValidationUtil.assertUniqueBusRouteIds(dataMap, busRouteId);

      final Map<Integer, Integer> busRouteMap = new HashMap<>();

      for (int counter = 0; scanner.hasNext(); counter++) {
        final int stationId = scanner.nextInt();
        DataValidationUtil.assertUniqueStationIdsInBusRoute(busRouteMap, stationId, busRouteId);
        busRouteMap.put(stationId, counter);
      }

      DataValidationUtil.assertNumberOfStationsWithinBounds(busRouteMap, busRouteId);

      dataMap.put(busRouteId, busRouteMap);
    }
  }
}
