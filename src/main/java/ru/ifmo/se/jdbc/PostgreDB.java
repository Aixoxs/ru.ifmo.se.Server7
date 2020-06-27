package ru.ifmo.se.jdbc;

import ru.ifmo.se.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class PostgreDB {
    private Connection connection;
    private String host;
    private String port;
    private String db;
    private String user;
    private String password;

    public PostgreDB() {

    }

    public void init(){
        System.out.println("Введите данные для подключения к бд");
        host = readProp("host");
        port = readProp("port");
        db = readProp("место хранени базы данных");
        user = readProp("login владельца");
        password = readProp("password");
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://" + host + ":" + port + "/" + db, user, password);
            createTables();
            System.out.println("Данные введены успешно");
        } catch (SQLException e) {
            System.out.println("Неправильно введены данные, повторите попытку:");
            init();
        }
    }

    String readProp(String prop){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите " + prop + ":");
        return scanner.nextLine();
    }

    void createTables() throws SQLException {
        Statement stmt = connection.createStatement();
        String sql = "DO\n" +
                "$do$\n" +
                "DECLARE\n" +
                "   _kind \"char\";\n" +
                "BEGIN\n" +
                "   SELECT relkind\n" +
                "   FROM   pg_class\n" +
                "   WHERE  oid = 'content_tb_id_seq'::regclass  -- sequence name, optionally schema-qualified\n" +
                "   INTO  _kind;\n" +
                "\n" +
                "   IF NOT FOUND THEN       -- name is free\n" +
                "      CREATE SEQUENCE content_tb_id_seq\n" +
                "    INCREMENT BY 1\n" +
                "    NO MAXVALUE\n" +
                "    NO MINVALUE\n" +
                "    CACHE 1;\n" +
                "\n" +
                "   ELSIF _kind = 'S' THEN  -- sequence exists\n" +
                "      -- do nothing?\n" +
                "   ELSE                    -- object name exists for different kind\n" +
                "      -- do something!\n" +
                "   END IF;\n" +
                "END\n" +
                "$do$;" +
                "CREATE TABLE IF NOT EXISTS users (\n" +
                "                                     id       SERIAL PRIMARY KEY NOT NULL,\n" +
                "                                     login    VARCHAR(255) UNIQUE NOT NULL,\n" +
                "                                     password VARCHAR(255) UNIQUE NOT NULL\n" +
                ");" +
                "CREATE TABLE IF NOT EXISTS music_bands (\n" +
                "                                           id      integer DEFAULT nextval('content_tb_id_seq') NOT NULL,\n" +
                "                                           name     text      NOT NULL,\n" +
                "                                           coordinates_x        BIGINT,\n" +
                "                                           coordinates_y        FLOAT,\n" +
                "                                           creationDate  date   NOT NULL,\n" +
                "                                           numberOfParticipants INT,\n" +
                "                                           establishmentDate    date NOT NULL,\n" +
                "                                           genre    VARCHAR,\n" +
                "                                           person_name  text NOT NULL,\n" +
                "                                           person_height FLOAT NOT NULL,\n" +
                "                                           person_eyeColor VARCHAR NOT NULL,\n" +
                "                                           person_hairColor VARCHAR NOT NULL,\n" +
                "                                           person_nationality VARCHAR NOT NULL,\n" +
                "                                           userID   INTEGER NOT NULL,\n" +
                "                                           FOREIGN KEY (userID) REFERENCES users (id)\n" +
                ");";
        stmt.executeUpdate(sql);
        stmt.close();
    }

    public Connection getConnection() {
        return connection;
    }
}
