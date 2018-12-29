package online.sanen.cdm;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

import online.sanen.cdm.basic.BasicBean;
import online.sanen.cdm.basic.CdmConditionException;
import online.sanen.cdm.basic.QueryType;
import online.sanen.cdm.basic.ResultType;
import online.sanen.cdm.basic.Structure;
import online.sanen.cdm.component.Manager;
import online.sanen.cdm.component.Pipeline;
import online.sanen.cdm.component.PipelineDivice;
import online.sanen.cdm.condition.C;
import online.sanen.cdm.condition.Condition;
import online.sanen.cdm.condition.Condition.Cs;
import online.sanen.cdm.factory.HandelFactory;
import online.sanen.cdm.factory.PipelineFactory;

/**
 * 
 * @author online.sanen <br>
 * Date: 2017/11/25 <br>
 * Time： 9：24
 */
public class QueryEntityDevice implements QueryEntity {

	Structure structure;

	public QueryEntityDevice(Manager manager, BasicBean entry) {
		structure = new Structure(manager);
		structure.setEntry(entry);
	}

	public QueryEntityDevice(Manager manager, Collection<BasicBean> entrys) {
		structure = new Structure(manager);
		structure.setEntrys(entrys);
	}

	@Override
	public QueryEntity setTableName(String tableName) {
		structure.setTableName(tableName);
		return this;
	}

	@Override
	public QueryEntity setFields(String... fields) {
		
		if(fields ==null)
			return this;
		
		structure.setFields(new HashSet<String>(Arrays.asList(fields)));
		return this;
	}

	@Override
	public QueryEntity setExceptFields(String... fields) {
		
		if(fields ==null)
			return this;
		
		structure.setExceptes(new HashSet<String>(Arrays.asList(fields)));
		return this;
	}

	@Override
	public int insert() {

		if (structure.getEntrys() != null)
			return batchUpdate(QueryType.insert);

		return (int) Assembler.create(QueryType.insert, ResultType.Int, structure, new PipelineFactory() {
			@Override
			public Pipeline getPipeline() {

				Pipeline pipeline = new PipelineDivice();

				pipeline.addLast(HandelFactory.commonFieldHandel());
				pipeline.addLast(HandelFactory.sqlHandel());
				pipeline.addLast(HandelFactory.paramerHandel());
				pipeline.addLast(HandelFactory.resultHandel());
				pipeline.addLast(HandelFactory.debugHandel());

				return pipeline;

			}
		});
	}

	@Override
	public int delete() {

		if (structure.getEntrys() != null)
			return batchUpdate(QueryType.delete);

		return (int) Assembler.create(QueryType.delete, ResultType.Int, structure, new PipelineFactory() {
			@Override
			public Pipeline getPipeline() {
				Pipeline pipeline = new PipelineDivice();
				pipeline.addLast(HandelFactory.sqlHandel());
				pipeline.addLast(HandelFactory.primaryKeyHandel());
				pipeline.addLast(HandelFactory.conditionHandel());
				pipeline.addLast(HandelFactory.resultHandel());
				pipeline.addLast(HandelFactory.debugHandel());
				return pipeline;
			}
		});
	}

	@Override
	public int update() {

		if (structure.getEntrys() != null)
			return batchUpdate(QueryType.update);

		return (int) Assembler.create(QueryType.update, ResultType.Int, structure, new PipelineFactory() {
			@Override
			public Pipeline getPipeline() {
				Pipeline pipeline = new PipelineDivice();
				pipeline.addLast(HandelFactory.commonFieldHandel());
				pipeline.addLast(HandelFactory.sqlHandel());
				pipeline.addLast(HandelFactory.paramerHandel());
				pipeline.addLast(HandelFactory.primaryKeyHandel());
				pipeline.addLast(HandelFactory.conditionHandel());
				pipeline.addLast(HandelFactory.resultHandel());
				pipeline.addLast(HandelFactory.debugHandel());
				return pipeline;
			}
		});
	}

	private int batchUpdate(QueryType type) {
		return (int) Assembler.create(type, ResultType.Int, structure, new PipelineFactory() {
			@Override
			public Pipeline getPipeline() {
				Pipeline pipeline = new PipelineDivice();
				pipeline.addLast(HandelFactory.commonFieldHandel());
				pipeline.addLast(HandelFactory.sqlHandel());
				pipeline.addLast(HandelFactory.batchUpdate());
				pipeline.addLast(HandelFactory.debugHandel());
				return pipeline;
			}
		});
	}

	@Override
	public QueryUpdate addCondition(String fieldName, Cs cs) {
		try {
			structure.addCondition(C.buid(fieldName, cs));
		} catch (CdmConditionException e) {
			e.printStackTrace();
		}

		return new QueryUpdateDevice(structure);
	}
	

	@Override
	public QueryUpdate addCondition(String fieldName, Cs cs, Object value) {
		structure.addCondition(C.buid(fieldName, cs, value));
		return new QueryUpdateDevice(structure);
	}
	
	@Override
	public QueryUpdate addCondition(Condition cond) {
		structure.addCondition(cond);
		return new QueryUpdateDevice(structure);
	}

	
	@Override
	public QueryUpdate addCondition(Consumer<List<Condition>> consumer) {
		consumer.accept(structure.getConditions());
		return new QueryUpdateDevice(structure);
	}

	@Override
	public int create() {
		
		return (int) Assembler.create(QueryType.create, ResultType.Int, structure, new PipelineFactory() {
			@Override
			public Pipeline getPipeline() {
				Pipeline pipeline = new PipelineDivice();
				pipeline.addLast(HandelFactory.sqlHandel());
				pipeline.addLast(HandelFactory.resultHandel());
				pipeline.addLast(HandelFactory.debugHandel());
				return pipeline;
			}
		});
		
	}

	


}
