package com.uwefuchs.demo.goeuro.persistence;

import com.uwefuchs.demo.goeuro.model.domain.BusRoute;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class InMemoryBusRouteRepositoryImpl implements IBusRouteRepository {

  @Override
  public List<BusRoute> deliverAllBusRoutes() {
    return Collections.emptyList();
  }
}
