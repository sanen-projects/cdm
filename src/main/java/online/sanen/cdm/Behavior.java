package online.sanen.cdm;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import com.mhdt.analyse.Validate;
import com.mhdt.annotation.Alias;
import com.mhdt.annotation.NoDB;
import com.mhdt.annotation.Table;
import com.mhdt.toolkit.Assert;
import com.mhdt.toolkit.Reflect;

import online.sanen.cdm.Bootstrap;
import online.sanen.cdm.basic.BasicBean;
import online.sanen.cdm.basic.Cdm;
import online.sanen.cdm.basic.CdmQueryException;
import online.sanen.cdm.basic.CdmStructuralException;
import online.sanen.cdm.basic.Sorts;

import static online.sanen.cdm.condition.C.*;

import online.sanen.cdm.condition.C;
import online.sanen.cdm.condition.CompositeCondition;
import online.sanen.cdm.condition.Condition;
import online.sanen.cdm.condition.SimpleCondition;
import online.sanen.cdm.factory.BootstrapFactoty;

/**
 * <p>
 * User behavior interface
 * <p>
 * By default, it provides users with the basic function of <b>insert</b>,
 * <b>delete</b>, <b>update</b> and <b>select</b> singlecases.
 * <p>
 * To keep coding styles consistent, static methods implement common set
 * operations (although {@link Bootstrap} instances can still be used, we
 * recommend this approach).
 * <p>
 * All result sets correspond to subclass class name fields, class names
 * correspond to table names, object fields correspond to table fields, and
 * object field types correspond to table field types.
 * <p>
 * By default, the query is associated with the class name field name, or
 * aliased using the {@link Table} {@link Alias} annotation.
 * <p>
 * Note that this interface cannot execute SQL statements
 * 
 * @see Bootstrap
 *
 * @author LazyToShow <br>
 *         Date: Dec 7, 2018 <br>
 *         Time: 10:28:34 AM
 */
public interface Behavior<T> extends BasicBean {

	/**
	 * 
	 * @param fields
	 * @return
	 */
	default Perfect<T> setFields(String... fields) {
		Perfect<T> perfect = perfect(bootstrap(), this, fields, null);
		return perfect;
	}

	/**
	 * 
	 * @param fields
	 * @return
	 */
	default Perfect<T> setExceptFields(String... fields) {
		Perfect<T> perfect = perfect(bootstrap(), this, null, fields);
		return perfect;
	}

	/**
	 * Inserts the current instance to the database
	 * 
	 * @return
	 */
	default int insert() {
		return perfect(bootstrap(), this, null, null).insert();
	}

	/**
	 * Delete in the database according to the {@link #primarykey()}
	 * 
	 * @return
	 */
	default int delete() throws CdmQueryException {

		checkPrimary();
		return bootstrap().query(this).delete();
	}

	default void checkPrimary() {

		try {

			String primaryKey = Cdm.getPrimaryKey(this);
			Object primary = Reflect.getValue(this, primaryKey);
			Assert.notNull(primary, "The primary key is null:" + primaryKey + " " + this.getClass());

		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			throw new CdmQueryException(e);
		}
	}

	/**
	 * Specify field modification
	 * 
	 * @param fields - Only the specified fields in the entity class are modified
	 * @see #setFields(String...)
	 * @see #update(String...)
	 * @return
	 */
	default int update(String... fields) {
		checkPrimary();
		return setFields(fields).update();
	}

	/**
	 * Gets an instance from the database through the primary key
	 * 
	 * @return
	 */
	default Optional<T> findByPk() {

		return perfect(bootstrap(), this, null, null).findByPk();
	}

	/**
	 * 
	 * @param fields
	 * @return
	 */
	default Optional<T> findByFields(String... fields) throws CdmQueryException {

		return perfect(bootstrap(), this, null, null).findByFields(fields);
	}

