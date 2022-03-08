package example.grpcclient;

public class catObj {

    private String name;
    private int faveTreat;

    public catObj(String name, int faveTreat){
        this.name = name;
        this.faveTreat = faveTreat;
    }

    public String getName(){
        return name;
    }
    
    public int getFaveTreat(){
        return faveTreat;
    }
    
}