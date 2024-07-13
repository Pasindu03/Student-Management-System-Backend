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

@WebServlet(urlPatterns = "/student", loadOnStartup = 2)
public class Student2Controller extends HttpServlet{

    Connection connection;

    static String SAVE_STUDENT = "INSERT INTO student2 (id, name,city, email, level) VALUES (?,?,?,?,?)";
    static String GET_STUDENT = "SELECT id,name,city,email,level FROM student2 where id = ?";
    static String UPDATE_STUDENT = "UPDATE student2 SET name=?, city=?, email=?, level=? WHERE id=?";
    static String DELETE_STUDENT  = "DELETE FROM student2 where id = ?";

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

            if (preparedStatement.executeUpdate() != 0){
                System.out.println("Student Has been saved");
            } else {
                System.out.println("Student Has not been saved");
            }

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
                dto.setCity(resultSet.getString("city"));
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

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!req.getContentType().toLowerCase().contains("application/json") || (req.getContentType() == null)){
            //error
            resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        }

        try {
            var pstm = this.connection.prepareStatement(UPDATE_STUDENT);

            var id = req.getParameter("id");
            Jsonb jsonb = JsonbBuilder.create();
            var updatedStudent = jsonb.fromJson(req.getReader(),StudentDTO.class);

            pstm.setString(1, updatedStudent.getName());
            pstm.setString(2, updatedStudent.getCity());
            pstm.setString(3, updatedStudent.getEmail());
            pstm.setString(4, updatedStudent.getLevel());
            pstm.setString(5, id);

            if (pstm.executeUpdate() != 0){
                System.out.println("Student Has been updated");
            } else {
                System.out.println("Student Has not been updated");
            }

            pstm.executeUpdate();
            pstm.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!req.getContentType().toLowerCase().contains("application/json") || (req.getContentType() == null)){
            //error
            resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        }

        var stuId = req.getParameter("id");

        try {
            var pstm = this.connection.prepareStatement(DELETE_STUDENT);

            pstm.setString(1, stuId);

            if (pstm.executeUpdate() != 0){
                System.out.println("Student Has been deleted");
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                System.out.println("Student Has not been deleted");
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

            pstm.executeUpdate();
            pstm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