	/**
	 * Create tables based on data structures
	 * 
	 * @return
	 */
	default int createTable() {

		return perfect(bootstrap(), this, null, null).createTable();
	}

	/**
	 * Delete table
	 * 
	 * @return
	 */
	default int dropTable() {

		String tableName = Cdm.table(this.getClass());

		if (bootstrap().query(tableName).isExsites())
			return bootstrap().query(tableName).drop();

		return -1;
	}

	/**
	 * Get the {@link Bootstrap} instance corresponding to the current model. Note
	 * that new {@link Bootstrap} should not be constructed here, but should be
	 * obtained from the created object pool or cache container.
	 * 
	 * @return
	 */
	default Bootstrap bootstrap() {

		String bootstrapId = Reflect.getBootStrapId(this.getClass());

		if (Validate.isNullOrEmpty(bootstrapId) && BootstrapFactoty.isUniqueness())
			return BootstrapFactoty.getFirst();

		Assert.notNull(bootstrapId, "class:" + this.getClass().getName()
				+ " There is no injected bootstrap instance, please use the @BootStrapID annotation.");

		if (BootstrapFactoty.contains(bootstrapId))
			return BootstrapFactoty.get(bootstrapId);
		else
			throw new NullPointerException("BootstrapId: '" + bootstrapId
					+ "' index instance cannot be passed by class:" + this.getClass().getName());
	}

	/**
	 * Tell {@link Behavior} the type of instance the interface is about to operate
	 * on, which is a constructed start function
	 * 
	 * @param cls
	 * @return
	 */
	public static <T extends BasicBean> PerfectList<T> specify(Class<T> cls) {

		return perfectList(staticBootstrap(cls), cls);
	}

	
	public static <T extends BasicBean> void batchInsert(List<T> list) throws CdmQueryException {
		batchInsert(list);
	}

	/**
	 * 
	 * @param list
	 * @param fields
	 * @throws CdmQueryException
	 */
	@SuppressWarnings("unchecked")
	public static <T extends BasicBean> void batchInsert(List<T> list, String... fields) {

		Assert.state(list != null && !list.isEmpty(), "List is null or empty.");

		Class<T> class1 = (Class<T>) list.get(0).getClass();
		Bootstrap bootstrap = staticBootstrap(class1);
		perfectList(bootstrap, class1).setFelds(fields).batchInsert(list);
	}

	/**
	 * 
	 * @param cls
	 * @param conditions
	 * @return
	 * @throws CdmQueryException
	 */
	public static <T extends BasicBean> int delete(Class<T> cls, Consumer<List<Condition>> conditions) {

		return staticBootstrap(cls).query(cls).addCondition(conditions).delete();
	}

	/**
	 * 
	 * @param list
	 */
	public static <T extends BasicBean> void batchUpdate(List<T> list) {
		batchUpdate(list);
	}

	/**
	 * 
	 * @param list
	 * @param fields
	 */
	@SuppressWarnings("unchecked")
	public static <T extends BasicBean> void batchUpdate(List<T> list, String... fields) {

		Assert.state(list != null && !list.isEmpty(), "List is null or empty.");

		Class<T> class1 = (Class<T>) list.get(0).getClass();
		Bootstrap bootstrap = staticBootstrap(class1);
		perfectList(bootstrap, class1).setFelds(fields).batchUpdate(list);
	}

	static <T extends BasicBean> List<T> list(Class<T> cls) {
		return perfectList(staticBootstrap(cls), cls).list();
	}

	
	static <T extends BasicBean> List<T> list(Class<T> cls, Consumer<List<Condition>> consumer) {
		return perfectList(staticBootstrap(cls), cls).addCondtion(consumer).list();
	}

	public static <T extends BasicBean> List<T> list(Class<T> cls, String... fields) {

		Bootstrap $bootstrap = staticBootstrap(cls);
		return perfectList($bootstrap, cls).setFelds(fields).list();
	}

