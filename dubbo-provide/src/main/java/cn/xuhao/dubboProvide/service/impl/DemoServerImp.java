package cn.xuhao.dubboProvide.service.impl;

import org.springframework.stereotype.Service;

import cn.xuhao.dubbo.api.DemoService;

@com.alibaba.dubbo.config.annotation.Service(interfaceClass = DemoService.class)
@Service
public class DemoServerImp implements DemoService {
    @Override
    public String sayHello(String s) {
        return "服务提供方0:你好，"+s+"!";
    }
}

