package online.sanen.cdm;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import online.sanen.cdm.basic.CdmSupportsException;
import online.sanen.cdm.basic.ProductType;

/**
 * 
 * @author LazyToShow <br>
 *         Date: 2018/06/12 <br>
 *         Time: 09:17
 */
public interface SqlConversion extends Function<String, String> {

	@Override
	default String apply(String sb) {

		if (sb.length() < 1)
			return sb;

		ProductType productType = applyProductType();

		switch (productType) {

		case SQLITE:

			for (String it : list1)
				sb = sb.replace("${" + it + "}", "text ");
			for (String it : list2)
				sb = sb.replace("${" + it + "}", "integer");
			for (String it : list3)
				sb = sb.replace("${" + it + "}", "real");
			for (String it : list4)
				sb = sb.replace("${" + it + "}", "blob");

			sb = sb.replace("${PRIMARY}", " primary key autoincrement");

			break;

		case MYSQL:

			for (String it : list1)
				sb = sb.replace("${" + it + "}", "text ");
			for (String it : list2)
				sb = sb.replace("${" + it + "}", "integer");
			for (String it : list3)
				sb = sb.replace("${" + it + "}", "real");
			for (String it : list4)
				sb = sb.replace("${" + it + "}", "blob");

			if (sb.contains("${PRIMARY}")) {

				String modifier = ProductType.applyTableModifier(productType);

				sb = sb.replace("${PRIMARY}", "NOT NULL AUTO_INCREMENT");
				sb += ",PRIMARY KEY (" + modifier + applyPrimaryKey() + modifier + ")";
			}

			break;

		case MICROSOFT_SQL_SERVER:

			for (String it : list1)
				sb = sb.replace("${" + it + "}", "text ");
			for (String it : list2)
				sb = sb.replace("${" + it + "}", "integer");
			for (String it : list3)
				sb = sb.replace("${" + it + "}", "real");
			for (String it : list4)
				sb = sb.replace("${" + it + "}", "Binary");

			sb = sb.replace("${PRIMARY}", "primary key not null");

			break;

		case ORACLE:

			for (String it : list1)
				sb = sb.replace("${" + it + "}", "clob ");
			for (String it : list2)
				sb = sb.replace("${" + it + "}", "integer");
			for (String it : list3)
				sb = sb.replace("${" + it + "}", "real");
			for (String it : list4)
				sb = sb.replace("${" + it + "}", "Binary");

			sb = sb.replace("${PRIMARY}", "primary key not null");

			break;

		default:
			throw new CdmSupportsException(applyProductType());
		}

		return sb;
	}

	List<String> list1 = Arrays.asList("CHAR", "CHARACTER", "STRING", "BOOLEAN", "TIMESTAMP", "ORACLECLOB", "DATE",
			"VARCHAR", "VARCHAR2", "CLOB");
	List<String> list2 = Arrays.asList("SHORT", "INTEGER", "LONG", "INT", "BIGDECIMAL", "NUMBER");
	List<String> list3 = Arrays.asList("FLOAT", "DOUBLE");
	List<String> list4 = Arrays.asList("BYTE");

	String applyPrimaryKey();

	ProductType applyProductType();

}
