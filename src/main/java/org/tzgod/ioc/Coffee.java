package org.tzgod.ioc;


import org.tzgod.ioc.framework.annotation.Component;

@Component
public class Coffee implements IWater{

    public String getType(){
        return "拿铁";
    }

}
