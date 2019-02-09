/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author aozeel
 */
import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.net.URL;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import static java.awt.image.ImageObserver.ABORT;
import sun.applet.Main;


public class Game extends JPanel{
    
    int crx,cry;   //location of the crossing
    int car_x,car_y;  //coordinates of the user's car
    int speedX,speedY; //movement of the user's car
    int nOpponent;  // the number of the opponent cars'
    String imageOpp[]; //opponent car images' arrays
    int lx[],ly[]; //coordinates of the oncoming cars'
    int score; //number of passed cars
    int highScore; //value of the best score
    int speedOpp[]; //array used to store each opponent car speeds
    boolean isFinished; //bool value that keep if the game over
    boolean isUp,isDown,isLeft,isRight;//bool values that show us if defined keys pressed or not

public Game(){
    crx=cry=-999; //initalizing location of the crossing
    //add keylistener to the jpanel for detect key events from user
    addKeyListener(new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            moveCar(e); //move the car in the direction given by the key
        }

        @Override
        public void keyReleased(KeyEvent e) {
            stopCar(e); //stop movement of the car
        }
    });
    setFocusable(true);//added to the JPanel for keyListener
    car_x=car_y=300; //initializing setting the user's car location(300,300)
    isUp=isDown=isLeft=isRight=false; //initial arrow key values set to false, means that user hasn't pressed any arrow key yet
    speedX=speedY=0; //initial movement speed of the user's car
    lx=new int[20]; //array to be used to store the x position of all enemy cars
    ly=new int[20]; //array to be used to store the y position of all enemy cars
    imageOpp=new String[20]; //
    speedOpp=new int[20];
    isFinished=false; //when false, game is running; when true, game has ended
    score=highScore=0; //initial value of score and highscore
}    

    @Override
    public void paint(Graphics g) {
        super.paint(g); //To change body of generated methods, choose Tools | Templates.
        Graphics2D obj=(Graphics2D) g;
        obj.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        try {
            obj.drawImage(getToolkit().getImage("C:\\Users\\OZEL\\Documents\\NetBeansProjects\\car-game-project\\src\\images\\st_road2.png"),0,0,this); //draw road on window
        if(cry >= -499 && crx >= -499) //if a road crossing has passed the window view
                obj.drawImage(getToolkit().getImage("C:\\Users\\OZEL\\Documents\\NetBeansProjects\\car-game-project\\src\\images\\cross_road.png"),crx,cry,this); //draw another road crossing on window
            
        obj.drawImage(getToolkit().getImage("C:\\Users\\OZEL\\Documents\\NetBeansProjects\\car-game-project\\src\\images\\car_self.png"),car_x,car_y,this);   //draw car on window
        
        if(isFinished){ //if collision occurs
                obj.drawImage(getToolkit().getImage("C:\\Users\\OZEL\\Documents\\NetBeansProjects\\car-game-project\\src\\images\\boom.png"),car_x-30,car_y-30,this); //draw explosion image on window at collision to indicate the collision has occured
            }
        
        if(this.nOpponent > 0){ //if there is more than one opponent car in the game
                for(int i=0;i<this.nOpponent;i++){ //for every opponent car
                    obj.drawImage(getToolkit().getImage(this.imageOpp[i]),this.lx[i],this.ly[i],this); //draw onto window
                }
            }
        
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    //function that moves the road scene across the window to make it seems like the car is driving
    void moveRoad(int count){
        if (crx==-999 && cry==-999) { //if the road crossing has passed by
            if (count%10==0) { //after a certain time
                crx=0; //send crossing location back at the beginning
                cry=-499;
            }
        }
        else{  //otherwise
            cry++;
        }
        if (crx == 0 && cry == 499) { //
            crx = cry = -999; //
        }
        car_x += speedX; //update car x position
        car_y += speedY; //update car y position
        
        if (car_y < 0) { //if the car has reached or gone under its min axis value
            car_y=0; //keep its min axis value
        }
        if(car_y+93 >= 450){ //if the car has reached or gone over its max axis value
            car_y = 450-93; //keep its max value
        }
        if (car_x <= 120) {//if the car has reached or gone right side of the road
            car_x=120; //keep the car on the road
        }
        if (car_x >= 360-50) { //if the car has reached or gone left side of the road
            car_x = 360-50; // keep the car on the road
        }
        
        for (int i = 0; i < this.nOpponent; i++) { //for all opponent cars
            this.ly[i] += speedOpp[i]; //move across the screen  based on already calculated speed values
        }
        
        int index[] = new int[nOpponent];
        for (int i = 0; i < nOpponent; i++) {
            if (ly[i] <= 627) {
                index[i] = 1;
            }
            
        }
        int c = 0;
        
        for (int i = 0; i < nOpponent; i++) {
            if (index[i] == 1) {
                imageOpp[c] = imageOpp[i];
                lx[c] = lx[i];
                ly[c] = ly[i];
                speedOpp[c] = speedOpp[i];
                c++;
            }
        }
        
        score += nOpponent - c; //score is increases evert time user passes an opponent car
        
        if (score > highScore) {  // if the current score higher than high score
            highScore=score; //update current score as a high score
        }
        nOpponent=c;
        
        //check for collision1
        int diff=0; //difference between user's car and opponent's car initially set to zero
        for (int i = 0; i < nOpponent; i++) { //for all opponent cars
            diff=car_y - ly[i]; //diff is the distance between user's car and opponent's car
            if ((ly[i] >= car_y && ly[i] <= car_y+87) || (ly[i]+87 >= car_y && ly[i]+87 <= car_y+87)) { //if the cars collide vertically
                if(car_x+46 >= lx[i] && !(car_x >= lx[i]+46)){     //and collide horizantally
                    System.out.println("My car : "+car_x+", "+car_y);
                    System.out.println("Colliding car : "+lx[i]+", "+ly[i]);
                    this.finish(); //end game and print end message
                }
            }
        }
    }
    //function that shows a message after user lost the game
    void finish(){
        String str=""; //create an empty string that will be used for congratulations method
        isFinished=true; //indicates that game has finished to the rest of the program
        this.repaint(); //tells the window manager that the component has to be drawn
        if (score == highScore && score != 0) {  //if user scores a new high score or same
            str="\nCongratulations!!! Its a high score"; //create a congratulations message
        }
        JOptionPane.showMessageDialog(this,"Game Over!!!\nYour Score : "+score+"\nHigh Score : "+highScore+str, "Game Over", JOptionPane.YES_NO_OPTION); //displays game over message and user's score
        System.exit(ABORT); //terminate game
    }
    
    //function that handles input by user to move the user's car up, down, left, right
    public void moveCar(KeyEvent e){
        if (e.getKeyCode() == KeyEvent.VK_UP) {  //if user click on the up arrow key
            isUp=true;
            speedY=-1; //moves car forward
        }
        
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {  //if user click on the down arrow key
            isDown=true;
            speedY=+1; //moves car backwards
        }
        
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {  //if user click on the left arrow key
            isLeft=true;
            speedX=-1; //moves car left
        }
        
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {  //if user click on the right arrow key
            isRight=true;
            speedX=1; //moves car right
        }
    }
    
    public void stopCar(KeyEvent e){
        if (e.getKeyCode() == KeyEvent.VK_UP) {  //if user click on the up arrow key
            isUp=false;
            speedY=0; //set speed of the car to zero
        }
        
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) { //if user click on the down arrow key
            isDown=false;
            speedY=0; //set speed of the car to zero
        }
        
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) { //if user click on the left arrow key
            isLeft=false;
            speedX=0; //set speed of the car to zero
        }
        
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) { //if user click on the right arrow key
            isRight=false;
            speedX=0; //set speed of the car to zero
        }
    }
    
    //the main method where the java application begins processing
    public static void main(String args[]){
        JFrame jf=new JFrame("Racing Game"); //creating a new JFrame window
        Game ng=new Game(); //creating a new instance of a NewGame
        jf.add(ng); //Graphics2d components are added to JFrame window
        jf.setSize(500,500); //setting size of screen to 500x500
        jf.setVisible(true); //make JFrame window and its children displayed on the screen
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int count = 1, c=1;
        while (true) {            
            ng.moveRoad(count); //move the road
            while(c <= 1){
                ng.repaint();  //redraw road to match new locations
                try {
                    Thread.sleep(5);  //wait so that the road appears to be moving continously
                } catch (Exception e) {
                    System.out.println(e);
                }
                c++;
            }
            c=1;
            count++; //increment count value
            if (ng.nOpponent < 4 && count % 200 == 0) { //if there is less than 4 cars and count timer reaches 200
                ng.imageOpp[ng.nOpponent] = "C:\\Users\\OZEL\\Documents\\NetBeansProjects\\car-game-project\\src\\images\\car_left_"+((int)((Math.random()*100)%4)+1)+".png"; //assign images to the opponent cars
                ng.ly[ng.nOpponent] = -70; //set opponent cars start positions
                int p= (int)(Math.random()*100)%4; //create a random number that is the reminder of a number between 0 and 100 divided by 4.
                if(p == 0){     //if the remainder is 0
                    p = 250;    //place the car in the fourth lane
                }
                else if(p == 1){ //if the remainder is 1
                    p = 300;    //place the car in the second lane
                }
                else if(p == 2){ //if the remainder is 2
                    p = 185;    //place the car in the third lane
                }
                else{           //otherwise
                    p = 130;    //place the car in the fourth  lane
                }
                ng.lx[ng.nOpponent] = p; //assign line position of the car
                ng.speedOpp[ng.nOpponent] = (int)(Math.random()*100)%2 + 2; //sets the speed of the new opponent car to a random number that is the reminder of a number between 0 and 100, plus 2
                ng.nOpponent++; //add a new car to the game 
            }
        }
        
    }
    

}
