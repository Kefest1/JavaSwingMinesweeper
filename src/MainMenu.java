import javax.swing.*;
import java.awt.*;

public class MainMenu extends JLabel {
    GameFrame gameFrame;

    JLabel greeting;
    JLabel nicknameText;
    JTextField nicknameHolder;
    JButton startGameButton;
    JButton exitGameButton;

    private static final Color MAIN_MENU_COLOR = new Color(43, 43, 44);

    public MainMenu(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        prepareGreetingPanel();
        prepareThis();
        prepareNicknameHolder();
        prepareButtons();
    }

    private void prepareThis() {
        this.setOpaque(true);
        this.setBackground(MAIN_MENU_COLOR);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 12));
        gameFrame.add(this);
    }

    private void prepareGreetingPanel() {
        greeting = new JLabel();
        greeting.setBounds(30, 0 ,800, 450);
        greeting.setText("Welcome to the minesweeper game");
        greeting.setFont(new Font("Mv boli", Font.PLAIN, 42));
        greeting.setVerticalTextPosition(JLabel.TOP);
        greeting.setHorizontalTextPosition(JLabel.CENTER);
        greeting.setForeground(Color.RED);
        greeting.setIcon(new ImageIcon("icons\\mineInMenu.png"));
        greeting.setIconTextGap(50);
        this.add(greeting);
    }

    private void prepareNicknameHolder() {
        nicknameHolder = new JTextField();
        nicknameHolder.setBounds(100, 450, 200, 50);

        nicknameText = new JLabel("Enter username");
        nicknameText.setBounds(100, 400, 200, 50);
        nicknameText.setForeground(Color.WHITE);
        nicknameText.setFont(new Font("Mv boli", Font.PLAIN, 24));
        this.add(nicknameHolder);
        this.add(nicknameText);
    }

    private void prepareButtons() {
        startGameButton = new JButton("Start game");
        startGameButton.setFocusable(false);
        startGameButton.setBounds(100, 530, 100, 50);
        startGameButton.addActionListener(
                e -> {
                    this.gameFrame.menuLabel.setVisible(false);
                    this.gameFrame.gameLabel = new MineSweeper(this.gameFrame);
                }
        );

        exitGameButton = new JButton("Exit");
        exitGameButton.setFocusable(false);
        exitGameButton.setBounds(100, 600, 100, 50);
        exitGameButton.addActionListener(
                e -> this.gameFrame.dispose()
        );

        this.add(startGameButton);
        this.add(exitGameButton);
    }

}
