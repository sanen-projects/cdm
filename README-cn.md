# cdm-core
<p align="center">
 <a  href="http://www.sanen.online"><img height="100px" src="http://download.sanen.online/assets/img/logo.png"/></a>
</p>

<h1 align="center">Cdm Framework</h1>

[![Maven central](https://img.shields.io/badge/maven%20central-2.0.5-brightgreen.svg)](https://search.maven.org/artifact/online.sanen/cdm-core/2.0.5/jar)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)



一个使用简单，零配置，容错率高，效率的Java™ ORM 数据库框架

### ✨ 特性
* **使用简单**  没有依赖，一行代码即可初始化连接，调用接口像使用脚本语言一样畅爽
* **零配置** 约定大于配置,如有必要则使用注解替代XML,JSON配置文件
* **容错率高** 大多数情况下如果不是致命错误，采取默认方案来取代异常的抛出
* **效率** 解决项目中90%的重复sql工作,因为您不需要编写sql


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

支持常用数据库 Mysql,Sqlite,Oracle,Sqlserver


# 🆚 与Mybatis比较
* 与Mybatis相比，零配置文件
* 使用简单，小巧，只需要看看示例你就能够学会使用
* 大部分情况下通过组合函数来替代sql，数据库移植性好
* 注解替代XML标签配置
* 默认与对象名,字段进行映射


# 🆚 与Hibernate比较
* 不会因为配置复杂带来众多bug
* 支持批量修改，删除
* 执行效率更高
* 相较于sql，一对多关系会把问题变的复杂和难以维护





# ⭐ 文档

[Please refer to the Wiki for continuous updates](https://github.com/sanen-projects/cdm-core/wiki)

# ⛅ 下载



[![Maven cdm-api](https://img.shields.io/badge/Maven-cdm--api-ff69b4.svg)](http://repo1.maven.org/maven2/online/sanen/cdm-api/)
[![Maven cdm-core](https://img.shields.io/badge/Maven-cdm--core-ff69b4.svg)](http://repo1.maven.org/maven2/online/sanen/cdm-core/)
[![Maven mhdt-common](https://img.shields.io/badge/Maven-mhdt--common-ff69b4.svg)](http://repo1.maven.org/maven2/online/sanen/mhdt-common/)




# 🍭 示例

### 1.pom文件中导入依赖，<a href="https://mvnrepository.com/artifact/online.sanen/cdm-core">Maven address</a>

	
```xml
<!-- https://mvnrepository.com/artifact/online.sanen/cdm-core -->
<dependency>
	<groupId>online.sanen</groupId>
	<artifactId>cdm-core</artifactId>
	<!-- Try to keep it up to date -->
	<version>2.0.5</version>
</dependency>
```
	
### 2.创建实体类

实体类实现 **BasicBean** 接口即可.

	
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
	 
### 3.创建 **BootStrap** 实例
```java
Bootstrap bootstrap = BootStrapFactoty.load( obstract -> {
	obstract.setDriver(Driven.MYSQL);
	obstract.setUrl("jdbc:mysql://127.0.0.1:3306/test?useSSL=false");
	obstract.setUsername("root");
	obstract.setPassword("root");
	obstract.setFormat(true);
});
```
		
### 4.**CRUD** 操作

#### 新增
```java
bootstrap.query(user).insert();
```	
#### 删除
```java
bootstrap.query(user).delete();
```	
#### 修改
```java
bootstrap.query(user).update();
```	
#### 主键查询
```java
bootstrap.query(User.class,2).find();
```	
#### 列表查询
```java
bootstrap.query(User.class).addEntity(User.class).list();
```	

#### 条件筛选
```java
bootstrap.query(User.class)
	.addEntity(User.class)
	.addContion(C.eq("name","tom"))
	.list();
```
	





