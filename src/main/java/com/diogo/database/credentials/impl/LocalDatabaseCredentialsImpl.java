package com.diogo.database.credentials.impl;

import com.diogo.database.DatabaseType;
import com.diogo.database.credentials.LocalDatabaseCredentials;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LocalDatabaseCredentialsImpl implements LocalDatabaseCredentials {

    private final DatabaseType type;
    private final String file;

    @Override
    public DatabaseType getType() {
        return type;
    }

    @Override
    public String getFile() {
        return file;
    }
}
