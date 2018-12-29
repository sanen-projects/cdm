package online.sanen.cdm.handel;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.mhdt.analyse.Validate;
import com.mhdt.toolkit.Collections;
import com.mhdt.toolkit.Reflect;

import online.sanen.cdm.basic.QueryType;
import online.sanen.cdm.basic.ResultType;
import online.sanen.cdm.basic.Structure;
import online.sanen.cdm.template.GeneratedKeyHolder;
import online.sanen.cdm.template.JdbcTemplate;
import online.sanen.cdm.template.KeyHolder;
import online.sanen.cdm.template.SqlRowSet;

/**
 * The execution result
 * 
 * @author LazyToShow <br>
 * 
 *         Date: 2017/10/21 <br>
 *         Time: 23:19
 */
public class QueryResultPackingHandel extends AbstractHandel {

	@Override
	public Object handel(Structure structure, Object product) {

		QueryType queryType = structure.getQueryType();

		// Modify the operating
		if (queryType != QueryType.select) {

			if (structure.isCache())
				noticeUpdate(structure);

			return update(queryType, structure);
		}

		// Initialize the underlying data
		String sql = structure.getSql().toString();
		Collection<Object> paramers = structure.getParamers().values();
		JdbcTemplate template = (JdbcTemplate) structure.getTemplate();

		// Query operation
		ResultType resultType = structure.getResultType();

		Object result = null;

		// Try to read the results from the cache
		if (structure.isCache() && (result = tryReadFromCache(structure)) != null)
			return processCacheResult(result, resultType);

		switch (resultType) {

		case Int:
			result = queryForInt(sql, template, paramers);
			break;

		case String:
			result = queryForString(sql, template, paramers);
			break;

		case List:
			result = queryForList(structure, sql, template, paramers);
			break;

		case Maps:
			result = queryForMaps(sql, template, paramers);
			break;

		case Map:
			result = queryForMap(sql, template, paramers);
			break;

		case Object:
			result = queryForObject(structure, sql, template, paramers);
			break;

		case Bean:
			result = queryForBean(structure, sql, template, paramers);
			break;

		case Beans:
			result = queryForBeans(structure, sql, template, paramers);
			break;

		default:
			break;
		}

		// The result set requires manual limitations ?
		result = processLimit(result, structure);

		// Add the select cache
		if (structure.isCache())
			noticeAdd(structure, result);

		return result;
	}

	private Object update(QueryType queryType, Structure structure) {
		String sql = structure.getSql().toString();
		Object[] paramers = structure.getParamers().values().toArray();
		JdbcTemplate template = (JdbcTemplate) structure.getTemplate();

		if (queryType.equals(QueryType.drop)) {
			TakeIntersectionFieldsHandel takeIntersectionFieldsHandel = (TakeIntersectionFieldsHandel) Handels.getCommonFieldHandel();
			takeIntersectionFieldsHandel.remove(structure);
		}

		if (queryType.equals(QueryType.insert)) {

			KeyHolder keyHolder = new GeneratedKeyHolder();


			template.update(con -> {
				PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

				for (int i = 0; i < paramers.length; i++) {

					int parameterPosition = i + 1;
					Object argValue = paramers[i];

					try {

						if (Validate.isEnum(argValue)) {
							ps.setString(parameterPosition, String.valueOf(argValue));
						} else {
							
							Method method = Reflect.getMethod(ps, "set" + argValue.getClass().getSimpleName(),
									int.class, argValue.getClass());
							method.invoke(ps, parameterPosition, argValue);
						}

					} catch (Exception e) {
						ps.setObject(parameterPosition, argValue);
					}

				}

				return ps;
			}, keyHolder);

			try {
				return Optional.ofNullable(keyHolder.getKey()).map(mapper -> mapper.intValue()).orElse(1);
			} catch (Exception e) {
				return 1;
			}

		} else {

			return template.update(sql, paramers);
		}
	}

	/**
	 * Resolve different problems with the same SQL result set<br>
	 * For example, the first query result is a list, and the second query result is
	 * object The second result is to be processed in the cache result
	 * 
	 * @param result
	 * @param resultType
	 * @return
	 */
	private Object processCacheResult(Object result, ResultType resultType) {

		if (result instanceof List && (resultType.equals(ResultType.Object) || resultType.equals(ResultType.Bean))) {
			return ((List<?>) result).get(0);
		}

		return result;
	}

