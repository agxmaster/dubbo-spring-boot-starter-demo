package cn.xuhao.dubboConsumer.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.xuhao.dubboConsumer.service.imp.DubboTestService;

import javax.annotation.Resource;

@RestController
public class DubboTestController {

    @Resource
    private DubboTestService dubboTestService;

    @RequestMapping(value = "hello/{name}",method = RequestMethod.GET)
    public String Hello(@PathVariable String name){
        return dubboTestService.Echo(name);
    }
}