package com.uwefuchs.demo.goeuro.service.filebased;

import com.uwefuchs.demo.goeuro.AbstractBusRouteChallengeTest;
import com.uwefuchs.demo.goeuro.dataprocessing.filebased.FileBasedBusRouteService;
import com.uwefuchs.demo.goeuro.domain.BusRouteInfo;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FileBasedBusRouteServiceTest
	extends AbstractBusRouteChallengeTest
{
	@Test
	public void testLookUpBusRoute()
		throws IOException
	{
		String pathname = createTempDataFile(Arrays.asList("2", "0 0 1 2 3 4", "1 3 1 6 5"));
		FileBasedBusRouteService service = new FileBasedBusRouteService();
		service.setPathname(pathname);
		service.cacheBusRouteData();
		
		BusRouteInfo info = service.lookUpBusRoute(3, 6);
		assertTrue(info.getDirect_bus_route());
		
		info = service.lookUpBusRoute(6, 3);
		assertFalse(info.getDirect_bus_route());
		
		info = service.lookUpBusRoute(0, 5);
		assertFalse(info.getDirect_bus_route());
	}
}
