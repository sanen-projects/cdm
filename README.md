
<img src="https://github.com/sanen-projects/cdm/blob/master/20181229161612418.jpg?raw=true" alt="20181229161612418.jpg">

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



# 文档

[Wiki](https://github.com/sanen-projects/cdm-core/wiki)
[Javadoc](https://apidoc.gitee.com/sanen-projects/cdm)


#### 使用过程中有疑问或改进建议？
请提交 [Issue](https://github.com/sanen-projects/cdm/issues)或直接邮件 282854237@qq.com,将会在24小时内作出答复

