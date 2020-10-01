package com.uwefuchs.demo.goeuro.service;

import com.uwefuchs.demo.goeuro.persistence.IBusRouteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BusRouteServiceImplTest {

  @Mock
  IBusRouteRepository busRouteRepository;

  @InjectMocks
  BusRouteServiceImpl serviceUnderTest;

  @Test
  void shouldFindSuitableBusRoute() {
    //final List<BusRoute> busRouteDataList = Arrays.asList(2, "0 0 1 2 3 4", "1 3 1 6 5");

  }
}
