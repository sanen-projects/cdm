package online.sanen.cdm.handel;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mhdt.analyse.Validate;
import com.mhdt.toolkit.Reflect;

import online.sanen.cdm.basic.BasicBean;
import online.sanen.cdm.basic.ProductType;
import online.sanen.cdm.basic.QueryType;
import online.sanen.cdm.basic.Structure;
import online.sanen.cdm.template.JdbcTemplate;

/**
 * 
 * @author LazyToShow <br>
 *         Date: 2017/11/29 <br>
 *         Time： 11：51
 */
public class BatchOperationHandel extends AbstractHandel {

	/**
	 * The original return value of batch operations is an int array, and now it is
	 * omitted to return 1 or -1 to represent whether an exception has occurred
	 */
	@Override
	public Object handel(Structure structure, Object product) {

		try {

			if ((structure.getEntrys() == null || structure.getEntrys().isEmpty())
					&& (structure.getEnMaps() == null || structure.getEnMaps().isEmpty()))
				return new NullPointerException("Batch operation data source is null");

			QueryType type = structure.getQueryType();
			JdbcTemplate template = (JdbcTemplate) structure.getTemplate();

			// Batch delete does not exist, this is to unify the interface to do the
			// adaptation,
			// delete the way to use in function, separate processing
			if (type.equals(QueryType.delete)) {

				BasicBean basicBean = structure.getEntrys().stream().findFirst().get();
				String primaryKey = structure.getPrimaryKey();

				if (Validate.isNullOrEmpty(primaryKey))
					primaryKey = Validate.isNullOrEmpty(basicBean.primarykey()) ? "id" : basicBean.primarykey();

				return batchRemove(template, structure.getEntrys(), structure.getSql(), primaryKey);
			}

			// if update or insert should add where condition by primary key
			if (type.equals(QueryType.update)) {

				try {

					BasicBean basicBean = structure.getEntrys().stream().findFirst().get();
					appendPrimaryCondition(structure.getSql(), basicBean);

				} catch (NullPointerException e) {

				}

			}

			List<Object[]> paramer_objects = new ArrayList<>();

			if (structure.getEnMaps() != null && !structure.getEnMaps().isEmpty()) {

				for (Map<String, Object> map : structure.getEnMaps()) {

					List<Object> paramers = new ArrayList<>();

					structure.getCommonFields().forEach(field -> {
						Object obj = map.get(field);

						if (obj instanceof Timestamp)
							obj = obj.toString();

						paramers.add(obj);
					});

					paramer_objects.add(paramers.toArray());
				}

			} else {

				for (BasicBean entry : structure.getEntrys()) {

					// if insert or update should add fields
					List<Object> paramers = new ArrayList<>();

					if (type.equals(QueryType.insert) || type.equals(QueryType.update))
						paramers.addAll(getValues(entry, structure.getCommonFields()));

					// if update should add primary key
					if (type.equals(QueryType.update))
						paramers.add(getPrimaryValua(entry));

					paramer_objects.add(paramers.toArray());
				}

			}

			// Handling transactions manually under Sqlite can effectively improve insert
			// speed
			if (structure.productType() != ProductType.ORACLE)
				template.execute("begin;");

			template.batchUpdate(structure.getSql().toString(), paramer_objects);

			if (structure.productType() != ProductType.ORACLE)
				template.execute("commit;");

			return 1;

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

	}

	/**
	 * Batch delete use in function
	 * 
	 * @param template
	 * @param entrys
	 * @param sql
	 * @param basicBean
	 * @return
	 */
	private Object batchRemove(JdbcTemplate template, Collection<BasicBean> entrys, StringBuilder sql,
			String primaryKey) {
		try {

			List<Object> paramers = new ArrayList<>();

			sql.append(" where " + primaryKey + " in(");

			for (BasicBean entry : entrys) {
				sql.append("?,");
				paramers.add(Reflect.getValue(entry, primaryKey));
			}

			sql.setLength(sql.length() - 1);
			sql.append(")");

			template.update(sql.toString(), paramers.toArray());
			return 1;

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

	}

	/**
	 * Add a primary key constraint
	 * 
	 * @param sql
	 * @param basicBean
	 */
	private void appendPrimaryCondition(StringBuilder sql, BasicBean basicBean) {

		String primaryKey = Validate.isNullOrEmpty(basicBean.primarykey()) ? "id" : basicBean.primarykey();
		sql.append(" where " + primaryKey + "=?");
	}

	/**
	 * Get the primary key
	 * 
	 * @param basicBean
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	private Object getPrimaryValua(BasicBean basicBean)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {

		String primaryKey = Validate.isNullOrEmpty(basicBean.primarykey()) ? "id" : basicBean.primarykey();
		return Reflect.getValue(basicBean, primaryKey);

	}

	/**
	 * Gets the value of the common field
	 * 
	 * @param entry
	 * @param commonFields
	 * @return
	 */
	private List<Object> getValues(BasicBean entry, Set<String> commonFields) {

		List<Object> list = new ArrayList<>();

		try {

			for (String field : commonFields) {
				Field f = Reflect.getField(entry, field);
				f = (f == null ? Reflect.getFieldByAlias(entry, field) : f);
				list.add(f.get(entry));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

}
