package com.uwefuchs.demo.goeuro.persistence;

import com.uwefuchs.demo.goeuro.model.domain.BusRoute;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InMemoryBusRouteRepositoryImpl implements IBusRouteRepository {

  @Value("${pathname}")
  private String pathname;

  private final List<BusRoute> cachedBusRoutes = Collections.emptyList();

  @Override
  public List<BusRoute> deliverAllBusRoutes() {
    return this.cachedBusRoutes;
  }
}
