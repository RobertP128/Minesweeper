package com.example.webservice;

import net.poppinger.Application;
import net.poppinger.TileStatus;

import java.io.PrintWriter;

public class View {


    public void renderBoard(PrintWriter out, String contextPath, TileStatus[][] board, Application.GameStatus gamestatus){
        out.println("<html>" +
                "<link rel=\"stylesheet\" href=\""+contextPath+"/css/styles.css\">" +
                "<body>");
        out.println("<h1>" + "Test" + "</h1>");
        if (gamestatus.equals(Application.GameStatus.PLAYER_LOST)){
            out.println("<h1>" + "Spieler hat verloren" + "</h1>");
        }
        out.println("<table border=\"0\" cellspacing=\"0\">");
        for (int y=0;y<board[0].length;y++){
            out.println("<tr>");
            for(int x=0;x<board.length;x++){
                String className="field_";
                if (!board[x][y].revealed) {
                    switch (board[x][y].marker) {
                        case BOMB:
                            className += "b";
                            break;
                        case Free:
                            className += "f";
                            break;
                        default:
                            className += "_";
                    }
                }
                else {
                    className+="_";
                }
                className+="_";
                if (board[x][y].revealed) {
                    if (board[x][y].showBombCount) {
                        className+=board[x][y].bombCount;
                    }
                    else {
                        className+="_";
                    }
                }
                else {
                    className+="X";

                }

                out.println("<td class=\""+className+"\">");
                out.println("<a href=\"/minesweeper/servlet/command/A "+(x+1)+","+(y+1)+"\">&nbsp;</a>");
                out.println("</td>");

            }
            out.println("</tr>");
        }

        out.println("</table>");

        out.println("</body></html>");
    }
}
