package org.tzgod.ioc.framework;

import com.qianfeng.ioc.Test2;
import com.qianfeng.ioc.framework.annotation.Autowired;
import com.qianfeng.ioc.framework.annotation.Component;
import com.qianfeng.ioc.framework.exception.UnexpectedBeanDefitionalException;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 秘书：自定IOC框架
 */
public class InjectionContext {

    private final static HashMap<Class, List<Class>> fatherSonHashMap = new HashMap<Class, List<Class>>();

    //beanmap用来放初始化好的对象。id：对象
    public static final  HashMap<String, Object> beansMap = new HashMap<String, Object>();

    private static final List<Class> beansList = new ArrayList<Class>();

    private String basePackage;

    public InjectionContext() {
    }

    public InjectionContext(String basePackage) {
        this.basePackage = basePackage;

        try {
            init(basePackage);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getBean(String id){
        return beansMap.get(id);
    }

    /**
     * 属性注入
     * @param leaderClass
     * @param
     * @return
     * @throws Exception
     */
    private  Object inject(Class leaderClass) throws Exception{
        try {
            //通过反射给Leader中的water变量赋值
//            Class<Leader> leaderClass = Leader.class;
            //获取所有的属性
            Field[] declaredFields = leaderClass.getDeclaredFields();

            //保证单例  begin
            String id1 = getId(leaderClass);
            Object leader = beansMap.get(id1);
            if (leader == null) {
                 leader = leaderClass.newInstance();
                  beansMap.put(id1,leader);
            }
            //保证单例  end

            //找到water
            for (int i = 0; i < declaredFields.length; i++) {
                Field field = declaredFields[i];
                //允许私有属性通过反射进行赋值
                field.setAccessible(true);
                //获取属性上是否有Autowired注解
                Autowired annotation = field.getAnnotation(Autowired.class);
                if (annotation != null) {
                    //问题：如果属性的类型是一个接口，则aClass.newInstance()会出错
                    //如果此处是一个接口，则需要通过接口找到其所有的实现类，如果有多个实现类，则通过属性名称来判断使用哪一个实现类
                    Class aClass = field.getType();

                    //如果变量的类型是一个接口，逻辑如下
                    if (aClass.isInterface()){
                        //如果此处是一个接口，则需要通过接口找到其所有的实现类，如果有多个实现类，则通过属性名称来判断使用哪一个实现类
                        //获取接口的所有实现类
                        List<Class> classList = fatherSonHashMap.get(aClass);
                        if (classList == null) {
                            throw new NullPointerException();
                        }

                        // 一个接口如果只有一个实现类，则直接注入
                        if (classList != null && classList.size() == 1) {
                            Class sonClass = classList.get(0);
                            //解决单例问题
                            String id = getId(sonClass);
                            Object bean = beansMap.get(id);
                            if (bean == null) {
                                Object instance = sonClass.newInstance();
                                field.set(leader,instance);
                                beansMap.put(id,instance);
                            } else {
                                field.set(leader,bean);
                            }

                        } else {
                            //顶一个标签，用来表示这种情况：有多个实现类，但是一个都没有匹配成功
                            boolean isAutowiredFail = true;

                            for (int j = 0; j < classList.size(); j++) {
                                Class sonClass = classList.get(j);
                                String fieldName = field.getName();
                                String substring = fieldName.substring(1, fieldName.length());
                                String upperCase = fieldName.toUpperCase();
                                char fristChar = upperCase.charAt(0);
                                String className = fristChar+substring;
                                //对比sonClass的名称和属性的名称是否一致，如果一致则找到需要注入的对象。没有找到这抛出异常
                                if (sonClass.getName().endsWith(className)) {
                                    //找到了名称一致的接口的实现类的对象，并且完成注入
                                    //解决单例问题
                                    String id = getId(sonClass);
                                    Object bean = beansMap.get(id);
                                    if (bean == null) {
                                        Object instance = sonClass.newInstance();
                                        field.set(leader,instance);
                                        beansMap.put(id,instance);
                                    } else {
                                        field.set(leader,bean);
                                    }
//                                    field.set(leader,sonClass.newInstance());
                                    isAutowiredFail = false;
                                }
                            }

                            if (isAutowiredFail) {
                                throw new UnexpectedBeanDefitionalException("原本期望找到1个对象注入，但是了找到了2个，无法识别应该注入哪一个");
                            }
                        }



                    } else {
                        //当变量的类型不是一个接口
                        //参数1：属性所有者
//                        field.set(leader,aClass.newInstance());
                        //解决单利问题
                        String id = getId(aClass);
                        Object bean = beansMap.get(id);
                        if (bean == null) {
                            Object instance = aClass.newInstance();
                            field.set(leader,instance);
                            beansMap.put(id,instance);
                        } else {
                            field.set(leader,bean);
                        }
                    }

                }


                //declaredFields[i].getName() 获取当前属性的名称
//                if ("water".equals(field.getName())) {
//                    // 给water属性赋值
//                    //参数1：属性所有者
//                    field.set(leader,new Water());
//                }
            }

            return leader;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }






    /**
     * 1、找到所有的标注有@Component注解的类
     *
     */
    private void init(String basePackage) throws Exception {
//        String basePackage = "com.qianfeng.ioc";
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

                    //为了给下方标识为1922这一处代码使用
                    beansList.add(aClass);


                    //所有Component标注的类的接口，以及接口对应的所有实现类 （此处逻辑需要在依赖注入之前执行）
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

        //--1922-此处就已经找到了所有被Component标注的类 （要对找到了类进行依赖注入）

        for (int i = 0,size=beansList.size(); i < size; i++) {
            Class aClass = beansList.get(i);
            Object obj = inject(aClass);
            String id = getId(aClass);
            beansMap.put(id,obj);

        }

        System.out.println(beansMap);


    }

    /**
     * 获取对象对应的Id
     * @param aClass
     * @return
     */
    private String getId(Class aClass){
        String tempId = aClass.getName();
        //获取最后一个“.”的下标
        int lastIndex = tempId.lastIndexOf(".");
        tempId = tempId.substring(lastIndex+1, tempId.length());
        char firstChar = tempId.toLowerCase().charAt(0);
        String substring = tempId.substring(1, tempId.length());
        String id = firstChar+substring;
        return id;
    }




}
