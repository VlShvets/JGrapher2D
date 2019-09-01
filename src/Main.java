import com.jgrapher2d.JGrapher2D;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame main = new JFrame();

                JGrapher2D jgrapher2D = new JGrapher2D();
                main.add(jgrapher2D, BorderLayout.CENTER);

                main.setTitle("JGrapher2D");
                main.setExtendedState(JFrame.MAXIMIZED_BOTH);
                main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                main.setVisible(true);
            }
        });
    }
}
