import com.grapher2d.Grapher2D;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Grapher2D grapher2D = new Grapher2D();
                grapher2D.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                grapher2D.setVisible(true);
            }
        });
    }
}
