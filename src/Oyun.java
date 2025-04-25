
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
    
    private int gecen_sure=0;
    private int harcanan_ates=0;
    
    private BufferedImage gemi_image;
    
    private BufferedImage uzayli_image;
    
    private BufferedImage arkaplan_image;
    
    private ArrayList<Ates> atesler=new ArrayList<Ates>();
    
    private int atesdirY=1;
    
    private int topX = (800 - 20) / 2;//Ortalamak için
    
    private int topdirX=3;
    
    private int uzaygemisiX=0;
    
    private int diruzayX=20;//Sağ ve sola bastığımızda hareket edeceği birim

    private boolean atesEdiliyorMu=false;
    
    private Clip background;
    private Clip win;
    private Clip laser;
    
    public boolean kontrolEt(){
        
        for(Ates ates: atesler){
            Rectangle atesRect = new Rectangle(ates.getX(), ates.getY(), 10, 20);
            Rectangle topRect = new Rectangle(topX, 50, 55, 55);


            if (atesRect.intersects(topRect)) {
                return true;
            }
        }
        return false;
    }
    
    public Oyun() {
        
        try{
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

        
        timer.start();
        
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); 
        
        g.drawImage(arkaplan_image, 0, 0,getWidth(),getHeight(), this);
        
        gecen_sure+=5;
    
        g.setColor(Color.red);
        
        //g.fillOval(topX, 0, 20, 20);//Çizim yapmak yerine görsel ekliyoruz.
        
        g.drawImage(uzayli_image, topX, 50, 70, 70, this);

        g.drawImage(gemi_image, uzaygemisiX, 450, gemi_image.getWidth()/10,gemi_image.getHeight()/10,this);
        
        for(Ates ates: atesler){
            if(ates.getY()<0){
                atesler.remove(ates);
            }
        }
        
        g.setColor(Color.YELLOW);
        
        for(Ates ates: atesler){
            
            //Alevli atış
            /*Graphics2D g2d = (Graphics2D) g;
            GradientPaint gp = new GradientPaint(ates.getX(), ates.getY(), Color.YELLOW,ates.getX(), ates.getY() + 20, Color.RED);
            g2d.setPaint(gp);
            g2d.fillRect(ates.getX(), ates.getY(), 6, 20);
            */

            //Rengarenk lazer atışları
            Color[] renkler = {Color.RED, Color.ORANGE, Color.YELLOW, Color.CYAN};
            g.setColor(renkler[new Random().nextInt(renkler.length)]);
            g.fillRect(ates.getX(), ates.getY(), 8, 18);

            //Kare ateşler için
            //g.fillRect(ates.getX(),ates.getY(),10,20);
        }
        
        g.setFont(new Font("Courier", Font.PLAIN, 20));
        g.setColor(Color.WHITE);
        g.drawString("Time: "+(gecen_sure/1000)+" sec", 10, 20);
        g.drawString("Shots: "+harcanan_ates,10, 40);

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
            harcanan_ates++;
            
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
       
        for(Ates ates: atesler){
            ates.setY(ates.getY()-atesdirY);
            
        }
        
        topX+=topdirX;
        
        if(topX>=705){
            topdirX = -topdirX;
        }
        
        if(topX<=10){
            topdirX= -topdirX;
        }
        
        if(kontrolEt()){
            background.stop();
            win.start();
            timer.stop();
            String message="You Win!!!\n"+
                           "Shots Fired: "+harcanan_ates+
                           "\nTime Elapsed: "+(gecen_sure/1000.0)+" sec";
            JOptionPane.showMessageDialog(this, message);
            System.exit(0);
        }
        
        repaint();
        
    }
    
    
    
}
