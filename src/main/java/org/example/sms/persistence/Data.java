package org.example.sms.persistence;

import org.example.sms.dto.StudentDTO;

import java.sql.Connection;
import java.sql.SQLException;

public sealed interface Data permits DataProcess {
    StudentDTO getStudent(String id , Connection connection) throws SQLException;
    String saveStudent(StudentDTO dto, Connection connection) throws SQLException;
    String deleteStudent(String id, Connection connection)throws SQLException;
    boolean updateStudent(String id, StudentDTO dto, Connection connection)throws SQLException;
}
