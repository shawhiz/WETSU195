/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wetsu195.Data.model;

public class Incrementtypes  {

     private Integer incrementTypeId;
    
    private String incrementTypeDescription;

    public Incrementtypes() {
    }

    public Incrementtypes(Integer incrementTypeId) {
        this.incrementTypeId = incrementTypeId;
    }

    public Incrementtypes(Integer incrementTypeId, String incrementTypeDescription) {
        this.incrementTypeId = incrementTypeId;
        this.incrementTypeDescription = incrementTypeDescription;
    }

    public Integer getIncrementTypeId() {
        return incrementTypeId;
    }

    public void setIncrementTypeId(Integer incrementTypeId) {
        this.incrementTypeId = incrementTypeId;
    }

    public String getIncrementTypeDescription() {
        return incrementTypeDescription;
    }

    public void setIncrementTypeDescription(String incrementTypeDescription) {
        this.incrementTypeDescription = incrementTypeDescription;
    }

}
