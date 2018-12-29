package online.sanen.cdm;

import com.mhdt.Print;

import online.sanen.cdm.factory.BootStrapFactoty;

public class SqlServer {

	public static void main(String[] args) {

		try {
			Bootstrap bootstrap = BootStrapFactoty.load(obstract -> {

				obstract.setDriver("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				obstract.setUrl("jdbc:sqlserver://192.168.0.20:1433;DatabaseName=bqhis");
				obstract.setUsername("sa");
				obstract.setPassword("7788919");
			});
			
			Print.info(bootstrap.dataInformation().getFields("ba_icd10"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
