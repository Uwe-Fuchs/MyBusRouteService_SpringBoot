package com.uwefuchs.demo.goeuro.fileprocessing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static com.uwefuchs.demo.goeuro.FileBasedTestHelper.*;

import com.uwefuchs.demo.goeuro.exceptions.DataContraintViolationException;
import com.uwefuchs.demo.goeuro.exceptions.InconsistentDataException;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.junit.After;
import org.junit.Test;

public class BusRouteDataFileUtilTest {

  @After
  public void doAfter() throws IOException {
    final File dir = new File(System.getProperty("java.io.tmpdir"));
    final FileFilter fileFilter = new WildcardFileFilter("testdata*.tmp");
    Arrays.asList(dir.listFiles(fileFilter))
        .forEach(File::delete);
  }

  @Test
  public void testReadAndCacheBusRouteData()
      throws IOException {
    String pathname = createTempDataFile(Arrays.asList("2", "0 0 1 2 3 4", "1 3 1 6 5"));
    Map<Integer, Map<Integer, Integer>> dataMap = BusRouteDataFileUtil.createBusRouteDataCache(pathname);
    assertEquals(dataMap.size(), 2);

    Map<Integer, Integer> busRoute = dataMap.get(0);
    assertEquals(busRoute.size(), 5);

    busRoute = dataMap.get(1);
    assertEquals(busRoute.size(), 4);
    assertEquals(busRoute.get(6), Integer.valueOf(2));
    assertNull(busRoute.get(7));

    pathname = createTempDataFile(Arrays.asList("3", "0 0 1 2 3 4", "1 3 1 6 5", "2 0 6 4"));
    dataMap = BusRouteDataFileUtil.createBusRouteDataCache(pathname);
    busRoute = dataMap.get(2);
    assertEquals(dataMap.size(), 3);
    assertEquals(busRoute.get(0), Integer.valueOf(0));
  }

  @Test
  public void testProcessSingleLine()
      throws IOException {
    final String pathname = createTempDataFile(Arrays.asList("1", "0 0 1 2 3 4"));
    final Map<Integer, Map<Integer, Integer>> dataMap = BusRouteDataFileUtil.createBusRouteDataCache(pathname);

    assertEquals(dataMap.size(), 1);

    final Map<Integer, Integer> busRoute = dataMap.get(0);
    assertEquals(busRoute.size(), 5);

    for (int lineId = 0; lineId < busRoute.size(); lineId++) {
      assertEquals(busRoute.get(lineId), Integer.valueOf(lineId));
    }
  }

  @Test(expected = NullPointerException.class)
  public void testWithoutPathname() {
    BusRouteDataFileUtil.createBusRouteDataCache(null);
  }

  @Test(expected = com.uwefuchs.demo.goeuro.exceptions.IOException.class)
  public void testWithInvalidPathname() {
    BusRouteDataFileUtil.createBusRouteDataCache("someInvalidPathName");
  }

  @Test(expected = DataContraintViolationException.class)
  public void testWithoutStations()
      throws IOException {
    final String pathname = createTempDataFile(Arrays.asList("1", "1"));
    BusRouteDataFileUtil.createBusRouteDataCache(pathname);
  }

  @Test(expected = InconsistentDataException.class)
  public void testWithNonUniqueBusRouteIds()
      throws IOException {
    String testFile = createTempDataFile(Arrays.asList("2", "1 1 2 3 4 5", "2 6 7 8 9 10"));
    final Map<Integer, Map<Integer, Integer>> dataMap = BusRouteDataFileUtil.createBusRouteDataCache(testFile);
    assertEquals(2, dataMap.size());

    testFile = createTempDataFile(Arrays.asList("2", "1 1 2 3 4 5", "1 6 7 8 9 10"));
    BusRouteDataFileUtil.createBusRouteDataCache(testFile);
  }

  @Test(expected = InconsistentDataException.class)
  public void testWithNonUniqueStationIds()
      throws IOException {
    String testFile = createTempDataFile(Arrays.asList("1", "0 0 1 2 3 4"));
    final Map<Integer, Map<Integer, Integer>> dataMap = BusRouteDataFileUtil.createBusRouteDataCache(testFile);
    assertEquals(1, dataMap.size());

    testFile = createTempDataFile(Arrays.asList("1", "0 0 1 1 3 4"));
    BusRouteDataFileUtil.createBusRouteDataCache(testFile);
  }

