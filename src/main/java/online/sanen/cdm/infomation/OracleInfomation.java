package online.sanen.cdm.infomation;

import java.util.List;

import online.sanen.cdm.Bootstrap;
import online.sanen.cdm.DataInformation;
import online.sanen.cdm.basic.DataField;

/**
 * 
 * @author LazyToShow Date： 2018年9月5日 Time: 下午5:41:20
 */
public class OracleInfomation extends DataInformation {

	public OracleInfomation(Bootstrap bootstrap) {
		super(bootstrap);
	}

	@Override
	public List<String> getDatabases() {
		String sql = "select tablespace_name from dba_tablespaces;";
		return bootstrap.createSQL(sql).list();
	}

	@Override
	public List<String> getTableNames() {
		String sql = "SELECT table_name FROM USER_TABLES";
		return bootstrap.createSQL(sql).list();
	}

	@Override
	public List<DataField> getFields(String tableName) {

		if (tableName.contains(".")) {
			tableName = tableName.split("\\.")[1];
		}

		String sql = "SELECT t1.COLUMN_NAME name,t1.DATA_TYPE type,(SELECT comments FROM"
				+ " all_col_comments t2 WHERE t2.TABLE_NAME=t1.TABLE_NAME\n"
				+ "   AND COLUMN_NAME=t1.COLUMN_NAME)AS \"COMMENT\" FROM all_tab_columns t1 WHERE t1.TABLE_NAME=?";

		List<DataField> list = bootstrap.createSQL(sql, tableName).addEntry(DataField.class).list();

		sql = "SELECT col.column_name FROM all_constraints con,all_cons_columns col WHERE con.constraint_name=col.constraint_name AND con.constraint_type='P' AND col.table_name=?";

		List<String> pks = bootstrap.createSQL(sql, tableName).list();

		if (pks != null)
			list.forEach(item -> {
				if (pks.contains(item.getName()))
					item.setPrimary(true);
			});

		return list;
	}

}
