package online.sanen.cdm.handel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import online.sanen.cdm.Handel;
import online.sanen.cdm.basic.CdmQueryException;
import online.sanen.cdm.basic.DataField;
import online.sanen.cdm.basic.Structure;
import online.sanen.cdm.template.JdbcTemplate;

/**
 * 
 * Extract field information from Sql statements
 *
 * @author LazyToShow <br>
 *         Date: 2018/09/10 <br>
 *         Time: PM 9:31:42
 */
public class SqlColumnsExtractHandel implements Handel {

	@Override
	public Object handel(Structure structure, Object product) {

		String sql = structure.getSql().toString();

		JdbcTemplate template = (JdbcTemplate) structure.getTemplate();

		try (Connection connection = template.getDataSource().getConnection()) {

			PreparedStatement ps = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY,
					ResultSet.CONCUR_READ_ONLY);

			initFetchSize(ps, structure.productType());

			ResultSet rs = ps.executeQuery();

			// Assembly fields
			ResultSetMetaData metaData = rs.getMetaData();

			List<DataField> dataFields = new ArrayList<>();

			for (int i = 0; i < metaData.getColumnCount(); i++) {

				DataField dataField = new DataField();

				dataField.setName(metaData.getColumnLabel(i + 1));
				dataField.setCls(Class.forName(metaData.getColumnClassName(i + 1)));
				dataField.setType(metaData.getColumnTypeName(i + 1));
				dataFields.add(dataField);
			}

			return dataFields;

		} catch (Exception e) {
			throw new CdmQueryException(e);
		}

	}

}
