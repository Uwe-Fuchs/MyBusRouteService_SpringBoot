package com.uwefuchs.demo.goeuro.persistence;

import com.google.common.base.Preconditions;
import com.uwefuchs.demo.goeuro.model.domain.BusRoute;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class InMemoryBusRouteRepositoryImpl implements IBusRouteRepository {

  private final List<BusRoute> cachedBusRoutes;

  public InMemoryBusRouteRepositoryImpl(final List<BusRoute> cachedBusRoutes) {
    Preconditions.checkNotNull(cachedBusRoutes, "cachedBusRoutes is required!");
    this.cachedBusRoutes = cachedBusRoutes;
  }

  @Override
  public List<BusRoute> deliverAllBusRoutes() {
    return this.cachedBusRoutes;
  }
}
