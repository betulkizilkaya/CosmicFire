import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class DBManager {
    
    private static final String URL="jdbc:mysql://localhost:3306/game_db";
    private static final String USER="root";
    private static final String PASSWORD = "";
    

    public static int getHighScore() {
        int highscore = 0;
        
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement stmt = conn.prepareStatement("SELECT MAX(score) AS max_score FROM highscores");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                highscore = rs.getInt("max_score");
            }
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return highscore;
    }
    
    public static void saveScore(int score){
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO highscores(score) VALUES(?)");
            stmt.setInt(1, score);
            stmt.executeUpdate();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
