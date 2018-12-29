package online.sanen.cdm.handel;

import static com.mhdt.toolkit.Assert.notNullOrEmpty;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.mhdt.analyse.Validate;
import com.mhdt.toolkit.Bean;

import online.sanen.cdm.Handel;
import online.sanen.cdm.QuerySql;
import online.sanen.cdm.SqlConversion;
import online.sanen.cdm.basic.Cdm;
import online.sanen.cdm.basic.CdmConditionException;
import online.sanen.cdm.basic.CdmQueryException;
import online.sanen.cdm.basic.ProductType;
import online.sanen.cdm.basic.Structure;
import online.sanen.cdm.resource.R;

/**
 * 根据structue构造sql
 * @author LazyToShow <br>
 *         Date: 2017/10/21 <br>
 *         Time: 23:19
 */
public class SqlConstructHandel implements Handel {

	@Override
	public Object handel(Structure structure, Object product) {

		structure.getParamers().clear();

		// 如果指定了sql 直接返回
		if (structure.getCls() != null && structure.getClass().isAssignableFrom(QuerySql.class))
			return null;

		structure.getSql().setLength(0);

		// 获取表名
		String tableName = Cdm.processTableName(structure.getTableName(), structure.productType());

		// 表名为空判定为严重异常，终止进程
		if (Validate.isNullOrEmpty(tableName))
			throw new CdmConditionException(R.strings.Exception_TableName_Null);

		// 根据query类别来生成Sql
		switch (structure.getQueryType()) {
		case insert:
			createInsertSql(tableName, structure);
			break;

		case delete:
			createRemoveSql(tableName, structure);
			break;
		case update:
			createUpdateSql(tableName, structure);
			break;

		case select:
			createSelectSql(tableName, structure);
			break;

		case create:
			createCreateSql(tableName, structure);
			break;

		case drop:
			createDropSql(tableName, structure);
			break;
		}

		return null;
	}

	private void createDropSql(String tableName, Structure structure) {

		structure.setSql("DROP TABLE " + tableName);
	}

	private void createCreateSql(String tableName, Structure structure) {

		Map<String, Class<?>> map = new ConcurrentHashMap<>();

		// Convert Entry into the structure ( name-> typeClass )
		if (structure.getEnMap() != null) {

			for (Map.Entry<String, Object> entry : structure.getEnMap().entrySet()) {

				if (entry.getValue() == null)
					map.put(entry.getKey(), String.class);
				else
					map.put(entry.getKey(), entry.getValue().getClass());
			}

		} else {
			map = new ConcurrentHashMap<>(Bean.structured(structure.getEntry()));
			structure.setPrimaryKey(Cdm.getPrimaryKey(structure.getEntry()));
		}

		// Filter field
		if (structure.getFields() != null) {

			for (Map.Entry<String, Class<?>> entry : map.entrySet())
				if (!structure.getFields().contains(entry.getKey()))
					map.remove(entry.getKey());

		} else if (structure.getExceptes() != null)
			structure.getExceptes().forEach(map::remove);

		notNullOrEmpty(map, "An empty table cannot be created because no valid reserved fields are specified.");

		String sql = "CREATE TABLE " + tableName;
		sql += "(" + organizationFields(structure.getPrimaryKey(), structure.productType(), map) + ")";

		structure.setSql(sql);
	}

	/**
	 * 
	 * @param primaryKey
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	private static String organizationFields(String primaryKey, ProductType productType, Map<String, Class<?>> map) {
		StringBuilder sb = new StringBuilder();

		String modifer = ProductType.applyTableModifier(productType);

		for (Map.Entry<String, Class<?>> entry : map.entrySet()) {

			sb.append(modifer + entry.getKey() + modifer + " ");
			sb.append("${" + entry.getValue().getSimpleName().toUpperCase() + "}");

			if (primaryKey != null && primaryKey.equals(entry.getKey()) && Validate.isNumber(entry.getValue()))
				sb.append(" ${PRIMARY}");

			sb.append(",");
		}

		if (sb.length() > 0)
			sb.setLength(sb.length() - 1);

		return new SqlConversion() {
			@Override
			public ProductType applyProductType() {
				return productType;
			}

			@Override
			public String applyPrimaryKey() {
				return primaryKey;
			}
		}.apply(sb.toString());
	}

	private void createSelectSql(String tableName, Structure structure) {

		StringBuilder sb = structure.getSql();
		String modifer = ProductType.applyTableModifier(structure.productType());

		sb.append("SELECT ");
		for (String field : structure.getCommonFields()) {
			sb.append(modifer + field + modifer + ",");
		}
		sb.setLength(sb.length() - 1);
		sb.append(" FROM " + tableName);
	}

	private void createUpdateSql(String tableName, Structure structure) {

		String modifer = ProductType.applyTableModifier(structure.productType());

		structure.getSql().append("UPDATE " + tableName + " SET ");

		for (String field : structure.getCommonFields()) {
			structure.getSql().append(modifer + field + modifer + "=?,");
		}

		structure.getSql().setLength(structure.getSql().length() - 1);

	}

	private void createRemoveSql(String tableName, Structure structure) {
		structure.getSql().append("DELETE FROM " + tableName);

	}

	private void createInsertSql(String tableName, Structure structure) {

		if (structure.getCommonFields().isEmpty())
			throw new CdmQueryException("Cannot get generic fields from table: " + tableName);

		String modifier = ProductType.applyTableModifier(structure.productType());

		String sql = "INSERT INTO " + tableName + " (";
		String sql1 = " values (";

		for (String field : structure.getCommonFields()) {
			sql += modifier + field + modifier + ",";
			sql1 += "?,";
		}

		sql = sql.substring(0, sql.lastIndexOf(",")) + ")";
		sql1 = sql1.substring(0, sql1.lastIndexOf(",")) + ")";

		structure.getSql().append(sql + sql1);
	}

}
