package org.tzgod.ioc.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) //作用在类上
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
}
