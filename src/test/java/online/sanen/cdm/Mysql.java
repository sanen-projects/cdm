package online.sanen.cdm;

import online.sanen.cdm.basic.Driven;
import online.sanen.cdm.basic.Sorts;
import online.sanen.cdm.condition.C;
import online.sanen.cdm.factory.BootstrapFactoty;

public class Mysql {

	public static void main(String[] args) throws Exception {

		Bootstrap bootstrap = BootstrapFactoty.load("sys", obstract -> {
			obstract.setDriver(Driven.MYSQL);
			obstract.setUrl("jdbc:mysql://127.0.0.1:3306/test?useSSL=false");
			obstract.setUsername("root");
			obstract.setPassword("root");
			obstract.setFormat(true);
		});

		Behavior.specify(Topic.class).setFelds("name").sort(Sorts.DESC, "id").addCondition(C.eq("id", 80)).limit(0, 1)
				.list();
		
		

	}

}