	/**
	 * The result and the second limit,SQL that is not supported by the database
	 * Here we are implementing the interface manually
	 * 
	 * @param result
	 * @param structure
	 */
	private Object processLimit(Object result, Structure structure) {
		// If result is not list type,need not process
		if (!(result instanceof List))
			return result;

		// If there is a limit operation but SQL does not support, simulate limit
		if (structure.hasLimitAble() && !structure.isSupportLimitAble()) {

			Integer[] limit = structure.getLimit();

			Integer start = 0;
			Integer end = 0;

			if (limit.length == 1) {
				end = limit[0];

			} else if (limit.length == 2) {

				start = limit[0];
				end = limit[1];
				if (end == null)
					end = -1;

			}

			// If the limit second parameter is null converted to -1, the number of access
			// is not limited
			result = Collections.limit((List<?>) result, start, end);
		}

		return result;
	}

	/**
	 * 
	 * @param structure
	 * @param sql
	 * @param template
	 * @param paramers
	 * @return
	 */
	private Object queryForList(Structure structure, String sql, JdbcTemplate template, Collection<Object> paramers) {

		if (structure.getEntry_class() != null) {
			return queryForBeans(structure, sql, template, paramers);
		} else {

			List<Map<String, Object>> list = queryForMaps(sql, template, paramers);

			if (list == null || list.isEmpty())
				return java.util.Collections.emptyList();

			return processList(list);
		}

	}

	private Object processList(List<Map<String, Object>> list) {

		if (list.get(0).keySet().size() > 1)
			return list;

		List<Object> result = new ArrayList<Object>();

		for (Map<String, Object> map : list)
			result.add(map.values().toArray()[0]);

		return result;
	}

	/**
	 * 
	 * @param structure
	 * @param sql
	 * @param template
	 * @param paramers
	 * @return
	 */
	private Object queryForBeans(Structure structure, String sql, JdbcTemplate template, Collection<Object> paramers) {

		return template.queryForEntries(structure.getEntry_class(), sql, paramers.toArray());
	}

	/**
	 * 
	 * @param structure
	 * @param sql
	 * @param template
	 * @param paramers
	 * @return
	 */
	private Object queryForBean(Structure structure, String sql, JdbcTemplate template, Collection<Object> paramers) {
		return template.queryForEntry(structure.getEntry_class(), sql, paramers.toArray());
	}

	/**
	 * 
	 * @param structure
	 * @param sql
	 * @param template
	 * @param paramers
	 * @return
	 */
	private Object queryForObject(Structure structure, String sql, JdbcTemplate template, Collection<Object> paramers) {

		if (structure.getEntry_class() != null) {
			return queryForBean(structure, sql, template, paramers);
		} else {

			List<Map<String, Object>> maps = queryForMaps(sql, template, paramers);

			if (maps == null || maps.isEmpty())
				return null;

			Map<String, Object> map = null;

			if (maps.size() > 0)
				map = maps.get(0);

			return map.size() > 1 ? map : processObjectByType(map.values().toArray()[0]);
		}

	}

	private Object processObjectByType(Object object) {
		if (object instanceof Long) {

			long obj = (long) object;

			if (obj < Integer.MAX_VALUE) {
				return (int) obj;
			}
		}

		return object;
	}

	/**
	 * 
	 * @param sql
	 * @param template
	 * @param paramers
	 * @return
	 */
	private List<Map<String, Object>> queryForMaps(String sql, JdbcTemplate template, Collection<Object> paramers) {

		List<Map<String, Object>> queryForList = template.queryForList(sql, paramers.toArray());

		if (queryForList == null || queryForList.isEmpty())
			return java.util.Collections.emptyList();

		return queryForList;

	}

	/**
	 * 
	 * @param sql
	 * @param template
	 * @param paramers
	 * @return
	 */
	private Map<String, Object> queryForMap(String sql, JdbcTemplate template, Collection<Object> paramers) {
		return template.queryForMap(sql, paramers.toArray());

	}

	/**
	 * 
	 * @param sql
	 * @param template
	 * @param paramers
	 * @return
	 */
	private Object queryForString(String sql, JdbcTemplate template, Collection<Object> paramers) {
		SqlRowSet rs = template.queryForRowSet(sql, paramers.toArray());
		while (rs.next()) {
			return rs.getString(1);
		}
		return null;
	}

	/**
	 * 
	 * @param sql
	 * @param template
	 * @param paramers
	 * @return
	 */
	private Object queryForInt(String sql, JdbcTemplate template, Collection<Object> paramers) {

		SqlRowSet rs = template.queryForRowSet(sql, paramers.toArray());

		while (rs.next())
			return rs.getInt(1);

		return -1;
	}

}
