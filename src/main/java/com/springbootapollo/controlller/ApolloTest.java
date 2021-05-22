package com.springbootapollo.controlller;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloJsonValue;
import com.springbootapollo.domain.Student;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("apolloTest")
public class ApolloTest {
//    @Value("user.name")
    @Value("${timeout:5000}")
    private String timeout;

    @Resource
    private Student student;

    @ApolloConfig
    private Config config; // 注入application命名空间的属性，可以从config中获取到指定环境中，指定集群下，命名空间名称为：application中属性的值

    @ApolloConfig("application.yml")
    private Config config1; // 注入application.yml命名空间的属性，可以从config中获取到指定环境中，指定集群下，命名空间名称为：application.yml中属性的值

    @ApolloJsonValue("${student.phone}")
    private String userName; // @ApolloJsonValue和@Value一样的用法

    @RequestMapping("timeout")
    public String getPubProperties(){
        System.out.println(timeout);
        return timeout;
    }

    @RequestMapping("student")
    public String getStudentProperties(){
        System.out.println(student.toString());
        String property = config.getProperty("server.port", "默认值");
        System.out.println(property);
        String property1 = config1.getProperty("student.address", "默认值");
        System.out.println(property1);
        System.out.println(userName);
        return student.toString();
    }
}
