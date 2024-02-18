package com.diogo.database.credentials.impl;

import com.diogo.database.credentials.type.DatabaseType;
import com.diogo.database.credentials.DatabaseCredentials;
import lombok.AllArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;

@AllArgsConstructor
public class BukkitDatabaseCredentialsImpl implements DatabaseCredentials {

    private final ConfigurationSection section;

    @Override
    public DatabaseType getType() {
        return DatabaseType.valueOf(section.getString("type"));
    }

    @Override
    public String getHost() {
        return section.getString("host");
    }

    @Override
    public String getPort() {
        return section.getString("port");
    }

    @Override
    public String getDatabase() {
        return section.getString("database");
    }

    @Override
    public String getUsername() {
        return section.getString("username");
    }

    @Override
    public String getPassword() {
        return section.getString("password");
    }

}
