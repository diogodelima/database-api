package com.diogo.database.provider;

import com.diogo.database.Database;

public class SQLite extends Database {

    public SQLite(String file){
        super("jdbc:sqlite:" + file, "org.sqlite.JDBC");
    }

}
