package com.uwefuchs.demo.goeuro.persistence;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

import com.uwefuchs.demo.goeuro.model.domain.BusRoute;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class InMemoryBusRouteRepositoryImplTest {
  private static final List<Integer> STATION_ID_LIST = Arrays.asList(1, 2);
  private static final BusRoute BUS_ROUTE = new BusRoute(1L, STATION_ID_LIST);

  IBusRouteRepository classUnderTest;

  @BeforeEach
  void setUp() {
    List<BusRoute> cachedBusRoutes = Arrays.asList(BUS_ROUTE);
    classUnderTest = new InMemoryBusRouteRepositoryImpl(cachedBusRoutes);
  }

  @Test
  void shouldDeliverBusRoutes() {
    // given when
    List<BusRoute> result = classUnderTest.deliverAllBusRoutes();

    // then
    then(result).hasSize(1);
    then(result).containsExactly(BUS_ROUTE);
  }

  @Test
  void shouldFailWithoutBusRoutesList() {
    // when
    final Throwable result = catchThrowable(() -> new InMemoryBusRouteRepositoryImpl(null));

    // then
    then(result).isInstanceOf(NullPointerException.class);
    then(result.getMessage()).contains("cachedBusRoutes is required");
  }
}
