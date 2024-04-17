import javax.swing.*;

public class App extends JFrame
{
    App() {
        setTitle("snake");
        setVisible(true);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SnakeGame snakeGame = new SnakeGame();
        add(snakeGame);
        pack();
        snakeGame.requestFocus();
    }
    public static void main(String[] args) {
        new App();


    }
}
