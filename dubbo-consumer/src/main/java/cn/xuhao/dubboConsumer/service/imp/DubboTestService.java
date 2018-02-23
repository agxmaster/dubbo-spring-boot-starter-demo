package cn.xuhao.dubboConsumer.service.imp;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.xuhao.dubbo.api.DemoService;

import org.springframework.stereotype.Component;

@Component
public class DubboTestService {

	@Reference
    public DemoService demoService;

    public String Echo(String s){
        String ret_msg;
        try {
            ret_msg  = demoService.sayHello(s);
        }catch (Exception e){
            e.printStackTrace();
            ret_msg = "出错啦！";
        }
        return  ret_msg;
    }
}