package online.sanen.cdm.factory;

import org.junit.Test;

import online.sanen.cdm.basic.Driven;
import online.sanen.cdm.factory.BootstrapFactoty;

public class MysqlBootstrapFactoryTest {

	@Test
	public void testMysql() {
		
		BootstrapFactoty.load("sys", obstract -> {
			obstract.setDriver(Driven.MYSQL);
			obstract.setUrl("jdbc:mysql://127.0.0.1:3306/test?useSSL=false");
			obstract.setUsername("root");
			obstract.setPassword("root");
			obstract.setFormat(true);
		});

	}
}
