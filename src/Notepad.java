import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.file.Files;

public class Notepad extends JFrame {

    private JTextArea textArea;
    private boolean isModified = false;

    public Notepad() {
        super("Simple Notepad");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Text area with scroll
        textArea = new JTextArea();
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Detect text changes
        textArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { isModified = true; }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { isModified = true; }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { isModified = true; }
        });

        // Menu bar
        JMenuBar menuBar = new JMenuBar();

        // ===== File menu =====
        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem exitItem = new JMenuItem("Exit");

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);

        // ===== Edit menu =====
        JMenu editMenu = new JMenu("Edit");
        JMenuItem cutItem = new JMenuItem("Cut");
        JMenuItem copyItem = new JMenuItem("Copy");
        JMenuItem pasteItem = new JMenuItem("Paste");

        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);

        menuBar.add(editMenu);

        // ===== Format menu =====
        JMenu formatMenu = new JMenu("Format");
        JMenuItem fontItem = new JMenuItem("Font");
        JMenuItem colorItem = new JMenuItem("Color");

        formatMenu.add(fontItem);
        formatMenu.add(colorItem);

        menuBar.add(formatMenu);

        // ===== Help menu =====
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        helpMenu.add(aboutItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        // ===== File actions =====
        openItem.addActionListener(e -> {
            if (isModified) {
                int choice = JOptionPane.showConfirmDialog(this,
                        "You have unsaved changes. Continue without saving?",
                        "Unsaved Changes",
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (choice != JOptionPane.YES_OPTION) return;
            }
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                try {
                    java.io.File file = fileChooser.getSelectedFile();
                    String content = Files.readString(file.toPath());
                    textArea.setText(content);
                    isModified = false;
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error opening file: " + ex.getMessage());
                }
            }
        });

        saveItem.addActionListener(e -> saveFile());

        exitItem.addActionListener(e -> {
            if (isModified) {
                int option = JOptionPane.showConfirmDialog(this,
                        "You have unsaved changes. Do you want to save before exiting?",
                        "Unsaved Changes",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (option == JOptionPane.YES_OPTION) {
                    saveFile();
                    System.exit(0);
                } else if (option == JOptionPane.NO_OPTION) {
                    System.exit(0);
                }
            } else {
                System.exit(0);
            }
        });

        // ===== Edit actions =====
        cutItem.addActionListener(e -> textArea.cut());
        copyItem.addActionListener(e -> textArea.copy());
        pasteItem.addActionListener(e -> textArea.paste());

        // ===== Help action =====
        aboutItem.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Adhikari A.A.J.T. - 2022s19721", "About", JOptionPane.INFORMATION_MESSAGE)
        );

        // ===== Format actions =====
        // Color chooser
        colorItem.addActionListener(e -> {
            Color selectedColor = JColorChooser.showDialog(this, "Choose Text Color", textArea.getForeground());
            if (selectedColor != null) textArea.setForeground(selectedColor);
        });

        // Font chooser
        fontItem.addActionListener(e -> {
            String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
            JComboBox<String> fontBox = new JComboBox<>(fonts);
            fontBox.setSelectedItem(textArea.getFont().getFamily());

            String[] styles = {"Plain", "Bold", "Italic", "Bold+Italic"};
            JComboBox<String> styleBox = new JComboBox<>(styles);
            int style = textArea.getFont().getStyle();
            switch (style) {
                case Font.PLAIN -> styleBox.setSelectedIndex(0);
                case Font.BOLD -> styleBox.setSelectedIndex(1);
                case Font.ITALIC -> styleBox.setSelectedIndex(2);
                case Font.BOLD + Font.ITALIC -> styleBox.setSelectedIndex(3);
            }

            SpinnerNumberModel sizeModel = new SpinnerNumberModel(textArea.getFont().getSize(), 8, 72, 1);
            JSpinner sizeSpinner = new JSpinner(sizeModel);

            JPanel panel = new JPanel(new GridLayout(3, 2));
            panel.add(new JLabel("Font:"));
            panel.add(fontBox);
            panel.add(new JLabel("Style:"));
            panel.add(styleBox);
            panel.add(new JLabel("Size:"));
            panel.add(sizeSpinner);

            int result = JOptionPane.showConfirmDialog(this, panel, "Choose Font", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String selectedFont = (String) fontBox.getSelectedItem();
                int selectedStyle = switch ((String) styleBox.getSelectedItem()) {
                    case "Plain" -> Font.PLAIN;
                    case "Bold" -> Font.BOLD;
                    case "Italic" -> Font.ITALIC;
                    case "Bold+Italic" -> Font.BOLD + Font.ITALIC;
                    default -> Font.PLAIN;
                };
                int selectedSize = (int) sizeSpinner.getValue();
                textArea.setFont(new Font(selectedFont, selectedStyle, selectedSize));
            }
        });

        // ===== Keyboard shortcuts =====
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
        cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
        copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
        pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
        fontItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK));
        colorItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, KeyEvent.CTRL_DOWN_MASK));
    }

    private void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File file = fileChooser.getSelectedFile();
                Files.writeString(file.toPath(), textArea.getText());
                isModified = false;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Notepad n = new Notepad();
            n.setVisible(true);
        });
    }
}
