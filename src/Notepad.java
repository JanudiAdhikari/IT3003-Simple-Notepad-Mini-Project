import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Notepad extends JFrame {
    private JTextArea textArea;

    public Notepad() {
        super("Simple Notepad");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Text area inside a scroll pane
        textArea = new JTextArea();
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Menu bar
        JMenuBar menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Edit menu
        JMenu editMenu = new JMenu("Edit");
        JMenuItem cutItem = new JMenuItem("Cut");
        JMenuItem copyItem = new JMenuItem("Copy");
        JMenuItem pasteItem = new JMenuItem("Paste");
        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);

        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);

        // Simple action handlers
        cutItem.addActionListener(e -> textArea.cut());
        copyItem.addActionListener(e -> textArea.copy());
        pasteItem.addActionListener(e -> textArea.paste());

        exitItem.addActionListener(e -> {
            // you might want to check for unsaved changes here later
            System.exit(0);
        });

        aboutItem.addActionListener(e -> {
            // Replace with your name & ID
            JOptionPane.showMessageDialog(this, "Your Name - Your ID", "About", JOptionPane.INFORMATION_MESSAGE);
        });

        // TODO: implement openItem and saveItem using JFileChooser (next step)
        openItem.addActionListener(e -> {
            // implement with JFileChooser in next step
            JOptionPane.showMessageDialog(this, "Open not implemented yet");
        });
        saveItem.addActionListener(e -> {
            // implement with JFileChooser in next step
            JOptionPane.showMessageDialog(this, "Save not implemented yet");
        });
    }

    public static void main(String[] args) {
        // Always create GUI on the EDT
        SwingUtilities.invokeLater(() -> {
            Notepad n = new Notepad();
            n.setVisible(true);
        });
    }
}
