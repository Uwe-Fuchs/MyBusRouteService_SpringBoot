package com.uwefuchs.demo.goeuro.persistence;

import com.uwefuchs.demo.goeuro.model.domain.BusRoute;
import java.util.List;

/**
 * Exposes CRUD-Operations on a BusRoute-Datastore (filtering, maybe manipulating in a future step).
 *
 * @author info@uwefuchs.com
 */
public interface IBusRouteRepository {

  /**
   * delivers a {@link List} containing all {@link BusRoute}-Objects.
   *
   * @return {@link List} containing all {@link BusRoute}-Objects.
   */
  public List<BusRoute> deliverAllBusRoutes();
}
