package app;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;

public class TimersMainFrame extends JFrame {
    private static final long serialVersionUID = -7173377426870992392L;
    private JPanel contentPane;
    public static JButton addTimerButt;
    public static JButton defaultTimerButton;

    public static TimerList timerList;
    public static JTextArea timersTextArea;
    public static JLabel lblActiveTimers;
    public static JScrollPane scrollPane_1;
    public static JLabel lblFinishedTimers;
    public static JButton clearDeadTimersButt;
    public static JButton clearRunningTimersButt;
    public static JList<String> finishedTimersJList;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        EventQueue.invokeLater(() -> {
            try {
                TimersMainFrame frame = new TimersMainFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public TimersMainFrame() {
        setResizable(false);
        try {
            timerList = new TimerList();
        } catch (AWTException e) {
            System.out.println("System tray is not supported on this machine!");
            e.printStackTrace();
        }
        Thread t = new Thread(timerList);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 480, 427);
        setTitle("MultiTimer by Grizz");
        setIconImage(new ImageIcon("images/icon.png").getImage());

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu toolsMenu = new JMenu("Tools");
        menuBar.add(toolsMenu);

        JMenuItem settingsMenuItem = new JMenuItem("Settings");
        toolsMenu.add(settingsMenuItem);

        JSeparator separator = new JSeparator();
        toolsMenu.add(separator);

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        toolsMenu.add(exitMenuItem);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        addTimerButt = new JButton("Add timer");
        addTimerButt.setBounds(10, 337, 122, 32);
        addTimerButt.addActionListener(arg0 -> {
            HoursMinutesSecondsTimerAddingDial dialog = new HoursMinutesSecondsTimerAddingDial(TimersMainFrame.this);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        });
        contentPane.add(addTimerButt);

        defaultTimerButton = new JButton("10 Minutes");
        defaultTimerButton.setBounds(142, 337, 92, 32);
        defaultTimerButton.addActionListener(arg0 -> timerList.addTimer(new Timer(10 * 1000 * 60, "Started " + LocalDateTime.now().toLocalTime())));
        contentPane.add(defaultTimerButton);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(new LineBorder(new Color(0, 0, 0)));
        scrollPane.setBounds(10, 36, 222, 290);
        contentPane.add(scrollPane);

        timersTextArea = new JTextArea();
        timersTextArea.setFont(new Font("Consolas", Font.PLAIN, 11));
        timersTextArea.setEditable(false);
        scrollPane.setViewportView(timersTextArea);

        lblActiveTimers = new JLabel("Active timers");
        lblActiveTimers.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblActiveTimers.setBounds(81, 0, 151, 37);
        contentPane.add(lblActiveTimers);

        scrollPane_1 = new JScrollPane();
        scrollPane_1.setBorder(new LineBorder(Color.BLACK));
        scrollPane_1.setBounds(242, 36, 222, 290);
        contentPane.add(scrollPane_1);

        finishedTimersJList = new JList<>();
        finishedTimersJList.setBackground(Color.WHITE);
        finishedTimersJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        finishedTimersJList.setFont(new Font("Consolas", Font.PLAIN, 11));
        scrollPane_1.setViewportView(finishedTimersJList);
        finishedTimersJList.setSelectionBackground(Color.white);
        finishedTimersJList.setSelectionForeground(Color.black);
        finishedTimersJList.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent arg0) {
                if (finishedTimersJList.getSelectedIndex() != -1) {
                    timerList.remove(finishedTimersJList.getSelectedIndex());
                }
            }
        });

        lblFinishedTimers = new JLabel("Finished timers");
        lblFinishedTimers.setBounds(314, 0, 150, 37);
        contentPane.add(lblFinishedTimers);

        clearDeadTimersButt = new JButton("Clear finished timers");
        clearDeadTimersButt.setBounds(242, 337, 132, 32);
        clearDeadTimersButt.addActionListener(arg0 -> timerList.clearFinishedTimers());
        contentPane.add(clearDeadTimersButt);

        clearRunningTimersButt = new JButton("Clear all");
        clearRunningTimersButt.setBounds(384, 337, 80, 32);
        clearRunningTimersButt.addActionListener(arg0 -> {
            timerList.clearNotFinishedTimers();
            timerList.clearFinishedTimers();
        });
        contentPane.add(clearRunningTimersButt);

        t.start();
    }
}
