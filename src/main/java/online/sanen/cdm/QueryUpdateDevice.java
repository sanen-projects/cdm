package online.sanen.cdm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

import online.sanen.cdm.basic.CdmConditionException;
import online.sanen.cdm.basic.QueryType;
import online.sanen.cdm.basic.ResultType;
import online.sanen.cdm.basic.Structure;
import online.sanen.cdm.component.Pipeline;
import online.sanen.cdm.component.PipelineDivice;
import static online.sanen.cdm.condition.C.*;
import online.sanen.cdm.condition.Condition;
import online.sanen.cdm.condition.Condition.Cs;
import online.sanen.cdm.factory.HandelFactory;
import online.sanen.cdm.factory.PipelineFactory;

/**
 * 
 * @author LazyToShow
 * Date: 2018/06/12
 * Time: 09:17
 */
public class QueryUpdateDevice implements QueryUpdate {

	Structure structure;

	public QueryUpdateDevice(Structure structure) {
		this.structure = structure;
	}

	@Override
	public QueryUpdate setTableName(String tableName) {
		structure.setTableName(tableName);
		return this;
	}

	@Override
	public QueryUpdate setFields(String... fields) {
		
		if(fields ==null)
			return this;
		
		structure.setFields(new HashSet<>(Arrays.asList(fields)));
		return this;
	}
	
	@Override
	public QueryUpdate setExceptFields(String... fields) {
		
		if(fields ==null)
			return this;
		
		structure.setFields(new HashSet<>(Arrays.asList(fields)));
		return this;
	}

	@Override
	public QueryUpdate addCondition(String fieldName, Cs cs) {
		try {
			structure.addCondition(buid(fieldName, cs));
		} catch (CdmConditionException e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public QueryUpdate addCondition(String fieldName, Cs cs, Object value) {
		structure.addCondition(buid(fieldName, cs, value));
		return this;
	}

	@Override
	public QueryUpdate addCondition(Consumer<List<Condition>> consumer) {
		
		consumer.accept(structure.getConditions());
		return this;
	}
	
	@Override
	public QueryUpdate addCondition(Condition cond) {
		
		if(cond ==null)
			return this;
		
		structure.addCondition(cond);
		return new QueryUpdateDevice(structure);
	}

	

	@Override
	public int update() {
		return (int) Assembler.create(QueryType.update, ResultType.Int, structure, new PipelineFactory() {
			@Override
			public Pipeline getPipeline() {
				Pipeline pipeline = new PipelineDivice();
				pipeline.addLast(HandelFactory.commonFieldHandel());
				pipeline.addLast(HandelFactory.sqlHandel());
				pipeline.addLast(HandelFactory.paramerHandel());
				pipeline.addLast(HandelFactory.conditionHandel());
				pipeline.addLast(HandelFactory.resultHandel());
				pipeline.addLast(HandelFactory.debugHandel());
				return pipeline;
			}
		});
	}
	

}
