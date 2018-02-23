# dubbo-spring-boot-starter-demo

1、根据阿里官方提供的dubbo-spring-boot-starter 1.0.1 整合的dubbo 3.6.0和spring boot 1.5.9。
2、项目有四个工程，提供者provider、消费者 consumer、服务接口 api 、测试项目test。
3、consumer项目是web项目，其他的为jar项目。
4、注意修改application.yaml(application.properties) 配置文件中的zookeeper地址和端口号。
5、测试项目主要是一直发送请求，测试当服务的提供者，消费者和注册中心挂掉后请求是否能够丢失。
