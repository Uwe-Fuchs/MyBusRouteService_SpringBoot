package com.uwefuchs.demo.goeuro.dataprocessing;

import com.uwefuchs.demo.goeuro.exceptions.DataContraintViolationException;
import com.uwefuchs.demo.goeuro.exceptions.InconsistentDataException;

import java.util.Map;
import java.util.Scanner;

public class DataValidationUtil
{
    public static void assertUniqueBusRouteIds(Map<Integer, Map<Integer, Integer>> dataMap, int busRouteId)
    {
        if (dataMap.containsKey(busRouteId))
        {
            throw new InconsistentDataException(String.format("non-unique bus-route-id: [%d]", busRouteId));
        }
    }

    public static void assertNumberOfStationsWithinBounds(Map<Integer, Integer> busRouteMap, int busRouteId)
    {
        if (busRouteMap.size() < DataConstraints.MIN_NUMBER_OF_STATIONS_PER_ROUTE || busRouteMap.size() > DataConstraints.MAX_NUMBER_OF_STATIONS_PER_ROUTE)
        {
            throw new DataContraintViolationException(
                    String.format("number [%d] of stations not within defined bounds in bus-route [%d]", busRouteMap.size(), busRouteId));
        }
    }

    public static void assertUniqueStationIdsInBusRoute(Map<Integer, Integer> busRouteMap, int stationId, int busRouteId)
    {
        if (busRouteMap.containsKey(stationId))
        {
            throw new InconsistentDataException(String.format("double occurrence of station-id [%d ] in bus-route [%d]", stationId, busRouteId));
        }
    }

    public static void assertNumberOfBusRoutesWithinBounds(Map<Integer, Map<Integer, Integer>> dataMap)
    {
        if (dataMap.size() < 1 || dataMap.size() > DataConstraints.MAX_NUMBER_OF_BUS_ROUTES)
        {
            throw new DataContraintViolationException(String.format("more then [%d] bus-routes in given data", DataConstraints.MAX_NUMBER_OF_BUS_ROUTES));
        }
    }

    public static void assertNumberOfBusRoutesAsAnnounced(Map<Integer, Map<Integer, Integer>> dataMap, int numberOfBusRoutes)
    {
        if (numberOfBusRoutes != dataMap.size())
        {
            throw new InconsistentDataException(
                    String.format("Real number [%d] of bus-routes differs from announced number [%d]", dataMap.size(), numberOfBusRoutes));
        }
    }

    public static void assertFileNotEmpty(Scanner scanner, String pathname)
    {
        if (!scanner.hasNext())
        {
            throw new InconsistentDataException(String.format("No data found in given file [%s]", pathname));
        }
    }
}
