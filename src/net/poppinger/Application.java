package net.poppinger;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Coord{
    public Coord(){

    }
    public Coord(int x, int y){
        this.x=x;
        this.y=y;
    }
    int x;
    int y;
}

public class Application {

    private enum GameStatus{
        INPUT_ERROR,
        INPUT_OK,
        PLAYER_LOST
    }


    int maxRows=10;
    int maxCols=10;
    TileStatus[][] board;



    private void init(){
        board=new TileStatus[maxCols][maxRows];

        for(int x=0;x<maxCols;x++){
            for(int y=0;y<maxRows;y++){
                board[x][y]=new TileStatus(TileStatus.Status.EMPTY);
                board[x][y].marker=TileStatus.Marker.NOT_MARKED;
                board[x][y].status=TileStatus.Status.EMPTY;
            }
        }

    }

    public void main(String[] args) {
        System.out.println("Hello minesweeper");
        if (args.length>0){
            try{
                maxCols=Integer.parseInt(args[0]);
            }
            catch (Exception e){
                System.out.println("MaxRows not parseable!");
            }
        }
        if (args.length>1){
            try{
                maxRows=Integer.parseInt(args[1]);
            }
            catch (Exception e){
                System.out.println("MaxCols is not parseable!");
            }
        }
        int bombscount=15;
        if (args.length>2){
            try{
                bombscount=Integer.parseInt(args[2]);
                if (bombscount<10) bombscount=10;
                if (bombscount>(maxCols*maxRows)) bombscount=maxCols*maxRows;
            }
            catch (Exception e){
                System.out.println("MaxCols is not parseable!");
            }
        }
        init();
        initBombs(bombscount);
        setBombCount();
        //board[0][0].marker= TileStatus.Marker.Free;
        //board[0][1].marker= TileStatus.Marker.BOMB;
        //board[0][1].status= TileStatus.Status.BOMB;
        //printBoard(true);

        String command;
        Scanner scanner=new Scanner(System.in);
        do{
            printBoard(false);
            command=readCommand(scanner);
            GameStatus result=commandAuswerten(command);

            if (result.equals(GameStatus.PLAYER_LOST)){
                command="q";
                System.out.println("Sie haben verloren!");
                printBoard(true);
            }


        }while (!command.equals("q"));

        System.out.println("Danke sehr fürs Mitspielen");


    }


    private void initBombs(int numBombs){
        for (int nr=0;nr<numBombs;nr++){
            int x=(int)(Math.random()*maxCols);
            int y=(int)(Math.random()*maxRows);
            if (board[x][y].status!=TileStatus.Status.BOMB) {
                board[x][y].status = TileStatus.Status.BOMB;
            }
            else {
                nr--;
            }
        }
    }

    private int getBombCount(int x, int y){
        if (x<0 || y<0) return 0;
        if (x>=maxCols) return 0;
        if (y>=maxRows) return 0;

        if (board[x][y].status.equals(TileStatus.Status.BOMB)) return 1;

        return 0;
    }

    private void setBombCount(){
        for(int y=0;y<maxRows;y++){
            for(int x=0;x<maxCols;x++){
                int bc=0;
                bc+=getBombCount(x,y-1);
                bc+=getBombCount(x+1,y-1);
                bc+=getBombCount(x+1,y);
                bc+=getBombCount(x+1,y+1);
                bc+=getBombCount(x,y+1);
                bc+=getBombCount(x-1,y+1);
                bc+=getBombCount(x-1,y);
                bc+=getBombCount(x-1,y-1);

                board[x][y].bombCount=bc;
            }
        }
    }


    private String readCommand(Scanner scanner){
        System.out.print("Geben Sie einen Befehl ein. (?) für Hilfe:");
        var command= scanner.nextLine();
        return command;
    }


    /**
     *
     * @return true if success
     */
    private boolean parseCoordinates(String command,Coord coords){
        if (coords==null) return false;
        if (command==null || command.equals("")) return false;

        System.out.println(command);
        String[] parts1=command.split(" ");
        if (parts1.length<2) return false;
        String koos=parts1[1];
        String[] koosparts = koos.split(",");
        if (koosparts.length<2) return false;
        try {
            int x= Integer.parseInt(koosparts[0]);
            int y= Integer.parseInt(koosparts[1]);
            if (x<=0 || y<=0) return false;
            if (x>maxCols) return false;
            if (y>maxRows) return false;
            // finally all is checked
            coords.x=x-1;
            coords.y=y-1;
            return true;
        }
        catch (NumberFormatException e){
            return false;
        }
    }