	/**
	 * 
	 * @param t
	 * @param fields
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends BasicBean> List<T> list(T t, String... fields) {

		Class<T> $class = (Class<T>) t.getClass();
		Bootstrap $bootstrap = staticBootstrap($class);

		return perfectList($bootstrap, $class).addCondtion(conditions -> {

			for (String fieldName : fields) {

				if (Validate.hasField(t.getClass(), fieldName))

					try {
						conditions.add(eq(fieldName, Reflect.getValue(t, fieldName)));
					} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
							| SecurityException e) {
						e.printStackTrace();
					}
			}

		}).list();
	}

	/**
	 * 
	 * @param cls
	 * @return
	 */
	static Bootstrap staticBootstrap(Class<?> cls) {

		String bootStrapId = Reflect.getBootStrapId(cls);

		return Validate.isNullOrEmpty(bootStrapId) && BootstrapFactoty.isUniqueness() ? BootstrapFactoty.getFirst()
				: BootstrapFactoty.get(Reflect.getBootStrapId(cls));
	}

	/**
	 * 
	 * @param $bootstrap
	 * @param $class
	 * @return
	 */
	static <T extends BasicBean> PerfectList<T> perfectList(Bootstrap $bootstrap, Class<T> $class) {

		return new PerfectList<T>() {

			List<Condition> conditions = new ArrayList<>();

			Integer[] limit;

			private String[] fields;

			private String[] excepts;

			Sorts sorts;

			private String[] sortFields;

			@Override
			public PerfectList<T> addCondition(Condition cond) {
				this.conditions.add(cond);
				return this;
			}

			@Override
			public PerfectList<T> addCondtion(Consumer<List<Condition>> conds) {
				conds.accept(conditions);
				return this;
			}

			@Override
			public PerfectList<T> sort(Sorts sorts, String... fields) {
				this.sorts = sorts;
				this.sortFields = fields;
				return this;
			}

			@Override
			public PerfectList<T> limit(Integer... limit) {
				this.limit = limit;
				return this;
			}

			@Override
			public PerfectList<T> setFelds(String... fields) {
				this.fields = fields;
				return this;
			}

			@Override
			public PerfectList<T> setExcepts(String... excepts) {
				this.excepts = excepts;
				return this;
			}

			@Override
			public void batchInsert(List<T> list) {

				$bootstrap.query(list).setFields(fields).setExceptFields(excepts).insert();
			}

			@Override
			public void batchUpdate(List<T> list) {
				$bootstrap.query(list).setFields(fields).setExceptFields(excepts).update();

			}

			@Override
			public List<T> list() {
				return $bootstrap.query($class).setFields(fields).setExceptFields(excepts).addEntry($class).limit(limit)
						.sort(sorts, sortFields).addCondition(conds -> conds.addAll(conditions)).entities();
			}

			@Override
			public int delete() {
				return $bootstrap.query($class).addCondition(conds -> conds.addAll(conditions)).delete();
			}

		};
	}

	/**
	 * 
	 *
	 * @author LazyToShow <br>
	 *         Date: Dec 7, 2018 <br>
	 *         Time: 4:36:11 PM
	 */
	interface PerfectList<T> {

		/**
		 * Add the condition
		 * 
		 * @param cond - {@link C} {@link SimpleCondition} {@link CompositeCondition}
		 * @return
		 */
		public PerfectList<T> addCondition(Condition cond);

		/**
		 * Add the condition
		 * 
		 * @param conds - {@link C} {@link SimpleCondition} {@link CompositeCondition}
		 * @return
		 */
		public PerfectList<T> addCondtion(Consumer<List<Condition>> conds);

		/**
		 * Specify collation
		 * 
		 * @param sorts
		 * @param fields
		 * @return
		 */
		public PerfectList<T> sort(Sorts sorts, String... fields);

