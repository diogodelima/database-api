package com.diogo.database.executor;

import com.diogo.database.Database;
import com.diogo.database.DatabaseType;
import com.diogo.database.connection.DatabaseConnection;
import com.diogo.database.credentials.impl.DatabaseCredentialsImpl;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            new DatabaseCredentialsImpl(DatabaseType.SQLITE, "localhost", "3306", "test",
                    "root", "", "database.db")
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
                        statement.set(1, "Diogo");
                        statement.set(2, 20);
                    });

            Person expected = new Person("Diogo", 20);
            Optional<Person> person = executor
                    .query("""
                            SELECT * FROM test WHERE name = ?
                            """)
                    .readOne(statement -> statement.set(1, "Diogo"), query -> {

                        final String name = (String) query.get("name");
                        final int age = (Integer) query.get("age");

                        return new Person(name, age);
                    });

            if (person.isEmpty())
                throw new RuntimeException();

            assertEquals(expected, person.get());
        }

    }

    @Test
    public void testBatch(){

        final List<Person> data = List.of(
                new Person("Diogo", 20),
                new Person("Paulo", 3),
                new Person("João", 24),
                new Person("Tiago", 33),
                new Person("Afonso", 40),
                new Person("Maria", 22),
                new Person("Joana", 18),
                new Person("Mariana", 37),
                new Person("Catarina", 20)
        );

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
                    .batch(data, (person, statement) -> {
                        statement.set(1, person.name);
                        statement.set(2, person.age);
                    });

            List<Person> read = executor
                    .query("""
                            SELECT * FROM test
                            """)
                    .readMany(query -> {
                        final String name = (String) query.get("name");
                        final int age = (Integer) query.get("age");

                        return new Person(name, age);
                        }, ArrayList::new);

            assertEquals(data, read);

        }

    }

}