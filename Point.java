public class Point {
    private int x;
    private int y;

    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setX(int new_x){
        this.x = new_x;
    }

    public void setY(int new_y){
        this.y = new_y;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }
}
