package app;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

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
	public static JButton rightMenuButt = new JButton("");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TimersMainFrame frame = new TimersMainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
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
		addTimerButt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				HoursMinutesSecondsTimerAddingDial dialog = new HoursMinutesSecondsTimerAddingDial(TimersMainFrame.this);
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			}
		});
		contentPane.add(addTimerButt);

		defaultTimerButton = new JButton("10 Minutes");
		defaultTimerButton.setBounds(142, 337, 92, 32);
		defaultTimerButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				timerList.addTimer(new Timer(10 * 1000 * 60, "Started " + LocalDateTime.now().toLocalTime()));
			}
		});
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
		
		finishedTimersJList = new JList<String>();
		finishedTimersJList.setBackground(Color.WHITE);
		finishedTimersJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		finishedTimersJList.setFont(new Font("Consolas", Font.PLAIN, 11));
		scrollPane_1.setViewportView(finishedTimersJList);
		finishedTimersJList.setSelectionBackground(Color.white);
		finishedTimersJList.setSelectionForeground(Color.black);
		finishedTimersJList.addMouseListener(new MouseListener() {
			public void mousePressed(MouseEvent arg0) {
				if (finishedTimersJList.getSelectedIndex() == -1) {
	            	//Nothing
	            } else {
	            	timerList.remove(finishedTimersJList.getSelectedIndex());
	            }
			}
			
			public void mouseReleased(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseClicked(MouseEvent arg0) {}
		});
		
		lblFinishedTimers = new JLabel("Finished timers");
		lblFinishedTimers.setBounds(314, 0, 150, 37);
		contentPane.add(lblFinishedTimers);

		clearDeadTimersButt = new JButton("Clear finished timers");
		clearDeadTimersButt.setBounds(242, 337, 132, 32);
		clearDeadTimersButt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				timerList.clearFinishedTimers();
			}
		});
		contentPane.add(clearDeadTimersButt);

		clearRunningTimersButt = new JButton("Clear all");
		clearRunningTimersButt.setBounds(384, 337, 80, 32);
		clearRunningTimersButt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				timerList.clearNotFinishedTimers();
				timerList.clearFinishedTimers();
			}
		});
		contentPane.add(clearRunningTimersButt);
		
//		rightMenuButt.setBounds(473, 0, 21, 378);
//		//rightMenuButt
//		contentPane.add(rightMenuButt);
		
		t.start();
	}
}
