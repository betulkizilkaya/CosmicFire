
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;


class Ates{
    
    private int x;
    private int y;

    public Ates(int x, int y) {
        this.x = x;
        this.y = y;
        
    }

    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    
    
}

public class Oyun extends JPanel implements KeyListener,ActionListener{

    Timer timer=new Timer(5, this);/// 5 milisaniyede bir actionPerformed() metodunu tetikleyen zamanlayıcı
    
    private BufferedImage gemi_image;
    private BufferedImage uzayli_image;
    private BufferedImage arkaplan_image;
    
    private ArrayList<Rectangle> enemies=new ArrayList<Rectangle>();
    private ArrayList<Ates> atesler=new ArrayList<Ates>();
    
    private int atesdirY=1;
    private int uzaylidirX=3;
    private int uzaygemisiX=0;
    private int diruzayX=20;//Sağ ve sola bastığımızda hareket edeceği birim
    private int currentScore=0;
    private int highScore=0;
    private int currentTime=60000;
    private int enemyMoveCounter = 0;
   
    private boolean atesEdiliyorMu=false;
    
    private Clip background;
    private Clip win;
    private Clip laser;
    
    public boolean kontrolEt(){
        
        for(Rectangle enemy: enemies){
            for(Ates ates: atesler){
                Rectangle atesRect = new Rectangle(ates.getX(), ates.getY(), 10, 20);
                if (atesRect.intersects(enemy)) {
                    currentScore +=10;
                    enemies.remove(enemy);
                    addEnemy();
                    return true;
                }
            }
        }
        return false;
    }
    
    public void addEnemy(){//Uzaylımız öldüğünde yerine rastgele bir x koordinatında yenisini oluşturuyor.
        int panelWidth=getWidth();
        if(panelWidth<=55){
            panelWidth=680;
        }
        
        int randomX=new Random().nextInt(getWidth()-140)+70;
        enemies.add(new Rectangle(randomX,50,55,55));
    }
    
    public void restartGame(){
        enemies.clear();
        atesler.clear();
        
        uzaygemisiX=(800-gemi_image.getWidth()/10)/2;
        currentScore=0;
        currentTime=60000;
        uzaylidirX=2;
        
        addEnemy();
        background.setFramePosition(0);
        background.start();
        timer.start();
    }
    
    public void endGame(String messageTitle){
        background.stop();
        win.stop();
        timer.stop();
        
        background.stop();
        timer.stop();
        
        DBManager.saveScore(currentScore);
        highScore = DBManager.getHighScore();

        String[] options={"Try Again","Exit"};
        int choice=JOptionPane.showOptionDialog(this, messageTitle+
                                "\nScore: "+currentScore+
                                "\nHigh Score: "+highScore, "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
                                null, options, options[0]);
        
        if(choice==0){
            restartGame();
        }
        else{
            System.exit(0);
        }
    }
    
    
    public Oyun() {
        
        
        this.setSize(800,600);
        try{
            highScore = DBManager.getHighScore();
            AudioInputStream audioInputStream=AudioSystem.getAudioInputStream(new File("background.wav"));
            background=AudioSystem.getClip();
            background.open(audioInputStream);
            background.loop(Clip.LOOP_CONTINUOUSLY);//Sürekli çalmasını istediğimiz için LOOP'a aldık.
            
            AudioInputStream audioInputStreamWin = AudioSystem.getAudioInputStream(new File("win.wav"));
            win = AudioSystem.getClip();
            win.open(audioInputStreamWin);
            
            AudioInputStream audioInputStreamLaser = AudioSystem.getAudioInputStream(new File("laser.wav"));
            laser = AudioSystem.getClip();
            laser.open(audioInputStreamLaser);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        try {
            gemi_image=ImageIO.read(new FileImageInputStream(new File("uzaygemisi.png")));
            uzaygemisiX = (800 - gemi_image.getWidth() / 10) / 2;//Uzay gemisini ortaladık
            uzayli_image=ImageIO.read(new FileImageInputStream(new File("uzayli.png")));
            arkaplan_image=ImageIO.read(new FileImageInputStream(new File("arkaplan.png")));
        } catch (IOException ex) {
            Logger.getLogger(Oyun.class.getName()).log(Level.SEVERE, null, ex);
        }
        setBackground(new Color(10, 25, 47)); // Uzay görüntüsü için gece mavisi

        addEnemy();
        timer.start();
        
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); 
        
        g.drawImage(arkaplan_image, 0, 0,getWidth(),getHeight(), this);
    
        g.drawImage(gemi_image, uzaygemisiX, 450, gemi_image.getWidth()/10,gemi_image.getHeight()/10,this);
        
    
        for(Iterator<Ates> iterator=atesler.iterator(); iterator.hasNext();){
            Ates ates=iterator.next();
            if(ates.getY()<0){
                iterator.remove();
            }
        }
        
        for(Rectangle i: enemies){
            g.drawImage(uzayli_image, i.x, i.y, 70, 70, this);
        }
        
        g.setColor(Color.YELLOW);
        
        for(Ates ates: atesler){
            //Rengarenk lazer atışları
            Color[] renkler = {Color.RED, Color.ORANGE, Color.YELLOW, Color.CYAN};
            g.setColor(renkler[new Random().nextInt(renkler.length)]);
            g.fillRect(ates.getX(), ates.getY(), 8, 18);
        }
        
        g.setFont(new Font("Courier", Font.PLAIN, 20));
        g.setColor(Color.WHITE);
        
        g.drawString("Time Left: " + (currentTime/1000), 10, 20);
        g.drawString("Score: " + currentScore, 10, 40);
        g.drawString("High Score: " + highScore, 10,60);

    }

    @Override
    public void repaint() {
        super.repaint(); 
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
         int c=e.getKeyCode();
        
        if(c==KeyEvent.VK_LEFT){
            if(uzaygemisiX<=0){
                uzaygemisiX=0;
            }
            else{
                uzaygemisiX-=diruzayX;
            }
        }
        else if(c==KeyEvent.VK_RIGHT){
            if(uzaygemisiX>=680){
                uzaygemisiX=680;
            }
            else{
                uzaygemisiX +=diruzayX;
            }
        }
        
        else if(c==KeyEvent.VK_CONTROL && !atesEdiliyorMu){
            atesler.add(new Ates(uzaygemisiX+47,456));
            
            if(laser.isRunning()){
                laser.stop();
            }
            
            laser.setFramePosition(0);//Sesi başa sarar
            laser.start();
            
            atesEdiliyorMu = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
        atesEdiliyorMu = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) { 
        enemyMoveCounter++;
        if(enemyMoveCounter>=3){
            boolean reverseDirection=false;
            
            
            for(Rectangle i: enemies){
                i.x+=uzaylidirX;
                
                if(i.x<=0||i.x>=getWidth()-70){
                    reverseDirection=true;
                }
            }
            
            if(reverseDirection){
                uzaylidirX=-uzaylidirX;
                for(Rectangle enemy: enemies){
                    enemy.y+=50;
                }
            }
            enemyMoveCounter=0;
        }
        
       for(Rectangle enemy: enemies){
            if(enemy.y>=getHeight()-150){
                endGame("-GAME OVER-\nAn Alien Reached Earth!");
                return;
            }
        }
        
 
        for(Ates ates: atesler){
            ates.setY(ates.getY()-atesdirY);   
        }
        
        kontrolEt();
        
        if(currentTime>0){
            currentTime-=5;
        }
        
        if(currentTime<=0){
            win.start();
            endGame("-Time's Up!");
            return;
        }
        repaint();
    }
}
