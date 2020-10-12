package com.uwefuchs.demo.goeuro.configuration;

import com.uwefuchs.demo.goeuro.fileprocessing.BusRouteDataFileUtil;
import com.uwefuchs.demo.goeuro.model.domain.BusRoute;
import com.uwefuchs.demo.goeuro.persistence.IBusRouteRepository;
import com.uwefuchs.demo.goeuro.persistence.InMemoryBusRouteRepositoryImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

  @Value("${pathname}")
  private String pathname;

  @Bean
  public IBusRouteRepository busRouteRepository() {
    final List<BusRoute> busRouteList = BusRouteDataFileUtil.createBusRouteDataCache(this.pathname);
    return new InMemoryBusRouteRepositoryImpl(busRouteList);
  }
}