package com.uwefuchs.demo.goeuro.dataprocessing.filebased;

import com.uwefuchs.demo.goeuro.domain.BusRouteInfo;
import com.uwefuchs.demo.goeuro.service.IBusRouteService;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * file-based implementation of {@link IBusRouteService}
 * 
 * @author info@uwefuchs.com
 */
@Component
public class FileBasedBusRouteService
	implements IBusRouteService
{
	private static final Logger LOG = LoggerFactory.getLogger(FileBasedBusRouteService.class);
		
	private Map<Integer, Map<Integer, Integer>> dataMap = null;	
	private String pathname;

	public FileBasedBusRouteService()
	{
		super();
	}

	@Value("${pathname}")
	public void setPathname(String pathname)
	{
		Validate.notBlank(pathname, "path-name mus not be blank!");
		this.pathname = pathname;
	}

	@Override
	public BusRouteInfo lookUpBusRoute(Integer dep_sid, Integer arr_sid)
	{
		LOG.debug("Lookup direct bus-route with dep_sid {} and arr_sid {} ... ", dep_sid, arr_sid);
		
		boolean eq = dataMap
				.values()
				.stream()
				.anyMatch(m -> m.containsKey(dep_sid) && m.containsKey(arr_sid) && m.get(dep_sid) < m.get(arr_sid));
		
		BusRouteInfo info = new BusRouteInfo(dep_sid, arr_sid, eq);
		
		LOG.info("Returning busRouteInfo {}", info);
		
		return info;
		
//		for (Map<Integer, Integer> busRoute : dataMap.values())
//		{
//			if (busRoute.containsKey(dep_sid) && busRoute.containsKey(arr_sid) && busRoute.get(dep_sid) < busRoute.get(arr_sid))
//			{
//				return new BusRouteInfo(dep_sid, arr_sid, Boolean.TRUE);
//			}
//		}
//		
//		return new BusRouteInfo(dep_sid, arr_sid, Boolean.FALSE);
	}
	
	/**
	 * builds up the cache containing all bus-route-data.
	 */
	@PostConstruct
	public void cacheBusRouteData()
	{	
		dataMap = BusRouteDataFileUtil.readAndCacheBusRouteData(this.pathname);
	}
}