    private void revealNeighbours(int x, int y, List<Coord> visited){

        if (x<0 || y<0) return;
        if (x>=maxCols || y>=maxRows) return;

        // check if we already visited this
        for (var coord: visited ) {
            if (coord.x==x && coord.y==y) return;
        }
        visited.add(new Coord(x,y));

        board[x][y].revealed=true;
        if (board[x][y].bombCount>0){
            board[x][y].showBombCount=true;
            return;
        }

        // Bombcount==0 so we can check our neighbours
        revealNeighbours(x,y-1,visited);
        revealNeighbours(x+1,y-1,visited);
        revealNeighbours(x+1,y,visited);
        revealNeighbours(x+1,y+1,visited);
        revealNeighbours(x,y+1,visited);
        revealNeighbours(x-1,y+1,visited);
        revealNeighbours(x-1,y,visited);
        revealNeighbours(x-1,y-1,visited);

    }


    /**
     *
     * @param command
     * @return boolean true if success
     */
    private GameStatus commandAuswerten(String command){

        if (command==null) return GameStatus.INPUT_ERROR;

        if (command.equals("?")){
            System.out.println(
                    "Befehle sind:\n" +
                    "? ......... Hilfe\n" +
                    "MB x,y .... Markiere Bombe x-Koordinate,yKoordinate\n" +
                    "MF x,y .... Markiere Frei x-Koordinate,yKoordinate\n" +
                    "MK x,y .... Markierung löschen x-Koordinate,yKoordinate\n" +
                    "A x,y ..... Aufdecken x-Koordinate,yKoordinate\n" +
                    "q ......... Quit");
            return GameStatus.INPUT_OK;
        }
        else if (command.startsWith("MB")){
            // parse Coordinates
            var coords = new Coord();
            if (!parseCoordinates(command,coords)){
                System.out.println("Falsche Eingabe");
                return GameStatus.INPUT_ERROR;
            }
            board[coords.x][coords.y].marker= TileStatus.Marker.BOMB;
            return GameStatus.INPUT_OK;
        }
        else if (command.startsWith("MF")){
            // parse Coordinates
            var coords = new Coord();
            if (!parseCoordinates(command,coords)){
                System.out.println("Falsche Eingabe");
                return GameStatus.INPUT_ERROR;
            }
            board[coords.x][coords.y].marker= TileStatus.Marker.Free;
            return GameStatus.INPUT_OK;

        }
        else if (command.startsWith("MK")){
            // parse Coordinates
            var coords = new Coord();
            if (!parseCoordinates(command,coords)){
                System.out.println("Falsche Eingabe");
                return GameStatus.INPUT_ERROR;
            }
            board[coords.x][coords.y].marker= TileStatus.Marker.NOT_MARKED;
            return GameStatus.INPUT_OK;
        }
        else if (command.startsWith("A")){
            // parse Coordinates
            var coords = new Coord();
            if (!parseCoordinates(command,coords)){
                System.out.println("Falsche Eingabe");
                return GameStatus.INPUT_ERROR;
            }
            if (board[coords.x][coords.y].status.equals(TileStatus.Status.BOMB)){
                // Verloren
                return GameStatus.PLAYER_LOST;
            }

            board[coords.x][coords.y].revealed=true;
            revealNeighbours(coords.x,coords.y,new ArrayList<>());

            return GameStatus.INPUT_OK;
        }

        System.out.println("Unbekannter Befehl!");
        return GameStatus.INPUT_ERROR;
    }

    public void printBoard(boolean showBombs){
        // Print headline
        System.out.print(" ");
        for(int x=0;x<maxCols;x++){
            if (x<10) {
                System.out.print("  " + (x + 1) + "");
            }
            else {
                System.out.print(" " + (x + 1));
            }
        }
        System.out.println();
        for(int y=0;y<maxRows;y++){
            // print rownumber
            if ((y+1)<10){
                System.out.print((y+1)+" ");
            }
            else {
                System.out.print(y+1);
            }
            for(int x=0;x<maxCols;x++){
                System.out.print("|");
                switch (board[x][y].marker){
                    case NOT_MARKED -> System.out.print(" ");
                    case BOMB -> System.out.print("b");
                    case Free -> System.out.print("f");
                }
                if (showBombs && board[x][y].status.equals(TileStatus.Status.BOMB)){
                    System.out.print("B");
                }
                else {
                    if (board[x][y].showBombCount) {
                        System.out.print(board[x][y].bombCount);
                    }
                    else {
                        if (board[x][y].revealed) {
                            System.out.print(" ");
                        }
                        else
                        {
                            System.out.print("X");
                        }
                    }
                }
            }
            System.out.println("|");
        }
    }
}
