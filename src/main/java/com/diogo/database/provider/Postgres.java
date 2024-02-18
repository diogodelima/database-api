package com.diogo.database.provider;

import com.diogo.database.Database;
import com.diogo.database.executor.DatabaseExecutor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Postgres extends Database {

    private final Connection connection;

    public Postgres(String host, String port, String database, String username, String password){

        Properties properties = new Properties();
        properties.put("user", username);
        properties.put("password", password);

        try {
            connection = DriverManager.getConnection("jdbc:postgresql://" + host + ":" + port + "/postgres?currentSchema=" + database, properties);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void beginTransaction() {
        try (DatabaseExecutor executor = new DatabaseExecutor(getConnection(), "BEGIN TRANSACTION")){
            executor.write();
        }
    }

    @Override
    public void commitTransaction() {
        try (DatabaseExecutor executor = new DatabaseExecutor(getConnection(), "COMMIT TRANSACTION")){
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
