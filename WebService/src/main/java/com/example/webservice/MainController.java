package com.example.webservice;

import net.poppinger.Application;
import net.poppinger.TileStatus;

public class MainController {

    protected Application app;


    public void InitBoard(){
        app=new Application();
        app.maxCols=50;
        app.maxRows=30;
        app.init();
        app.initBombs(150);
        app.setBombCount();

    }
    public TileStatus[][] getBoard(){
        return app.board;
    }

    public Application.GameStatus command(String command){
        return app.commandAuswerten(command);
    }




}
