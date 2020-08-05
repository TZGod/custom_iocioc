package org.tzgod.ioc;


import org.tzgod.ioc.framework.annotation.Autowired;
import org.tzgod.ioc.framework.annotation.Component;

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
