package com.diogo.database;

import com.diogo.database.executor.DatabaseExecutor;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.sql.Connection;

@Getter
@AllArgsConstructor
public abstract class Database {

    private final HikariDataSource dataSource = new HikariDataSource();

    public Database(String jdbcUrl, String driverClassName) {
        this.dataSource.setJdbcUrl(jdbcUrl);
        this.dataSource.setDriverClassName(driverClassName);
    }

    @SneakyThrows
    public DatabaseExecutor execute(){
        return new DatabaseExecutor(dataSource.getConnection());
    }

}
