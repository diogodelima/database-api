package com.diogo.database.credentials.impl;

import com.diogo.database.credentials.DatabaseCredentials;
import com.diogo.database.credentials.type.DatabaseType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DatabaseCredentialsImpl implements DatabaseCredentials {

    private final DatabaseType type;
    private final String host, port, database, username, password;

    @Override
    public DatabaseType getType() {
        return type;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public String getPort() {
        return port;
    }

    @Override
    public String getDatabase() {
        return database;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

}
