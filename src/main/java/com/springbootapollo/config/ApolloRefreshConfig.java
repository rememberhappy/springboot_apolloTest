package com.springbootapollo.config;

import com.ctrip.framework.apollo.core.ConfigConsts;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 当一个类实现了这个接口（ApplicationContextAware）之后，这个类就可以方便获得ApplicationContext中的所有bean。换句话说，就是这个类可以直接获取spring配置文件中，所有有引用到的bean对象。
 *
 * 在使用@ConfigurationProperties注解映射配置信息到实体类中的时候，需要添加Apollo监听器控制在Apollo配置变化时自动更新注入的值
 * 两种方式，RefreshScope方式
 */
@Component
public class ApolloRefreshConfig implements ApplicationContextAware {
    ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Autowired
    RefreshScope refreshScope;

    // apollo监听器，指定的namespace是“application.yml“，如果不指定，默认只使用application
    @ApolloConfigChangeListener({"application.yml"})
    public void onChange(ConfigChangeEvent changeEvent) {
        for (String changedKey : changeEvent.changedKeys()) {
            System.out.println("apollo changed namespace:{"+changeEvent.getNamespace()+"} Key:{"+changedKey+"} value:{"+changeEvent.getChange(changedKey)+"}");
        }
        refreshProperties(changeEvent);
    }

    public void refreshProperties(ConfigChangeEvent changeEvent) {
        // this.applicationContext.publishEvent 用于主动触发指定Event事件的容器事件。
        this.applicationContext.publishEvent(new EnvironmentChangeEvent(changeEvent.changedKeys()));
        refreshScope.refreshAll();
    }
}
