package com.zwx.learn.learnHttp;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
 

@RequestMapping("/test")
@RestController
public class IndexController {
 
    /**
     * 响应post请求
     * @return
     */
    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public String respost(){



        return "";
    }
 
 
    /**
     * 发出post请求
     * @return
     */
    @GetMapping("/reqpost")
    public String reqpost(){
        System.out.println("this.getClass(): " + this.getClass().toString());
        return "";
    }
}