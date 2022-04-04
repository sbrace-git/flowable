package org.example;

import org.flowable.common.engine.impl.AbstractEngineConfiguration;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;

public class HelloRequest {
    public static void main(String[] args) {
        ProcessEngine processEngine = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:mysql://localhost:3306/flowable")
                .setJdbcUsername("root")
                .setJdbcPassword("1111")
                .setJdbcDriver("com.mysql.jdbc.Driver")
                .setDatabaseSchemaUpdate(AbstractEngineConfiguration.DB_SCHEMA_UPDATE_TRUE)
                .buildProcessEngine();
    }

}
