package com.diogo.database.adapter;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface DatabaseAdapter<T> {

    T adapt(ResultSet resultSet) throws SQLException;

}
