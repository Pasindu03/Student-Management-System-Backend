package org.example.sms.controller;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.sms.dto.StudentDTO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

@WebServlet(urlPatterns = "/student")
public class Student2Controller extends HttpServlet{

    Connection connection;

    static String SAVE_STUDENT = "INSERT INTO student2 (id, name,city, email, level) VALUES (?,?,?,?,?)";
    static String GET_STUDENT = "SELECT * FROM student2 where id = ?";

    @Override
    public void init() throws ServletException {
        try{
            var driverClass = getServletContext().getInitParameter("driver");
            var dbURL = getServletContext().getInitParameter("dbURL");
            var username = getServletContext().getInitParameter("dbUsername");
            var password = getServletContext().getInitParameter("dbPassword");
            Class.forName(driverClass);
            this.connection = DriverManager.getConnection(dbURL, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!req.getContentType().toLowerCase().contains("application/json") || (req.getContentType() == null)){
            //error
            resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        }

        Jsonb jsonb = JsonbBuilder.create();
        StudentDTO dto = jsonb.fromJson(req.getReader(), StudentDTO.class);

        try {
            var preparedStatement = connection.prepareStatement(SAVE_STUDENT);
            preparedStatement.setString(1, dto.getId());
            preparedStatement.setString(2,dto.getName());
            preparedStatement.setString(3,dto.getCity());
            preparedStatement.setString(4,dto.getEmail());
            preparedStatement.setString(5,dto.getLevel());

            /*if (preparedStatement != 0){
                System.out.println("Student Has been saved");
            } else {
                System.out.println("Student Has not been saved");
            }*/

            System.out.println(dto);

            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var dto = new StudentDTO();
        var studentID = req.getParameter("id");

        try (var writer = resp.getWriter()){
            var ps = connection.prepareStatement(GET_STUDENT);

            ps.setString(1, studentID);
            var resultSet = ps.executeQuery();
            while(resultSet.next()){
                dto.setId(resultSet.getString("id"));
                dto.setName(resultSet.getString("name"));
                dto.setEmail(resultSet.getString("city"));
                dto.setEmail(resultSet.getString("email"));
                dto.setLevel(resultSet.getString("level"));
            }

            resp.setContentType("application/json");
            var jsonb = JsonbBuilder.create();
            jsonb.toJson(dto, resp.getWriter());

            writer.write(dto.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
