package com.kk.spring_sec_demo.controller;

import com.kk.spring_sec_demo.model.Student;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@RestController
public class StudentController {
    public static final Logger LOGGER = LoggerFactory.getLogger(StudentController.class);
    List<Student> studentList = new ArrayList<>(List.of(
            new Student(1, "Abc", "Java"),
            new Student(2, "Def", "js"),
            new Student(3, "Kiran", "html")));

    @GetMapping("/students")
    public List<Student> getStudents() {
        return studentList;
    }

    @PostMapping(value = "/students", consumes = "application/json")
    public String addStudents(@RequestBody Student student) {
        studentList.add(student);
        return "Student added successfully";
    }

    @GetMapping("/getrequstattributes")
    public void getAllAttributes(HttpServletRequest request) {
//        priting all request attributes
        System.out.println("request = " + request);
        Enumeration<String> attributeNames = request.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            Object attributeValue = request.getAttribute(attributeName);
            System.out.println(attributeName + " = " + attributeValue);
        }
    }

    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        // try to get the CSRF token class so we can log it to the console and debug
        Object csrfAttribute = request.getAttribute("_csrf");
        if (csrfAttribute != null) {
            LOGGER.info("CSRF Attribute class = " + csrfAttribute.getClass().getName());
        }
        // get the CSRF token
        CsrfToken csrf = (CsrfToken) request.getAttribute("_csrf");
        if (csrf != null) {
            LOGGER.info("CSRF Token = " + csrf.getToken());
            LOGGER.info("CSRF Parameter Name = " + csrf.getParameterName());
            LOGGER.info("CSRF Header Name = " + csrf.getHeaderName());
        } else {
            LOGGER.warn("CSRF token not found in request attributes.");
        }
        return csrf;
    }

}
