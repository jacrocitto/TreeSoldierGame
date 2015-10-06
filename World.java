import processing.core.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.lang.Math;

public class World extends PApplet {

    public static final int ENTITY_ANIMATION_TIME = 10;
    public static final int BOOSTER_ANIMATION_TIME = 100;
    public static final int BULLET_ANIMATION_TIME = 1;
    public static final int RELATIVE_GUN_TIP_Y = 120;
    public static int WINDOW_WIDTH;
    public static int WINDOW_HEIGHT;
    public static int bullet_length;

    private int INITIAL_ENEMY_SPAWN_TIME = 6000; // 6 seconds
    private ArrayList<PImage> soldierFaceLeft;
    private ArrayList<PImage> soldierFaceRight;
    private ArrayList<PImage> enemyFaceLeft;
    private ArrayList<PImage> enemyFaceRight;
    private PImage bulletLeftImg;
    private PImage bulletRightImg;
    private PImage forrestBG;
    private PImage forrestBGTree;
    private int defaultEnemyWidth;
    private int defaultSoldierWidth;
    private LinkedList<Bullet> currentBullets;
    private LinkedList<Enemy> currentEnemies;
    private long nextEntityTime;
    private long nextBoosterTime;
    private long nextBulletTime;
    private long enemySpawnTime;
    private long enemyRegenTime;
    private Soldier soldier;
    private Key aKey;
    private Key dKey;
    private boolean isPlaying;
    private long jumpStartTime;
    private int soldierRestingY;
    private int enemySpawnX;


    public void setup() {

        isPlaying = true;

        // load background components
        forrestBG = loadImage("images/ForrestBG.png");
        forrestBGTree = loadImage("images/ForrestBG_Tree.png");

        // sets initial enemy regen time
        enemyRegenTime = INITIAL_ENEMY_SPAWN_TIME;

        // sets window height and width based on image size
        WINDOW_WIDTH = forrestBG.width;
        WINDOW_HEIGHT = forrestBG.height;

        size(WINDOW_WIDTH, WINDOW_HEIGHT);
        background(255, 204, 0);
        loadImages();

        // sets "next" animation times
        nextEntityTime = System.currentTimeMillis() + ENTITY_ANIMATION_TIME;
        nextBoosterTime = System.currentTimeMillis() + BOOSTER_ANIMATION_TIME;
        nextBulletTime = System.currentTimeMillis() + BULLET_ANIMATION_TIME;

        // creates Key objects for 'a' and 'd' key
        aKey = new Key();
        dKey = new Key();

        // CREATION OF THE LEGENDARY PROTAGONIST
        soldier = new Soldier(new Point(540, 500), soldierFaceLeft);
        createBulletEnemyLists();



    }

    // returns new Enemy object to be added to currentEnemies
    public Enemy createNewEnemy(){
        // keeps enemy from spawning too close to soldier
        do {
            enemySpawnX = randInt(0, WINDOW_WIDTH);
        } while ((Math.abs(enemySpawnX - soldier.getPos().getX())) < 250);
        Enemy new_enemy = new Enemy(new Point(enemySpawnX, 500), enemyFaceRight, "right");
        enemySpawnTime = System.currentTimeMillis();
        return new_enemy;
    }

    // CALLED REPEATEDLY, draws world
    public void draw() {
        // checks if game is over
        // draws game or "game over" screen
        if (soldier.getHealth() > 0) {

            // displays far layer of background
            image(forrestBG, 0, 0);

            long time = System.currentTimeMillis();

            // checks if new enemy is ready to spawn
            // also sets next enemy spawn time
            if ((time - enemySpawnTime) >= enemyRegenTime){
                currentEnemies.add(createNewEnemy());
                if (enemyRegenTime > 3000) {
                    enemyRegenTime -= 1000;
                }
            }

            // animates soldier and enemy movement
            moveEntities(time);

            // animates booster flame and enemy sword swing
            cycleEntityImgs(time);

            // keeps the bullets flowing
            animateBullets(time);

            drawEntities();
            drawSoldierHealth();
            drawScore();
            image(forrestBGTree, 1147, 0);
        }
        else{
            drawGameOver();
            isPlaying = false;
        }

    }

