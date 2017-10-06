package com.activiti.createTable1;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;
/**
 * Created by zyni on 2017/10/6.
 */
public class createTable {

    @Test
    public void createTableTest(){
        //表不存在的话创建表
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti.cfg.xml")
                .buildProcessEngine();
        System.out.println("------processEngine:" + processEngine);

    }
}
