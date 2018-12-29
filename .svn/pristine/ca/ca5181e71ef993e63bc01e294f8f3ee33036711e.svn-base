package online.sanen.cdm;

import java.util.Collection;
import java.util.Map;


import com.mhdt.analyse.Template;
import com.mhdt.analyse.Validate;
import com.mhdt.log.Log;
import com.mhdt.log.LogFactory;
import com.mhdt.toolkit.Assert;

import online.sanen.cdm.basic.BasicBean;
import online.sanen.cdm.basic.Cdm;
import online.sanen.cdm.basic.CdmSupportsException;
import online.sanen.cdm.component.Manager;
import online.sanen.cdm.infomation.MSInfomation;
import online.sanen.cdm.infomation.MySQLInfomation;
import online.sanen.cdm.infomation.OracleInfomation;
import online.sanen.cdm.infomation.SQLiteInfomation;
import online.sanen.cdm.template.JdbcTemplate;
import online.sanen.cdm.template.transaction.TransactionFactory;

/**
 * 
 * @author online.sanen <br>
 *         Date: 2017/10/21 <br>
 *         Time: 23:19
 */
public class BootstrapDevice implements Bootstrap {

	final Log logger = LogFactory.getLog(Bootstrap.class);

	Manager manager;

	public BootstrapDevice(JdbcTemplate jdbcTemplate) throws Exception {
		this(jdbcTemplate, true);
	}

	public BootstrapDevice(JdbcTemplate jdbcTemplate, boolean isShowSql) throws Exception {
		this(jdbcTemplate, isShowSql, false, false, null,null);
	}

	public BootstrapDevice(JdbcTemplate jdbctemplate, boolean isShowSql, boolean isCache, boolean isLog,
			TransactionFactory transactionFactory,Object id)  {

		manager = new ManagerDevice(id);
		setIsShowSql(isShowSql);
		setIsCache(isCache);
		setIsLog(isLog);
		setTemplate(jdbctemplate);
		if (transactionFactory != null)
			bindTransaction(transactionFactory);
	}

	boolean isLog;

	public boolean isLog() {
		return isLog;
	}

	public void setIsLog(boolean isLog) {
		this.isLog = isLog;
	}

	JdbcTemplate template;

	public void setTemplate(JdbcTemplate template)  {

		manager.setTemplate(template);

		if (isLog()) {
			Template tem = new Template(BootstrapDevice.class.getResourceAsStream("info.template"));
			tem.setParamer("dataBase", manager.productType().toString());
			tem.setParamer("url", manager.getUrl());
			logger.info(tem.getText());
		}

	}

	boolean isSqlFormat;

	public void setIsSqlFormat(boolean isSqlFormat) {
		manager.setSqlFormat(isSqlFormat);
	}

	boolean isShowSql;

	public void setIsShowSql(Boolean isShowSql) {
		manager.setIsShowSql(isShowSql);
	}

	boolean isCache;

	public void setIsCache(Boolean isCache) {
		manager.setIsCache(isCache);
	}

	@Override
	public QuerySql createSQL(String sql, Object... paramers) {

		if (Validate.isNullOrEmpty(sql))
			throw new NullPointerException("Sql is null");

		return new QuerySqlDevice(manager, sql, paramers);
	}

	@Override
	public QuerySql createSQL(String sql) {

		if (Validate.isNullOrEmpty(sql))
			throw new NullPointerException("Sql is null");

		return new QuerySqlDevice(manager, sql);
	}

	@Override
	public QueryEntity query(BasicBean entry) {

		if (entry == null)
			throw new NullPointerException("Entry is null");

		if (manager == null)
			throw new NullPointerException("manager is null");

		return new QueryEntityDevice(manager, entry);
	}

	@Override
	public QueryTable query(String tableName) {

		if (Validate.isNullOrEmpty(tableName))
			throw new NullPointerException("Table is null");

		return new QueryTableDevice(manager, tableName);
	}

	@Override
	public <T extends BasicBean> QueryPK<T> query(Class<T> entryCls, Object primarykey) {

		if (entryCls == null)
			throw new NullPointerException("Entry class is null");

		if (primarykey == null)
			throw new NullPointerException("Primary Key is null");

		return new QueryPKDevice<T>(manager, entryCls, primarykey);
	}

	@SuppressWarnings("unchecked")
	@Override
	public QueryEntity query(Collection<? extends BasicBean> entrys) {
		return new QueryEntityDevice(manager, (Collection<BasicBean>) entrys);
	}

	@Override
	public Manager manager() {
		return manager;
	}

	DataInformation dataInformation;

	@Override
	public DataInformation dataInformation() {

		if (dataInformation != null)
			return dataInformation;

		switch (manager().productType()) {
		case MYSQL:
			dataInformation = new MySQLInfomation(this);
			break;

		case MICROSOFT_SQL_SERVER:
			dataInformation = new MSInfomation(this);
			break;

		case SQLITE:
			dataInformation = new SQLiteInfomation(this);
			break;

		case ORACLE:
			dataInformation = new OracleInfomation(this);
			break;

		default:
			throw new CdmSupportsException(manager().productType());
		}

		return dataInformation;
	}

	@Override
	public QueryMap query(String tableName, Map<String, Object> map) {
		
		Assert.notNull(tableName, "TableName is null");
		Assert.notNull(map, "Unable to create table ，map is null");

		return new QueryMapDevice(manager, tableName, map);
	}

	@Override
	public QueryMap query(String tableName, Collection<Map<String, Object>> entrys) {
		if (entrys == null || entrys.isEmpty())
			throw new NullPointerException("Entrys is null or empty.");

		return new QueryMapDevice(manager, tableName, entrys);
	}

	@Override
	public <T extends BasicBean> QueryTable query(Class<T> cls) {
		return query(Cdm.table(cls));
	}

	@Override
	public void bindTransaction(TransactionFactory factory) {
		manager.getTemplate().bindTransaction(factory);
	}

}
