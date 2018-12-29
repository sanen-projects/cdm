package online.sanen.cdm;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import com.mhdt.analyse.Validate;

import online.sanen.cdm.basic.QueryType;
import online.sanen.cdm.basic.ResultType;
import online.sanen.cdm.basic.Structure;
import online.sanen.cdm.component.Manager;
import online.sanen.cdm.component.Pipeline;
import online.sanen.cdm.component.PipelineDivice;
import online.sanen.cdm.factory.HandelFactory;
import online.sanen.cdm.factory.PipelineFactory;

/**
 * 
 * @author LazyToShow
 * Date: 2018/06/12
 * Time: 09:17
 */
public class QueryMapDevice implements QueryMap {

	Structure structure;

	public QueryMapDevice(Manager manager, String tableName, Map<String, Object> entry) {
		structure = new Structure(manager);
		structure.setTableName(tableName);
		structure.setEnMap(entry);
	}

	public QueryMapDevice(Manager manager, String tableName, Collection<Map<String, Object>> maps) {
		structure = new Structure(manager);
		structure.setTableName(tableName);
		structure.setEnMaps(maps);
	}

	@Override
	public int insert() {

		if (structure.getEnMaps() != null)
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

		if (structure.getPrimaryKey() == null)
			throw new NullPointerException(
					"Primary key is null,Use the setPrimaryKey(String primary) method to set this ");

		if (structure.getEnMaps() != null)
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

	@Override
	public int update() {

		if (structure.getPrimaryKey() == null)
			throw new NullPointerException(
					"Primary key is null,Use the setPrimaryKey(String primary) method to set this ");

		if (structure.getEnMaps() != null)	return batchUpdate(QueryType.update);

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
	public int delete(String primary) {
		setPrimary(primary);
		return delete();
	}

	@Override
	public int updateBy(String primary) {
		setPrimary(primary);
		return update();
	}

	@Override
	public QueryMap setFields(String... fields) {
		
		if(fields ==null)
			return this;
		
		structure.setFields(new HashSet<>(Arrays.asList(fields)));
		return this;
	}

	@Override
	public QueryMap setExceptFields(String... fields) {
		
		if(fields ==null)
			return this;
		
		structure.setExceptes(new HashSet<>(Arrays.asList(fields)));
		return this;
	}

	@Override
	public QueryMap setPrimary(String primary) {
		if (Validate.isNullOrEmpty(primary))
			throw new NullPointerException("Primary key is null.");

		Object value = structure.getEnMap().get(primary);

		structure.setPrimaryKey(primary);
		structure.setPrimaryValue(value);
		return this;
	}

}
