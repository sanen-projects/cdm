package online.sanen.cdm.handel;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import com.mhdt.toolkit.Reflect;

import online.sanen.cdm.Handel;
import online.sanen.cdm.basic.CdmQueryException;
import online.sanen.cdm.basic.DataField;
import online.sanen.cdm.basic.StreamConsumer;
import online.sanen.cdm.basic.Structure;
import online.sanen.cdm.template.JdbcTemplate;

/**
 * 
 *
 * @author LazyToShow <br>
 *         Date: 2018年9月11日 <br>
 *         Time: 上午10:56:44
 */
public class StreamHandel implements Handel {

	private Consumer<List<Map<String, Object>>> consumer;

	private Map<String, String> aliases;

	private int bufferSize = 10000;

	private int count;

	public StreamHandel(int bufferSize, Consumer<List<Map<String, Object>>> datas) {
		this(bufferSize, datas, null);
	}

	public StreamHandel(int bufferSize, Consumer<List<Map<String, Object>>> consumer, Map<String, String> aliases) {
		this.consumer = consumer;
		this.aliases = aliases;
		if (bufferSize > 0)
			this.bufferSize = bufferSize;
	}

	public StreamHandel(int count) {
		this.count = count;
	}

	Function<List<DataField>, Object> function;

	StreamConsumer streamConsumer;

	public StreamHandel(int bufferSize, Function<List<DataField>, Object> consumer, StreamConsumer datas,
			Map<String, String> aliases) {
		if (bufferSize > 0)
			this.bufferSize = bufferSize;

		this.function = consumer;
		this.streamConsumer = datas;
		this.aliases = aliases;
	}

	@Override
	public Object handel(Structure structure, Object product) {

		String sql = structure.getSql().toString();

		Collection<Object> paramers = structure.getParamers().values();

		JdbcTemplate template = (JdbcTemplate) structure.getTemplate();

		try (Connection connection = template.getDataSource().getConnection()) {

			// connection.setAutoCommit(false);
			PreparedStatement ps = connection.prepareStatement(sql);

			initFetchSize(ps, structure.productType());

			// Set the parameters
			int index = 1;
			for (Iterator<Object> iterator = paramers.iterator(); iterator.hasNext();) {
				Object object = (Object) iterator.next();

				try {

					Method method = Reflect.getMethod(ps, "set" + object.getClass().getSimpleName(), int.class,
							object.getClass());

					method.invoke(ps, index++, object);

				} catch (Exception e) {
					ps.setString(index++, object.toString());
				}
			}

			ResultSet rs = ps.executeQuery();

			// Assembly fields
			ResultSetMetaData metaData = rs.getMetaData();

			List<DataField> dataFields = new ArrayList<>();

			List<String> columns = new ArrayList<>();
			for (int i = 0; i < metaData.getColumnCount(); i++) {

				DataField dataField = new DataField();
				dataField.setName(metaData.getColumnLabel(i + 1));
				dataField.setCls(Class.forName(metaData.getColumnClassName(i + 1)));
				dataField.setType(metaData.getColumnTypeName(i + 1));
				dataFields.add(dataField);
				columns.add(dataField.getName());
			}

			List<Map<String, Object>> list = new LinkedList<>();

			Object object = null;

			if (function != null) {
				object = function.apply(dataFields);
			}

			if (count > 0) {

				while (rs.next()) {
					list.add(populate(rs, columns));
					if (list.size() == count)
						break;

				}

				return list;

			} else {

				while (rs.next()) {
					list.add(populate(rs, columns));
					// Writes cached data
					if (list.size() == bufferSize) {

						if (streamConsumer != null) {
							streamConsumer.accept(object, list);
						} else {
							consumer.accept(list);
						}

						list.clear();
					}
				}

				if (list.size() > 0) {
					if (streamConsumer != null) {
						streamConsumer.accept(object, list);
					} else {
						consumer.accept(list);
					}
				}

			}

		} catch (Exception e) {
			throw new CdmQueryException(e.getMessage());
		}

		return null;
	}

	private Map<String, Object> populate(ResultSet rs, List<String> columns) {

		Map<String, Object> map = new HashMap<>(columns.size());

		columns.forEach(field -> {

			try {

				String key = (aliases != null && aliases.containsKey(field)) ? aliases.get(field) : field;
				Object value = rs.getObject(field);

				map.put(key, value);

			} catch (SQLException e) {
				throw new CdmQueryException(e);
			}
		});

		return map;
	}

}
