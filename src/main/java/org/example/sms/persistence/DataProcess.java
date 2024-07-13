package org.example.sms.persistence;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.example.sms.dto.StudentDTO;

import java.sql.Connection;
import java.sql.SQLException;

public final class DataProcess implements Data{
    static String GET_STUDENT = "SELECT id,name,city,email,level FROM student2 where id = ?";
    static String SAVE_STUDENT = "INSERT INTO student2 (id, name,city, email, level) VALUES (?,?,?,?,?)";
    static String UPDATE_STUDENT = "UPDATE student2 SET name=?, city=?, email=?, level=? WHERE id=?";
    static String DELETE_STUDENT  = "DELETE FROM student2 where id = ?";


    @Override
    public StudentDTO getStudent(String id, Connection connection) throws SQLException {
        var dto = new StudentDTO();
        try {
            var ps = connection.prepareStatement(GET_STUDENT);

            ps.setString(1, id);
            var resultSet = ps.executeQuery();
            while(resultSet.next()){
                dto.setId(resultSet.getString("id"));
                dto.setName(resultSet.getString("name"));
                dto.setCity(resultSet.getString("city"));
                dto.setEmail(resultSet.getString("email"));
                dto.setLevel(resultSet.getString("level"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dto;
    }

    @Override
    public String saveStudent(StudentDTO dto, Connection connection) throws SQLException {
        String message = "";
        try {
            var preparedStatement = connection.prepareStatement(SAVE_STUDENT);
            preparedStatement.setString(1, dto.getId());
            preparedStatement.setString(2,dto.getName());
            preparedStatement.setString(3,dto.getCity());
            preparedStatement.setString(4,dto.getEmail());
            preparedStatement.setString(5,dto.getLevel());

            preparedStatement.executeUpdate();

            if (preparedStatement.executeUpdate() != 0){
                message = "Save Student Successfully";
            } else {
                message = "Unsuccessful Save";
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;
    }

    @Override
    public String deleteStudent(String id, Connection connection) throws SQLException {
        String message = "";
        try {
            var pstm = connection.prepareStatement(DELETE_STUDENT);

            pstm.setString(1, id);

            if (pstm.executeUpdate() != 0){
                message = "Student Has been deleted";
            } else {
                message = "Student Has been deleted";
            }

            pstm.executeUpdate();
            pstm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;
    }

    @Override
    public boolean updateStudent(String id, StudentDTO dto, Connection connection) throws SQLException {
        try (var pstm = connection.prepareStatement(UPDATE_STUDENT);){

            pstm.setString(1, id);
            Jsonb jsonb = JsonbBuilder.create();

            pstm.setString(1, dto.getName());
            pstm.setString(2, dto.getCity());
            pstm.setString(3, dto.getEmail());
            pstm.setString(4, dto.getLevel());
            pstm.setString(5, id);

            if (pstm.executeUpdate() != 0){
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw e;
        }
    }
}
