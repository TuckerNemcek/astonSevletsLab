package com.astontech.servlet;

import java.io.IOException;

public class PersonServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        request.setAttribute("firstName", "Bipin");
        request.getRequestDispatcher("./person.jsp").forward(request, response);
    }
}
