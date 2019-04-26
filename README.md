
<p align="center">
 <a  href="http://www.sanen.online"><img height="100px" src="http://download.sanen.online/assets/img/logo.png"/></a>
</p>

<h1 align="center">Cdm Framework</h1>

[![Travis](https://api.travis-ci.org/sanen-projects/cdm-core.svg?branch=master)](https://travis-ci.org/sanen-projects/cdm-core) [![codecov](https://codecov.io/gh/sanen-projects/cdm-core/branch/master/graph/badge.svg)](https://codecov.io/gh/sanen-projects/cdm-core) [![Maven central](https://img.shields.io/badge/maven%20central-2.0.5-brightgreen.svg)](https://search.maven.org/artifact/online.sanen/cdm-core/2.0.5/jar) [![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)


简体中文 | [English](./README-en.md)

一个使用简单，零配置，容错率高，效率的Java™ ORM 数据库框架

### ✨ 特性
* **使用简单**  没有第三方依赖，一行代码即可初始化数据库连接，调用接口只需要记住一个引导器（Bootstrap），工厂[Bootstraps](#bootstraps)，[Behavior<T>](#behavior)）即可。
	
* **零配置** 设计原则遵循习惯大于约定,如有配置必要,使用注解替代XML,JSON等配置文件
* **容错率高** 非致命错误，自动采取默认方案替代
* **效率** 节省时间，虽然支持编写sql但大部分情况没有这样做的必要


# 🆚  与Mybatis比较
* 与Mybatis相比，无配置文件，少数需要配置的参数通过注解加以实现
* 小巧，使用简单，只需看看示例你就能够学会使用
* 大部分情况下通过组合函数来替代sql（支持复杂条件查询，limit,排序等），数据库移植性好




# 🆚  与Hibernate比较
* 不会因为配置复杂带来众多bug
* 支持批量修改，删除
* 内置缓存让执行效率更高
* 虽然是orm框架，但还是建议复杂问题sql解决，类似Hibernate的一对多关系相较于sql，会把问题变的复杂和难以维护


# 使用


### Maven

<a href="https://mvnrepository.com/artifact/online.sanen/cdm-all">https://mvnrepository.com/artifact/online.sanen/cdm-all</a>

```xml
	
<!-- https://mvnrepository.com/artifact/online.sanen/cdm-all -->
<dependency>
    <groupId>online.sanen</groupId>
    <artifactId>cdm-all</artifactId>
    <version>最新版本</version>
</dependency>


	
```

### Gradle

```js
	
compile group: 'online.sanen', name: 'cdm-all', version: '2.2.0'
	
```

# 下载


[![Maven cdm-api](https://img.shields.io/badge/Maven-cdm--api-ff69b4.svg)](http://repo1.maven.org/maven2/online/sanen/cdm-api/) [![Maven cdm-core](https://img.shields.io/badge/Maven-cdm--core-ff69b4.svg)](http://repo1.maven.org/maven2/online/sanen/cdm-core/)  [![Maven mhdt-common](https://img.shields.io/badge/Maven-mhdt--common-ff69b4.svg)](http://repo1.maven.org/maven2/online/sanen/mhdt-common/)

# 文档

[Wiki](https://github.com/sanen-projects/cdm-core/wiki)
[Javadoc](https://apidoc.gitee.com/sanen-projects/cdm)

# FAQ

#### 使用时候需要自行导入数据库驱动？
和你在其它环境下使用jdbc一样，驱动是必要的，根据自己的需要下载对应版本

#### 使用过程中有疑问或改进建议？
请提交 [Issue](https://github.com/sanen-projects/cdm/issues)或直接邮件 282854237@qq.com,将会在24小时内作出答复












# Bootstraps

#### Mysql

```java
Bootstrap bootstrap = Bootstraps.load("sqlite", obstract -> {
			obstract.setDriverOption(DriverOption.MYSQL);
			obstract.setUrl("jdbc:mysql://127.0.0.1:3306/test?useSSL=false");
			obstract.setUsername("root");
			obstract.setPassword("root");
			obstract.setFormat(true);
		});
```

#### Oracle

```java
Bootstrap bootstrap = Bootstraps.load("oracle", obstract -> {

			obstract.setDataSouseType(DataSouseType.Druid);
			obstract.setDriverOption(DriverOption.ORACLE);
			obstract.setUrl("jdbc:oracle:thin:@//127.0.0.1:1521/orcl");
			obstract.setUsername("username");
			obstract.setPassword("password");
		});
```

#### Sqlite

```java
Bootstrap bootstrap = Bootstraps.load("defaultBootstrap",obstract -> {
			obstract.setDriverOption(DriverOption.SQLITE);
			obstract.setUrl("jdbc:sqlite:test.sqlite");
		});
```

#### Sqlserver
```java
Bootstrap bootstrap = Bootstraps.load(obstract -> {
			obstract.setDriverOption(DriverOption.MS_2012);
			obstract.setUrl("jdbc:sqlserver://127.0.0.1:1433;DatabaseName=testDb");
			obstract.setUsername("username");
			obstract.setPassword("password");
		});
```

# BasicBean
 实体类须实现的基础接口，实现后就可以通过bootstrap调用,例如:

> bootstrap.queryTable(User.class)
> bootstrap.queryTable("user").addEntry(User.class)


	
 **示例**

1. 实体类映射

```java

class User{
	
	@Id
	int id;
		 
	String name;

		 
}
```
	 

 2. **CRUD** 操作
```java

		bootstrap.query(user).insert();
		bootstrap.query(user).delete();
		bootstrap.query(user).update();
		
		//主键/列表查询
		bootstrap.queryTable(User.class,2).find();
		bootstrap.queryTable(User.class).addEntry(User.class).list();
		
		//条件查询
		Condition condition = C.buid("name").eq("zhang san"); 	// 创建条件
		bootstrap.queryTable("user").addEntry(User.class).addCondition(condition).sort(Sorts.DESC, "id").limit(0,10).list();
		
```	



# Behavior

 充血模式（DDD），实体类实现后自身即可具备CRUD行为

 **示例**：

1. 实体类实现 **Behavior**  接口
	
```java

	@Table("user") // Set the table name to the class name by default
	@BootStrapID("defaultBootstrap")	// Identifies the bootstrap id
	public static class User implements Behavior<User>{
		
		@NoInsert
		@Id
		int id;
		String name;
		
		@Override
		public String toString() {
			return "User [id=" + id + ", name=" + name + "]";
		}
	}
```



2. CRUD操作

```java

		User user = new User();
		user.name = "zhangsan";
		user.createTable();
						
		int id = user.insert();
				
		List<User> list = Behavior.specify(User.class).list();
		System.out.println(list);
				
		user = new User(id).findByPk().get();
		user.name = "Li si";
		user.update();
				
		Condition condition = C.buid("name").eq("Li si");
		list = Behavior.specify(User.class).addCondition(condition).limit(0,10).list();
		System.out.println(list);
				
		user.delete();

```




