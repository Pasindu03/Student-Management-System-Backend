package org.example.sms.controller;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.sms.dto.StudentDTO;
import org.example.sms.persistence.Data;
import org.example.sms.persistence.DataProcess;
import org.example.sms.util.UtilProcess;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/student",
    initParams = {
        @WebInitParam(name = "driver", value = "com.mysql.cj.jdbc.Driver"),
        @WebInitParam(name = "dbURL", value = "jdbc:mysql://localhost:3306/AADSMS"),
        @WebInitParam(name = "dbUsername", value = "root"),
        @WebInitParam(name = "dbPassword", value = "Mixage03!")
    }
)
public class Student2Controller extends HttpServlet{

    Connection connection;

    /*static String SAVE_STUDENT = "INSERT INTO student2 (id, name,city, email, level) VALUES (?,?,?,?,?)";*/
    static String UPDATE_STUDENT = "UPDATE student2 SET name=?, city=?, email=?, level=? WHERE id=?";
    static String DELETE_STUDENT  = "DELETE FROM student2 where id = ?";

    @Override
    public void init() throws ServletException {
        try{
            var driverClass = getServletConfig().getInitParameter("driver");
            var dbURL = getServletConfig().getInitParameter("dbURL");
            var username = getServletConfig().getInitParameter("dbUsername");
            var password = getServletConfig().getInitParameter("dbPassword");
            Class.forName(driverClass);
            this.connection = DriverManager.getConnection(dbURL, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //todo : Saving a Student
        if (!req.getContentType().toLowerCase().contains("application/json") || (req.getContentType() == null)){
            //error
            resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        }

        try(var writer = resp.getWriter()) {
            Jsonb jsonb = JsonbBuilder.create();
            StudentDTO dto = jsonb.fromJson(req.getReader(), StudentDTO.class);
            DataProcess dp = new DataProcess();
            dto.setId(UtilProcess.generateId());
            writer.write(dp.saveStudent(dto, connection));
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //todo : Getting a Student
        var studentID = req.getParameter("id");
        DataProcess dp = new DataProcess();

        try (var writer = resp.getWriter()){
            StudentDTO dto = dp.getStudent(studentID, connection);
            resp.setContentType("application/json");
            var jsonb = JsonbBuilder.create();
            jsonb.toJson(dto, writer);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //todo : Updating a Student
        if (!req.getContentType().toLowerCase().contains("application/json") || (req.getContentType() == null)){
            //error
            resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        }

        DataProcess dp = new DataProcess();

        try (var writer = resp.getWriter()){

            var id = req.getParameter("id");
            Jsonb jsonb = JsonbBuilder.create();
            StudentDTO dto = jsonb.fromJson(req.getReader(),StudentDTO.class);

            if (dp.updateStudent(id,dto,connection)) {
                writer.write("Student Updated");
            } else {
                writer.write("Student Hasn't Updated");
            }
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //todo : Deleting a Student
        if (!req.getContentType().toLowerCase().contains("application/json") || (req.getContentType() == null)){
            //error
            resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        }

        var stuId = req.getParameter("id");
        DataProcess dp = new DataProcess();

        try(var writer = resp.getWriter()) {
            String dto = dp.deleteStudent(stuId, connection);
            resp.setContentType("application/json");
            var jsonb = JsonbBuilder.create();
            jsonb.toJson(dto, writer);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*Updated One*/

}
