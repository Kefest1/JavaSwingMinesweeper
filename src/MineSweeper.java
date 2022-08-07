import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class MineSweeper extends JLabel {
    public static final int ROW_FIELDS = 11;
    public static final int CHECK_BUTTON_BORDER_THICKNESS = 7;
    public static final int COLUMN_FIELDS = 13;
    public static final int MINES_AMOUNT = 16;
    public static final int FIELD_SIZE = 60;
    public static final int MINE_SIZE = 50;
    public static final int FIELD_GAP = 10;
    public static final int FIELD_GAP_HALF = FIELD_GAP / 2;

    private static final int TOTAL_FIELDS_TO_UNCOVER = (COLUMN_FIELDS * ROW_FIELDS) - MINES_AMOUNT;
    private int fieldsToUncover = TOTAL_FIELDS_TO_UNCOVER;
    private int minesRemaining = MINES_AMOUNT;


    Image imageFieldFlagged = new ImageIcon("icons\\fieldIsFlagged.PNG").getImage();
    Image[] fieldNeighbourMines = {
            new ImageIcon("icons\\fieldZero.png").getImage(),
            new ImageIcon("icons\\fieldOne.png").getImage(),
            new ImageIcon("icons\\fieldTwo.png").getImage(),
            new ImageIcon("icons\\fieldThree.png").getImage(),
            new ImageIcon("icons\\fieldFour.png").getImage(),
            new ImageIcon("icons\\fieldFive.png").getImage(),
            new ImageIcon("icons\\fieldSix.png").getImage(),
            new ImageIcon("icons\\fieldSeven.png").getImage(),
            new ImageIcon("icons\\fieldEight.png").getImage()
    };

    Random random;
    Coordinates[] minesCoordinates;
    Coordinates currentlyAt;
    JButton buttonMarkFieldAsMine;
    JButton buttonUncoverField;
    JButton buttonExit;
    JButton buttonRetry; // regardless of situation

    JLabel labelWithRemainingMinesAmount;
    JLabel youWonLabel;
    JLabel gameOverLabel;

    public static final Color BACKGROUND_COLOR = new Color(43, 43, 44);
    public static final Color MINE_FIELD_COLOR = new Color(181, 182, 181);
    public StatusOfField[][] mineField;

    GameFrame gameFrame;

    MineSweeper(GameFrame gameFrame) {
        setMineField();
        this.setFocusable(true);
        this.requestFocus();
        generateMines();
        this.setOpaque(true);
        this.setBackground(BACKGROUND_COLOR);
        currentlyAt = new Coordinates(0, 0);
        this.gameFrame = gameFrame;
        gameFrame.isRunning = true;
        createButtons();
        prepareLabelWithRemainingMinesAmount();
        gameFrame.add(this);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.setColor(MINE_FIELD_COLOR);
        for (int i = 0; i < COLUMN_FIELDS; i++) {
            for (int j = 0; j < ROW_FIELDS; j++) {
                if (mineField[i][j] == StatusOfField.FIELD_IS_FLAGGED) {
                    g.drawImage(new ImageIcon(imageFieldFlagged).getImage(), i * FIELD_SIZE + FIELD_GAP_HALF, j * FIELD_SIZE + FIELD_GAP_HALF + 300, null);
                } else if (mineField[i][j] == StatusOfField.FIELD_IS_UNCOVERED) {
                    g.drawImage(howManyMinedNeighboursPicture(i, j), i * FIELD_SIZE + FIELD_GAP_HALF, j * FIELD_SIZE + FIELD_GAP_HALF + 300, null);
                } else {
                    g.drawRect(i * FIELD_SIZE + FIELD_GAP_HALF, j * FIELD_SIZE + FIELD_GAP_HALF + 300, MINE_SIZE, MINE_SIZE);
                    g.fillRect(i * FIELD_SIZE + FIELD_GAP_HALF, j * FIELD_SIZE + FIELD_GAP_HALF + 300, MINE_SIZE, MINE_SIZE);
                }
            }
        }


        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setStroke(new java.awt.BasicStroke((float) CHECK_BUTTON_BORDER_THICKNESS));
        graphics2D.setColor(Color.BLUE);
        graphics2D.drawRect(currentlyAt.x + 3, currentlyAt.y + 303, MINE_SIZE + CHECK_BUTTON_BORDER_THICKNESS, MINE_SIZE + CHECK_BUTTON_BORDER_THICKNESS);
    }

    private void generateMines() {
        random = new Random();
        minesCoordinates = new Coordinates[MINES_AMOUNT];
        for (int i = 0; i < MINES_AMOUNT; i++) {
            do {
                minesCoordinates[i] = new Coordinates(random.nextInt(COLUMN_FIELDS), random.nextInt(ROW_FIELDS));
            } while (!checkIfMinesCoordinatesAreUnique(i));
        }
    }

    private boolean checkIfMinesCoordinatesAreUnique(int indexAt) {
        for (int i = indexAt - 1; i >= 0; i--)
            if (minesCoordinates[i].isEqual(minesCoordinates[indexAt]))
                return false;

        return true;
    }

    private void setMineField() {
        mineField = new StatusOfField[COLUMN_FIELDS][ROW_FIELDS];
        for (int i = 0; i < COLUMN_FIELDS; i++)
            for (int j = 0; j < ROW_FIELDS; j++)
                mineField[i][j] = StatusOfField.FIELD_IS_COVERED;
    }

    static class Coordinates {
        int x;
        int y;

        public Coordinates(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public boolean isEqual(Coordinates obj) {
            return this.x == obj.x && this.y == obj.y;
        }
    }

    private enum StatusOfField {
        FIELD_IS_UNCOVERED,
        FIELD_IS_COVERED,
        FIELD_IS_FLAGGED
    }

    private void createButtons() {
        buttonMarkFieldAsMine = new JButton("Mark it as a mine");
        buttonMarkFieldAsMine.setFocusable(false);
        buttonMarkFieldAsMine.setBounds(570, 100, 180, 75);
        buttonMarkFieldAsMine.addActionListener(
                e -> {
                    minesRemaining--;
                    prepareLabelWithRemainingMinesAmount();
                    if (mineField[getXFieldCoordinate()][getYFieldCoordinate()] == StatusOfField.FIELD_IS_COVERED)
                        mineField[getXFieldCoordinate()][getYFieldCoordinate()] = StatusOfField.FIELD_IS_FLAGGED;
                    else if (mineField[getXFieldCoordinate()][getYFieldCoordinate()] == StatusOfField.FIELD_IS_FLAGGED)
                        mineField[getXFieldCoordinate()][getYFieldCoordinate()] = StatusOfField.FIELD_IS_COVERED;

                    repaint();
                }
        );

        buttonUncoverField = new JButton("Mark it as a safe square");
        buttonUncoverField.setFocusable(false);
        buttonUncoverField.setBounds(570, 200, 180, 75);
        buttonUncoverField.addActionListener(
                e -> {
                    if (isMined(getXFieldCoordinate(), getYFieldCoordinate())) {
                        gameOver();
                    } else {
                        if (howManyMinedNeighboursIndex(getXFieldCoordinate(), getYFieldCoordinate()) == 0)
                            uncoverFieldsWithNoNeighbours(getXFieldCoordinate(), getYFieldCoordinate());
                        else fieldsToUncover--;
                        if (fieldsToUncover == 0) {
                            repaint();
                            prepareYouWonLabel();
                        } else repaint();
                        mineField[getXFieldCoordinate()][getYFieldCoordinate()] = StatusOfField.FIELD_IS_UNCOVERED;
                    }
                }
        );

        this.add(buttonMarkFieldAsMine);
        this.add(buttonUncoverField);
    }

    private void gameOver() {
        prepareLabelWithGameOver();
        prepareButtonWithRetry();
        repaint();
        gameFrame.isRunning = false;
        prepareExitButton();
    }

    private boolean isMined(int x, int y) {
        for (int i = 0; i < MINES_AMOUNT; i++)
            if (minesCoordinates[i].x == x && minesCoordinates[i].y == y)
                return true;

        return false;
    }

    private int isMinedInt(int x, int y) {
        // Important - returns zero when out of bounds
        for (int i = 0; i < MINES_AMOUNT; i++)
            if (minesCoordinates[i].x == x && minesCoordinates[i].y == y)
                return 1;

        return 0;
    }

    private int getXFieldCoordinate() {
        return currentlyAt.x / FIELD_SIZE;
    }

    private int getYFieldCoordinate() {
        return currentlyAt.y / FIELD_SIZE;
    }

    private Image howManyMinedNeighboursPicture(int x, int y) {
        return fieldNeighbourMines[howManyMinedNeighboursIndex(x, y)];
    }

    private int howManyMinedNeighboursIndex(int x, int y) {
        int retIndexOfPicture = 0;

        retIndexOfPicture += isMinedInt(x + 1, y - 1);
        retIndexOfPicture += isMinedInt(x + 1, y);
        retIndexOfPicture += isMinedInt(x + 1, y + 1);

        retIndexOfPicture += isMinedInt(x - 1, y);
        retIndexOfPicture += isMinedInt(x - 1, y + 1);
        retIndexOfPicture += isMinedInt(x - 1, y - 1);

        retIndexOfPicture += isMinedInt(x, y - 1);
        retIndexOfPicture += isMinedInt(x, y + 1);

        return retIndexOfPicture;
    }

    private void uncoverFieldsWithNoNeighbours(int x, int y) {
        uncoverFieldsWithNoNeighboursHelp(x, y);
        repaint();
    }

    private void uncoverFieldsWithNoNeighboursHelp(int x, int y) {
        if (x >= 0 && y >= 0 && x < COLUMN_FIELDS && y < ROW_FIELDS) {

            if (mineField[x][y] == StatusOfField.FIELD_IS_UNCOVERED)
                return;

            uncoverField(x, y);
            if (howManyMinedNeighboursIndex(x, y) == 0) {
                uncoverFieldsWithNoNeighboursHelp(x + 1, y - 1);
                uncoverFieldsWithNoNeighboursHelp(x + 1, y);
                uncoverFieldsWithNoNeighboursHelp(x + 1, y + 1);

                uncoverFieldsWithNoNeighboursHelp(x - 1, y - 1);
                uncoverFieldsWithNoNeighboursHelp(x - 1, y);
                uncoverFieldsWithNoNeighboursHelp(x - 1, y + 1);

                uncoverFieldsWithNoNeighboursHelp(x, y - 1);
                uncoverFieldsWithNoNeighboursHelp(x, y + 1);
            }
        }
    }

    private void uncoverField(int x, int y) {
        fieldsToUncover--;
        mineField[x][y] = StatusOfField.FIELD_IS_UNCOVERED;
    }

    private void prepareLabelWithRemainingMinesAmount() {
        if (labelWithRemainingMinesAmount != null) this.remove(labelWithRemainingMinesAmount);
        labelWithRemainingMinesAmount = new JLabel(minesRemaining + " mines remaining");
        labelWithRemainingMinesAmount.setFont(new Font("Ink Free", Font.BOLD, 40));
        labelWithRemainingMinesAmount.setBounds(50, 50, 400, 100);
        labelWithRemainingMinesAmount.setForeground(Color.CYAN);
        this.add(labelWithRemainingMinesAmount);
    }

    private void prepareLabelWithGameOver() {
        gameOverLabel = new JLabel("Game over");
        gameOverLabel.setFont(new Font("Ink Free", Font.BOLD, 64));
        gameOverLabel.setForeground(Color.RED);
        gameOverLabel.setBounds(100, 100, 400, 75);
        this.add(gameOverLabel);
    }

    private void prepareButtonWithRetry() {
        this.remove(labelWithRemainingMinesAmount);
        buttonRetry = new JButton("Retry");
        buttonRetry.setBounds(100, 180, 150, 75);
        buttonRetry.setFocusable(false);
        buttonRetry.addActionListener(
                e -> prepareNewGame()
        );
        this.add(buttonRetry);
    }

    private void prepareNewGame() {
        fieldsToUncover = TOTAL_FIELDS_TO_UNCOVER;
        minesRemaining = MINES_AMOUNT;
        prepareLabelWithRemainingMinesAmount();
        this.remove(labelWithRemainingMinesAmount);
        setMineField();
        generateMines();
        currentlyAt = new Coordinates(0, 0);
        createButtons();
        prepareLabelWithRemainingMinesAmount();
        gameFrame.isRunning = true;
        currentlyAt.x = currentlyAt.y = 0;
        if (youWonLabel != null) youWonLabel.setVisible(false);
        if (gameOverLabel != null) gameOverLabel.setVisible(false);
        if (buttonRetry != null) buttonRetry.setVisible(false);
        if (buttonExit != null) buttonExit.setVisible(false);
        repaint();
        gameFrame.add(this);
    }

    private void prepareYouWonLabel() {
        youWonLabel = new JLabel("You won!");
        youWonLabel.setFont(new Font("Ink Free", Font.BOLD, 64));
        youWonLabel.setForeground(Color.RED);
        youWonLabel.setBounds(100, 100, 400, 75);
        this.add(youWonLabel);
        //prepareNewGame();
        prepareExitButton();
        prepareButtonWithRetry();
    }

    private void prepareExitButton() {
        buttonExit = new JButton("Exit");
        buttonExit.setBounds(300, 180, 150, 75);
        buttonExit.setFocusable(false);
        buttonExit.addActionListener(
                e -> {
                    gameFrame.remove(this);
                    this.gameFrame.dispose();
                }
        );
        this.add(buttonExit);
    }

}
