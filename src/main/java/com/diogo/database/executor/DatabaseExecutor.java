package com.diogo.database.executor;

import com.diogo.database.adapter.DatabaseAdapter;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.function.Consumer;
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

    public <T> T readOne(Consumer<PreparedStatement> action, DatabaseAdapter<T> adapter){

        try (PreparedStatement statement = connection.prepareStatement(query)){

            action.accept(statement);

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? adapter.adapt(resultSet) : null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public <T> T readOne(DatabaseAdapter<T> adapter){
        return readOne(preparedStatement -> {}, adapter);
    }

    public <C extends Collection<T>, T> C readMany(Consumer<PreparedStatement> action, DatabaseAdapter<T> adapter, Supplier<C> supplier){

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

    }

    public <C extends Collection<T>, T> C readMany(DatabaseAdapter<T> adapter, Supplier<C> supplier){
        return readMany(preparedStatement -> {}, adapter, supplier);
    }

    public void write(Consumer<PreparedStatement> action){

        try (PreparedStatement statement = connection.prepareStatement(query)){

            action.accept(statement);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void write(){
        write(preparedStatement -> {});
    }

    @Override
    public void close() throws Exception {

        if (this.connection != null && !connection.isClosed())
            connection.close();

    }
}
