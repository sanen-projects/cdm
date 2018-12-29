package online.sanen.cdm.handel;

import java.util.Collection;

import online.sanen.cdm.basic.ProductType;
import online.sanen.cdm.basic.Structure;
import online.sanen.cdm.template.JdbcTemplate;

/**
 * 
 *
 * @author LazyToShow <br>
 *         Date: 2018/09/14 <br>
 *         Time: 10:09:06
 */
public class CreateAndInsertSqlHandel extends AbstractHandel {

	String newTableName;

	public CreateAndInsertSqlHandel(String newTableName) {
		this.newTableName = newTableName;
	}

	@Override
	public Object handel(Structure structure, Object product) {
		String modifier = ProductType.applyTableModifier(structure.productType());
		structure.getSql().insert(0, "CREATE TABLE " + modifier + newTableName + modifier + " AS ");
		Collection<Object> paramers = structure.getParamers().values();
		JdbcTemplate template = (JdbcTemplate) structure.getTemplate();
		template.update(structure.getSql().toString(), paramers.toArray());
		return getSql(structure);
	}

}
