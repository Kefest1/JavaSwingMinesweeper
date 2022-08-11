import javax.swing.JLabel;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class GameTimer extends JLabel {
    Timer timer;
    int seconds = 0;
    int minutes = 0;
    JLabel gameAt;

    public GameTimer(JLabel gameAt) {
        this.gameAt = gameAt;
        timer = new Timer();

        this.setBounds(570, 10, 300, 100);
        this.setFont(new Font("MV Boli", Font.BOLD, 48));
        this.setForeground(Color.RED);
    }

    void startCounting() {
        timer.scheduleAtFixedRate(new MyTimerTask(this), 0, 1000);
    }

    void stopCounting() {
        timer.cancel();
    }

}

class MyTimerTask extends TimerTask {
    GameTimer timerClass;

    public MyTimerTask(GameTimer timerClass) {
        this.timerClass = timerClass;
    }

    @Override
    public void run() {
        if (timerClass.seconds == 59) {
            timerClass.seconds = 0;
            timerClass.minutes++;
        }
        else timerClass.seconds++;

        timerClass.setText(String.format("%02d", timerClass.minutes) + ":" + String.format("%02d", timerClass.seconds));
    }
}
