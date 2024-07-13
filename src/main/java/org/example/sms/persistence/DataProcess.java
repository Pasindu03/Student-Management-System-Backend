package org.example.sms.persistence;

import org.example.sms.dto.StudentDTO;

import java.sql.Connection;
import java.sql.SQLException;

public class DataProcess implements Data{
    @Override
    public StudentDTO getStudent(String id, Connection connection) throws SQLException {
        return null;
    }

    @Override
    public String saveStudent(StudentDTO dto, Connection connection) throws SQLException {
        return "";
    }

    @Override
    public boolean deleteStudent(String id, Connection connection) throws SQLException {
        return false;
    }

    @Override
    public boolean updateStudent(String id, StudentDTO dto, Connection connection) throws SQLException {
        return false;
    }
}
