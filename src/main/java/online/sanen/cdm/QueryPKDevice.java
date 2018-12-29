package online.sanen.cdm;

import online.sanen.cdm.basic.BasicBean;
import online.sanen.cdm.basic.QueryType;
import online.sanen.cdm.basic.ResultType;
import online.sanen.cdm.basic.Structure;
import online.sanen.cdm.component.Manager;
import online.sanen.cdm.component.Pipeline;
import online.sanen.cdm.component.PipelineDivice;
import online.sanen.cdm.factory.HandelFactory;
import online.sanen.cdm.factory.PipelineFactory;

/**
 * @author online.sanen Date: 2017/11/23 Time： 9:39
 */
public class QueryPKDevice<T extends BasicBean> implements QueryPK<T> {
	Structure structure;

	public QueryPKDevice(Manager manager, Class<T> entryCls, Object primarykey) {
		structure = new Structure(manager);
		structure.setPrimaryValue(primarykey);
		structure.setEntry_class(entryCls);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T find() {
		return (T) Assembler.create(QueryType.select, ResultType.Bean, structure, new PipelineFactory() {
			@Override
			public Pipeline getPipeline() {
				Pipeline pipeline = new PipelineDivice();
				pipeline.addLast(HandelFactory.commonFieldHandel());
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
	public int delete() {

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

}
