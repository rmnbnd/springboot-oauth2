package com.resource.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.InitialContext;
import javax.naming.NamingException;

@RestController
public class JNDIController {

    @RequestMapping("/jndi")
    @ResponseBody
    public String direct() throws NamingException {
        return new InitialContext().lookup("java:comp/env/jdbc/myDataSource").toString();
    }

}
