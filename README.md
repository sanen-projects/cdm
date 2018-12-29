
<p align="center">
 <a  href="http://www.sanen.online"><img height="100px" src="http://download.sanen.online/assets/img/logo.png"/></a>
</p>

<h1 align="center">Cdm Framework</h1>

[![Maven central](https://img.shields.io/badge/maven%20central-2.0.5-brightgreen.svg)](https://search.maven.org/artifact/online.sanen/cdm-core/2.0.5/jar) [![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

English | [简体中文](./README-cn.md)

A simple to use, zero configuration, high fault rate, the efficiency of the Java ™ ORM database framework

### ✨ Features
* **Easy**  Without relying on any third party, one line of code can initialize the database connection. To call the interface, you only need to remember one Bootstrap, one BootstrapFactory and two interfaces (BasicBean, Behavior<T>).
* **Zero configuration** Design principles follow conventions rather than conventions. If necessary, use annotations instead of XML,JSON and other configuration files
* **High fault tolerance rate** Non-fatal error automatically takes the default option instead
* **Efficiency** Save time and while SQL writing is supported, most of the time it is not necessary


```java

public class SqlLite {
	
	@Table("user") // Set table name (by default table name class name)
	@BootStrapID("defaultBootstrap")	// Identifies the bootstrap id
	public static class User implements BasicBean{
		
		@NoInsert
		int id;
		
		String name;
		
		@Override
		public String toString() {
			return "User [id=" + id + ", name=" + name + "]";
		}

		@Override
		public String primarykey() {
			return "id";
		}
	}

	public static void main(String[] args) {

		Bootstrap bootstrap = BootstrapFactoty.load("defaultBootstrap",obstract -> {
			obstract.setDriver(Driven.SQLITE);
			obstract.setUrl("jdbc:sqlite:test.sqlite");
		});
		
				
		bootstrap.query(user).create();	//create table
		bootstrap.query(user).insert(); // insert entity
		user = bootstrap.query(User.class,1).find(); // find by pk(id)
		System.out.println("The user where id=1 ? === 	"+user+"	==="); 
		bootstrap.query(User.class, 1).delete();	// delete by pk(id)
	}

}

```


Support common database *Mysql*,*Sqlite*,*Oracle*,*SqlServer*

#  Compared with Mybatis
* In contrast to Mybatis, there is no configuration file and a few parameters that need to be configured are implemented by annotations
* Small, easy to use, just look at the examples you can learn to use
* In most cases, combining functions to replace SQL (support complex conditional queries, limit, sort, etc.) is good for database portability




#  Compared with Hibernate
* It won't introduce many bugs due to complicated configuration
* Support batch modification and deletion
* Built-in caching makes execution more efficient
* Although it is an orm framework, SQL is still recommended to solve complex problems. Compared with SQL, a one-to-many relationship similar to Hibernate will make the problem more complex and difficult to maintain




#  Document

[Please refer to the Wiki for continuous updates](https://github.com/sanen-projects/cdm-core/wiki)

#  Installation

Import <a href="https://mvnrepository.com/artifact/online.sanen/cdm-core">Maven rely on</a>

### Maven
```xml
	
	<dependency>
		<groupId>online.sanen</groupId>
		<artifactId>cdm-core</artifactId>
		<version>2.0.5</version>
	</dependency>
	
```

### Gradle

```js
	
	compile group: 'online.sanen', name: 'cdm-core', version: '2.0.5'
	
```



# Download

## Mysql

```java
Bootstrap bootstrap = BootstrapFactoty.load("sqlite", obstract -> {
			obstract.setDriver(Driven.MYSQL);
			obstract.setUrl("jdbc:mysql://127.0.0.1:3306/test?useSSL=false");
			obstract.setUsername("root");
			obstract.setPassword("root");
			obstract.setFormat(true);
		});
```

## Oracle

```java
Bootstrap bootstrap = BootstrapFactoty.load("oracle", obstract -> {

				obstract.setDataSouseType(DataSouseType.Dbcp);
				obstract.setDriver(Driven.ORACLE);
				obstract.setUrl("jdbc:oracle:thin:@//127.0.0.1:1521/orcl");
				obstract.setUsername("username");
				obstract.setPassword("password");
			});
```

## Sqlite

```java
Bootstrap bootstrap = BootstrapFactoty.load("defaultBootstrap",obstract -> {
			obstract.setDriver(Driven.SQLITE);
			obstract.setUrl("jdbc:sqlite:test.sqlite");
		});
```

## Sqlserver
```java
Bootstrap bootstrap = BootstrapFactoty.load(obstract -> {

				obstract.setDriver("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				obstract.setUrl("jdbc:sqlserver://127.0.0.1:1433;DatabaseName=testDb");
				obstract.setUsername("username");
				obstract.setPassword("password");
			});
```

[![Maven cdm-api](https://img.shields.io/badge/Maven-cdm--api-ff69b4.svg)](http://repo1.maven.org/maven2/online/sanen/cdm-api/) [![Maven cdm-core](https://img.shields.io/badge/Maven-cdm--core-ff69b4.svg)](http://repo1.maven.org/maven2/online/sanen/cdm-core/)  [![Maven mhdt-common](https://img.shields.io/badge/Maven-mhdt--common-ff69b4.svg)](http://repo1.maven.org/maven2/online/sanen/mhdt-common/)

# BootstrapFactory



#  Interface

## BasicBean.java
 The basic interface that the entity class must implement can be called through bootstrap. For example:

> bootstrap.query(User.class)
> bootstrap.query("user").addEntry(User.class)


	
 **The sample**

1. The entity class implements the **BasicBean** interface

```java

class User implements BasicBean{
	int id;
		 
	String name;

	@Override
	public String primarykey() {
		return "id";
	}
		 
}
```
		
 2. **CRUD** operations
```java

		bootstrap.query(user).insert();
		bootstrap.query(user).delete();
		bootstrap.query(user).update();
		
		//Primary key/list query
		bootstrap.query(User.class,2).find();
		bootstrap.query(User.class).addEntry(User.class).list();
		
		//Conditions of the query
		Condition condition = C.buid("name").eq("zhang san"); 	
		// Create conditions
		bootstrap.query("user").addEntry(User.class)
			.addCondition(condition).sort(Sorts.DESC, "id")
			.limit(0,10).list();
		
```	

## Behavior.java

 Hyperemia mode (DDD), the entity class implementation itself can have CRUD behavior

 **The sample**：

1. The entity class implements the **Behavior** interface
	
```java

	@Table("user") // Set the table name to the class name by default
	@BootStrapID("defaultBootstrap")	// Identifies the bootstrap id
	public static class User implements Behavior<User>{
		
		@NoInsert
		int id;
		String name;
		
		@Override
		public String toString() {
			return "User [id=" + id + ", name=" + name + "]";
		}

		@Override
		public String primarykey() {
			return "id";
		}
	}
```



2. **CRUD** operations

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
🌙  Can go to[Github cdm-core](https://github.com/sanen-projects/cdm-core) submit questions/Suggestions [ISSUES](https://github.com/sanen-projects/cdm-core/issues)




