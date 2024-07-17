package com.diogo.database.executor;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;

@RequiredArgsConstructor
public class DatabaseQuery implements AutoCloseable {

    private final ResultSet resultSet;

    @SneakyThrows
    public boolean next(){
        return resultSet.next();
    }

    @SneakyThrows
    public Object get(String s){
        return resultSet.getObject(s);
    }

    @Override
    public void close() throws Exception {
        this.resultSet.close();
    }
}
