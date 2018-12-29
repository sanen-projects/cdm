package online.sanen.cdm;

import com.mhdt.Print;

import online.sanen.cdm.basic.QueryType;
import online.sanen.cdm.basic.ResultType;
import online.sanen.cdm.basic.Structure;
import online.sanen.cdm.component.Pipeline;
import online.sanen.cdm.factory.PipelineFactory;

/**
 * 
 * @author online.sanen <br>
 *         Date: 2017/10/21 <br>
 *         Time: 23:19
 */
public class Assembler {

	/**
	 * 
	 * @param queryType
	 * @param resultType
	 * @param structure
	 * @param factory
	 * @return
	 */
	public static Object create(QueryType queryType, ResultType resultType, Structure structure,
			PipelineFactory factory) {

		long lastTime = System.currentTimeMillis();
		structure.setQueryType(queryType);
		structure.setResultType(resultType);

		Object result = null;

		Pipeline pipeline = factory.getPipeline();

		for (Handel handel : pipeline.getHandels()) {
			Object obj = handel.handel(structure, result);
			result = (obj == null ? result : obj);

			if (pipeline.getLast() == handel) {
				print(lastTime, structure);
				return result;
			}

		}

		return null;

	}

	public static ThreadLocal<StringBuilder> threadLocal = new ThreadLocal<>();

	private static void print(long lastTime, Structure structure) {

		StringBuilder sb = threadLocal.get();

		if (structure.isShowSql()) {

			sb.append(Print.translate("WHITE", "Time: " + (System.currentTimeMillis() - lastTime) / 1000f + "s "));

			if (structure.isCache() && structure.getQueryType().equals(QueryType.select))
				sb.append("\tcache:" + CacheUtil.getInstance().getCacheInfo(structure));

			System.out.println(sb.toString() + "\r\n");
		}
	}

}