  @Test(expected = DataContraintViolationException.class)
  public void testTooManyStations()
      throws IOException {
    String testLine = generateBusRoute(1, BusRouteDataFileUtil.MAX_NUMBER_OF_STATIONS_PER_ROUTE);
    String pathname = createTempDataFile(Arrays.asList("1", testLine));
    final Map<Integer, Map<Integer, Integer>> dataMap = BusRouteDataFileUtil.createBusRouteDataCache(pathname);

    final Map<Integer, Integer> busRoute = dataMap.get(1);
    assertEquals(busRoute.size(), BusRouteDataFileUtil.MAX_NUMBER_OF_STATIONS_PER_ROUTE);

    testLine = generateBusRoute(1, BusRouteDataFileUtil.MAX_NUMBER_OF_STATIONS_PER_ROUTE + 1);
    pathname = createTempDataFile(Arrays.asList("1", testLine));
    BusRouteDataFileUtil.createBusRouteDataCache(pathname);
  }

  @Test(expected = DataContraintViolationException.class)
  public void testTooLessStations()
      throws IOException {
    String testLine = generateBusRoute(1, BusRouteDataFileUtil.MIN_NUMBER_OF_STATIONS_PER_ROUTE);
    String pathname = createTempDataFile(Arrays.asList("1", testLine));
    final Map<Integer, Map<Integer, Integer>> dataMap = BusRouteDataFileUtil.createBusRouteDataCache(pathname);

    final Map<Integer, Integer> busRoute = dataMap.get(1);
    assertEquals(busRoute.size(), BusRouteDataFileUtil.MIN_NUMBER_OF_STATIONS_PER_ROUTE);

    testLine = generateBusRoute(1, BusRouteDataFileUtil.MIN_NUMBER_OF_STATIONS_PER_ROUTE - 1);
    pathname = createTempDataFile(Arrays.asList("1", testLine));
    BusRouteDataFileUtil.createBusRouteDataCache(pathname);
  }

  @Test(expected = DataContraintViolationException.class)
  public void testTooLessBusRoutes()
      throws IOException {
    final List<String> testData = generateListOfBusRoutes(1, 100);
    String pathname = createTempDataFile(testData);
    final Map<Integer, Map<Integer, Integer>> dataMap = BusRouteDataFileUtil.createBusRouteDataCache(pathname);

    assertEquals(1, dataMap.size());

    pathname = createTempDataFile(Arrays.asList("0", ""));
    BusRouteDataFileUtil.createBusRouteDataCache(pathname);
  }

  @Test(expected = DataContraintViolationException.class)
  public void testTooManyBusRoutes()
      throws IOException {
    List<String> testData = generateListOfBusRoutes(BusRouteDataFileUtil.MAX_NUMBER_OF_BUS_ROUTES, 100);
    String pathname = createTempDataFile(testData);
    final Map<Integer, Map<Integer, Integer>> dataMap = BusRouteDataFileUtil.createBusRouteDataCache(pathname);

    assertEquals(BusRouteDataFileUtil.MAX_NUMBER_OF_BUS_ROUTES, dataMap.size());

    testData = generateListOfBusRoutes(BusRouteDataFileUtil.MAX_NUMBER_OF_BUS_ROUTES + 1, 100);
    pathname = createTempDataFile(testData);
    BusRouteDataFileUtil.createBusRouteDataCache(pathname);
  }

  @Test(expected = InconsistentDataException.class)
  public void testWithEmptyFile()
      throws IOException {
    final String pathname = createTempDataFile(new ArrayList<>());
    BusRouteDataFileUtil.createBusRouteDataCache(pathname);
  }

  @Test(expected = InconsistentDataException.class)
  public void testWithNonNumericSingleValue()
      throws IOException {
    final String pathname = createTempDataFile(Arrays.asList("someNonNumericValue", "0 0 1 2"));
    BusRouteDataFileUtil.createBusRouteDataCache(pathname);
  }

  @Test(expected = InconsistentDataException.class)
  public void testWithNonNumericValueInList()
      throws IOException {
    final String pathname = createTempDataFile(Arrays.asList("1", "someNonNumericValue 0 1 2"));
    BusRouteDataFileUtil.createBusRouteDataCache(pathname);
  }

  @Test(expected = InconsistentDataException.class)
  public void testNumberOfBusRoutesNotAsAnnounced()
      throws IOException {
    final String pathname = createTempDataFile(Arrays.asList("1", "0 0 1 2 3 4", "1 3 1 6 5"));
    BusRouteDataFileUtil.createBusRouteDataCache(pathname);
  }
}
