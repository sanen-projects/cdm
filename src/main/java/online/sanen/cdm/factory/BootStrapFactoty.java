package online.sanen.cdm.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import online.sanen.cdm.Bootstrap;
import online.sanen.cdm.BootstrapDevice;
import online.sanen.cdm.basic.CdmStructuralException;
import online.sanen.cdm.basic.Obstract;
import online.sanen.cdm.template.JdbcTemplate;
import online.sanen.cdm.template.transaction.Transaction;

/**
 * A {@link Bootstrap} instance is quickly created to replace the <b>new</b>
 * method, and it also has certain container functions
 * 
 * @author LazyToShow <br>
 *         Date: 2018/06/12 <br>
 *         Time: 09:17
 */
public class BootstrapFactoty {

	static Map<Object, Bootstrap> cache = new HashMap<>();

	public static boolean contains(Object key) {
		return cache.containsKey(key);
	}

	public static Bootstrap get(Object key) {

		return cache.get(key);
	}

	/**
	 * 
	 * @param bootStrapId
	 * @param bootstrap
	 * @return
	 */
	public static Bootstrap put(Object bootStrapId, Bootstrap bootstrap) {

		cache.put(bootStrapId, bootstrap);
		return bootstrap;

	}

	/**
	 * 
	 * @param bootstrapId
	 * @return
	 */
	public static Bootstrap remove(String bootstrapId) {
		return cache.remove(bootstrapId);
	}

	public static void removeByPrefix(String prefix) {

		List<String> array = new ArrayList<>();

		cache.keySet().forEach(item -> {
			if (item.toString().startsWith(prefix))
				array.add(item.toString());
		});

		array.forEach(cache::remove);
	}

	/**
	 * Quickly create a Bootstrap instance，This instance is temporary
	 * @param consumer
	 * @return
	 */
	public static Bootstrap load(Consumer<Obstract> consumer) {

		return load(null, consumer);
	}

	/**
	 * Load the {@link Bootstrap} instance and if it loads successfully, it is
	 * cached for the next time through {@link #get(Object)}
	 * @param bootStrapId
	 * @param consumer
	 * @return
	 */
	public static Bootstrap load(Object bootStrapId, Consumer<Obstract> consumer) {

		try {

			if (bootStrapId != null && cache.containsKey(bootStrapId))
				return cache.get(bootStrapId);

			Obstract obstract = new Obstract();
			consumer.accept(obstract);

			Bootstrap bootstrap = new BootstrapDevice(new JdbcTemplate(DataSourceFactory.create(obstract)),
					obstract.isShowSql(), obstract.isCache(), obstract.isLog(), obstract.getTransactionFactory(),
					bootStrapId);

			if (bootstrap != null)
				cache.put(bootStrapId, bootstrap);

			return bootstrap;

		} catch (Exception e) {
			throw new CdmStructuralException(e);
		}

	}

	/**
	 * Whether the BootStrap instance is unique
	 * 
	 * @return boolean
	 */
	public static boolean isUniqueness() {
		return cache.size() == 1;
	}

	/**
	 * Returns the first {@link Bootstrap} instance, or null if the cache is empty.
	 * 
	 * @return {@link Bootstrap}
	 */
	public static Bootstrap getFirst() {
		Optional<Object> findFirst = cache.keySet().stream().findFirst();
		return findFirst.isPresent() ? cache.get(findFirst.get()) : null;
	}

	static Map<Bootstrap, Transaction> transactions = new HashMap<>();

	/**
	 * 
	 * @param bootstrap
	 * @param transaction
	 */
	public static void registedTransaction(Bootstrap bootstrap, Transaction transaction) {

		transactions.put(bootstrap, transaction);
	}

}
