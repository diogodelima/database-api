package com.diogo.database;

import com.diogo.database.executor.DatabaseExecutor;
import lombok.AllArgsConstructor;

import java.sql.Connection;

@AllArgsConstructor
public abstract class Database {

    public abstract Connection getConnection();

    public abstract void close();

    public abstract void beginTransaction();

    public abstract void commitTransaction();

    public abstract void rollbackTransaction();

    public DatabaseExecutor executor(String query){
        return new DatabaseExecutor(getConnection(), query);
    }

}
