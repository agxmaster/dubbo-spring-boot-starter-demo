package cn.xuhao.service.impl;

import org.springframework.stereotype.Service;

import cn.xuhao.dubbo.api.DemoService;
import cn.xuhao.dubbo.api.TestService;

@com.alibaba.dubbo.config.annotation.Service(interfaceClass = TestService.class)
@Service
public class ServerImp implements TestService {
	
    @Override
    public String testService(String s) {
        return "service服务提供方0:测试多服务，"+s+"!";
    }
}

