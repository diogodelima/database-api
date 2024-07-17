package com.diogo.database.executor;

import com.diogo.database.adapter.DatabaseAdapter;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@AllArgsConstructor
public class DatabaseExecutor implements AutoCloseable {

    private final Connection connection;
    private String query;

    public DatabaseExecutor(Connection connection){
        this(connection, null);
    }

    public DatabaseExecutor query(String query){
        this.query = query;
        return this;
    }

    @SneakyThrows
    public <T> void batch(Collection<T> data, Function<T, Consumer<DatabaseStatement>> function){

        try (DatabaseStatement statement = new DatabaseStatement(connection.prepareStatement(query))){

            this.connection.setAutoCommit(false);

            for (T t : data){
                function.apply(t).accept(statement);
                statement.addBatch();
            }

            statement.executeBatch();
            this.connection.commit();
            this.connection.setAutoCommit(true);
        }

    }

    @SneakyThrows
    public <T> Optional<T> readOne(Consumer<DatabaseStatement> action, DatabaseAdapter<T> adapter){

        try (DatabaseStatement statement = new DatabaseStatement(connection.prepareStatement(query))){

            action.accept(statement);

            try (DatabaseQuery query = statement.getQuery()){
                return query.next() ? Optional.ofNullable(adapter.adapt(query)) : Optional.empty();
            }

        }

    }

    public <T> Optional<T> readOne(DatabaseAdapter<T> adapter){
        return readOne(preparedStatement -> {}, adapter);
    }

    @SneakyThrows
    public <C extends Collection<T>, T> C readMany(Consumer<DatabaseStatement> action, DatabaseAdapter<T> adapter, Supplier<C> supplier){

        try (DatabaseStatement statement = new DatabaseStatement(connection.prepareStatement(query))){

            action.accept(statement);

            try (DatabaseQuery query = statement.getQuery()){

                C c = supplier.get();

                while (query.next())
                    c.add(adapter.adapt(query));

                return c;
            }

        }

    }

    public <C extends Collection<T>, T> C readMany(DatabaseAdapter<T> adapter, Supplier<C> supplier){
        return readMany(preparedStatement -> {}, adapter, supplier);
    }

    public void write(Consumer<DatabaseStatement> action){

        try (DatabaseStatement statement = new DatabaseStatement(connection.prepareStatement(query))){

            action.accept(statement);
            statement.execute();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void write(){
        write(preparedStatement -> {});
    }

    @SneakyThrows
    @Override
    public void close() {

        if (this.connection != null && !connection.isClosed())
            connection.close();

    }
}
