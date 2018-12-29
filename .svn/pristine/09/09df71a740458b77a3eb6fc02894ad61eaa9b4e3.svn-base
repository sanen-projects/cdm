package online.sanen.cdm.handel;

import com.mhdt.Print;
import com.mhdt.toolkit.DateUtility;

import online.sanen.cdm.Assembler;
import online.sanen.cdm.basic.Structure;

/**
 * 
 *
 * @author LazyToShow <br>
 *         Date: Dec 6, 2018 <br>
 *         Time: 6:16:58 PM
 */
public class DebugHandel extends AbstractHandel {

	@Override
	public Object handel(Structure structure, Object product) {

		StringBuilder sb = new StringBuilder("\r\n");

		if (structure.isShowSql()) {

			// [2018-12-06 17:14:58.105][SQLITE ：BI][0][SQLITE][]
			sb.append("[");
			sb.append(DateUtility.getNow("YYYY-MM-dd HH:mm:ss.ms"));
			sb.append("]");
			sb.append("[");
			sb.append(Print.translate("MAGENTA", String.valueOf(structure.productType())) + ":"
					+ Print.translate("GREEN", String.valueOf(structure.getId())));
			sb.append("]");
			sb.append("\r\n");

			sb.append(Print.translate("CYAN", structure.isFormatSql() ? formatSql(structure) : getSql(structure)));
			sb.append("\r\n");
			sb.append(Print.translate("WHITE",
					"----------------------------------------------------------------------------------------------"));
			sb.append("\r\n");
		}

		Assembler.threadLocal.set(sb);

		if (structure.isLog()) {

		}

		return null;
	}

}
