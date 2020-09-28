package com.uwefuchs.demo.goeuro.model.domain;

import com.google.common.base.Preconditions;

import java.util.List;

public class BusRoute {
    private Integer busRouteId;
    private List<Integer> stationIds;

    public BusRoute(Integer busRouteId, List<Integer> stationIds) {
        Preconditions.checkNotNull(busRouteId, "busRouteId is required!");
        Preconditions.checkNotNull(stationIds, "list of stationIds is required!");
        Preconditions.checkArgument(!stationIds.isEmpty(), "list of stationIds should contain entries!");
        this.busRouteId = busRouteId;
        this.stationIds = stationIds;
    }

    public Integer getBusRouteId() {
        return busRouteId;
    }

    public List<Integer> getStationIds() {
        return stationIds;
    }

    public boolean checkBusRoute(Integer dep_sid, Integer arr_sid) {
        return stationIds.contains(dep_sid)
                && stationIds.contains(arr_sid)
                && stationIds.lastIndexOf(dep_sid) < stationIds.lastIndexOf(arr_sid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BusRoute busRoute = (BusRoute) o;

        if (!busRouteId.equals(busRoute.busRouteId)) return false;
        return stationIds.equals(busRoute.stationIds);
    }

    @Override
    public int hashCode() {
        int result = busRouteId.hashCode();
        result = 31 * result + stationIds.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "BusRoute{" +
                "busRouteId=" + busRouteId +
                ", stationIds=" + stationIds +
                '}';
    }
}
