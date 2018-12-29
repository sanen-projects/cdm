package online.sanen.cdm.infomation;

import java.util.List;

import com.mhdt.toolkit.Assert;

import online.sanen.cdm.Bootstrap;
import online.sanen.cdm.DataInformation;
import online.sanen.cdm.basic.DataField;

/**
 * 
 * @author LazyToShow Date: 2018/06/12 Time: 09:17
 */
public class MySQLInfomation extends DataInformation {

	public MySQLInfomation(Bootstrap bootstrap) {
		super(bootstrap);
	}

	@Override
	public List<String> getDatabases() {

		String sql = "SHOW DATABASES";

		return bootstrap.createSQL(sql).list();
	}

	@Override
	public List<String> getTableNames() {

		String sql = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA =?";

		return bootstrap.createSQL(sql, bootstrap.manager().databaseName()).list();
	}

	@Override
	public List<DataField> getFields(String tableName) {

		Assert.notNullOrEmpty(tableName, "TableName is null or empty");

		String sql = "SELECT COLUMN_NAME name,DATA_TYPE type,COLUMN_COMMENT comment,COLUMN_KEY  FROM INFORMATION_SCHEMA.`COLUMNS` WHERE TABLE_NAME=? AND TABLE_SCHEMA=?";

		return bootstrap.createSQL(sql, tableName, bootstrap.manager().databaseName()).addEntry(DataField.class).entities();
	}

}
