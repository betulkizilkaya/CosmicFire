
import java.awt.HeadlessException;
import javax.swing.JFrame;

public class OyunEkrani extends JFrame {

    public OyunEkrani(String title) throws HeadlessException {
        super(title);
    }

    public static void main(String[] args) {

        OyunEkrani ekran = new OyunEkrani("Cosmic Fire");
        ekran.setResizable(false);
        ekran.setFocusable(false);// Pencerenin klavye odaklı olayları almaması sağlanıyor

        ekran.setSize(800, 600);
        ekran.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Oyun oyun = new Oyun();
        oyun.requestFocus();// Klavyeden girişleri alabilmek için oyun bileşenine odaklanılıyor 

        oyun.addKeyListener(oyun);// Oyun nesnesi, kendi klavye olaylarını dinlemesi için KeyListener olarak ekleniyor
        oyun.setFocusable(true);
        oyun.setFocusTraversalKeysEnabled(false);// Sekme tuşları gibi focus geçiş tuşlarının varsayılan davranışı kapatılıyor

        ekran.add(oyun);
        ekran.setLocationRelativeTo(null);//Oyun ekranını ortalar
        ekran.setVisible(true);

    }

}
