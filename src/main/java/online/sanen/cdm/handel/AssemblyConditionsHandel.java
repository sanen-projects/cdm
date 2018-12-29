package online.sanen.cdm.handel;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import online.sanen.cdm.Handel;
import online.sanen.cdm.basic.CdmConditionException;
import online.sanen.cdm.basic.ProductType;

import static online.sanen.cdm.condition.Condition.Associated.*;
import static online.sanen.cdm.condition.Condition.Cs.*;

import online.sanen.cdm.basic.Structure;
import online.sanen.cdm.condition.CompositeCondition;
import online.sanen.cdm.condition.Condition;
import online.sanen.cdm.condition.SimpleCondition;

/**
 * <pre>
 * 
 * &#64;author LazyToShow
 * Date: 2017/10/21 
 * Time: 23:19
 * </pre>
 */
public class AssemblyConditionsHandel implements Handel {

	@Override
	public Object handel(Structure structure, Object product) {
		processCondition(structure);
		return null;
	}

	private void processCondition(Structure structure) {

		if (structure.getConditions() == null || structure.getConditions().isEmpty())
			return;

		StringBuilder sb = new StringBuilder(" WHERE ");
		process(structure.productType(), sb, structure.getConditions(), obj -> structure.addParamer(obj));
		structure.getSql().append(sb.toString());
	}

	public static String process(ProductType productType, StringBuilder sb, List<? extends Condition> conditions,
			Consumer<Object> paramerValueConsumer) {

		String modifier = ProductType.applyTableModifier(productType);

		for (Condition condition : conditions) {
			processPefix(sb, condition);

			sb.append("(");

			if (condition instanceof CompositeCondition) {
				process(productType, sb, ((CompositeCondition) condition).getConditions(), paramerValueConsumer);
				sb.append(")");
				continue;
			}

			SimpleCondition cond = (SimpleCondition) condition;

			if (cond.getCondition() == CONTAINS || cond.getCondition() == NO_CONTAINS
					|| cond.getCondition() == START_WITH || cond.getCondition() == END_WITH) {

				try {
					sb.append(modifier + cond.getFieldName() + modifier
							+ cond.getCondition().annotation.replace("?", cond.getValue().toString()));
				} catch (NullPointerException e) {
					throw new CdmConditionException("Condition value is null ," + condition.toString(), e);
				}

			} else if (cond.getCondition() == IN || cond.getCondition() == NOT_IN) {

				sb.append(modifier + cond.getFieldName() + modifier
						+ cond.getCondition().annotation.replace("?", processParamersOfIn(cond.getValue())));
				paramerValueConsumer.accept(cond.getValue());

			} else if (cond.getCondition() == MATCH) {

				sb.append("match(" + modifier + cond.getFieldName() + modifier + ")" + cond.getCondition().annotation);
				paramerValueConsumer.accept(cond.getValue());

			} else if (cond.getCondition() == IS_EMPTY || cond.getCondition() == IS_NULL
					|| cond.getCondition() == IS_NOT_EMPTY || cond.getCondition() == IS_NOT_NULL) {

				sb.append(modifier + cond.getFieldName() + modifier + cond.getCondition().annotation);

			} else {

				sb.append(modifier + cond.getFieldName() + modifier + cond.getCondition().annotation);
				paramerValueConsumer.accept(cond.getValue());
			}

			sb.append(")");
		}

		return sb.toString();
	}

	/**
	 * (?,?,?)
	 * 
	 * @param value
	 * @return
	 */
	private static CharSequence processParamersOfIn(Object value) {

		if (value.getClass().isArray()) {
			Object[] array = (Object[]) value;
			StringBuilder sql = new StringBuilder();

			for (int i = 0; i < array.length; i++)
				sql.append("?,");

			sql.setLength(sql.length() - 1);

			return sql.toString();

		} else if (value instanceof Collection) {

			@SuppressWarnings("unchecked")
			Collection<Object> array = (Collection<Object>) value;
			StringBuilder sql = new StringBuilder();

			for (int i = 0; i < array.size(); i++)
				sql.append("?,");

			sql.setLength(sql.length() - 1);

			return sql.toString();

		} else {
			return "?";
		}
	}

	private static void processPefix(StringBuilder sql, Condition condition) {

		if (sql.toString().endsWith(" WHERE ") || sql.toString().endsWith("("))
			return;

		if (!(condition.getAssociated() == AND)) {
			sql.append(" OR ");
		} else {
			sql.append(" AND ");
		}
	}

}
