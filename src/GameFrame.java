import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    MineSweeper gameLabel;
    MainMenu menuLabel;

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = SCREEN_WIDTH + 200;

    public GameFrame() {
        prepareTheFrame();
        menuLabel = new MainMenu(this);
        this.setVisible(true);
    }

    private void prepareTheFrame() {
        this.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("Minesweeper");
    }

}
