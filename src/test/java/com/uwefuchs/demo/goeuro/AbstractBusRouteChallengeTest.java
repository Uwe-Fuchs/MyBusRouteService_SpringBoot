package com.uwefuchs.demo.goeuro;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.junit.After;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractBusRouteChallengeTest
{	
	@After
	public void doAfter()
		throws IOException
	{
		File dir = new File(System.getProperty("java.io.tmpdir"));
		FileFilter fileFilter = new WildcardFileFilter("testdata*.tmp");
		Arrays.asList(dir.listFiles(fileFilter))
			.forEach(File::delete);
	}
	
	protected String createTempDataFile(List<String> data)
		throws IOException
	{
		Path file = Files.createTempFile("testdata", ".tmp");
		Files.write(file, data, StandardCharsets.UTF_8);
		
		return file.toString();
	}
	
	protected String generateBusRoute(long routeId, int count)
	{
		StringBuilder sb = new StringBuilder()
				.append(routeId)
				.append(" ");
		
		for (int i = 0; i < count; i++)
		{
			sb.append(i);
			sb.append(" ");
		}
		
		return sb.toString().trim();
	}
	
	protected List<String> generateListOfBusRoutes(long routesCount, int stationsCount)
	{
		List<String> routesList = new ArrayList<>();
		
		routesList.add(Long.toString(routesCount));
		
		for (long tmpCounter = 1; tmpCounter <= routesCount; tmpCounter+= 1)
		{
			routesList.add(generateBusRoute(tmpCounter, stationsCount));
		}		

		return routesList;
	}
}
