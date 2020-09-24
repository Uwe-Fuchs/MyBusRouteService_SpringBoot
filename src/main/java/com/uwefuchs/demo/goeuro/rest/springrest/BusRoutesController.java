package com.uwefuchs.demo.goeuro.rest.springrest;

import com.uwefuchs.demo.goeuro.domain.BusRouteInfo;
import com.uwefuchs.demo.goeuro.service.IBusRouteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BusRoutesController
{
	private static final Logger LOG = LoggerFactory.getLogger(BusRoutesController.class);

	@Autowired
	private IBusRouteService busRouteService;

	@GetMapping("/direct")
	public BusRouteInfo findDirectRoute(
		@RequestParam(required = true, value = "dep_sid") Integer dep_sid,
		@RequestParam(required = true, value = "arr_sid") Integer arr_sid)
	{
		LOG.info("calling busRouteService with dep_sid [{}] and arr_sid [{}]", dep_sid, arr_sid);
		return busRouteService.lookUpBusRoute(dep_sid, arr_sid);
	}
}
