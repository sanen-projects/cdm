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
public class MSInfomation extends DataInformation {

	public MSInfomation(Bootstrap bootstrap) {
		super(bootstrap);
	}

	@Override
	public List<String> getDatabases() {
		String sql = "SELECT name FROM  master..sysdatabases WHERE name NOT IN ( 'master', 'model', 'msdb', 'tempdb', 'northwind','pubs' )  ";
		return bootstrap.createSQL(sql).list();
	}

	@Override
	public List<String> getTableNames() {
		String sql = "SELECT  o.name FROM sysobjects o LEFT JOIN sysusers u ON o.uid = u.uid LEFT JOIN ::fn_listextendedproperty('MS_Description', 'USER', 'dbo', 'TABLE', NULL, NULL, NULL) p ON o.name = p.objname COLLATE database_default LEFT JOIN sysindexes i ON o.id = i.id AND i.indid < 2 WHERE u.name = 'dbo' AND (o.xtype='U' OR o.xtype='S') ORDER BY o.name";
		return bootstrap.createSQL(sql).list();
	}

	@Override
	public List<DataField> getFields(String tableName) {

		String sql = "SELECT  syscolumns.name AS name,systypes.name AS type, CONVERT(char,sysproperties.[value]) AS comment FROM sysproperties RIGHT OUTER JOIN sysobjects INNER JOIN syscolumns ON sysobjects.id = syscolumns.id INNER JOIN systypes ON syscolumns.xtype = systypes.xtype ON sysproperties.id = syscolumns.id AND sysproperties.smallid = syscolumns.colid WHERE (sysobjects.xtype = 'u' OR sysobjects.xtype = 'v') AND (systypes.name <> 'sysname') AND (sysobjects.name = ?)";
		return bootstrap.createSQL(sql, tableName).addEntry(DataField.class).list();
	}

}
