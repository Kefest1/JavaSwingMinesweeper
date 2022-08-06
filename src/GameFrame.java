import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameFrame extends JFrame {
    MineSweeper gameLabel;
    MainMenu menuLabel;
    public boolean isRunning = false;

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = SCREEN_WIDTH + 200;

    public GameFrame() {
        prepareTheFrame();
        menuLabel = new MainMenu(this);
        this.setFocusable(true);
        this.requestFocus();
        this.addKeyListener(new MyKeyAdapter());
        this.setVisible(true);
    }

    private void prepareTheFrame() {
        this.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("Minesweeper");
    }

    public class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if (isRunning) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    if (gameLabel.currentlyAt.x != 0) {
                        gameLabel.currentlyAt.x -= MineSweeper.FIELD_SIZE;
                        gameLabel.repaint();
                    }
                }
                else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    if (gameLabel.currentlyAt.x != ((MineSweeper.COLUMN_FIELDS - 1) * (MineSweeper.FIELD_SIZE))) {
                        gameLabel.currentlyAt.x += MineSweeper.FIELD_SIZE;
                        gameLabel.repaint();
                    }
                }
                else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if (gameLabel.currentlyAt.y != ((MineSweeper.ROW_FIELDS - 1) * MineSweeper.FIELD_SIZE)) {
                            gameLabel.currentlyAt.y += MineSweeper.FIELD_SIZE;
                            gameLabel.repaint();
                        }
                    }
                else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    if (gameLabel.currentlyAt.y != 0) {
                        gameLabel.currentlyAt.y -= MineSweeper.FIELD_SIZE;
                        gameLabel.repaint();
                    }
                }


            }
        }
    }


}
