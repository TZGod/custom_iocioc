package org.tzgod.ioc;

import com.qianfeng.ioc.framework.annotation.Component;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Test2 {

    private final static HashMap<Class, List<Class>> fatherSonHashMap = new HashMap<Class, List<Class>>();

    /**
     * 1、找到所有的标注有@Component注解的类
     * @param args
     */
    public static void main(String[] args) throws ClassNotFoundException {
        String basePackage = "com.qianfeng.ioc";
        URL resource = Test2.class.getResource("/");
        System.out.println(resource.getPath());
        String resourcePath = resource.getPath();
        String replaceAll = basePackage.replaceAll("\\.", "/");
        String path = resourcePath+replaceAll;

        //通过此类获取当前路径下有哪些文件
        File file = new File(path);
        File[] files = file.listFiles();
        for (File f: files) {
            String name = f.getName();
            //获取Java源文件
            if (f.isFile() && name.endsWith(".class")) {
                String[] split = name.split("\\.");
                //找到哪些类上面有Component注解
                Class<?> aClass = Class.forName(basePackage + "." + split[0]);
                Component annotation = aClass.getAnnotation(Component.class);
                if (annotation != null) {
                    Class<?>[] interfaces = aClass.getInterfaces();
                    for (int j=0; j<interfaces.length; j++) {
                        Class<?> anInterface = interfaces[j];


                        List<Class> sonList = fatherSonHashMap.get(anInterface);
                        if (sonList == null) {
                            //第一次找到这个father，就保存到map中
                            ArrayList<Class> sonClassList = new ArrayList<Class>();
                            sonClassList.add(aClass);
                            fatherSonHashMap.put(anInterface,sonClassList);
                        } else {
                            //第二次找到，就将fater对应的son集合获取到，并且将新的son加入集合中
                            sonList.add(aClass);
                        }

                    }

                }

                System.out.println(fatherSonHashMap);
                //在有component注解的类上找他们的父类或者接口
            }
        }


    }
}
