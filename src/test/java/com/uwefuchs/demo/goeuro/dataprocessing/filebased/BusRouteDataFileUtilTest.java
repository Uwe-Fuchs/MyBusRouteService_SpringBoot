package com.uwefuchs.demo.goeuro.dataprocessing.filebased;

import com.uwefuchs.demo.goeuro.AbstractBusRouteChallengeTest;
import com.uwefuchs.demo.goeuro.dataprocessing.DataConstraints;
import com.uwefuchs.demo.goeuro.exceptions.DataContraintViolationException;
import com.uwefuchs.demo.goeuro.exceptions.InconsistentDataException;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BusRouteDataFileUtilTest
	extends AbstractBusRouteChallengeTest
{
	@Test
	public void testReadAndCacheBusRouteData()
		throws IOException
	{
		String pathname = createTempDataFile(Arrays.asList("2", "0 0 1 2 3 4", "1 3 1 6 5"));
		Map<Integer, Map<Integer, Integer>> dataMap = BusRouteDataFileUtil.readAndCacheBusRouteData(pathname);
		assertEquals(dataMap.size(), 2);
		
		Map<Integer, Integer> busRoute = dataMap.get(0);
		assertEquals(busRoute.size(), 5);
		
		busRoute = dataMap.get(1);
		assertEquals(busRoute.size(), 4);		
		assertEquals(busRoute.get(6), new Integer(2));
		assertNull(busRoute.get(7));
		
		pathname = createTempDataFile(Arrays.asList("3", "0 0 1 2 3 4", "1 3 1 6 5", "2 0 6 4"));
		dataMap = BusRouteDataFileUtil.readAndCacheBusRouteData(pathname);
		busRoute = dataMap.get(2);	
		assertEquals(dataMap.size(), 3);
		assertEquals(busRoute.get(0), new Integer(0));
	}

	@Test
	public void testProcessSingleLine()
		throws IOException
	{
		String pathname = createTempDataFile(Arrays.asList("1", "0 0 1 2 3 4"));
		Map<Integer, Map<Integer, Integer>> dataMap = BusRouteDataFileUtil.readAndCacheBusRouteData(pathname);
		
		assertEquals(dataMap.size(), 1);
		
		Map<Integer, Integer> busRoute = dataMap.get(0);
		assertEquals(busRoute.size(), 5);
		
		for (int lineId = 0; lineId < busRoute.size(); lineId++)
		{
			assertEquals(busRoute.get(lineId), new Integer(lineId));
		}
	}

	@Test(expected=NullPointerException.class)
	public void testWithoutPathname()
	{	
		BusRouteDataFileUtil.readAndCacheBusRouteData(null);
	}

	@Test(expected=com.uwefuchs.demo.goeuro.exceptions.IOException.class)
	public void testWithInvalidPathname()
	{	
		BusRouteDataFileUtil.readAndCacheBusRouteData("someInvalidPathName");
	}

	@Test(expected=DataContraintViolationException.class)
	public void testWithoutStations()
		throws IOException
	{
		String pathname = createTempDataFile(Arrays.asList("1", "1"));
		BusRouteDataFileUtil.readAndCacheBusRouteData(pathname);
	}

	@Test(expected=InconsistentDataException.class)
	public void testWithNonUniqueBusRouteIds()
		throws IOException
	{
		String testFile = createTempDataFile(Arrays.asList("2", "1 1 2 3 4 5", "2 6 7 8 9 10"));
		Map<Integer, Map<Integer, Integer>> dataMap = BusRouteDataFileUtil.readAndCacheBusRouteData(testFile);
		assertEquals(2, dataMap.size());
		
		testFile = createTempDataFile(Arrays.asList("2", "1 1 2 3 4 5", "1 6 7 8 9 10"));
		BusRouteDataFileUtil.readAndCacheBusRouteData(testFile);
	}

	@Test(expected=InconsistentDataException.class)
	public void testWithNonUniqueStationIds()
		throws IOException
	{
		String testFile = createTempDataFile(Arrays.asList("1", "0 0 1 2 3 4"));
		Map<Integer, Map<Integer, Integer>> dataMap = BusRouteDataFileUtil.readAndCacheBusRouteData(testFile);
		assertEquals(1, dataMap.size());
		
		testFile = createTempDataFile(Arrays.asList("1", "0 0 1 1 3 4"));
		BusRouteDataFileUtil.readAndCacheBusRouteData(testFile);
	}

	@Test(expected=DataContraintViolationException.class)
	public void testTooManyStations()
		throws IOException
	{
		String testLine = generateBusRoute(1, DataConstraints.MAX_NUMBER_OF_STATIONS_PER_ROUTE);
		String pathname = createTempDataFile(Arrays.asList("1", testLine));
		Map<Integer, Map<Integer, Integer>> dataMap = BusRouteDataFileUtil.readAndCacheBusRouteData(pathname);
		
		Map<Integer, Integer> busRoute = dataMap.get(1);
		assertEquals(busRoute.size(), DataConstraints.MAX_NUMBER_OF_STATIONS_PER_ROUTE);
		
		testLine = generateBusRoute(1, DataConstraints.MAX_NUMBER_OF_STATIONS_PER_ROUTE + 1);
		pathname = createTempDataFile(Arrays.asList("1", testLine));
		BusRouteDataFileUtil.readAndCacheBusRouteData(pathname);
	}

	@Test(expected=DataContraintViolationException.class)
	public void testTooLessStations()
		throws IOException
	{
		String testLine = generateBusRoute(1, DataConstraints.MIN_NUMBER_OF_STATIONS_PER_ROUTE);
		String pathname = createTempDataFile(Arrays.asList("1", testLine));
		Map<Integer, Map<Integer, Integer>> dataMap = BusRouteDataFileUtil.readAndCacheBusRouteData(pathname);
		
		Map<Integer, Integer> busRoute = dataMap.get(1);
		assertEquals(busRoute.size(), DataConstraints.MIN_NUMBER_OF_STATIONS_PER_ROUTE);
		
		testLine = generateBusRoute(1, DataConstraints.MIN_NUMBER_OF_STATIONS_PER_ROUTE - 1);
		pathname = createTempDataFile(Arrays.asList("1", testLine));
		BusRouteDataFileUtil.readAndCacheBusRouteData(pathname);
	}

	@Test(expected=DataContraintViolationException.class)
	public void testTooLessBusRoutes()
		throws IOException
	{
		List<String> testData = generateListOfBusRoutes(1, 100);
		String pathname = createTempDataFile(testData);
		Map<Integer, Map<Integer, Integer>> dataMap = BusRouteDataFileUtil.readAndCacheBusRouteData(pathname);
		
		assertEquals(1, dataMap.size());

		pathname = createTempDataFile(Arrays.asList("0", ""));
		BusRouteDataFileUtil.readAndCacheBusRouteData(pathname);
	}

	@Test(expected=DataContraintViolationException.class)
	public void testTooManyBusRoutes()
		throws IOException
	{
		List<String> testData = generateListOfBusRoutes(DataConstraints.MAX_NUMBER_OF_BUS_ROUTES, 100);
		String pathname = createTempDataFile(testData);
		Map<Integer, Map<Integer, Integer>> dataMap = BusRouteDataFileUtil.readAndCacheBusRouteData(pathname);
		
		assertEquals(DataConstraints.MAX_NUMBER_OF_BUS_ROUTES, dataMap.size());
		
		testData = generateListOfBusRoutes(DataConstraints.MAX_NUMBER_OF_BUS_ROUTES + 1, 100);
		pathname = createTempDataFile(testData);
		BusRouteDataFileUtil.readAndCacheBusRouteData(pathname);
	}

	@Test(expected=InconsistentDataException.class)
	public void testWithEmptyFile()
		throws IOException
	{
		String pathname = createTempDataFile(new ArrayList<>());
		BusRouteDataFileUtil.readAndCacheBusRouteData(pathname);
	}

	@Test(expected=InconsistentDataException.class)
	public void testWithNonNumericSingleValue()
			throws IOException
	{
		String pathname = createTempDataFile(Arrays.asList("someNonNumericValue", "0 0 1 2"));
		BusRouteDataFileUtil.readAndCacheBusRouteData(pathname);
	}

	@Test(expected=InconsistentDataException.class)
	public void testWithNonNumericValueInList()
		throws IOException
	{
		String pathname = createTempDataFile(Arrays.asList("1", "someNonNumericValue 0 1 2"));
		BusRouteDataFileUtil.readAndCacheBusRouteData(pathname);
	}

	@Test(expected=InconsistentDataException.class)
	public void testNumberOfBusRoutesNotAsAnnounced()
		throws IOException
	{
		String pathname = createTempDataFile(Arrays.asList("1", "0 0 1 2 3 4", "1 3 1 6 5"));
		BusRouteDataFileUtil.readAndCacheBusRouteData(pathname);
	}
}
