package online.sanen.cdm;

import online.sanen.cdm.basic.Driven;
import online.sanen.cdm.basic.Obstract.DataSouseType;
import online.sanen.cdm.factory.BootstrapFactoty;

public class Oracle {

	public static void main(String[] args) {

		try {

			Bootstrap bootstrap = BootstrapFactoty.load("123", obstract -> {

				obstract.setDataSouseType(DataSouseType.Dbcp);
				obstract.setDriver(Driven.ORACLE);
				obstract.setUrl("jdbc:oracle:thin:@//127.0.0.1:1521/orcl");
				obstract.setUsername("bi");
				obstract.setPassword("123");
				obstract.setLog(true);
			});
			
			
			bootstrap.dataInformation().getFields("");


		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
