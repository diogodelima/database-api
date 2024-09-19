package com.diogo.database.credentials;

public interface RemoteDatabaseCredentials extends DatabaseCredentials {

    String getHost();

    String getPort();

    String getDatabase();

    String getUsername();

    String getPassword();

}
