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