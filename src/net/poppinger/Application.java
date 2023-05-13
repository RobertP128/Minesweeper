package net.poppinger;


import java.util.Scanner;

class Coord{
    int x;
    int y;
}

public class Application {

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
        System.out.println("Hello world!");
        init();
        initBombs(15);
        //board[0][0].marker= TileStatus.Marker.Free;
        //board[0][1].marker= TileStatus.Marker.BOMB;
        //board[0][1].status= TileStatus.Status.BOMB;
        printBoard(true);

        String command;
        Scanner scanner=new Scanner(System.in);
        do{
            printBoard(false);
            command=readCommand(scanner);
            commandAuswerten(command);

        }while (!command.equals("q"));

        System.out.println("Danke sehr fürs mitspielen");


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

    /**
     *
     * @param command
     * @return boolean true if success
     */
    private boolean commandAuswerten(String command){

        if (command==null) return false;

        if (command.equals("?")){
            System.out.println(
                    "Befehle sind:\n" +
                    "? ......... Hilfe\n" +
                    "MB x,y .... Markiere Bombe x-Koordinate,yKoordinate\n" +
                    "MF x,y .... Markiere Frei x-Koordinate,yKoordinate\n" +
                    "MK x,y .... Markierung löschen x-Koordinate,yKoordinate\n" +
                    "A x,y ..... Aufdecken x-Koordinate,yKoordinate\n" +
                    "q ......... Quit");
            return true;
        }
        else if (command.startsWith("MB")){
            // parse Coordinates
            var coords = new Coord();
            if (!parseCoordinates(command,coords)){
                System.out.println("Falsche Eingabe");
                return false;
            }
            board[coords.x][coords.y].marker= TileStatus.Marker.BOMB;
            return true;
        }
        else if (command.startsWith("MF")){
            // parse Coordinates
            var coords = new Coord();
            if (!parseCoordinates(command,coords)){
                System.out.println("Falsche Eingabe");
                return false;
            }
            board[coords.x][coords.y].marker= TileStatus.Marker.Free;
            return true;

        }
        else if (command.startsWith("MK")){
            // parse Coordinates
            var coords = new Coord();
            if (!parseCoordinates(command,coords)){
                System.out.println("Falsche Eingabe");
                return false;
            }
            board[coords.x][coords.y].marker= TileStatus.Marker.NOT_MARKED;
            return true;
        }
        else if (command.startsWith("A")){
            // parse Coordinates
            var coords = new Coord();
            if (!parseCoordinates(command,coords)){
                System.out.println("Falsche Eingabe");
                return false;
            }
            System.out.println("not Implemented yet.....");
            return false;
        }

        System.out.println("Unbekannter Befehl!");
        return false;
    }

    public void printBoard(boolean showBombs){
        // Print headline
        System.out.print(" ");
        for(int y=0;y<maxRows;y++){
            if (y<10) {
                System.out.print("  " + (y + 1) + "");
            }
            else {
                System.out.print(" " + (y + 1));
            }
        }
        System.out.println();
        for(int y=0;y<maxCols;y++){
            // print rownumber
            if ((y+1)<10){
                System.out.print((y+1)+" ");
            }
            else {
                System.out.print(y+1);
            }
            for(int x=0;x<maxRows;x++){
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
                    System.out.print(" ");
                }
            }
            System.out.println("|");
        }
    }
}
