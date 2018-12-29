package online.sanen.cdm;

import java.sql.Connection;
import java.sql.SQLException;

import com.mhdt.toolkit.StringUtility;

import online.sanen.cdm.basic.CdmQueryException;
import online.sanen.cdm.basic.ProductType;
import online.sanen.cdm.component.Manager;
import online.sanen.cdm.template.JdbcTemplate;

/**
 * 
 * @author LazyToShow <br>
 *         Date: 2017/10/21 <br>
 *         Time: 23:19
 */
public class ManagerDevice implements Manager {

	JdbcTemplate template;

	String id;

	public ManagerDevice(Object id) {
		setId(id);
	}

	@Override
	public void setTemplate(JdbcTemplate template) {
		this.template = template;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(Object id) {
		this.id = id == null ? null : String.valueOf(id);
	}

	boolean isShowSql;

	@Override
	public boolean isShowSql() {
		return isShowSql;
	}

	public void setIsShowSql(boolean flag) {
		isShowSql = flag;
	}

	public JdbcTemplate getTemplate() {
		return template;
	}

	String productName;

	@Override
	public ProductType productType() {

		if (productName == null) {

			try (Connection conn = getTemplate().getDataSource().getConnection()) {
				productName = conn.getMetaData().getDatabaseProductName().toUpperCase();
			} catch (Exception e) {
				throw new CdmQueryException(e);
			}
		}

		return ProductType.valueOf(StringUtility.removeBlankChar(productName.replaceAll(" ", "_")));
	}

	@Override
	public String databaseName() {

		try (Connection conn = template.getDataSource().getConnection()) {
			return conn.getCatalog();
		} catch (Exception e) {
			throw new CdmQueryException(e);
		}

	}

	boolean isCache = true;

	@Override
	public void setIsCache(boolean isCache) {
		this.isCache = isCache;
	}

	@Override
	public boolean isCache() {
		return isCache;
	}

	boolean isLog;

	public boolean isLog() {
		return isLog;
	}

	public void setLog(boolean isLog) {
		this.isLog = isLog;
	}

	boolean sqlFormat = true;

	@Override
	public void setSqlFormat(boolean isFormat) {
		sqlFormat = isFormat;
	}

	@Override
	public boolean getSqlFormat() {
		return sqlFormat;
	}

	@Override
	public String toString() {

		try (Connection conn = template.getDataSource().getConnection()) {
			return "BootStrap Manager Configuration information " + conn.getMetaData().getURL() + "\r\n[isShowSql:"
					+ isShowSql + ", isCache:" + isCache + ", sqlFormat:" + sqlFormat + ", isLog:" + isLog + "]";

		} catch (SQLException e) {
			e.printStackTrace();
			return e.getMessage();
		}

	}

	@Override
	public void setIsLog(boolean isLog) {
		this.isLog = isLog;
	}

	String url;

	@Override
	public String getUrl() {

		try (Connection conn = ((JdbcTemplate) getTemplate()).getDataSource().getConnection()) {
			return conn.getMetaData().getURL();
		} catch (Exception e) {
			throw new CdmQueryException(e);
		}

	}

}
