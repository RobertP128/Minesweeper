package net.poppinger;

public class TileStatus {

    enum Status{
        EMPTY,
        BOMB
    }
    enum Marker{
        NOT_MARKED,
        BOMB,
        Free,
    }


    public Marker marker;
    public Status status;
    public int bombCount;
    public boolean showBombCount;
    public boolean revealed;

    public TileStatus(Status status){
        status=status;
        marker=Marker.NOT_MARKED;
        bombCount=0;
        showBombCount=false;
        revealed=false;
    }
}