		/**
		 * Paging function, the specific implementation by the database vendor
		 * 
		 * @param limit
		 * @return
		 */
		public PerfectList<T> limit(Integer... limit);

		/**
		 * Specifies that entity class fields participate in the operation
		 * 
		 * @param fields
		 * @return
		 */
		public PerfectList<T> setFelds(String... fields);

		/**
		 * Exclude entity class fields from the operation
		 * 
		 * @param excepts
		 * @return
		 */
		public PerfectList<T> setExcepts(String... excepts);

		/**
		 * 
		 * @param list
		 */
		void batchInsert(List<T> list);

		/**
		 * 
		 * @param list
		 */
		void batchUpdate(List<T> list);

		/**
		 * 
		 * @return
		 */
		List<T> list();

		/**
		 * 
		 */
		int delete();
	}

	/**
	 * 
	 * @param $bootstrap
	 * @param $bean
	 * @param $fields
	 * @param $excepts
	 * @return
	 */
	default Perfect<T> perfect(Bootstrap $bootstrap, Behavior<T> $bean, String[] $fields, String[] $excepts) {

		return new Perfect<T>() {

			@Override
			public int insert() {

				return $bootstrap.query($bean).setFields($fields).setExceptFields($excepts).insert();
			}

			@Override
			public int update() {
				return $bootstrap.query($bean).setFields($fields).setExceptFields($excepts).update();
			}

			@Override
			public Optional<T> findByPk() {

				String primaryKey = Cdm.getPrimaryKey($bean);
				Object value;

				try {
					value = Reflect.getValue($bean, primaryKey);
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
						| SecurityException e) {
					throw new CdmStructuralException(e);
				}

				return Optional
						.ofNullable($bootstrap.query($bean.getClass()).setFields($fields).setExceptFields($excepts)
								.addCondition(conditions -> conditions.add(buid(primaryKey).eq(value)))
								.setFields($fields).setExceptFields($excepts).addEntry($bean.getClass()).unique());
			}

			@Override
			public Optional<T> findByFields(String... fields) {

				return Optional.ofNullable($bootstrap.query($bean.getClass()).setFields($fields)
						.setExceptFields($excepts).addCondition(conditions -> {

							for (String fieldName : fields) {

								if (Validate.hasField($bean.getClass(), fieldName))

									try {
										conditions.add(eq(fieldName, Reflect.getValue($bean, fieldName)));
									} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
											| SecurityException e) {
										throw new CdmQueryException(e);
									}
							}

						}).addEntry($bean.getClass()).unique());
			}

			@Override
			public int createTable() {

				if (!$bootstrap.query(Cdm.table($bean.getClass())).isExsites())
					return $bootstrap.query($bean).setFields($fields).setExceptFields($excepts).create();

				return -1;

			}

		};

	}

	/**
	 * 
	 *
	 * @author LazyToShow <br>
	 *         Date: Dec 7, 2018 <br>
	 *         Time: 4:35:55 PM
	 */
	interface Perfect<T> {

		int insert();

		/**
		 * <p>
		 * Modify the current object persistence layer based on the instance primary key
		 * {@link BasicBean#primarykey()} value
		 * <p>
		 * For fields that you don't want to modify, try {@link NoDB} or
		 * {@link #setFields(String...)}
		 * <p>
		 * note that before performing the modification operation, please ensure that
		 * the instance data is up-to-date and complete. It is recommended that a query
		 * be executed first.
		 * 
		 * @return
		 */
		int update();

		/**
		 * Primary key query
		 * 
		 * @return
		 */
		Optional<T> findByPk();

		/**
		 * Query by the specified field. The specified fields are taken into the query
		 * as equivalent conditions
		 * 
		 * @param fields - Must be the fields that the entity class contains
		 * @return
		 */
		Optional<T> findByFields(String... fields);

		/**
		 * Create tables based on entity class data structures
		 * 
		 * @return
		 */
		int createTable();
	}

}
