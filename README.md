### 简介
1、根据阿里官方提供的dubbo-spring-boot-starter 1.0.1 整合的dubbo 2.6.0和spring boot 1.5.9。

2、项目有四个工程，提供者provider、消费者 consumer、服务接口 api 、测试项目test。

3、consumer项目是web项目，其他的为jar项目。

4、注意修改application.yaml(application.properties) 配置文件中的zookeeper地址和端口号。

5、测试项目主要是一直发送请求，测试当服务的提供者，消费者和注册中心挂掉后请求是否能够丢失。

## 操作步骤：

### 1、创建maven parent工程统一管理项目所有的jar的版本，parent的父项目设置为spring boot

![image.png](http://upload-images.jianshu.io/upload_images/8844416-c65e747dab4b1cac.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

勾选‘Create a simplate project’ ，创建简单项目

![image.png](http://upload-images.jianshu.io/upload_images/8844416-98994f9a1cec3592.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

设置组名，项目名，包类型为pom

![image](http://upload-images.jianshu.io/upload_images/8844416-124e6a17979388e6?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

在dubbo-parent项目的pom.xml文件中添加spring-boot依赖使项目变成spring-boot项目。

```
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>1.5.9.RELEASE</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
		<java.version>1.8</java.version>
	</properties>
```

添加项目的公共依赖jar包：

```
	<!--dubbo-springBoot依赖-->
	<dependency>
	        <groupId>com.alibaba.spring.boot</groupId>
	        <artifactId>dubbo-spring-boot-starter</artifactId>
	        <version>1.0.1</version>
	</dependency>
        <!--zookeeper依赖-->
        <dependency>
              <groupId>org.apache.zookeeper</groupId>
              <artifactId>zookeeper</artifactId>
              <version>3.4.11</version>
        </dependency>
    	<dependency>
		<groupId>com.101tec</groupId>
		<artifactId>zkclient</artifactId>
		<version>0.10</version>
	</dependency>
```

添加打包配置： 红色部分配置自己的javac地址或者在maven的settings.xml中配置javapath

```
	<build>
		<finalName>dubbo-parent</finalName>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
			<plugin>  
			    <groupId>org.apache.maven.plugins</groupId>  
			    <artifactId>maven-compiler-plugin</artifactId>  
			    <version>3.1</version>  
			    <configuration>  
			        <verbose>true</verbose>  
			        <fork>true</fork>  
			        <executable>D://Program Files/Java/jdk1.8.0_25/bin/javac</executable>  
			    </configuration>  
			</plugin> 
		</plugins>
	</build>
```

### 2、创建模块项目 dubbo-api，此项目是服务的接口项目，需要打成jar包供服务端和消费端调用

![image.png](http://upload-images.jianshu.io/upload_images/8844416-9585c67e5d3d41e4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

创建 Maven Model 项目

![image.png](http://upload-images.jianshu.io/upload_images/8844416-70d99b15c692ac29.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

选择默认的简单项目

![image.png](http://upload-images.jianshu.io/upload_images/8844416-a83e69cd45a85a40.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

模块项目的目录

![image.png](http://upload-images.jianshu.io/upload_images/8844416-deae18166d22703c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

编写服务暴露接口 DemoService.java ：

```
package cn.xuhao.dubbo.api;

public interface DemoService {
	public String sayHello(String s);
}
```

在 bean 包中添加公共实体类。

通过maven打包，放到本地仓库中，用于服务端和消费端调用。

### 3、创建dubbo-provide 服务端项目，可创建jar项目或者web项目，这里创建jar项目。

创建项目同dubbo-api项目相同。

修改pom.xml文件，添加dubbo-api的依赖

```
		<dependency>
			<groupId>cn.xuhao</groupId>
			<artifactId>dubbo-api</artifactId>
			<version>0.0.1-SNAPSHOT</version>
		</dependency>
```

在spring-boot的启动类：Application.java中添加注解（@EnableCaching和@EnableDubboConfiguration）和挂起服务(不挂起执行完就结束了)：

如果创建的是web项目就不用修改代码，只添加注解就好了。

```
package cn.xuhao.dubboProvide;

import java.util.concurrent.CountDownLatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;

@EnableCaching
@SpringBootApplication
@EnableDubboConfiguration
public class Application {
	//使用jar方式打包的启动方式
	private static CountDownLatch countDownLatch = new CountDownLatch(1);
	public static void main(String[] args) throws InterruptedException {
	SpringApplication.run(Application.class, args).registerShutdownHook();
	countDownLatch.await();
	}
}
```

实现dubbo-api项目中的接口 DemoServerImp.java ：

```
package cn.xuhao.dubboProvide.service.impl;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.xuhao.dubbo.api.DemoService;
import cn.xuhao.dubbo.api.TestService;

@com.alibaba.dubbo.config.annotation.Service(interfaceClass = DemoService.class)
@Service
public class DemoServerImp implements DemoService {

	@Reference(check=false)//启动消费者不检查服务者是否存在
    public TestService testService;
	
    @Override
    public String sayHello(String s) {
    	String test = "调用失败";
		try {
			test = testService.testService(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return "provide服务提供方0:你好，"+s+"! \t\t " + test;
    }
}
```

编写项目中的application.yaml（application.properties）配置文件，配置dubbo服务，注意修改协议的端口号：

```
spring:
  dubbo:
    appname: provider-test
    registry: zookeeper://192.168.76.128:2181
    protocol: dubbo
    port: 20880
```

### 4、创建dubbo-consumer服务的消费端。项目创建为web类型的项目。

项目pom.xml文件中添加web和接口依赖

```
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
		<!--服务接口 -->
	    <dependency>
	      <groupId>cn.xuhao</groupId>
	      <artifactId>dubbo-api</artifactId>
	      <version>0.0.1-SNAPSHOT</version>
	    </dependency>
	</dependencies>
```

项目的目录结构为：

![image.png](http://upload-images.jianshu.io/upload_images/8844416-b1f1703705418587.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

在项目启动的Application.class文件中添加@EnableDubboConfiguration注解:

```
package cn.xuhao.dubboConsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;

@SpringBootApplication
@EnableDubboConfiguration
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
```

编写DubboTestController.java ,使用@Reference注解使用dubbo服务端服务

```
package cn.xuhao.dubboConsumer.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.xuhao.dubbo.api.DemoService;


@RestController
public class DubboTestController {
    @Reference(check=false)//启动消费者不检查服务者是否存在
    public DemoService demoService;

    @RequestMapping(value = "hello/{name}",method = RequestMethod.GET)
    public String Hello(@PathVariable String name){
        String ret_msg;
        try {
            ret_msg  = demoService.sayHello(name);
        }catch (Exception e){
            e.printStackTrace();
            ret_msg = "出错啦！";
        }
        return  ret_msg;
    }
}
```

修改application.properties配置文件，配置tomcat端口和dubbo服务

```
## tomcat端口号配置  
server.port=8088
## 项目访问路径配置  
server.context-path=/
## Dubbo 应用名称  
spring.dubbo.appname=consumer-test
##Dubbo 注册中心地址  
spring.dubbo.registry=zookeeper://192.168.76.128:2181
##Dubbo 协议名称  
spring.dubbo.protocol=dubbo
```

现在依次启动dubbo-provide，dubbo-consumer项目,就会在dubbo管控中心看到对应的提供者和消费者

![image.png](http://upload-images.jianshu.io/upload_images/8844416-ff3b56fe93aee1fe.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![image.png](http://upload-images.jianshu.io/upload_images/8844416-93923f08a208a306.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 5、创建测试项目dubbo-test

项目的创建方式同dubbo-api项目，创建简单的jar项目，通过jsoup调用服务消费者。

项目目录：

![image.png](http://upload-images.jianshu.io/upload_images/8844416-123c010bc5d879c8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

编写简单的测试类Main：

```
package dubboTest;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Main{
	public static void main(String[] args){
		long sumtime = 0;
		int count = 10000;
		//循环调用，请求id连续，判断请求是否丢失
		for(int i= 0;i< count;i++) {
			try {
				long start = System.currentTimeMillis();
				//jsoup 调用消费者地址
				Document doc = Jsoup.connect("http://localhost:8088/hello/请求"+i).get();
				Elements elements = doc.select("body");  //获取body中的内容
				long end = System.currentTimeMillis();
				long time = end-start;  //记录调用时间
				sumtime += time;
				String result = "执行时间" + time + "\t\t"+elements.text();
				System.out.println(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//计算平均调用时间
		float avgTime = sumtime/count;
		System.out.println("平均执行时间：" + avgTime +"毫秒");
	}
}
```

测试执行结果：

##### 1、三个服务提供者，测试1w次请求：

![image.png](http://upload-images.jianshu.io/upload_images/8844416-d1b64db792785ec2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

测试请求1w次，平均执行时间3ms

##### 2、测试多个服务端，其中一个服务端挂掉，请求与调用是否会丢失？

一个服务端挂掉后，消费者的rpc调用失败，dubbo进行重连，请求会被别的服务端执行不会丢失。

我关闭 服务提供方1，第2346次请求调用服务提供方1失败，重连后调用的服务提供方0.

![image.png](http://upload-images.jianshu.io/upload_images/8844416-e348e066af37f233.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![image.png](http://upload-images.jianshu.io/upload_images/8844416-814c7a9d39406591.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

##### 3、服务端全部挂掉后服务怎么办。

服务消费端报错，服务调用失败，服务消费端无限重连。

![image.png](http://upload-images.jianshu.io/upload_images/8844416-16b1baf9f7e6f83e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![image.png](http://upload-images.jianshu.io/upload_images/8844416-8e90dd8bbd1f241b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

##### 4、服务服务注册中心挂掉。

消费端能够缓存注册中心注册的服务端，注册中心挂掉后，消费端能够根据缓存调用服务端，当有服务端挂掉也会根据缓存的服务端进行失败重连，直到所有服务端挂掉才会调用失败，dubbo管控台不会感知挂掉的服务。

我关掉服务提供端0，dubbo只调用的提供端1，没有服务丢失。

![image.png](http://upload-images.jianshu.io/upload_images/8844416-bfa4475b2f40bd06.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![image.png](http://upload-images.jianshu.io/upload_images/8844416-f10fefab1c284df2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![image.png](http://upload-images.jianshu.io/upload_images/8844416-0e49eeb1f274558c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

dubbo服务调用执行流程：

![image](http://upload-images.jianshu.io/upload_images/8844416-7fd53635af532e41..png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

调用流程：

1、服务提供方启动运行，向注册中心注册服务；

2、服务消费者启动运行，向注册中心注册，并订阅自己所需的服务。

3、注册中心返回服务提供者地址列表给消费者，如果有变更，注册中心将基于长连接推送不变更数据给消费者。

4、服务消费者，从提供者地址列表中基于负载均衡算法，选一台提供者进行调用，如果调用失败再选另一台调用。

5、服务消费者和提供者，在内存中累计调用次数和调用时间，每分钟发送一次统计数据到监控中心。


github源码地址： https://github.com/xuejian0616/dubbo-spring-boot-starter-demo
