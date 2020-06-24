package view;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void launchLoginFrame() {
        EventQueue.invokeLater(() -> new LoginFrame().setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE));
    }

    public static void main(String[] args) {
        launchLoginFrame();
    }
}