    // returns random int between two specified values
    public int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    // loads entity images (soldier, enemies, bullets)
    public void loadImages(){
        // loads images for soldier
        soldierFaceLeft = new ArrayList<>();
        defaultSoldierWidth = loadImage("images/Soldier1L.png").width;
        soldierFaceLeft.add(loadImage("images/Soldier1L.png"));
        soldierFaceLeft.add(loadImage("images/Soldier2L.png"));
        soldierFaceLeft.add(loadImage("images/Soldier3L.png"));
        soldierFaceLeft.add(loadImage("images/Soldier2L.png"));
        soldierFaceLeft.add(loadImage("images/Soldier1L.png"));

        soldierFaceRight = new ArrayList<>();
        soldierFaceRight.add(loadImage("images/Soldier1R.png"));
        soldierFaceRight.add(loadImage("images/Soldier2R.png"));
        soldierFaceRight.add(loadImage("images/Soldier3R.png"));
        soldierFaceRight.add(loadImage("images/Soldier2R.png"));
        soldierFaceRight.add(loadImage("images/Soldier1R.png"));

        // loads images for enemy
        enemyFaceLeft = new ArrayList<>();
        defaultEnemyWidth = loadImage("images/DefaultEnemyL.png").width;
        enemyFaceLeft.add(loadImage("images/AttackEnemy1L.png"));
        enemyFaceLeft.add(loadImage("images/AttackEnemy2L.png"));
        enemyFaceLeft.add(loadImage("images/AttackEnemy3L.png"));
        enemyFaceLeft.add(loadImage("images/AttackEnemy4L.png"));

        enemyFaceRight = new ArrayList<>();
        enemyFaceRight.add(loadImage("images/AttackEnemy1R.png"));
        enemyFaceRight.add(loadImage("images/AttackEnemy2R.png"));
        enemyFaceRight.add(loadImage("images/AttackEnemy3R.png"));
        enemyFaceRight.add(loadImage("images/AttackEnemy4R.png"));

        // loads bullet images
        bulletLeftImg = loadImage("images/BulletL.png");
        bulletRightImg = loadImage("images/BulletR.png");
        bullet_length = bulletLeftImg.width;
    }

    // creates running lists for current enemies and bullets on screen
    // also adds first enemy to world
    public void createBulletEnemyLists(){
        // creates running list for bullets on screen
        currentBullets = new LinkedList();
        // creates running list for enemies on screen
        currentEnemies = new LinkedList();
        // adds first enemy
        currentEnemies.add(createNewEnemy());
    }

    // updates positions of soldier and enemies
    public void moveEntities(long time){
        if (time >= nextEntityTime) {
            if (soldier.getJumping()) {
                long delta_t = System.currentTimeMillis() - jumpStartTime;
                soldier.jump(delta_t, soldierRestingY);
            }

            if (aKey.getBeingHeld() || dKey.getBeingHeld()) {
                soldier.moveHorizontal(defaultSoldierWidth);
            }
            nextEntityTime = time + ENTITY_ANIMATION_TIME; // update animation time
        }


        if (currentEnemies.size() != 0) {
            for (int i = 0; i < currentEnemies.size(); i++) {
                Enemy enemy = currentEnemies.get(i);
                if (enemy.getHealth() <= 0) {
                    // removes dead enemies
                    currentEnemies.remove(i);
                    soldier.addOneToScore();
                } else {
                    // moves enemy
                    enemy.attackMovement(soldier, defaultEnemyWidth,
                            defaultSoldierWidth, soldierRestingY);
                }
            }
        }
    }

    // cycles through entity images
    public void cycleEntityImgs(long time){
        if (time >= nextBoosterTime) {
            // cycles through soldier imgs
            soldier.setCurrentImg((soldier.getCurrentImg() + 1) % soldier.getImgs().size());

            if (currentEnemies.size() != 0) {
                for (int i = 0; i < currentEnemies.size(); i++) {
                    Enemy enemy = currentEnemies.get(i);
                    // flips direction of enemy if necessary
                    if ((soldier.getPos().getX() + defaultSoldierWidth) <= enemy.getPos().getX()) {
                        enemy.setDir("left");
                        enemy.setImgs(enemyFaceLeft);
                    } else if (soldier.getPos().getX() >= (enemy.getPos().getX() + defaultEnemyWidth)) {
                        enemy.setDir("right");
                        enemy.setImgs(enemyFaceRight);
                    }
                    // cycles through enemy imgs
                    enemy.setCurrentImg((enemy.getCurrentImg() + 1) % enemy.getImgs().size());
                }
            }
            nextBoosterTime = time + BOOSTER_ANIMATION_TIME; // update animation time
        }
    }

    // draws soldier and enemies on screen
    public void drawEntities(){
        // draws enemies on screen
        if (currentEnemies.size() != 0) {
            for (int i = 0; i < currentEnemies.size(); i++) {
                Enemy enemy = currentEnemies.get(i);
                image(enemy.getImgs().get(enemy.getCurrentImg()), enemy.getPos().getX(),
                        enemy.getPos().getY());
            }
        }
        //draws soldier on screen
        image(soldier.getImgs().get(soldier.getCurrentImg()), soldier.getPos().getX(),
                soldier.getPos().getY());
    }

