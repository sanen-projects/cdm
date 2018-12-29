package online.sanen.cdm;

import online.sanen.cdm.basic.Driven;
import online.sanen.cdm.factory.BootStrapFactoty;

public class SqlLite {

	public static void main(String[] args) {

		BootStrapFactoty.load(obstract -> {
			obstract.setDriver(Driven.SQLITE);
			obstract.setUrl("jdbc:sqlite:test.sqlite");
		});

	}

}
