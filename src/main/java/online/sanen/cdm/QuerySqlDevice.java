package online.sanen.cdm;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import com.mhdt.toolkit.Assert;

import online.sanen.cdm.basic.BasicBean;
import online.sanen.cdm.basic.QueryType;
import online.sanen.cdm.basic.ResultType;
import online.sanen.cdm.basic.StreamConsumer;
import online.sanen.cdm.basic.CdmConditionException;
import online.sanen.cdm.basic.DataField;
import online.sanen.cdm.basic.Structure;
import online.sanen.cdm.component.Manager;
import online.sanen.cdm.component.Pipeline;
import online.sanen.cdm.component.PipelineDivice;
import online.sanen.cdm.factory.HandelFactory;
import online.sanen.cdm.factory.PipelineFactory;
import online.sanen.cdm.resource.R;

/**
 * 
 *
 * @author online.sanen <br>
 * Date:2017年11月30日 <br>
 * Time:下午8:37:20
 */
public class QuerySqlDevice implements QuerySql {

	Structure structure;

	public QuerySqlDevice(Manager manager, String sql, Object... paramers) {
		structure = new Structure(manager);
		structure.setSql(sql);
		structure.setCls(this.getClass());
		addParamers(paramers);
	}

	@Override
	public QuerySql addParamer(int index, Object paramer) {

		if (index < 1)
			try {
				throw new CdmConditionException(R.strings.Exception_Parametric_Anomaly);
			} catch (CdmConditionException e) {
				e.printStackTrace();
			}

		if (paramer == null)
			try {
				throw new CdmConditionException(R.strings.Exception_Parametric_Null);
			} catch (CdmConditionException e) {
				e.printStackTrace();
			}

		structure.addParamer(index, paramer);
		return this;
	}

	@Override
	public QuerySql addEntry(Class<? extends BasicBean> entry) {

		if (entry == null)
			try {
				throw new CdmConditionException(R.strings.Exception_EntryClass_Null);
			} catch (CdmConditionException e) {
				e.printStackTrace();
			}

		structure.setEntry_class(entry);
		return this;
	}

	@Override
	public int update() {

		return (int) Assembler.create(QueryType.update, ResultType.Int, structure, new PipelineFactory() {
			@Override
			public Pipeline getPipeline() {
				Pipeline pipeline = new PipelineDivice();
				pipeline.addLast(HandelFactory.resultHandel());
				pipeline.addLast(HandelFactory.debugHandel());
				return pipeline;
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<?> list() {
		return (List<?>) Assembler.create(QueryType.select, ResultType.List, structure, new PipelineFactory() {
			@Override
			public Pipeline getPipeline() {
				Pipeline pipeline = new PipelineDivice();
				pipeline.addLast(HandelFactory.resultHandel());
				pipeline.addLast(HandelFactory.debugHandel());
				return pipeline;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends BasicBean> List<T> entities() {
		
		Assert.notNull(structure.getEntry_class(), "Entity class is null");
		
		return (List<T>) Assembler.create(QueryType.select, ResultType.Beans, structure, new PipelineFactory() {
			@Override
			public Pipeline getPipeline() {
				Pipeline pipeline = new PipelineDivice();
				pipeline.addLast(HandelFactory.resultHandel());
				pipeline.addLast(HandelFactory.debugHandel());
				return pipeline;
			}
		});
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> maps() {
		return (List<Map<String, Object>>) Assembler.create(QueryType.select, ResultType.Maps, structure,
				new PipelineFactory() {
					@Override
					public Pipeline getPipeline() {
						Pipeline pipeline = new PipelineDivice();
						pipeline.addLast(HandelFactory.resultHandel());
						pipeline.addLast(HandelFactory.debugHandel());
						return pipeline;
					}
				});
	}

	@Override
	public void stream(int bufferSize, Consumer<List<Map<String, Object>>> consumer) {

		Assembler.create(QueryType.select, ResultType.Maps, structure, new PipelineFactory() {
			@Override
			public Pipeline getPipeline() {
				Pipeline pipeline = new PipelineDivice();
				pipeline.addLast(HandelFactory.streamHandel(bufferSize, consumer, null));
				return pipeline;
			}
		});
	}

	@Override
	public void stream(int bufferSize, Consumer<List<Map<String, Object>>> consumer,Map<String,String> aliases) {

		Assembler.create(QueryType.select, ResultType.Maps, structure, new PipelineFactory() {
			@Override
			public Pipeline getPipeline() {
				Pipeline pipeline = new PipelineDivice();
				pipeline.addLast(HandelFactory.streamHandel(bufferSize, consumer, null));
				return pipeline;
			}
		});

	}
	
	@Override
	public void stream(int bufferSize, Function<List<DataField>, Object> consumer, StreamConsumer datas,
			Map<String, String> aliases) {
		Assembler.create(QueryType.select, ResultType.Maps, structure, new PipelineFactory() {
			@Override
			public Pipeline getPipeline() {
				Pipeline pipeline = new PipelineDivice();
				pipeline.addLast(HandelFactory.streamHandel(bufferSize, consumer,datas, aliases));
				return pipeline;
			}
		});
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> stream(int count) {
		
		return (List<Map<String, Object>>) Assembler.create(QueryType.select, ResultType.Maps, structure,
				new PipelineFactory() {
			
					@Override
					public Pipeline getPipeline() {
						Pipeline pipeline = new PipelineDivice();
						pipeline.addLast(HandelFactory.streamHandel(count));
						return pipeline;
					}
					
				});
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> map() {
		return (Map<String, Object>) Assembler.create(QueryType.select, ResultType.Map, structure,
				new PipelineFactory() {
					@Override
					public Pipeline getPipeline() {
						Pipeline pipeline = new PipelineDivice();
						pipeline.addLast(HandelFactory.resultHandel());
						pipeline.addLast(HandelFactory.debugHandel());
						return pipeline;
					}
				});
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object unique() {
		return Assembler.create(QueryType.select, ResultType.Object, structure, new PipelineFactory() {
			@Override
			public Pipeline getPipeline() {
				Pipeline pipeline = new PipelineDivice();
				pipeline.addLast(HandelFactory.resultHandel());
				pipeline.addLast(HandelFactory.debugHandel());
				return pipeline;
			}
		});
	}

	private void addParamers(Object[] paramers) {
		if (paramers != null) {
			for (int i = 0; i < paramers.length; i++) {
				addParamer(i + 1, paramers[i]);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DataField> getQueryColumns() {

		return (List<DataField>) Assembler.create(QueryType.select, ResultType.DataField, structure,
				new PipelineFactory() {
					@Override
					public Pipeline getPipeline() {
						Pipeline pipeline = new PipelineDivice();
						pipeline.addLast(HandelFactory.resultColumnsHandel());
						return pipeline;
					}
				});

	}

	

}
