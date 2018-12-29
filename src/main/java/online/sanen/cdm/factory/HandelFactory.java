package online.sanen.cdm.factory;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import online.sanen.cdm.Handel;
import online.sanen.cdm.basic.DataField;
import online.sanen.cdm.basic.StreamConsumer;
import online.sanen.cdm.handel.BatchOperationHandel;
import online.sanen.cdm.handel.TakeIntersectionFieldsHandel;
import online.sanen.cdm.handel.AssemblyConditionsHandel;
import online.sanen.cdm.handel.CreateAndInsertSqlHandel;
import online.sanen.cdm.handel.DebugHandel;
import online.sanen.cdm.handel.QueryResultLimitHandel;
import online.sanen.cdm.handel.ModifyParametersHandel;
import online.sanen.cdm.handel.PrimaryKeyAsConditionHandel;
import online.sanen.cdm.handel.SqlColumnsExtractHandel;
import online.sanen.cdm.handel.QueryResultPackingHandel;
import online.sanen.cdm.handel.SqlConstructHandel;
import online.sanen.cdm.handel.StreamHandel;

/**
 * 
 *<pre>
 * @author LazyToShow
 * Date: 2017/10/21
 * Time: 23:19
 * </pre>
 */
public class HandelFactory {
	
	static Handel sqlHandel;
	
	public static Handel sqlHandel() {
		if(sqlHandel==null)
			sqlHandel = new SqlConstructHandel();
		
		return sqlHandel;
	}
	
	static Handel tableFieldHandel;
	
	public static Handel commonFieldHandel() {
		if(tableFieldHandel==null)
			tableFieldHandel = new TakeIntersectionFieldsHandel();
		
		return tableFieldHandel;
	}
	
	static Handel conditionHandel;
	
	public static Handel conditionHandel() {
		if(conditionHandel==null)
			conditionHandel = new AssemblyConditionsHandel();
		
		return conditionHandel;
	}
	
	static Handel queryHandel;
	
	public static Handel resultHandel() {
		if(queryHandel==null)
			queryHandel = new QueryResultPackingHandel();
		
		return queryHandel;
	}
	
	static Handel paramerHandel;
	
	public static Handel paramerHandel() {
		if(paramerHandel==null)
			paramerHandel = new ModifyParametersHandel();
		
		return paramerHandel;
	}
	
	static Handel debugHandel;
	
	public static Handel debugHandel() {
		if(debugHandel==null)
			debugHandel = new DebugHandel();
		
		return debugHandel;
	}
	
	static PrimaryKeyAsConditionHandel primaryKeyAsConditionHandel;
	
	public static Handel primaryKeyHandel() {
		if(primaryKeyAsConditionHandel==null)
			primaryKeyAsConditionHandel = new PrimaryKeyAsConditionHandel();
		
		return primaryKeyAsConditionHandel;
	}


	static Handel limitHandel;
	
	public static Handel limitHandel() {
		if(limitHandel==null)
			limitHandel = new QueryResultLimitHandel();
		
		return limitHandel;
	}
	
	static Handel batchUpdate;

	public static Handel batchUpdate() {
		if(batchUpdate==null)
			batchUpdate = new BatchOperationHandel();
		
		
		return batchUpdate;
	}
	
	public static Handel streamHandel(int bufferSize, Function<List<DataField>, Object> consumer, StreamConsumer datas,Map<String, String> aliases) {
		return new StreamHandel(bufferSize,consumer,datas,aliases);
	}

	public static Handel streamHandel(int bufferSize,Consumer<List<Map<String,Object>>> datas, Map<String, String> aliases) {
		
		return new StreamHandel(bufferSize,datas,aliases);
	}

	
	public static Handel streamHandel(int count) {
		return new StreamHandel(count);
	}
	
	
	static Handel resultColumnsHandel;
	
	public static Handel resultColumnsHandel() {
		
		if(resultColumnsHandel==null)
			resultColumnsHandel = new SqlColumnsExtractHandel();
		
		return resultColumnsHandel;
	}

	
	public static Handel createAndInsert(String newTableName) {
		
		return new CreateAndInsertSqlHandel(newTableName);
	}
	

}
