package com.uwefuchs.demo.goeuro.service;

import com.uwefuchs.demo.goeuro.model.api.BusRouteInfoResource;
import com.uwefuchs.demo.goeuro.model.domain.BusRoute;
import com.uwefuchs.demo.goeuro.persistence.IBusRouteRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusRouteServiceImpl implements IBusRouteService {

  @Autowired
  IBusRouteRepository busRouteRepository;

  @Override
  public BusRouteInfoResource existsSuitableBusRoute(final Integer dep_sid, final Integer arr_sid) {
    final List<BusRoute> allBusRoutes = this.busRouteRepository.deliverAllBusRoutes();

    final boolean hasAny = allBusRoutes.stream().anyMatch(r -> r.isSuitableBusRoute(dep_sid, arr_sid));

    return allBusRoutes
        .stream()
        .filter(r -> r.isSuitableBusRoute(dep_sid, arr_sid))
        .findFirst()
        .map(r -> new BusRouteInfoResource(dep_sid, arr_sid, true))
        .orElse(new BusRouteInfoResource(dep_sid, arr_sid, false));
  }
}
