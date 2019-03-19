package com.takeaway.employeeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class EmployeeServiceApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(EmployeeServiceApplication.class, args);
    }
}
