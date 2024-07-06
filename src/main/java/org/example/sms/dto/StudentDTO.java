package org.example.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudentDTO implements Serializable {
    private String id;
    private String name;
    private String city;
    private String email;
    private String level;
}
