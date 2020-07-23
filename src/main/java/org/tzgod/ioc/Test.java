package org.tzgod.ioc;

import com.qianfeng.ioc.framework.InjectionContext;


public class Test {

    public static void main(String[] args) {
//        Teacher teacher = InjectionContext.getBean(Teacher.class);
//        teacher.drink();
//        Leader bean = InjectionContext.getBean(Leader.class);
//        bean.drink();
//        Class<?>[] classes = IWater.class.getDeclaredClasses();
//        System.out.println(classes);

        InjectionContext injectionContext = new InjectionContext("com.qianfeng.ioc");
        try {
            Leader leader = (Leader) injectionContext.getBean("leader");
            System.out.println(leader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
