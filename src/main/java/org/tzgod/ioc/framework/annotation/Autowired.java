package org.tzgod.ioc.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//元注解
@Retention(RetentionPolicy.RUNTIME)  //表示自定义的注解再程序运行期间有效
@Target(ElementType.FIELD) //表示当前自定义的注解只能作用于属性上面
public @interface Autowired {

    //Class value(); //等于是注解中的属性
}
