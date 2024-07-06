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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebServlet(urlPatterns = "/student2")
public class StudentController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Todo : Save Student

        if (!req.getContentType().toLowerCase().contains("application/json") || (req.getContentType() == null)){
            //error
            resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        }

        String id = UUID.randomUUID().toString();

        Jsonb sonb = JsonbBuilder.create();
        List<StudentDTO> list = sonb.fromJson(req.getReader(), new ArrayList<StudentDTO>() {}.getClass().getGenericSuperclass());
        list.forEach(System.out::println);

        /*Jsonb jsonb = JsonbBuilder.create();
        StudentDTO dto = jsonb.fromJson(req.getReader(), StudentDTO.class);
        dto.setId(id);
        System.out.println(dto);*/

       /* //process
        BufferedReader bufferedReader = req.getReader();
        StringBuilder builder = new StringBuilder();

        PrintWriter writer = resp.getWriter();

        bufferedReader.lines().forEach(line -> builder.append(line + "\n"));
        System.out.println(builder);

        writer.write(builder.toString());
        writer.close();*/

        /*//JSON Manipulated with Parson
        JsonReader reader = Json.createReader(req.getReader());

        *//*//*System.out.print("Your email is : ");
        System.out.println(jsonObject.getString("email"));*//*

        JsonArray array = reader.readArray();
        for (int i = 0; i < array.size(); i++) {
            JsonObject jsonObject = array.getJsonObject(i);
            System.out.println(jsonObject.getString("name"));
        }*/
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Todo : Update Student
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Todo : Delete Student
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Todo : Read Student
    }
}
