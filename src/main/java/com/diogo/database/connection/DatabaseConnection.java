package com.diogo.database.connection;

import com.diogo.database.credentials.DatabaseCredentials;
import com.diogo.database.provider.MySQL;
import com.diogo.database.provider.Postgres;
import com.diogo.database.Database;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DatabaseConnection {

    private final DatabaseCredentials databaseCredentials;

    public Database setup(){

        return switch (databaseCredentials.getType()){
            case MYSQL -> new MySQL(databaseCredentials.getHost(), databaseCredentials.getPort(), databaseCredentials.getDatabase(), databaseCredentials.getUsername(), databaseCredentials.getPassword());
            case POSTGRES -> new Postgres(databaseCredentials.getHost(), databaseCredentials.getPort(), databaseCredentials.getDatabase(), databaseCredentials.getUsername(), databaseCredentials.getPassword());
        };

    }

}
