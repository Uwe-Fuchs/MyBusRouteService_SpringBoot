package com.uwefuchs.demo.goeuro.rest.springrest;

import com.uwefuchs.demo.goeuro.model.api.BusRouteInfoResource;
import com.uwefuchs.demo.goeuro.service.IBusRouteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/direct")
public class BusRoutesController {

  private static final Logger LOG = LoggerFactory.getLogger(BusRoutesController.class);

  @Autowired
  private IBusRouteService busRouteService;

  @GetMapping
  public BusRouteInfoResource findDirectRoute(
      @RequestParam(required = true, value = "dep_sid") final Integer dep_sid,
      @RequestParam(required = true, value = "arr_sid") final Integer arr_sid) {
    
    LOG.info("calling busRouteService with dep_sid [{}] and arr_sid [{}]", dep_sid, arr_sid);
    return this.busRouteService.existsSuitableBusRoute(dep_sid, arr_sid);
  }
}
