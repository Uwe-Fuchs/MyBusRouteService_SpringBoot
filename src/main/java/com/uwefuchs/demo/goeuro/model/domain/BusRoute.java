package com.uwefuchs.demo.goeuro.model.domain;

import com.google.common.base.Preconditions;
import java.util.List;

public class BusRoute {

  private final Long busRouteId;
  private final List<Integer> stationIds;

  public BusRoute(final Long busRouteId, final List<Integer> stationIds) {
    Preconditions.checkNotNull(busRouteId, "busRouteId is required!");
    Preconditions.checkNotNull(stationIds, "list of stationIds is required!");
    Preconditions.checkArgument(!stationIds.isEmpty(), "stationId-list should not be empty!");
    this.busRouteId = busRouteId;
    this.stationIds = stationIds;
  }

  public Long getBusRouteId() {
    return this.busRouteId;
  }

  public List<Integer> getStationIds() {
    return this.stationIds;
  }

  public boolean isSuitableBusRoute(final Integer dep_sid, final Integer arr_sid) {
    return this.stationIds.contains(dep_sid)
        && this.stationIds.contains(arr_sid)
        && this.stationIds.lastIndexOf(dep_sid) < this.stationIds.lastIndexOf(arr_sid);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }

    final BusRoute busRoute = (BusRoute) o;

    if (!this.busRouteId.equals(busRoute.busRouteId)) {
      return false;
    }
    return this.stationIds.equals(busRoute.stationIds);
  }

  @Override
  public int hashCode() {
    int result = this.busRouteId.hashCode();
    result = 31 * result + this.stationIds.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "BusRoute{" +
        "busRouteId=" + this.busRouteId +
        ", stationIds=" + this.stationIds +
        '}';
  }
}
