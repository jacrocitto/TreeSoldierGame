import processing.core.*;

public class Bullet {
    private PImage img;
    private Point pos;
    private String dir;
    private int deltaT;

    public Bullet(PImage img, Point pos, String dir){
        this.img = img;
        this.pos = pos;
        this.dir = dir;
        this.deltaT = 50;
    }

    public PImage getImg(){
        return this.img;
    }

    public Point getPos(){
        return this.pos;
    }

    // keeps bullet moving in whatever direction it's facing
    public void moveHorizontal(){
        int bullet_x = this.pos.getX();
        int new_x;
        if (this.dir.equals("right")){
            new_x = bullet_x + this.deltaT;
            this.pos.setX(new_x);
        }
        else{
            new_x = bullet_x - this.deltaT;
            this.pos.setX(new_x);
        }
    }

    // checks if a bullet has left the screen
    public Boolean outOfBounds(int bullet_length){
        int bullet_x = this.pos.getX();
        return (bullet_x > World.WINDOW_WIDTH ||
                bullet_x < (0 - bullet_length));
    }

}
