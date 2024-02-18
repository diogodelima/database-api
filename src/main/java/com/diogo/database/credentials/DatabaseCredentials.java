package com.diogo.database.credentials;

import com.diogo.database.credentials.type.DatabaseType;

public interface DatabaseCredentials {

    DatabaseType getType();

    String getHost();

    String getPort();

    String getDatabase();

    String getUsername();

    String getPassword();

}