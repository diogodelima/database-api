package com.diogo.database.executor;

import com.diogo.database.Database;
import com.diogo.database.DatabaseType;
import com.diogo.database.connection.DatabaseConnection;
import com.diogo.database.credentials.impl.DatabaseCredentialsImpl;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseExecutorTest {

    private record Person(String name, int age) {

        @Override
            public boolean equals(Object obj) {

                if (this == obj)
                    return true;

                if (!(obj instanceof Person person))
                    return false;

                return person.name.equals(this.name) && person.age == this.age;
            }

            @Override
            public String toString() {
                return "{name=" + this.name + ", age=" + this.age + "}";
            }

        }

    private final Database database = new DatabaseConnection(
            new DatabaseCredentialsImpl(DatabaseType.MYSQL, "localhost", "3306", "test",
                    "root", "", "")
    ).setup();

    @Test
    public void testWriteAndRead(){

        try (DatabaseExecutor executor = database.execute()){

            executor
                    .query("""
                            CREATE TABLE IF NOT EXISTS test(name VARCHAR(32) PRIMARY KEY, age INTEGER NOT NULL)
                            """)
                    .write();

            executor
                    .query("""
                            INSERT INTO test VALUES(?,?)
                            """)
                    .write(statement -> {
                        try {
                            statement.setString(1, "Diogo");
                            statement.setInt(2, 20);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });

            Person expected = new Person("Diogo", 20);
            Person person = executor
                    .query("""
                            SELECT * FROM test WHERE name = ?
                            """)
                    .readOne(statement -> {
                        try {
                            statement.setString(1, "Diogo");
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }, resultSet -> {

                        final String name = resultSet.getString("name");
                        final int age = resultSet.getInt("age");

                        return new Person(name, age);
                    });

            assertEquals(expected, person);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}