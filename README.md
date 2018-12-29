<p align="center">
 <a  href="http://www.sanen.online"><img height="100px" src="http://download.sanen.online/assets/img/logo.png"/></a>
</p>

<h1 align="center">Cdm Framework</h1>


English | [简体中文](./README-cn.md)


An easy to use, zero configuration, high fault tolerance rate, the pursuit of the efficiency of the Java ™ ORM database framework


[![Maven central](https://img.shields.io/badge/maven%20central-2.0.5-brightgreen.svg)](https://search.maven.org/artifact/online.sanen/cdm-core/2.0.5/jar)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

### ✨ Features
* **Easy.** Without dependencies, and calling the interface is as straightforward as using a scripting language
* **Zero configuration.** Convention is greater than configuration, using annotations instead of XML and JSON configuration
* **High rate of fault tolerance.** In most cases, if the error is not fatal, take the default handling without throwing an exception
* **Efficiency.** Solve 90% of the repetitive SQL work in your project because you don't need to write SQL


```java
Bootstrap bootstrap = BootStrapFactoty.load("default",config->{
	config.setDriver(Driven.SQLITE);
	config.setUrl("jdbc:sqlite:test.sqlite");
});

class User implements BasicBean{

	int id;
	String name;

	@Override
	public String primarykey() {
		return "id";
	}
}

bootstrap.query(new User()).create();
```


Support for common databases: Mysql,Sqlite,Oracle,Sqlserver



# 🆚 Compare with Mybatis
* Compared with Mybatis, the zero configuration file
* Easy to use, small, you can learn to use just look at the examples
* In most cases, the combination of functions to replace SQL, database portability is good
* Annotations replace XML tag configuration
* Default to object name, field mapping does not require additional Settings


# 🆚 Compare with Hibernate
* It won't introduce many bugs due to complicated configuration
* Support batch modification and deletion
* More efficient execution
* Hibernate's one-to-many relationships make problems more complex and difficult to maintain than SQL





# ⭐ Document

[Please refer to the Wiki for continuous updates](https://github.com/sanen-projects/cdm-core/wiki)

# ⛅ Download
[![Maven cdm-api](https://img.shields.io/badge/Maven-cdm--api-ff69b4.svg)](http://repo1.maven.org/maven2/online/sanen/cdm-api/)
[![Maven cdm-core](https://img.shields.io/badge/Maven-cdm--core-ff69b4.svg)](http://repo1.maven.org/maven2/online/sanen/cdm-core/)
[![Maven mhdt-common](https://img.shields.io/badge/Maven-mhdt--common-ff69b4.svg)](http://repo1.maven.org/maven2/online/sanen/mhdt-common/)


# 🍭 The sample

### 1.Import dependencies in the pom，<a href="https://mvnrepository.com/artifact/online.sanen/cdm-core">Maven address</a>

```xml
<!-- https://mvnrepository.com/artifact/online.sanen/cdm-core -->
<dependency>
    <groupId>online.sanen</groupId>
    <artifactId>cdm-core</artifactId>
    <!-- Try to keep it up to date -->
    <version>2.0.5</version>
</dependency>
```
	
### 2.Create entity classes

```java
class User implements BasicBean {
	int id;

	String name;
	@Override
	public String primarykey() {
		return "id";
	}
}
```

	 
### 3.Create the **BootStrap** instance

```java
Bootstrap bootstrap = BootStrapFactoty.load( obstract -> {
	obstract.setDriver(Driven.MYSQL);
	obstract.setUrl("jdbc:mysql://127.0.0.1:3306/test?useSSL=false");
	obstract.setUsername("root");
	obstract.setPassword("root");
	obstract.setFormat(true);
});
```		
		
### 4.**CRUD** Operations

#### Add

```java
bootstrap.query(user).insert();
```	
#### Delete

```java
bootstrap.query(user).delete();
```	
#### Update

```java
bootstrap.query(user).update();
```	
#### Find by pk

```java
bootstrap.query(User.class,2).find();
```	
#### List query

```java
bootstrap.query(User.class).addEntity(User.class).list();
```	

#### Add Condition

```java
bootstrap.query(User.class)
	.addEntity(User.class)
	.addContion(C.eq("name","tom"))
	.list();
```
	





