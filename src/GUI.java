
import javax.swing.*;

import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;


public class GUI {
    private JFrame frame;
    private JTextArea qubitInfoTextArea;
    private int qubitCount = 0;
    ArrayList<Integer> qubitList = new ArrayList<Integer>();

    public GUI() {
        frame = new JFrame("QuantumCircuitSimulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(frame);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setup();
        frame.setVisible(true);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
            }
        });
    }

    public void setup() {
        JSplitPane main_split_pane = create_main_split_pane();
        JSplitPane child_split_pane = setup_main_split_pane_top(main_split_pane);
        setup_child_split_pane_left(child_split_pane);
        setup_child_split_pane_right(child_split_pane);

        JMenuBar menu_bar = create_menu_bar();

        JMenu file_menu = create_menu("File", menu_bar);
        add_item_new(file_menu);

        file_menu.addSeparator();
        file_menu.addSeparator();
        add_item_quit(file_menu);

        JMenu circuit_menu = create_menu("Circuit", menu_bar);

        circuit_menu.addSeparator();

        JMenu help_menu = create_menu("Help", menu_bar);
        add_item_about(help_menu);

        JButton inputQubitButton = new JButton("Input Qubit");
        inputQubitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showInputQubitDialog();
            }
        });
        JButton inputQGateButton = new JButton("Input QGate");
        inputQGateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showQGateDialog();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(inputQGateButton);
        buttonPanel.add(inputQubitButton);

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);
        frame.getContentPane().add(main_split_pane, BorderLayout.CENTER);
        frame.pack();
    }

    // NEW INPUT CODE
    private void showInputQubitDialog() {
        JTextField inputField = new JTextField();
        Object[] message = {
                "Enter qubit initial state:\n\n" +
                        "(Accepted inputs: 0, 1, i, -i)", inputField
        };
        int option = JOptionPane.showConfirmDialog(null, message, "Input Qubit", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String inputValue = inputField.getText();
            if (isValidInput(inputValue)) {
                int intValue = convertToInteger(inputValue);
                if (qubitCount < 10) {
                    Main.qubitGateList[qubitCount][0] = intValue;
                    qubitCount++;
                    updateRecordMenu();
                } else {
                    JOptionPane.showMessageDialog(null, "Maximum number of qubits reached (10).");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid state (0, 1, i, -i).");
            }
        }
    }
    //Input QGate code
    private void showQGateDialog() {
        JTextField qubitNumberField = new JTextField();
        JTextField gatePositionField = new JTextField();
        JTextField gateField = new JTextField();
        Object[] message = {
                "Qubit Number:", qubitNumberField,
                "Gate Position (1-10):", gatePositionField,
                "Gate (Hadamard, Pauli X, Pauli Y):", gateField
        };
        int option = JOptionPane.showConfirmDialog(null, message, "Input QGate", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String qubitNumberValue = qubitNumberField.getText();
            String gatePositionValue = gatePositionField.getText();
            String gateValue = gateField.getText();
            if (isValidQubitNumber(qubitNumberValue) && isValidGatePosition(gatePositionValue) && isValidGate(gateValue)) {
                int qubitNumber = Integer.parseInt(qubitNumberValue);
                int gatePosition = Integer.parseInt(gatePositionValue);
                if (qubitNumber >= 1 && qubitNumber <= qubitCount) {
                    int gateIndex;
                    switch (gateValue.toLowerCase()) {
                        case "hadamard":
                            gateIndex = 1;
                            break;
                        case "pauli x":
                            gateIndex = 2;
                            break;
                        case "pauli y":
                            gateIndex = 3;
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "Invalid gate! Please enter Hadamard, Pauli X, Pauli Y.");
                            return;
                    }
                    int qubitIndex = qubitNumber - 1;
                    Main.qubitGateList[qubitIndex][gateIndex] = gatePosition;
                    updateRecordMenu();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid qubit number. Please enter a valid qubit number.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid values.");
            }
        }
    }
    //Checks
    private boolean isValidQubitNumber(String input) {
        try {
            int qubitNumber = Integer.parseInt(input);
            return qubitNumber >= 1 && qubitNumber <= qubitCount;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidGatePosition(String input) {
        try {
            int gatePosition = Integer.parseInt(input);
            return gatePosition >= 1 && gatePosition <= 10;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidGate(String gate) {
        return gate.equalsIgnoreCase("hadamard") ||
                gate.equalsIgnoreCase("pauli x") ||
                gate.equalsIgnoreCase("pauli y");
    }
    private boolean isValidInput(String input) {
        return input.matches("[01i]|-?i");
    }
    //Print
    private void updateRecordMenu() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < qubitCount; i++) {
            sb.append("Qubit ").append(i + 1).append(" (").append(Main.qubitGateList[i][0]).append("): ");
            for (int j = 1; j < 5; j++) {
                if (Main.qubitGateList[i][j] != 0) {
                    sb.append(getGateName(j)).append(" (").append(Main.qubitGateList[i][j]).append(")").append(", ");
                }
            }
            if (sb.toString().endsWith(", ")) {
                sb.delete(sb.length() - 2, sb.length());
            }
            sb.append("Probability: ").append(calculateProbability(Main.qubitGateList, i)).append("\n");
        }
        qubitInfoTextArea.setText(sb.toString());
    }
    private double calculateProbability(int[][] qubitGateList, int qubitIndex) {
        int initialState = qubitGateList[qubitIndex][0];
        double probability = 0.0;

        switch (initialState) {
            case 0:
                probability = 0;
                break;
            case 1:
                probability = 1;
                break;
            case 2:
                probability = 0.5;
                break;
            case 3:
                probability = -0.5;
                break;
        }

        for (int i = 1; i < 4; i++) {
            int gateCount = qubitGateList[qubitIndex][i];
            if (gateCount != 0) {
                switch (i) {
                    case 1: // Hadamard gate
                        if (probability <0.5 && probability!= 0.5){probability += 0.5;}
                        else if (probability >0.5) {probability -= 0.5;}
                        break;
                    case 2: // Pauli-X gate
                        if (probability == 0){probability = 1;}
                        else if (probability == 1) {probability = 0;}
                        break;
                    case 3: // Pauli-Y gate
                        if (probability == 0){probability = 1;}
                        else if (probability == 1) {probability = 0;}
                        else if (probability == 0.5) {probability *= -1;}
                        break;
                }
            }
        }

        return probability;
    }
    private String getGateName(int index) {
        switch (index) {
            case 1:
                return "Hadamard";
            case 2:
                return "Pauli X";
            case 3:
                return "Pauli Y";
            default:
                return "";
        }
    }
    private int convertToInteger(String value) {
        switch (value) {
            case "0":
                return 0;
            case "1":
                return 1;
            case "i":
                return 2;
            case "-i":
                return 3;
            default:
                return -1;  // Invalid value
        }
    }


    // Window GUI setup functions

    private JSplitPane create_main_split_pane() {
        JSplitPane main_split_pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        main_split_pane.setResizeWeight(main_split_pane_resize_weight);
        frame.getContentPane().add(main_split_pane);

        return main_split_pane;
    }

    private JSplitPane setup_main_split_pane_top(final JSplitPane split_pane) {
        JSplitPane child_split_pane = new JSplitPane();
        child_split_pane.setResizeWeight(child_split_pane_resize_weight);
        split_pane.setLeftComponent(child_split_pane);

        return child_split_pane;
    }



    private void setup_child_split_pane_left(final JSplitPane split_pane) {
        JScrollPane quantum_system_scroll_pane = new JScrollPane();
        split_pane.setLeftComponent(quantum_system_scroll_pane);

        JPanel quantum_system_panel = new JPanel();
        quantum_system_scroll_pane.setViewportView(quantum_system_panel);

        qubitInfoTextArea = new JTextArea(10, 10);
        qubitInfoTextArea.setEditable(false);
        quantum_system_panel.add(qubitInfoTextArea);
    }

    private void setup_child_split_pane_right(final JSplitPane split_pane){
        JScrollPane result_scroll_pane = new JScrollPane();
        split_pane.setRightComponent(result_scroll_pane);

        JPanel result_panel = new JPanel();
        result_panel.setLayout(new BoxLayout(result_panel, BoxLayout.Y_AXIS));
        result_scroll_pane.setViewportView(result_panel);

        result_text_pane = new JTextPane();
        result_text_pane.setEditable(false);
        result_text_pane.setContentType("text/html");
        result_text_pane.setText("<html>Start by creating a Qubit.<br>" +
                "The program can contain up to ten qubits,<br>" +
                "and the probability is calculated and displayed each time the circuit is updated." +
                "<br>A negative probability indicates 180 degrees on the y axis of the bloch sphere.</html>");
        result_panel.add(result_text_pane);

        JPanel checkbox_panel = new JPanel();
        result_panel.add(checkbox_panel);

        show_all_checkbox = new JCheckBox("This is a checkbox");
        checkbox_panel.add(show_all_checkbox);
    }

    // Menu setup functions

    private JMenuBar create_menu_bar() {
        JMenuBar menu_bar = new JMenuBar();
        frame.setJMenuBar(menu_bar);

        return menu_bar;
    }

    private JMenu create_menu(final String name, final JMenuBar menu_bar) {
        JMenu menu = new JMenu(name);
        menu_bar.add(menu);

        return menu;
    }

    private void add_item_new(final JMenu file_menu) {
        JMenuItem item_new = new JMenuItem(new AbstractAction("New") {
            private static final long serialVersionUID = 3699016056959009199L;

            @Override
            public void actionPerformed(ActionEvent arg0) {
                frame.getContentPane().removeAll();
                setup();
                frame.validate();
                set_current_file(null);
            }
        });
        item_new.setAccelerator(KeyStroke.getKeyStroke('N', menu_mask));
        file_menu.add(item_new);
    }


    private void add_item_quit(final JMenu file_menu) {
        JMenuItem item_quit = new JMenuItem(new AbstractAction("Quit") {
            private static final long serialVersionUID = -4441750652720636192L;

            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        item_quit.setAccelerator(KeyStroke.getKeyStroke('Q', menu_mask));
        file_menu.add(item_quit);
    }



    private void add_item_about(final JMenu help_menu) {
        JMenuItem item_about = new JMenuItem(new AbstractAction("About"){
            private static final long serialVersionUID = -8311117685045905144L;

            @Override
            public void actionPerformed(ActionEvent arg0) {
                final String text = "Made by Sebastian Zhu\n\n" +
                        "Credits - DeutschSim";

                JOptionPane.showMessageDialog(frame, text,
                        "About", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        help_menu.add(item_about);
    }

    // Helper functions
    private File save_prompt(final String title) {
        JFileChooser file_chooser = new JFileChooser() {
            private static final long serialVersionUID = 4649847794719144813L;

            @Override
            public void approveSelection() {
                File file = getSelectedFile();
                if (file.exists()) {
                    final int result = JOptionPane.showConfirmDialog(this,
                            "The file exists, overwrite?", "File exists",
                            JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION)
                        super.approveSelection();

                    return;
                }

                super.approveSelection();
            }
        };
        file_chooser.setCurrentDirectory(new File("."));
        file_chooser.setSelectedFile(new File(".dcirc"));
        file_chooser.setDialogTitle(title);

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "DeutschSim circuits", "dcirc");
        file_chooser.setFileFilter(filter);

        final int return_value = file_chooser.showSaveDialog(frame);
        if (return_value == JFileChooser.APPROVE_OPTION)
            return file_chooser.getSelectedFile();

        return null;
    }



    private void set_current_file(final File file) {
        current_file = file;
        if (file != null)
            frame.setTitle("QuantumCircuitSimulator" + file.getName());
        else
            frame.setTitle("QuantumCircuitSimulator - Untitled");
    }


    private JTextPane result_text_pane;
    private JCheckBox show_all_checkbox;


    private File current_file;

    private static final int gate_table_cell_size = 43,
            gate_table_row_height = gate_table_cell_size + 2;
    private static final double main_split_pane_resize_weight = 0.85,
            child_split_pane_resize_weight = 0.8;
    private static final int menu_mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
}
