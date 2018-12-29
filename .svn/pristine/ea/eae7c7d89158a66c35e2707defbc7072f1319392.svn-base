package online.sanen.cdm.handel;

import static online.sanen.cdm.condition.C.buid;

import online.sanen.cdm.Handel;
import online.sanen.cdm.basic.Structure;

/**
 * <pre>
 * &#64;author LazyToShow
 * Date: 2017/11/23
 * Time: 16:15
 * </pre>
 */
public class PrimaryKeyAsConditionHandel implements Handel {

	@Override
	public Object handel(Structure structure, Object product) {

		structure.addCondition(buid(structure.getPrimaryKey()).eq(structure.getPrimaryValue()));
		return null;
	}

}
