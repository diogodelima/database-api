package com.diogo.database.credentials.impl;

import com.diogo.database.credentials.type.DatabaseType;
import org.bukkit.configuration.ConfigurationSection;

public class BukkitDatabaseCredentialsImpl extends DatabaseCredentialsImpl {


    public BukkitDatabaseCredentialsImpl(ConfigurationSection section) {

        super(DatabaseType.valueOf(section.getString("type")), section.getString("host"), section.getString("port"),
                section.getString("database"), section.getString("username"), section.getString("password"));

    }
}
