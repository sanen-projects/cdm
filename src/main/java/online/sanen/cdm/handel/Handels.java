package online.sanen.cdm.handel;

import online.sanen.cdm.Handel;
import online.sanen.cdm.handel.AssemblyConditionsHandel;
import online.sanen.cdm.handel.DebugHandel;
import online.sanen.cdm.handel.ModifyParametersHandel;
import online.sanen.cdm.handel.QueryResultPackingHandel;
import online.sanen.cdm.handel.SqlConstructHandel;
import online.sanen.cdm.handel.TakeIntersectionFieldsHandel;

/**
 * 
 *<pre>
 * @author LazyToShow
 * Date: 2017/10/21
 * Time: 23:19
 * </pre>
 */
public class Handels {
	
	static Handel sqlHandel;
	
	static Handel tableFieldHandel;
	
	static Handel conditionHandel;
	
	static Handel queryHandel;
	
	static Handel paramerHandel;
	
	static Handel debugHandel;
	
	public static Handel getSqlHandel() {
		if(sqlHandel==null)
			sqlHandel = new SqlConstructHandel();
		
		return sqlHandel;
	}
	
	
	public static Handel getCommonFieldHandel() {
		if(tableFieldHandel==null)
			tableFieldHandel = new TakeIntersectionFieldsHandel();
		
		return tableFieldHandel;
	}
	
	public static Handel getConditionHandel() {
		if(conditionHandel==null)
			conditionHandel = new AssemblyConditionsHandel();
		
		return conditionHandel;
	}
	
	
	public static Handel getResultHandel() {
		if(queryHandel==null)
			queryHandel = new QueryResultPackingHandel();
		
		return queryHandel;
	}
	
	public static Handel getParamerHandel() {
		if(paramerHandel==null)
			paramerHandel = new ModifyParametersHandel();
		
		return paramerHandel;
	}
	
	public static Handel getDebugHandel() {
		if(debugHandel==null)
			debugHandel = new DebugHandel();
		
		return debugHandel;
	}
	
	

}
