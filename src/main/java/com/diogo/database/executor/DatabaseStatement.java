package com.diogo.database.executor;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.PreparedStatement;

@RequiredArgsConstructor
public class DatabaseStatement implements AutoCloseable {

    private final PreparedStatement preparedStatement;

    @SneakyThrows
    public void set(int i, Object o){
        this.preparedStatement.setObject(i, o);
    }

    @SneakyThrows
    public DatabaseQuery getQuery(){
        return new DatabaseQuery(this.preparedStatement.executeQuery());
    }

    @SneakyThrows
    public void execute(){
        this.preparedStatement.executeUpdate();
    }

    @SneakyThrows
    public void addBatch(){
        this.preparedStatement.addBatch();
    }

    @SneakyThrows
    public void executeBatch(){
        this.preparedStatement.executeBatch();
    }

    @Override
    public void close() throws Exception {
        this.preparedStatement.close();
    }
}
