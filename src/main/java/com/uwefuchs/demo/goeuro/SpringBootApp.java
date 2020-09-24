package com.uwefuchs.demo.goeuro;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * application-class for our microservice.
 * 
 * @author info@uwefuchs.com
 */
@SpringBootApplication
public class SpringBootApp
{
	private static final Logger LOG = LoggerFactory.getLogger(SpringBootApp.class);

	public static void main(String[] args)
	{
		ApplicationContext ctx = SpringApplication.run(SpringBootApp.class, args);
		printProvidedSpringBeans(ctx);
	}

	private static void printProvidedSpringBeans(ApplicationContext ctx)
	{
		LOG.info("Let's inspect the beans provided by Spring Boot:");

		String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);

		for (String beanName : beanNames)
		{
			LOG.info(beanName);
		}
	}
}
