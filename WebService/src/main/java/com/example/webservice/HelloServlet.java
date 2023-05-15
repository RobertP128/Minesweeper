package com.example.webservice;

import net.poppinger.Application;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;



public class HelloServlet extends HttpServlet {
    private String message;

    public HelloServlet(){
        super();
    }

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        Application app=new Application();
        app.maxCols=50;
        app.maxRows=40;
        app.init();
        app.initBombs(150);
        app.setBombCount();


        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + "Test" + "</h1>");
        out.println("<table>");
        for (int y=0;y<app.maxRows;y++){
            out.println("<tr>");
            for(int x=0;x<app.maxCols;x++){
                out.println("<td>");
                out.println(app.board[x][y].bombCount);
                out.println("</td>");

            }
            out.println("</tr>");
        }

        out.println("</table>");

        out.println("</body></html>");
    }

    public void destroy() {
    }
}