    // draws bullets and keeps them flowing in correct direction
    // handles when bullets hit enemies
    public void animateBullets(long time){
        if (time >= nextBulletTime) {
            if (currentBullets.size() != 0) {
                for (int i = 0; i < currentBullets.size(); i++) {
                    Bullet bullet = currentBullets.get(i);
                    if (bullet.outOfBounds(bullet_length)) {
                        currentBullets.remove(i);
                    }
                    bullet.moveHorizontal();
                    if (currentEnemies.size() != 0) {
                        for (int j = 0; j < currentEnemies.size(); j++) {
                            Enemy enemy = currentEnemies.get(j);
                            int bullet_x = bullet.getPos().getX();
                            int bullet_y = bullet.getPos().getY();
                            int enemy_x = enemy.getPos().getX();
                            // does the bullet hit an enemy?
                            if ((bullet_x >= enemy_x &&
                                    bullet_x <= (enemy_x + defaultEnemyWidth)) &&
                                    (bullet_y >= soldierRestingY)){
                                int old_enemy_health = enemy.getHealth();
                                enemy.setHealth(old_enemy_health - 10);
                                currentBullets.remove(i);
                                break;
                            }
                        }
                    }
                        Point bullet_pos = bullet.getPos();
                        image(bullet.getImg(), bullet_pos.getX(), bullet_pos.getY());
                }
            }
            nextBulletTime = time + BULLET_ANIMATION_TIME; // update animation time
        }
    }

    // draws soldiers health
    public void drawSoldierHealth(){
        int current_soldier_health = soldier.getHealth();
        textSize(42);
        textAlign(CENTER, BOTTOM);
        if (current_soldier_health > 20){
            fill(0, 225, 0);
        }
        else {
            fill(225, 0, 0);
        }
        text(current_soldier_health, soldier.getPos().getX() + (defaultSoldierWidth / 2),
                soldier.getPos().getY());
    }

    public void drawScore(){
        textSize(42);
        textAlign(CENTER, TOP);
        fill(225, 0, 0);
        text("Score: " + soldier.getScore(), WINDOW_WIDTH / 2, 10);
    }

    // produces game-over screen
    public void drawGameOver(){
        background(0);
        textAlign(CENTER, CENTER);
        textSize(100);
        fill(225, 0, 0);
        text("GAME OVER.\nYour Score: " + soldier.getScore() + "\nPress 'R' to play again",
                WINDOW_WIDTH / 2, WINDOW_HEIGHT/2);
    }

    // handles key presses
    public void keyPressed() {
        switch (key) {
            case 'a':
                soldier.setImgs(soldierFaceLeft);
                soldier.setDir("left");
                aKey.setBeingHeld(true);
                break;
            case 'd':
                soldier.setImgs(soldierFaceRight);
                soldier.setDir("right");
                dKey.setBeingHeld(true);
                break;
            case 'w':
                if (!soldier.getJumping()) {
                    soldierRestingY = soldier.getPos().getY();
                    soldier.setJumping(true);
                    jumpStartTime = System.currentTimeMillis();
                }
                break;
            case 'r':
                if (isPlaying == false) {
                    // restarts the game
                    setup();
                }
                break;
        }
    }

    // handles key releases
    public void keyReleased() {
        switch (key) {
            case 'a':
                aKey.setBeingHeld(false);
                break;
            case 'd':
                dKey.setBeingHeld(false);
        }
    }

    // adds new bullet to environment when mouse is clicked
    public void mousePressed(){
        currentBullets.add(createNewBullet());
        Sound.play("C:\\Users\\James Crocitto\\IdeaProjects\\TreeSoldier\\src\\sounds\\gunshot.wav");
    }

    //returns a Bullet object based on soldier position/direction
    public Bullet createNewBullet(){
        int bullet_y = soldier.getPos().getY() + RELATIVE_GUN_TIP_Y;
        if (soldier.getDir().equals("left")){
            int bullet_x = soldier.getPos().getX() - bullet_length;
            return new Bullet(bulletLeftImg, new Point(bullet_x, bullet_y), "left");
        }
        else{
            int soldier_width = soldier.getImgs().get(soldier.getCurrentImg()).width;
            int bullet_x = soldier.getPos().getX() + soldier_width;
            return new Bullet(bulletRightImg, new Point(bullet_x, bullet_y), "right");
        }
    }
}
