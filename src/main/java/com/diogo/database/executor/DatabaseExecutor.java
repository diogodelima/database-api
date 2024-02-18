package com.diogo.database.executor;

import com.diogo.database.adapter.DatabaseAdapter;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

@AllArgsConstructor
public class DatabaseExecutor implements AutoCloseable {

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final Connection connection;
    private final String query;

    public <T> CompletableFuture<T> readOne(Consumer<PreparedStatement> action, DatabaseAdapter<T> adapter){

        return CompletableFuture.supplyAsync(() -> {

            try (PreparedStatement statement = connection.prepareStatement(query)){

                action.accept(statement);

                ResultSet resultSet = statement.executeQuery();
                return resultSet.next() ? adapter.adapt(resultSet) : null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }, executorService);

    }

    public <T> CompletableFuture<T> readOne(DatabaseAdapter<T> adapter){
        return readOne(preparedStatement -> {}, adapter);
    }

    public <C extends Collection<T>, T> CompletableFuture<C> readMany(Consumer<PreparedStatement> action, DatabaseAdapter<T> adapter, Supplier<C> supplier){

        return CompletableFuture.supplyAsync(() -> {

            try (PreparedStatement statement = connection.prepareStatement(query)){

                C c = supplier.get();
                action.accept(statement);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next())
                    c.add(adapter.adapt(resultSet));

                return c;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }, executorService);

    }

    public <C extends Collection<T>, T> CompletableFuture<C> readMany(DatabaseAdapter<T> adapter, Supplier<C> supplier){
        return readMany(preparedStatement -> {}, adapter, supplier);
    }

    public void write(Consumer<PreparedStatement> action){

        CompletableFuture.runAsync(() -> {

            try (PreparedStatement statement = connection.prepareStatement(query)){

                action.accept(statement);
                statement.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }, executorService);

    }

    public void write(){
        write(preparedStatement -> {});
    }

    @Override
    public void close() {

        try {

            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {

            try {
                if (connection != null && !connection.isClosed())
                    connection.close();
            } catch (SQLException ignored) {}

        }

    }

}
