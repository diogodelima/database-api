package com.diogo.database.provider;

import com.diogo.database.Database;

public class MySQL extends Database {

    public MySQL(String host, String port, String database, String username, String password) {
        super("jdbc:mysql://" + host + ":" + port + "/" + database, "com.mysql.cj.jdbc.Driver");
        getDataSource().setUsername(username);
        getDataSource().setPassword(password);
    }

}
