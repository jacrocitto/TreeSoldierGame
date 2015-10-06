import processing.core.*;
import java.util.ArrayList;

public class Soldier extends Entity {

    private int score;

    public Soldier(Point pos, ArrayList<PImage> imgs){
        super(pos, 100, imgs, "left", 10);
        this.score = 0;
    }

    public int getScore(){
        return this.score;
    }

    public void setScore(int new_score){
        this.score = new_score;
    }

    public void addOneToScore(){
        this.score += 1;
    }

    // makes soldier jump in parabolic motion (basic kinematic eq application)
    public void jump(long delta_t, int resting_y) {
        int new_y = (int) (resting_y - (2 * delta_t) + (0.002 * delta_t * delta_t));
        if (new_y > resting_y) {
            this.getPos().setY(resting_y);
            this.setJumping(false);
        } else {
            this.getPos().setY(new_y);
        }
    }

}
