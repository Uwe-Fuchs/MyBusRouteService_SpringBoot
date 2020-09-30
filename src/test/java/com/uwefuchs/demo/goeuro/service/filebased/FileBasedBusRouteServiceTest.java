package com.uwefuchs.demo.goeuro.service.filebased;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.uwefuchs.demo.goeuro.AbstractBusRouteChallengeTest;
import com.uwefuchs.demo.goeuro.dataprocessing.filebased.FileBasedBusRouteService;
import com.uwefuchs.demo.goeuro.model.api.BusRouteInfoResource;
import java.io.IOException;
import java.util.Arrays;
import org.junit.Test;

public class FileBasedBusRouteServiceTest
    extends AbstractBusRouteChallengeTest {

  @Test
  public void testLookUpBusRoute()
      throws IOException {

    final String pathname = this.createTempDataFile(Arrays.asList("2", "0 0 1 2 3 4", "1 3 1 6 5"));
    final FileBasedBusRouteService service = new FileBasedBusRouteService();
    service.setPathname(pathname);
    service.cacheBusRouteData();

    BusRouteInfoResource info = service.existsSuitableBusRoute(3, 6);
    assertTrue(info.getDirect_bus_route());

    info = service.existsSuitableBusRoute(6, 3);
    assertFalse(info.getDirect_bus_route());

    info = service.existsSuitableBusRoute(0, 5);
    assertFalse(info.getDirect_bus_route());
  }
}
