package online.sanen.cdm.handel;

import online.sanen.cdm.basic.ProductType;
import online.sanen.cdm.basic.Structure;

/**
 * 
 * @author LazyToShow  <br>
 * Date: 2017/10/21  <br>
 * Time: 23:19
 */
public class QueryResultLimitHandel extends AbstractHandel {

	@Override
	public Object handel(Structure structure, Object product) {
		// The tag needs to be treated with limit.
		if (!structure.hasLimitAble())
			return null;

		
		boolean isSupport = ProductType.processLimit(structure.productType(), structure.getSql(),structure.getLimit());
		
		// Marked as a temporary unsupported SQL limit, which is handled by the
		// resultHandel general method.
		structure.isSupportLimit(isSupport);

		return null;
	}

}
