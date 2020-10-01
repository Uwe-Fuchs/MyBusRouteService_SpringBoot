package com.uwefuchs.demo.goeuro.model.domain;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

public class BusRouteTest {

  static final Long BUS_ROUTE_ID = 1L;
  static final List<Integer> STATION_IDS = Arrays.asList(3, 1, 6);
  static final Integer NON_EXISTING_STATION = 111;

  BusRoute entityUnderTest;

  @Test
  void shouldFailWithoutBusRouteId() {
    // when
    final Throwable result = catchThrowable(() -> new BusRoute(null, STATION_IDS));

    // then
    then(result).isInstanceOf(NullPointerException.class);
    then(result.getMessage()).contains("busRouteId is required");
  }

  @Test
  void shouldFailWithoutStationIdList() {
    // when
    final Throwable result = catchThrowable(() -> new BusRoute(BUS_ROUTE_ID, null));

    // then
    then(result).isInstanceOf(NullPointerException.class);
    then(result.getMessage()).contains("list of stationIds is required");
  }

  @Test
  void shouldFailWithEmptyStationIdList() {
    // when
    final Throwable result = catchThrowable(() -> new BusRoute(BUS_ROUTE_ID, Collections.emptyList()));

    // then
    then(result).isInstanceOf(IllegalArgumentException.class);
    then(result.getMessage()).contains("stationId-list should not be empty");
  }

  @Test
  void shouldAcceptGivenBusRoutes() {
    // given
    this.entityUnderTest = new BusRoute(BUS_ROUTE_ID, STATION_IDS);

    // when then
    assertAll(
        () -> then(this.entityUnderTest.isSuitableBusRoute(3, 1)).isTrue(),
        () -> then(this.entityUnderTest.isSuitableBusRoute(3, 6)).isTrue(),
        () -> then(this.entityUnderTest.isSuitableBusRoute(1, 6)).isTrue()
    );
  }

  @Test
  void shouldRejectBusRoutesWithWrongDirection() {
    // given
    this.entityUnderTest = new BusRoute(BUS_ROUTE_ID, STATION_IDS);

    // when then
    assertAll(
        () -> then(this.entityUnderTest.isSuitableBusRoute(1, 3)).isFalse(),
        () -> then(this.entityUnderTest.isSuitableBusRoute(6, 3)).isFalse(),
        () -> then(this.entityUnderTest.isSuitableBusRoute(6, 1)).isFalse()
    );
  }

  @Test
  void shouldRejectBusRoutesWithNonExistingStation() {
    // given
    this.entityUnderTest = new BusRoute(BUS_ROUTE_ID, STATION_IDS);

    // when then
    assertAll(
        () -> then(this.entityUnderTest.isSuitableBusRoute(3, NON_EXISTING_STATION)).isFalse(),
        () -> then(this.entityUnderTest.isSuitableBusRoute(1, NON_EXISTING_STATION)).isFalse(),
        () -> then(this.entityUnderTest.isSuitableBusRoute(6, NON_EXISTING_STATION)).isFalse(),
        () -> then(this.entityUnderTest.isSuitableBusRoute(NON_EXISTING_STATION, 3)).isFalse(),
        () -> then(this.entityUnderTest.isSuitableBusRoute(NON_EXISTING_STATION, 1)).isFalse(),
        () -> then(this.entityUnderTest.isSuitableBusRoute(NON_EXISTING_STATION, 6)).isFalse()
    );
  }
}
