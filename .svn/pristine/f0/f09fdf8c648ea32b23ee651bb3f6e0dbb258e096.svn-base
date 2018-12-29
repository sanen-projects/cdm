package online.sanen.cdm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import com.mhdt.toolkit.Assert;

import online.sanen.cdm.basic.BasicBean;
import online.sanen.cdm.basic.DataField;
import online.sanen.cdm.basic.ProductType;
import online.sanen.cdm.basic.QueryType;
import online.sanen.cdm.basic.ResultType;
import online.sanen.cdm.basic.Sorts;
import online.sanen.cdm.basic.StreamConsumer;
import online.sanen.cdm.basic.Structure;
import online.sanen.cdm.basic.Structure.SortSupport;
import online.sanen.cdm.component.Manager;
import online.sanen.cdm.component.Pipeline;
import online.sanen.cdm.component.PipelineDivice;
import static online.sanen.cdm.condition.C.*;
import online.sanen.cdm.condition.Condition;
import online.sanen.cdm.condition.Condition.Cs;
import online.sanen.cdm.factory.HandelFactory;
import online.sanen.cdm.factory.PipelineFactory;
import online.sanen.cdm.template.JdbcTemplate;

/**
 * 
 * @author LazyToShow <br>
 *         Date: 2017/11/23 <br>
 *         Time: 22:22
 */
public class QueryTableDevice implements QueryTable {

	Structure structure;

	public QueryTableDevice(Manager manager, String tableName) {

		Assert.notNullOrEmpty(tableName, "Table name is null or empty");

		structure = new Structure(manager);
		structure.setTableName(tableName);
	}

	@Override
	public QueryTable addEntry(Class<? extends BasicBean> entry) {

		if (entry == null)
			return this;

		structure.setEntry_class(entry);
		return this;
	}

	@Override
	public QueryTable addCondition(String fieldName, Cs cs) {
		structure.addCondition(buid(fieldName, cs));
		return this;
	}

	@Override
	public QueryTable addCondition(String fieldName, Cs cs, Object value) {
		structure.addCondition(buid(fieldName, cs, value));
		return this;
	}

	@Override
	public QueryTable addCondition(Condition cond) {

		if (cond == null)
			return this;

		structure.addCondition(cond);
		return this;
	}

	@Override
	public QueryTable addCondition(Consumer<List<Condition>> consumer) {

		if (consumer == null)
			return this;

		consumer.accept(structure.getConditions());
		return this;
	}

	@Override
	public QueryTable sort(final Sorts sorts, final String... fields) {

		if (sorts == null)
			return this;

		structure.setSortSupport(new SortSupport() {

			@Override
			public String toString() {

				StringBuilder sb = new StringBuilder(" order by ");

				for (String field : fields) {
					sb.append(field + ",");
				}

				sb.setLength(sb.length() - 1);
				sb.append(" " + sorts.getValue());

				return sb.toString();
			}
		});

		return this;
	}

	@Override
	public QueryTable setFields(String... fields) {

		if (fields == null)
			return this;

		structure.setFields(new HashSet<>(Arrays.asList(fields)));

		return this;
	}

