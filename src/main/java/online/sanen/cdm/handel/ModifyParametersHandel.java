package online.sanen.cdm.handel;

import com.mhdt.toolkit.Reflect;

import online.sanen.cdm.Handel;
import online.sanen.cdm.basic.Structure;

/**
 * Modify modify operation parameters.
 * 
 * @author LazyToShow
 * Date: 2017/10/21 
 * Time: 23:19
 */
public class ModifyParametersHandel implements Handel {

	@Override
	public Object handel(Structure structure,Object product) {
		
		//Add the sort statement after the condition is processed
		if(structure.getSortSupport()!=null)
			structure.getSql().append(structure.getSortSupport().toString());

		// If you customize the parameters
		if (structure.getParamers() != null && structure.getParamers().size() > 0)
			return null;

		switch (structure.getQueryType()) {

		case insert:
			processParamers(structure);
			break;

		case update:
			processParamers(structure);
			break;
			
		default:
			break;
		}

		return null;
	}


	private void processParamers(Structure structure) {
		
		int index = 1;
		
		for (String column : structure.getCommonFields())
			if(structure.getEntry()!=null)
				structure.addParamer(index++, Reflect.getInject(structure.getEntry(), column));
			else
				structure.addParamer(index++, structure.getEnMap().get(column));
		
	}

}
