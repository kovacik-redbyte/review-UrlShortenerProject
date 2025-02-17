package com.demo.urlshortener;
import org.jooq.*;
import org.jooq.impl.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class JooqConfig {

    // TODO mojsej lepsie je pouzivat constructor injection ako field injection, pretoze sa potom sa da lepsie spravit MOCKovany komponent
    private final DataSource dataSource;

    @Autowired
    public JooqConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public DSLContext dslContext() {
        return DSL.using(dataSource, SQLDialect.POSTGRES);
    }
}
