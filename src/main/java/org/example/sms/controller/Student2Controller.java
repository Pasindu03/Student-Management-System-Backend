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

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/student")
public class Student2Controller extends HttpServlet{

    Connection connection;

    @Override
    public void init() throws ServletException {
        try{
            var ctx = new InitialContext();
            DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/stuRegistration");
            this.connection = pool.getConnection();
        } catch (SQLException | NamingException e) {
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
