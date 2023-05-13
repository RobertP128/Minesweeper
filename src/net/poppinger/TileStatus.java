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
    public TileStatus(Status status){
        status=status;
        marker=Marker.NOT_MARKED;
    }
}
