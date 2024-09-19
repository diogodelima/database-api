package com.diogo.database.connection;

import com.diogo.database.credentials.DatabaseCredentials;
import com.diogo.database.credentials.LocalDatabaseCredentials;
import com.diogo.database.credentials.RemoteDatabaseCredentials;
import com.diogo.database.provider.MySQL;
import com.diogo.database.Database;
import com.diogo.database.provider.SQLite;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DatabaseConnection {

    private final DatabaseCredentials databaseCredentials;

    public Database setup(){

        return switch (databaseCredentials.getType()){
            case MYSQL -> {
                final RemoteDatabaseCredentials remoteDatabaseCredentials = (RemoteDatabaseCredentials) databaseCredentials;
                yield new MySQL(remoteDatabaseCredentials.getHost(), remoteDatabaseCredentials.getPort(), remoteDatabaseCredentials.getDatabase(), remoteDatabaseCredentials.getUsername(), remoteDatabaseCredentials.getPassword());
            }
            case SQLITE -> {
                final LocalDatabaseCredentials localDatabaseCredentials = (LocalDatabaseCredentials) databaseCredentials;
                yield new SQLite(localDatabaseCredentials.getFile());
            }
            default -> throw new IllegalStateException("Unexpected value: " + databaseCredentials.getType());
        };

    }

}
