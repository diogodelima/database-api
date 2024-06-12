package com.diogo.database.provider;

import com.diogo.database.Database;
import com.diogo.database.executor.DatabaseExecutor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite extends Database {

    private Connection connection;
    private final String file;

    public SQLite(String file){
        this.file = file;
    }

    @Override
    public Connection getConnection() {

        try {

            if (this.connection == null || this.connection.isClosed()){
                this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.file);
            }

            return this.connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void close() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                try {
                    this.connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void beginTransaction() {

        try (DatabaseExecutor executor = execute()){
            executor
                    .query("BEGIN TRANSACTION")
                    .write();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void commitTransaction() {

        try (DatabaseExecutor executor = execute()){
            executor
                    .query("COMMIT")
                    .write();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void rollbackTransaction() {

        try (DatabaseExecutor executor = execute()){
            executor
                    .query("ROLLBACK")
                    .write();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
