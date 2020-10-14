package com.uwefuchs.demo.goeuro;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@TestPropertySource(properties = {"pathname=./src/test/resources/busroutes.dat"})
@SpringBootTest
public class SpringBootAppTest {

  @Test
  public void contextLoads() {
  }
}
