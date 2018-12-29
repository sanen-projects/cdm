package online.sanen.cdm.factory;

import online.sanen.cdm.Bootstrap;
import online.sanen.cdm.basic.Driven;
import online.sanen.cdm.basic.Obstract.DataSouseType;
import online.sanen.cdm.factory.BootstrapFactoty;

public class Oracle {

	public static void main(String[] args) {

		try {

			Bootstrap bootstrap = BootstrapFactoty.load("oracle", obstract -> {

				obstract.setDataSouseType(DataSouseType.Dbcp);
				obstract.setDriver(Driven.ORACLE);
				obstract.setUrl("jdbc:oracle:thin:@//127.0.0.1:1521/orcl");
				obstract.setUsername("username");
				obstract.setPassword("password");
				obstract.setLog(true);
			});
			
			
			bootstrap.dataInformation().getFields("");


		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
