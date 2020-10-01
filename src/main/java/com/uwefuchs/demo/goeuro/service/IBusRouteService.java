package com.uwefuchs.demo.goeuro.service;

import com.uwefuchs.demo.goeuro.model.api.BusRouteInfoResource;

/**
 * A service containing bus-route-operations (filtering, maybe manipulating in a future step).
 *
 * @author info@uwefuchs.com
 */
public interface IBusRouteService {

  /**
   * checks if a bus-route exists for given dep_sid as start and arr_sid as arrival (i.e.: station with dept_id is
   * BEFORE station with arr_id).
   *
   * @param dep_sid : department-station identifier.
   * @param arr_sid : arrival station identifier.
   * @return {@link BusRouteInfoResource} with given sid's and examination-result.
   */
  BusRouteInfoResource existsSuitableBusRoute(Integer dep_sid, Integer arr_sid);
}
