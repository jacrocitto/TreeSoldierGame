import processing.core.*;
import java.util.ArrayList;

public abstract class Entity {
    private Point pos;
    private int health;
    private ArrayList<PImage> imgs;
    private String dir;
    private Boolean jumping;
    private int deltaT;
    private int currentImg;


    public Entity(Point pos, int health, ArrayList<PImage> imgs, String dir, int delta_t){
        this.pos = pos;
        this.health = health;
        this.imgs = imgs;
        this.dir = dir;
        this.jumping = false;
        this.deltaT = delta_t;
        this.currentImg = 0;
    }

    //SETTERS

    public void setPos(Point newPos){
        this.pos = newPos;
    }

    public void setHealth(int newHealth){
        this.health = newHealth;
    }

    public void setImgs(ArrayList<PImage> newImgs){
        this.imgs = newImgs;
    }

    public void setDir(String new_dir){
        this.dir = new_dir;
    }

    public void setJumping(Boolean new_jump){
        this.jumping = new_jump;
    }

    public void setCurrentImg(int cur){
        this.currentImg = cur;
    }

    //GETTERS

    public Point getPos(){
        return this.pos;
    }

    public int getHealth(){
        return this.health;
    }

    public ArrayList<PImage> getImgs(){
        return this.imgs;
    }

    public String getDir(){
        return this.dir;
    }

    public Boolean getJumping(){
        return this.jumping;
    }

    public int getCurrentImg(){
        return this.currentImg;
    }

    // keeps entity moving in whatever direction its currently facing
    public void moveHorizontal(int entity_width) {
        int soldier_x = this.pos.getX();
        int new_x;
        int bound;
        if (this.getDir().equals("left")) {
            bound = 0;
            new_x = Math.max(soldier_x - this.deltaT, bound);
            this.pos.setX(new_x);
        } else {
            bound = World.WINDOW_WIDTH - entity_width;
            new_x = Math.min(soldier_x + this.deltaT, bound);
            this.pos.setX(new_x);
        }
    }

}
