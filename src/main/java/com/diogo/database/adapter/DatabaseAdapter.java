package com.diogo.database.adapter;

import java.sql.ResultSet;

public interface DatabaseAdapter<T> {

    T adapt(ResultSet resultSet);

}
