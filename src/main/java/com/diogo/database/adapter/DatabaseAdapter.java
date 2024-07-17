package com.diogo.database.adapter;

import com.diogo.database.executor.DatabaseQuery;

import java.sql.SQLException;

@FunctionalInterface
public interface DatabaseAdapter<T> {

    T adapt(DatabaseQuery query) throws SQLException;

}
