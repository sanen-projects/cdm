package online.sanen.cdm.handel;

import com.mhdt.analyse.Validate;
import com.mhdt.degist.DegistTool;
import com.mhdt.degist.DegistTool.Encode;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import online.sanen.cdm.CacheUtil;
import online.sanen.cdm.Handel;
import online.sanen.cdm.basic.QueryType;
import online.sanen.cdm.basic.Structure;

/**
 * <pre>
 * 
 * @author LazyToShow
 *  Date: 2017/10/21 
 *  Time: 23:19
 * </pre>
 */
public abstract class AbstractHandel implements Handel {

	protected void noticeUpdate(Structure structure) {
		if (structure.getQueryType().equals(QueryType.select))
			return;

		String tabelName = Validate.isNullOrEmpty(structure.getTableName()) ? CacheUtil.defaultCache
				: structure.getTableName();
		Cache cache = CacheUtil.getInstance().getCache(tabelName);
		if (cache == null)
			return;

		cache.removeAll();
	}

	protected Object tryReadFromCache(Structure structure) {
		if (!structure.getQueryType().equals(QueryType.select))
			return null;
		String tabelName = Validate.isNullOrEmpty(structure.getTableName()) ? CacheUtil.defaultCache
				: structure.getTableName();
		String md5 = DegistTool.md5(getSql(structure),Encode.HEX);
		return CacheUtil.getInstance().get(tabelName, md5);

	}

	protected void noticeAdd(Structure structure, Object result) {
		if (!structure.getQueryType().equals(QueryType.select))
			return;

		String tabelName = Validate.isNullOrEmpty(structure.getTableName()) ? CacheUtil.defaultCache
				: structure.getTableName();
		String md5 = DegistTool.md5(getSql(structure),Encode.HEX);

		Cache cache = CacheUtil.getInstance().getCache(tabelName);
		if (cache == null)
			cache = CacheUtil.getInstance().createCache(tabelName);

		cache.put(new Element(md5, result));
	}

	protected String getSql(Structure structure) {

		String sql = structure.getSql().toString();
		
		sql = sql.replace("?", "#{?}");

		for (Object obj : structure.getParamers().values()) {
			sql = sql.replaceFirst("#\\{\\?\\}", processObj(obj));
		}

		
		return sql;
	}

	// 换行且缩进
	static String[] Keywords = new String[] { "select", "insert into", "delete", "update", "from", "group by", "where",
			" values", "order by", "limit", "set" };
	// 换行
	static String[] Keywords2 = new String[] { " and ", " having " };

	// 大写
	static String[] Keywords3 = new String[] { " id ", " no ", " desc ", " set "," top " };

	public String formatSql(Structure structure) {
		
		String sql = structure.getSql().toString();
		
		for (String keyword : Keywords)
			sql = sql.replaceAll(keyword + "[\\s]+", "\r\n" + keyword.toUpperCase() + "\r\n ");

		for (String keyword : Keywords2)
			sql = sql.replaceAll(keyword, "\r\n  " + keyword.toUpperCase());

		for (String keyword : Keywords3)
			sql = sql.replaceAll(keyword, keyword.toUpperCase());

		structure.setSql(sql);
		
		return getSql(structure);
	}

	private String processObj(Object obj) {
		if (Validate.isNullOrEmpty(obj))
			return "" + obj;
		
		String str = obj.toString();
		if(str.length()>100)
			str = "[TEXT TOO LONG]";
		return (obj instanceof Integer) ? str : "'" + str + "'";
	}

}
