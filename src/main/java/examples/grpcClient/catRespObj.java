package example.grpcclient;

public class catRespObj {

    private String resp;
    private String type;

    public catRespObj(String resp, String type){
        this.resp = resp;
        this.type = type;
    }

    public String getResp(){
        return resp;
    }
    
    public String getType(){
        return type;
    }
    
}