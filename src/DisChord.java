import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.*;

public class DisChord {

    public static void main(String[] args) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty(
            "com.apple.mrj.application.apple.menu.about.name", "Name");
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {

                JFrame frame = new JFrame("DisChord");
                final JPanel dm = new JPanel() {

                    @Override
                    public Dimension getPreferredSize() {
                        return new Dimension(320, 240);
                    }
                };
                //dm.setBorder(BorderFactory.createLineBorder(Color.blue, 10));

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(dm);
                frame.pack();
                frame.setLocationByPlatform(true);

                JMenuBar menuBar = new JMenuBar();
                JMenu windowMenu = new JMenu("Window");
                JMenuItem tuningItem = new JMenuItem("Tuning");
                JMenuItem chordItem = new JMenuItem("Chord");

                tuningItem.addActionListener(e -> {
                    TuningWindow tuningWindow = new TuningWindow();
                });

                windowMenu.add(tuningItem);
                windowMenu.add(chordItem);

                menuBar.add(windowMenu);
                frame.setJMenuBar(menuBar);
                frame.setVisible(true);
            }
        });
    }
}
