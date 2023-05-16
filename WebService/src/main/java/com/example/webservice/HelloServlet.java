package com.example.webservice;

import net.poppinger.Application;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;



public class HelloServlet extends HttpServlet {
    private String message;

    private MainController mainController;
    public HelloServlet(){
        super();

        mainController=new MainController();

    }

    public void init() {
        mainController.InitBoard();
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        String commandPrefix="/command/";

        if (request.getPathInfo().equals("/getBoard")) {
            var board = mainController.getBoard();
            new View().renderBoard(response.getWriter(),request.getContextPath(), board, Application.GameStatus.INPUT_OK);
        }

        else if (request.getPathInfo().startsWith(commandPrefix)) {

            String[] parts=request.getPathInfo().split(commandPrefix);
            if (parts.length>1) {
                var gamestatus=mainController.command(parts[1]);
                var board = mainController.getBoard();
                new View().renderBoard(response.getWriter(),request.getContextPath(), board,gamestatus);
            }
        }
        else if (request.getPathInfo().equals("/RST")) {

                mainController.InitBoard();
                var board = mainController.getBoard();
                new View().renderBoard(response.getWriter(),request.getContextPath(), board, Application.GameStatus.INPUT_OK);
        }

    }

    public void destroy() {
    }
}