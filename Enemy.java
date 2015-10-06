import processing.core.*;
import java.util.ArrayList;

public class Enemy extends Entity{

    public Enemy(Point pos, ArrayList<PImage> imgs, String dir){
        super(pos, 100, imgs, dir, 5);
    }

    // moves enemy towards soldier and reduces soldier health
    public void attackMovement(Soldier soldier, int enemy_width, int soldier_width, int soldier_resting_y){
        int soldier_x = soldier.getPos().getX();
        int enemy_x = this.getPos().getX();
        if ((enemy_x + enemy_width) < soldier_x ||
                soldier_x < enemy_x){
            this.moveHorizontal(enemy_width);
        }
        else{
            if(!soldier.getJumping()) {
                soldier.setHealth(soldier.getHealth() - 1);
            }
        }
    }
}
