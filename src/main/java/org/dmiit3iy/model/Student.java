package org.dmiit3iy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private long id;
    private String fio;
    private int age;
    private String num;
    private BigDecimal salary;
}
