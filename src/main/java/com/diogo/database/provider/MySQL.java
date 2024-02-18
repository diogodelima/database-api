package com.diogo.database.provider;

import com.diogo.database.Database;
import com.diogo.database.executor.DatabaseExecutor;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQL extends Database {

    private final HikariDataSource dataSource = new HikariDataSource();

    public MySQL(String host, String port, String database, String username, String password) {

        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

    }


    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        dataSource.close();
    }

    @Override
    public void beginTransaction() {
        try (DatabaseExecutor executor = new DatabaseExecutor(getConnection(), "BEGIN TRANSACTION")){
            executor.write();
        }
    }

    @Override
    public void commitTransaction() {
        try (DatabaseExecutor executor = new DatabaseExecutor(getConnection(), "COMMIT")){
            executor.write();
        }
    }

    @Override
    public void rollbackTransaction() {
        try (DatabaseExecutor executor = new DatabaseExecutor(getConnection(),"ROLLBACK")){
            executor.write();
        }
    }
}
