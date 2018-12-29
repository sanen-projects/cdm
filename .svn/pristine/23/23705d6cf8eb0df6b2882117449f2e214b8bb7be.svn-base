package online.sanen.cdm.factory;

import java.util.Properties;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mhdt.toolkit.Assert;

import online.sanen.cdm.basic.Driven;
import online.sanen.cdm.basic.Obstract;

/**
 * 
 * @author LazyToShow <br>
 *         Date: 2018/06/12 <br>
 *         Time: 09:17
 */
public class DataSourceFactory {

	public static DataSource create(final Obstract obstract) throws Exception {


			switch (obstract.getDataSouseType()) {

			case Druid:
				DruidDataSource druidDataSource = (DruidDataSource) DruidDataSourceFactory
						.createDataSource(createProperties(obstract));
				druidDataSource.setTestOnBorrow(false);
				druidDataSource.setRemoveAbandoned(false);
				druidDataSource.setMaxWait(5000);

				if (obstract.getDriver().equals(Driven.ORACLE.getValue())) {
					druidDataSource.setValidationQuery("SELECT 1 FROM DUAL");
				} else {
					druidDataSource.setValidationQuery("SELECT 1");
				}

				return druidDataSource;

			default:
				BasicDataSource dataSource = BasicDataSourceFactory.createDataSource(createProperties(obstract));

				if (obstract.getDriver().equals(Driven.ORACLE.getValue())) {
					dataSource.setValidationQuery("SELECT 1 FROM DUAL");
				} else {
					dataSource.setValidationQuery("SELECT 1");
				}
				return dataSource;
			}


	}

	private static Properties createProperties(Obstract obstract) {
		
		Assert.notNull(obstract.getDriver(), "Driver is null from Obstract");
		Assert.notNull(obstract.getUrl(), "Url is null from Obstract");

		return new Properties() {
			private static final long serialVersionUID = 1L;
			{
				setProperty("driverClassName", obstract.getDriver());
				setProperty("url", obstract.getUrl());
				setProperty("username", obstract.getUsername());
				setProperty("password", obstract.getPassword());
				setProperty("validationQuery", "select 1");
			}
		};

	}

}
