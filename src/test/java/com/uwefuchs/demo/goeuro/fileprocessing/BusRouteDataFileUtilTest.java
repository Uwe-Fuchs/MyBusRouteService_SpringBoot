package com.uwefuchs.demo.goeuro.fileprocessing;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

import com.uwefuchs.demo.goeuro.BusRouteDataTestHelper;
import com.uwefuchs.demo.goeuro.exceptions.DataConstraintViolationException;
import com.uwefuchs.demo.goeuro.exceptions.InconsistentDataException;
import com.uwefuchs.demo.goeuro.model.domain.BusRoute;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class BusRouteDataFileUtilTest {

  @AfterEach
  public void doAfter() {
    FileOperationsHelper.deleteTmpFiles();
  }

  @Test
  public void shouldCreateBusRouteListFromFile() throws IOException {
    // given
    String pathToTmpDataFile = FileOperationsHelper.createTempDataFile(Arrays.asList("3", "0 0 1 2 3 4", "1 3 1 6 5", "2 0 6 4"));

    // when
    List<BusRoute> busRouteList = BusRouteDataFileUtil.createBusRouteDataCache(pathToTmpDataFile);

    // then
    then(busRouteList.size()).isEqualTo(3);

    BusRoute aBusRoute = busRouteList.get(0);
    then(aBusRoute.getStationIds().size()).isEqualTo(5);
    then(aBusRoute.getStationIds().get(0)).isEqualTo(0);
    then(aBusRoute.getStationIds().get(1)).isEqualTo(1);
    then(aBusRoute.getStationIds().get(2)).isEqualTo(2);
    then(aBusRoute.getStationIds().get(3)).isEqualTo(3);
    then(aBusRoute.getStationIds().get(4)).isEqualTo(4);

    aBusRoute = busRouteList.get(1);
    then(aBusRoute.getStationIds().size()).isEqualTo(4);
    then(aBusRoute.getStationIds().get(0)).isEqualTo(3);
    then(aBusRoute.getStationIds().get(1)).isEqualTo(1);
    then(aBusRoute.getStationIds().get(2)).isEqualTo(6);
    then(aBusRoute.getStationIds().get(3)).isEqualTo(5);

    aBusRoute = busRouteList.get(2);
    then(aBusRoute.getStationIds().size()).isEqualTo(3);
    then(aBusRoute.getStationIds().get(0)).isEqualTo(0);
    then(aBusRoute.getStationIds().get(1)).isEqualTo(6);
    then(aBusRoute.getStationIds().get(2)).isEqualTo(4);
  }

  @Test
  public void shouldProcessSingleLine() throws IOException {
    // given
    final String pathToTmpDataFile = FileOperationsHelper.createTempDataFile(Arrays.asList("1", "0 3 1 6 5"));

    // when
    final List<BusRoute> busRouteList = BusRouteDataFileUtil.createBusRouteDataCache(pathToTmpDataFile);

    // then
    then(busRouteList.size()).isEqualTo(1);
    final BusRoute aBusRoute = busRouteList.get(0);
    then(aBusRoute.getStationIds().size()).isEqualTo(4);
    then(aBusRoute.getStationIds().get(0)).isEqualTo(3);
    then(aBusRoute.getStationIds().get(1)).isEqualTo(1);
    then(aBusRoute.getStationIds().get(2)).isEqualTo(6);
    then(aBusRoute.getStationIds().get(3)).isEqualTo(5);
  }

  @Test
  public void shouldFailWithoutPathToFile() {
    // when
    final Throwable result = catchThrowable(() -> BusRouteDataFileUtil.createBusRouteDataCache(null));

    // then
    then(result).isInstanceOf(NullPointerException.class);
    then(result.getMessage()).contains("path-name is required");
  }

  @Test
  public void shouldFailWithInvalidPathToFile() {
    // when
    final Throwable result = catchThrowable(()
        -> BusRouteDataFileUtil.createBusRouteDataCache("someInvalidPathName"));

    // then
    then(result).isInstanceOf(com.uwefuchs.demo.goeuro.exceptions.IOException.class);
    then(result.getMessage()).contains("IOException when reading data-file");
  }

  @Test
  public void shouldFailWithoutStationsInRoute() throws IOException {
    // given
    final String pathToTmpDataFile = FileOperationsHelper.createTempDataFile(Arrays.asList("1", "0"));

    // when
    final Throwable result = catchThrowable(() -> BusRouteDataFileUtil.createBusRouteDataCache(pathToTmpDataFile));

    // then
    then(result).isInstanceOf(InconsistentDataException.class);
    then(result.getMessage()).contains("Number of stations not within bounds");
  }

  @Test
  public void shouldFailWithNonUniqueBusRouteIds() throws IOException {
    // given
    String pathToTmpDataFile = FileOperationsHelper.createTempDataFile(Arrays.asList("2", "0 1 2", "0 3 4"));

    // when
    final Throwable result = catchThrowable(() -> BusRouteDataFileUtil.createBusRouteDataCache(pathToTmpDataFile));

    // then
    then(result).isInstanceOf(DataConstraintViolationException.class);
    then(result.getMessage()).contains("double occurrence of bus-route-id [0]");
  }

  @Test
  public void shouldFailWithNonUniqueStationIds() throws IOException {
    // given
    String pathToTmpDataFile = FileOperationsHelper.createTempDataFile(Arrays.asList("1", "0 0 1 1 3 4"));

    // when
    final Throwable result = catchThrowable(() -> BusRouteDataFileUtil.createBusRouteDataCache(pathToTmpDataFile));

    // then
    then(result).isInstanceOf(DataConstraintViolationException.class);
    then(result.getMessage()).contains("double occurrence of station-id [1]");
  }

  @Test
  public void shouldFailWithTooManyStations() throws IOException {
    // given
    String testLine = BusRouteDataTestHelper.generateBusRoute(1,
        BusRouteDataFileUtil.MAX_NUMBER_OF_STATIONS_PER_ROUTE + 1);
    String pathToTmpDataFile = FileOperationsHelper.createTempDataFile(Arrays.asList("1", testLine));

    // when
    final Throwable result = catchThrowable(() -> BusRouteDataFileUtil.createBusRouteDataCache(pathToTmpDataFile));

    // then
    then(result).isInstanceOf(InconsistentDataException.class);
    then(result.getMessage()).contains("Number of stations not within bounds");
  }

  @Test
  public void shouldFailWithTooLessStations() throws IOException {
    // given
    String testLine = BusRouteDataTestHelper.generateBusRoute(1,
        BusRouteDataFileUtil.MIN_NUMBER_OF_STATIONS_PER_ROUTE - 1);
    String pathToTmpDataFile = FileOperationsHelper.createTempDataFile(Arrays.asList("1", testLine));

    // when
    final Throwable result = catchThrowable(() -> BusRouteDataFileUtil.createBusRouteDataCache(pathToTmpDataFile));

    // then
    then(result).isInstanceOf(InconsistentDataException.class);
    then(result.getMessage()).contains("Number of stations not within bounds");
  }

  @Test
  public void shouldFailWithTooLessBusRoutes() throws IOException {
    // given
    String pathToTmpDataFile = FileOperationsHelper.createTempDataFile(Arrays.asList("0", ""));

    // when
    final Throwable result = catchThrowable(() -> BusRouteDataFileUtil.createBusRouteDataCache(pathToTmpDataFile));

    // then
    then(result).isInstanceOf(InconsistentDataException.class);
    then(result.getMessage()).contains("Number of bus-routes not within bounds");
  }

  @Test
  public void shouldFailWithEmptyDataFile() throws IOException {
    // given
    final String pathToTmpDataFile = FileOperationsHelper.createTempDataFile(new ArrayList<>());

    // when
    final Throwable result = catchThrowable(() -> BusRouteDataFileUtil.createBusRouteDataCache(pathToTmpDataFile));

    // then
    then(result).isInstanceOf(InconsistentDataException.class);
    then(result.getMessage()).contains("No data found in given file");
  }

  @Test
  public void shouldFailWithNonNumericNumberOfStations() throws IOException {
    // given
    final String pathToTmpDataFile = FileOperationsHelper.createTempDataFile(Arrays.asList("someNonNumericValue", "0 0 1 2"));

    // when
    final Throwable result = catchThrowable(() -> BusRouteDataFileUtil.createBusRouteDataCache(pathToTmpDataFile));

    // then
    then(result).isInstanceOf(InconsistentDataException.class);
    then(result.getMessage()).contains("InputMismatchException");
  }

  @Test
  public void shouldFailWithNonNumericStationId() throws IOException {
    // given
    final String pathToTmpDataFile = FileOperationsHelper.createTempDataFile(Arrays.asList("1", "someNonNumericValue 0 1 2"));

    // when
    final Throwable result = catchThrowable(() -> BusRouteDataFileUtil.createBusRouteDataCache(pathToTmpDataFile));

    // then
    then(result).isInstanceOf(InconsistentDataException.class);
    then(result.getMessage()).contains("InputMismatchException");
  }

  @Test
  public void shouldFailWithOtherNumberOfBusRoutes() throws IOException {
    // given
    final String pathToTmpDataFile = FileOperationsHelper.createTempDataFile(Arrays.asList("1", "0 0 1 2 3 4", "1 3 1 6 5"));

    // when
    final Throwable result = catchThrowable(() -> BusRouteDataFileUtil.createBusRouteDataCache(pathToTmpDataFile));

    // then
    then(result).isInstanceOf(InconsistentDataException.class);
    then(result.getMessage()).contains("Real number [2] of bus-routes differs from announced number [1]");
  }

  /*  @Ignore
  @Test(expected = DataConstraintViolationException.class)
  public void testTooManyBusRoutes()
      throws IOException {
    List<String> testData = BusRouteDataTestHelper
        .generateListOfBusRoutes(BusRouteDataFileUtil.MAX_NUMBER_OF_BUS_ROUTES + 1, 100);
    String pathname = FileOperationsHelper.createTempDataFile(testData);
    BusRouteDataFileUtil.createBusRouteDataCache(pathname);
  }
*/
}
