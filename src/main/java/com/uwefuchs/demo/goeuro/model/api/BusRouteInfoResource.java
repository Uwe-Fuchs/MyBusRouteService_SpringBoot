package com.uwefuchs.demo.goeuro.model.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * contains all information for a specific bus-route.
 *
 * @author info@uwefuchs.com
 */
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusRouteInfoResource {

  private Integer dep_sid;
  private Integer arr_sid;
  private Boolean direct_bus_route;

  private BusRouteInfoResource() {
    // Jackson
  }

  public BusRouteInfoResource(final Integer dep_sid, final Integer arr_sid, final Boolean direct_bus_route) {
    this.dep_sid = dep_sid;
    this.arr_sid = arr_sid;
    this.direct_bus_route = direct_bus_route;
  }

  public Integer getDep_sid() {
    return this.dep_sid;
  }

  public void setDep_sid(final Integer dep_sid) {
    this.dep_sid = dep_sid;
  }

  public Integer getArr_sid() {
    return this.arr_sid;
  }

  public void setArr_sid(final Integer arr_sid) {
    this.arr_sid = arr_sid;
  }

  public Boolean getDirect_bus_route() {
    return this.direct_bus_route;
  }

  public void setDirect_bus_route(final Boolean direct_bus_route) {
    this.direct_bus_route = direct_bus_route;
  }
}
