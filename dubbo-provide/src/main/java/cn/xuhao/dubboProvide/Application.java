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