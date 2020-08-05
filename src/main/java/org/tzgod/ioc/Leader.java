package org.tzgod.ioc;


import org.tzgod.ioc.framework.annotation.Autowired;
import org.tzgod.ioc.framework.annotation.Component;

/**
 * 领导
 */
@Component
public class Leader {
    @Autowired
    private Coffee coffee;

    @Autowired
    public IWater water;


}
