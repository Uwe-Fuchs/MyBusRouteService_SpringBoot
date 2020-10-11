package com.uwefuchs.demo.goeuro.fileprocessing;

import static com.uwefuchs.demo.goeuro.fileprocessing.FileOperationsHelper.createTempDataFile;
import static org.junit.Assert.assertEquals;

import com.uwefuchs.demo.goeuro.BusRouteDataTestHelper;
import com.uwefuchs.demo.goeuro.exceptions.DataConstraintViolationException;
import com.uwefuchs.demo.goeuro.exceptions.InconsistentDataException;
import com.uwefuchs.demo.goeuro.model.domain.BusRoute;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

public class BusRouteDataFileUtilTest {

  @After
  public void doAfter() {
    FileOperationsHelper.deleteTmpFiles();
  }

  @Test
  public void shouldCreateBusRouteListFromFile()
      throws IOException {
    String pathname = createTempDataFile(Arrays.asList("2", "0 0 1 2 3 4", "1 3 1 6 5"));
    List<BusRoute> busRouteList = BusRouteDataFileUtil.createBusRouteDataCache(pathname);
    assertEquals(busRouteList.size(), 2);

    BusRoute aBusRoute = busRouteList.get(0);
    assertEquals(aBusRoute.getStationIds().size(), 5);
    assertEquals(aBusRoute.getStationIds().get(0), Integer.valueOf(0));
    assertEquals(aBusRoute.getStationIds().get(1), Integer.valueOf(1));
    assertEquals(aBusRoute.getStationIds().get(2), Integer.valueOf(2));
    assertEquals(aBusRoute.getStationIds().get(3), Integer.valueOf(3));
    assertEquals(aBusRoute.getStationIds().get(4), Integer.valueOf(4));

    aBusRoute = busRouteList.get(1);
    assertEquals(aBusRoute.getStationIds().size(), 4);
    assertEquals(aBusRoute.getStationIds().get(0), Integer.valueOf(3));
    assertEquals(aBusRoute.getStationIds().get(1), Integer.valueOf(1));
    assertEquals(aBusRoute.getStationIds().get(2), Integer.valueOf(6));
    assertEquals(aBusRoute.getStationIds().get(3), Integer.valueOf(5));

    pathname = createTempDataFile(Arrays.asList("3", "0 0 1 2 3 4", "1 3 1 6 5", "2 0 6 4"));
    busRouteList = BusRouteDataFileUtil.createBusRouteDataCache(pathname);
    assertEquals(busRouteList.size(), 3);
    assertEquals(busRouteList.get(0).getStationIds().size(), 5);
    assertEquals(busRouteList.get(1).getStationIds().size(), 4);
    assertEquals(busRouteList.get(2).getStationIds().size(), 3);
  }

  @Test
  public void testProcessSingleLine()
      throws IOException {
    final String pathname = createTempDataFile(Arrays.asList("1", "0 0 1 2 3 4"));
    final List<BusRoute> busRouteList = BusRouteDataFileUtil.createBusRouteDataCache(pathname);

    assertEquals(busRouteList.size(), 1);

    final BusRoute aBusRoute = busRouteList.get(0);
    assertEquals(aBusRoute.getStationIds().size(), 5);

    for (int lineId = 0; lineId < aBusRoute.getStationIds().size(); lineId++) {
      assertEquals(aBusRoute.getStationIds().get(lineId), Integer.valueOf(lineId));
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

  @Test(expected = DataConstraintViolationException.class)
  public void testWithoutStations()
      throws IOException {
    final String pathname = createTempDataFile(Arrays.asList("1", "1"));
    BusRouteDataFileUtil.createBusRouteDataCache(pathname);
  }

  @Test(expected = DataConstraintViolationException.class)
  public void testWithNonUniqueBusRouteIds()
      throws IOException {
    String testFile = createTempDataFile(Arrays.asList("2", "0 1 2", "0 3 4"));
    BusRouteDataFileUtil.createBusRouteDataCache(testFile);
  }

  @Test(expected = DataConstraintViolationException.class)
  public void testWithNonUniqueStationIds()
      throws IOException {
    String testFile = createTempDataFile(Arrays.asList("1", "0 0 1 1 3 4"));
    BusRouteDataFileUtil.createBusRouteDataCache(testFile);
  }

  @Test(expected = DataConstraintViolationException.class)
  public void testTooManyStations()
      throws IOException {
    String testLine = BusRouteDataTestHelper.generateBusRoute(1, BusRouteDataFileUtil.MAX_NUMBER_OF_STATIONS_PER_ROUTE + 1);
    String pathname = createTempDataFile(Arrays.asList("1", testLine));
    BusRouteDataFileUtil.createBusRouteDataCache(pathname);
  }

  @Test(expected = DataConstraintViolationException.class)
  public void testTooLessStations()
      throws IOException {
    String testLine = BusRouteDataTestHelper.generateBusRoute(1, BusRouteDataFileUtil.MIN_NUMBER_OF_STATIONS_PER_ROUTE - 1);
    String pathname = createTempDataFile(Arrays.asList("1", testLine));
    BusRouteDataFileUtil.createBusRouteDataCache(pathname);
  }

  @Test(expected = DataConstraintViolationException.class)
  public void testTooLessBusRoutes()
      throws IOException {
    final List<String> testData = BusRouteDataTestHelper.generateListOfBusRoutes(1, 100);
    String pathname = createTempDataFile(testData);
    final List<BusRoute> busRouteList = BusRouteDataFileUtil.createBusRouteDataCache(pathname);

    assertEquals(1, busRouteList.size());

    pathname = createTempDataFile(Arrays.asList("0", ""));
    BusRouteDataFileUtil.createBusRouteDataCache(pathname);
  }

  @Ignore
  @Test(expected = DataConstraintViolationException.class)
  public void testTooManyBusRoutes()
      throws IOException {
    List<String> testData = BusRouteDataTestHelper
        .generateListOfBusRoutes(BusRouteDataFileUtil.MAX_NUMBER_OF_BUS_ROUTES + 1, 100);
    String pathname = createTempDataFile(testData);
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
