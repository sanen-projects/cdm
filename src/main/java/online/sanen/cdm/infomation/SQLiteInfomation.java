package online.sanen.cdm.infomation;

import java.util.List;

import online.sanen.cdm.Bootstrap;
import online.sanen.cdm.DataInformation;
import online.sanen.cdm.basic.DataField;

/**
 * 
 * @author LazyToShow
 * Date: 2018/06/12
 * Time: 09:17
 */
public class SQLiteInfomation extends DataInformation{
	
	public SQLiteInfomation(Bootstrap bootstrap) {
		super(bootstrap);
	}

	@Override
	public List<String> getDatabases() {
		
		return null;
	}

	@Override
	public List<String> getTableNames() {
		
		String sql = "SELECT name FROM sqlite_master where name<>'sqlite_sequence'";
		return bootstrap.createSQL(sql).list();
	}

	@Override
	public List<DataField> getFields(String tableName) {
		String sql = "PRAGMA table_info('"+tableName+"')";
		return bootstrap.createSQL(sql).addEntry(DataField.class).list();
	}

}
