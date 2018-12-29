package online.sanen.cdm.handel;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.mhdt.analyse.Validate;
import com.mhdt.annotation.Alias;
import com.mhdt.annotation.NoDB;
import com.mhdt.annotation.NoInsert;
import com.mhdt.annotation.NoUpdate;
import com.mhdt.degist.DegistTool;
import com.mhdt.degist.DegistTool.Encode;
import com.mhdt.toolkit.Reflect;

import online.sanen.cdm.Handel;

import online.sanen.cdm.basic.ProductType;
import online.sanen.cdm.basic.QueryType;
import online.sanen.cdm.basic.Structure;
import online.sanen.cdm.template.JdbcTemplate;

/**
 * <pre>
 * Field processing, which takes the intersection of entry and database tables,
 * the type of which is the entry .
 * 
 * &#64;author LazyToShow 
 * Date: 2017/10/21 
 * Time: 23:19
 * </pre>
 */
public class TakeIntersectionFieldsHandel implements Handel {

	static Map<String, HashSet<String>> tableColumnsCache = new HashMap<String, HashSet<String>>();

	@Override
	public Object handel(Structure structure, Object product) {

		// If the fields are specified
		if (structure.getFields() != null && structure.getFields().size() > 0) {
			structure.setCommonFields(structure.getFields());
			return null;

		}

		// Get the table field
		String tableName = structure.getTableName();

		// The query table has all the fields
		Set<String> tableFields = getTableFields(tableName, structure);
		//com.mhdt.Print.info("tableFields:"+tableFields);
		
		Set<String> entryFields = getEntryField(structure);
		//com.mhdt.Print.info("entryFields:"+tableFields);
		
		// Table fields and class fields are collected and set
		if (entryFields == null)
			entryFields = tableFields;
		else {
			entryFields = new HashSet<>(entryFields.stream().filter(item -> {
				item = item.toUpperCase();
				for (String s : tableFields) {
					if (s.toUpperCase().equals(item))
						return true;
				}
				return false;
			}).collect(Collectors.toList()));
		}

		structure.setCommonFields(entryFields);
		
		//com.mhdt.Print.info("commonFields:"+entryFields);

		return null;
	}

	private Set<String> getEntryField(Structure structure) {

		if (structure.getEnMap() != null) {
			
			Set<String> fs = structure.getEnMap().keySet();
			
			if (structure.getExceptes() != null)
				fs.removeAll(structure.getExceptes());
			
			return fs;
		}

		if (structure.getEnMaps() != null) {
			
			Set<String> fs = structure.getEnMaps().stream().findFirst().get().keySet();
			
			if (structure.getExceptes() != null)
				fs.removeAll(structure.getExceptes());
			
			return fs;
		}

		Class<?> cls = structure.getEntry_class();
		
		if (cls == null)
			return null;

		QueryType queryType = structure.getQueryType();
		Set<String> columns = new HashSet<>();

		for (Field field : Reflect.getFields(cls)) {

			// Annotations to skip
			if (Validate.hasAnnotation(field, NoDB.class))
				continue;

			// Annotations to skip
			if (queryType.equals(QueryType.update) && Validate.hasAnnotation(field, NoUpdate.class))
				continue;

			// Annotations to skip
			if (queryType.equals(QueryType.insert) && Validate.hasAnnotation(field, NoInsert.class))
				continue;

			// Annotations to skip
			if (structure.getExceptes() != null && structure.getExceptes().stream()
					.anyMatch(item -> item.toLowerCase().equals(field.getName().toLowerCase())))
				continue;

			// The alias is preferred
			if (Validate.hasAnnotation(field, Alias.class)) {
				columns.add(Reflect.getColumnValue(field).toLowerCase());
				continue;
			}

			// Add field name
			columns.add(field.getName().toLowerCase());
		}

		return columns;

	}

	/**
	 * 
	 * @param tableName
	 * @param structure
	 * @return
	 * @throws SQLException
	 */
	private HashSet<String> getTableFields(String tableName, Structure structure) {

		String md5 = DegistTool.md5(structure.getUrl() + tableName + "CDM", Encode.HEX);

		// If there is a cache, return directly
		if (tableColumnsCache.containsKey(md5)) {
			return tableColumnsCache.get(md5);
		}

		// Find all the fields of the table through Sql
		JdbcTemplate template = (JdbcTemplate) structure.getTemplate();
		ProductType productType = structure.productType();
		HashSet<String> result = ProductType.getColumnsFromTableName(productType, template, tableName);

		// Join the cache
		tableColumnsCache.put(md5, result);
		
		return result;
	}

	public void remove(Structure structure) {
		tableColumnsCache.remove( DegistTool.md5(structure.getUrl() + structure.getTableName() + "CDM", Encode.HEX));
	}

}
