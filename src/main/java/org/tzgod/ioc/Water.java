package org.tzgod.ioc;

import com.qianfeng.ioc.framework.annotation.Autowired;
import com.qianfeng.ioc.framework.annotation.Component;

/**
 * 水
 */
@Component
public class Water implements IWater{

    private String type;

    @Autowired
    private Leader leader;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return "白开水";
    }
}
