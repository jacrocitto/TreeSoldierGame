public class Key {
    private Boolean beingHeld;

    public Key(){
        this.beingHeld = false;
    }

    public void setBeingHeld(Boolean new_val){
        this.beingHeld = new_val;
    }

    public Boolean getBeingHeld(){
        return this.beingHeld;
    }

}