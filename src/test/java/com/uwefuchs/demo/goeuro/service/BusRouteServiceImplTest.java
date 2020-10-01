package com.uwefuchs.demo.goeuro.service;

import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.BDDAssertions.then;

import com.uwefuchs.demo.goeuro.model.api.BusRouteInfoResource;
import com.uwefuchs.demo.goeuro.model.domain.BusRoute;
import com.uwefuchs.demo.goeuro.persistence.IBusRouteRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BusRouteServiceImplTest {
  List<BusRoute> busRouteDataList = null;

  @Mock
  IBusRouteRepository busRouteRepository;

  @InjectMocks
  BusRouteServiceImpl serviceUnderTest;

  @BeforeEach
  void setUp() {
    busRouteDataList = this.buildBusRouteDataList();
    given(busRouteRepository.deliverAllBusRoutes()).willReturn(busRouteDataList);
  }

  @Test
  void shouldFindBusRoute() {
    // given
    final Integer anSid = 1;
    final Integer anotherSid = 3;

    // when
    BusRouteInfoResource busRouteInfoResource = serviceUnderTest.existsSuitableBusRoute(anSid, anotherSid);

    // then
    then(busRouteInfoResource.getDep_sid()).isEqualTo(anSid);
    then(busRouteInfoResource.getArr_sid()).isEqualTo(anotherSid);
    then(busRouteInfoResource.getDirect_bus_route()).isTrue();

    // when
    busRouteInfoResource = serviceUnderTest.existsSuitableBusRoute(anotherSid, anSid);

    // then
    then(busRouteInfoResource.getDep_sid()).isEqualTo(anotherSid);
    then(busRouteInfoResource.getArr_sid()).isEqualTo(anSid);
    then(busRouteInfoResource.getDirect_bus_route()).isTrue();
  }

  @Test
  void shouldNotFindBusRouteBecauseOfWrongOrder() {
    // given
    final Integer anSid = 1;
    final Integer anotherSid = 2;

    // when
    BusRouteInfoResource busRouteInfoResource = serviceUnderTest.existsSuitableBusRoute(anSid, anotherSid);

    // then
    then(busRouteInfoResource.getDep_sid()).isEqualTo(anSid);
    then(busRouteInfoResource.getArr_sid()).isEqualTo(anotherSid);
    then(busRouteInfoResource.getDirect_bus_route()).isTrue();

    // when
    busRouteInfoResource = serviceUnderTest.existsSuitableBusRoute(anotherSid, anSid);

    // then
    then(busRouteInfoResource.getDep_sid()).isEqualTo(anotherSid);
    then(busRouteInfoResource.getArr_sid()).isEqualTo(anSid);
    then(busRouteInfoResource.getDirect_bus_route()).isFalse();

  }

  List<BusRoute> buildBusRouteDataList() {
    final List<BusRoute> busRouteDataList = new ArrayList<>();
    busRouteDataList.add(new BusRoute(1L, Arrays.asList(0, 1, 2, 3, 4)));
    busRouteDataList.add(new BusRoute(2L, Arrays.asList(3, 1, 6, 5)));

    return busRouteDataList;
  }
}
