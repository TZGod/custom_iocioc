package org.tzgod.ioc;

import com.qianfeng.ioc.framework.annotation.Autowired;
import com.qianfeng.ioc.framework.annotation.Component;

/**
 * 领导
 */
@Component
public class Leader {


    @Autowired
    private Coffee coffee;

    @Autowired
    public IWater water;





    /**
     * 喝水
     */
//    @Deprecated
//    public void drink(){
//        System.out.println("喝"+water1.getType());
//    }
}