	@Override
	public QueryTable setExceptFields(String... fields) {

		if (fields == null)
			return this;

		structure.setExceptes(new HashSet<>(Arrays.asList(fields)));
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T unique() {

		return (T) Assembler.create(QueryType.select, ResultType.Object, structure, new PipelineFactory() {
			@Override
			public Pipeline getPipeline() {
				Pipeline pipeline = new PipelineDivice();
				pipeline.addLast(HandelFactory.commonFieldHandel());
				pipeline.addLast(HandelFactory.sqlHandel());
				pipeline.addLast(HandelFactory.conditionHandel());
				pipeline.addLast(HandelFactory.paramerHandel());
				pipeline.addLast(HandelFactory.resultHandel());
				pipeline.addLast(HandelFactory.debugHandel());
				return pipeline;
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> list() {

		return (List<T>) Assembler.create(QueryType.select, ResultType.List, structure, new PipelineFactory() {
			@Override
			public Pipeline getPipeline() {
				Pipeline pipeline = new PipelineDivice();
				pipeline.addLast(HandelFactory.commonFieldHandel());
				pipeline.addLast(HandelFactory.sqlHandel());
				pipeline.addLast(HandelFactory.conditionHandel());
				pipeline.addLast(HandelFactory.paramerHandel());
				pipeline.addLast(HandelFactory.limitHandel());
				pipeline.addLast(HandelFactory.resultHandel());
				pipeline.addLast(HandelFactory.debugHandel());
				return pipeline;
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BasicBean> List<T> entities() {

		return (List<T>) Assembler.create(QueryType.select, ResultType.Beans, structure, new PipelineFactory() {
			@Override
			public Pipeline getPipeline() {
				Pipeline pipeline = new PipelineDivice();
				pipeline.addLast(HandelFactory.commonFieldHandel());
				pipeline.addLast(HandelFactory.sqlHandel());
				pipeline.addLast(HandelFactory.conditionHandel());
				pipeline.addLast(HandelFactory.paramerHandel());
				pipeline.addLast(HandelFactory.limitHandel());
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
				pipeline.addLast(HandelFactory.commonFieldHandel());
				pipeline.addLast(HandelFactory.sqlHandel());
				pipeline.addLast(HandelFactory.conditionHandel());
				pipeline.addLast(HandelFactory.paramerHandel());
				pipeline.addLast(HandelFactory.streamHandel(bufferSize, consumer, null));
				return pipeline;
			}
		});

	}

	@Override
	public void stream(int bufferSize, Consumer<List<Map<String, Object>>> consumer, Map<String, String> aliases) {

		Assembler.create(QueryType.select, ResultType.Maps, structure, new PipelineFactory() {

			@Override
			public Pipeline getPipeline() {
				Pipeline pipeline = new PipelineDivice();
				pipeline.addLast(HandelFactory.commonFieldHandel());
				pipeline.addLast(HandelFactory.sqlHandel());
				pipeline.addLast(HandelFactory.conditionHandel());
				pipeline.addLast(HandelFactory.paramerHandel());
				pipeline.addLast(HandelFactory.streamHandel(bufferSize, consumer, aliases));
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
				pipeline.addLast(HandelFactory.commonFieldHandel());
				pipeline.addLast(HandelFactory.sqlHandel());
				pipeline.addLast(HandelFactory.conditionHandel());
				pipeline.addLast(HandelFactory.paramerHandel());
				pipeline.addLast(HandelFactory.streamHandel(bufferSize, consumer, datas, aliases));
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
						pipeline.addLast(HandelFactory.commonFieldHandel());
						pipeline.addLast(HandelFactory.sqlHandel());
						pipeline.addLast(HandelFactory.conditionHandel());
						pipeline.addLast(HandelFactory.paramerHandel());
						pipeline.addLast(HandelFactory.limitHandel());
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
				pipeline.addLast(HandelFactory.conditionHandel());
				pipeline.addLast(HandelFactory.paramerHandel());
				pipeline.addLast(HandelFactory.resultHandel());
				pipeline.addLast(HandelFactory.debugHandel());
				return pipeline;
			}
		});
	}

	@Override
	public QueryTable limit(Integer... args) {

		if (args == null)
			return this;

		structure.setLimit(args);
		structure.setHasLimitAble(true);
		return this;
	}

	@Override
	public boolean isExsites() {

		try {

			JdbcTemplate template = (JdbcTemplate) structure.getTemplate();
			template.queryForRowSet("SELECT 1 FROM " + structure.getTableName());
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	@Override
	public int updateName(String newName) {

		structure.setSql(ProductType.updateTableNameSQL(structure.productType(), structure.getTableName(), newName));

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

	@Override
	public int clear() {
		return delete();
	}

	@Override
	public int drop() {

		return (int) Assembler.create(QueryType.drop, ResultType.Int, structure, new PipelineFactory() {
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
	public int addColumn(String columnName, String type) {

		structure.setSql("ALTER TABLE " + structure.getTableName() + " ADD COLUMN  " + columnName + " " + type);

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

	@Override
	public String createAndInsert(String newTableName) {
		return (String) Assembler.create(QueryType.select, ResultType.String, structure, new PipelineFactory() {
			@Override
			public Pipeline getPipeline() {
				Pipeline pipeline = new PipelineDivice();
				pipeline.addLast(HandelFactory.commonFieldHandel());
				pipeline.addLast(HandelFactory.sqlHandel());
				pipeline.addLast(HandelFactory.conditionHandel());
				pipeline.addLast(HandelFactory.paramerHandel());
				pipeline.addLast(HandelFactory.limitHandel());
				pipeline.addLast(HandelFactory.createAndInsert(newTableName));
				pipeline.addLast(HandelFactory.debugHandel());
				return pipeline;
			}
		});
	}

}
