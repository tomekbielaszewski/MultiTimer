package app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import ch.swingfx.twinkle.NotificationBuilder;
import ch.swingfx.twinkle.style.INotificationStyle;
import ch.swingfx.twinkle.style.theme.DarkDefaultNotification;
import ch.swingfx.twinkle.window.Positions;

public class HoursMinutesSecondsTimerAddingDial extends JDialog {
	private static final long serialVersionUID = 7138252394781923014L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtDescription;
	private JButton btnOk;
	private JButton btnCancel;
	private JTextField textFieldHours;
	private JTextField textFieldMinutes;
	private JTextField textFieldSeconds;
	private INotificationStyle style = new DarkDefaultNotification()
											.withWidth(250)
											.withAlpha(0.7f)
											.withTitleFontColor(Color.red)
											.withMessageFontColor(new Color(250, 100, 100));
	
	private static int counter;
	//private static LinkedList<Pattern> patterns; TODO: Potrzebne do tego nizej

	/**
	 * Create the dialog.
	 */
	public HoursMinutesSecondsTimerAddingDial(JFrame owner) {
		super(owner);
		setBounds(100, 100, 174, 215);
		setTitle("Add timer");
		setLocationRelativeTo(owner);
		setIconImage(new ImageIcon("images/icon.png").getImage());
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblMinutes = new JLabel("Minutes");
		lblMinutes.setBounds(20, 73, 56, 20);
		contentPanel.add(lblMinutes);
		
		JLabel lblHours = new JLabel("Hours");
		lblHours.setBounds(20, 42, 56, 20);
		contentPanel.add(lblHours);
		
		JLabel lblSeconds = new JLabel("Seconds");
		lblSeconds.setBounds(20, 104, 56, 20);
		contentPanel.add(lblSeconds);
		
		btnOk = new JButton("OK");
		btnOk.setBounds(10, 135, 52, 32);
		contentPanel.add(btnOk);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(66, 135, 82, 32);
		contentPanel.add(btnCancel);
		
		txtDescription = new JTextField();
		txtDescription.setBounds(10, 11, 138, 20);
		contentPanel.add(txtDescription);
		
		textFieldHours = new JTextField("0");
		textFieldHours.setBounds(86, 42, 62, 20);
		contentPanel.add(textFieldHours);
		textFieldHours.setColumns(10);
		
		textFieldMinutes = new JTextField("0");
		textFieldMinutes.setBounds(86, 73, 62, 20);
		contentPanel.add(textFieldMinutes);
		textFieldMinutes.setColumns(10);
		
		textFieldSeconds = new JTextField("0");
		textFieldSeconds.setBounds(86, 104, 62, 20);
		contentPanel.add(textFieldSeconds);
		textFieldSeconds.setColumns(10);
		
		setUpComponents();
		//initPatterns(); TODO potrzebne do tego nizej
	}
	
	private void setUpComponents(){
		MyFocusListener myFocusListener = new MyFocusListener();
		MyKeyListener myKeyListener = new MyKeyListener();
		txtDescription.addFocusListener(myFocusListener);
		txtDescription.addKeyListener(myKeyListener);
		
		textFieldHours.addFocusListener(myFocusListener);
		textFieldHours.addKeyListener(myKeyListener);
		
		textFieldMinutes.addFocusListener(myFocusListener);
		textFieldMinutes.addKeyListener(myKeyListener);
		
		textFieldSeconds.addFocusListener(myFocusListener);
		textFieldSeconds.addKeyListener(myKeyListener);
		
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parseHMS(textFieldHours.getText(),
						textFieldMinutes.getText(),
						textFieldSeconds.getText());

				HoursMinutesSecondsTimerAddingDial.this.dispose();
			}
		});
		
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HoursMinutesSecondsTimerAddingDial.this.dispose();
			}
		});
	}
	
	/**
	 * Parsuje zawartosc textFieldow i dodaje wytworzony timer
	 */
	private void parseHMS(String h, String m, String s){
		Pattern pattern = Pattern.compile("[0-9]+");
		long hours = -1;
		long minutes = -1;
		long seconds = -1;
		String parseLog = "";

		Matcher matcher = pattern.matcher(textFieldHours.getText());
		if(matcher.matches()){
			hours = Long.parseLong(textFieldHours.getText())*3600000;
		}else{
			hours = 0;
			parseLog += "Problem przy parsowaniu godzin! Ustawiam na zero!\n";
		}

		matcher = pattern.matcher(textFieldMinutes.getText());
		if(matcher.matches()){
			minutes = Long.parseLong(textFieldMinutes.getText())*60000;
		}else{
			minutes = 0;
			parseLog += "Problem przy parsowaniu minut! Ustawiam na zero!\n";
		}

		matcher = pattern.matcher(textFieldSeconds.getText());
		if(matcher.matches()){
			seconds = Long.parseLong(textFieldSeconds.getText())*1000;
		}else{
			seconds = 0;
			parseLog += "Problem przy parsowaniu sekund! Ustawiam na zero!\n";
		}
		
		counter++;
		TimersMainFrame.timerList.addTimer(new Timer(hours + minutes + seconds, 
											txtDescription.getText().replaceAll("\\s", "").equals("") ? "["+counter+"]" : txtDescription.getText()));
		if(!parseLog.equals("")){
			new NotificationBuilder()
			.withStyle(style) // Required. here we set the previously set style
			.withTitle("Blad!") // Required.
			.withMessage(parseLog) // Optional
			.withIcon("images/icon.png") // Optional. You could also use a String path
			.withDisplayTime(5000) // Optional
			.withPosition(Positions.SOUTH_EAST) // Optional. Show it at the center of the screen
			.showNotification(); // this returns a UUID that you can use to identify events on the listener
		}
	}
	
	private class MyFocusListener implements FocusListener{
		@Override
		public void focusGained(FocusEvent arg0) {
			if(arg0.getComponent() instanceof JTextField)
				((JTextField)arg0.getSource()).selectAll();
		}
		
		public void focusLost(FocusEvent arg0) {}
	}
	
	private class MyKeyListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				btnOk.doClick();
			}else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
				HoursMinutesSecondsTimerAddingDial.this.dispose();
			}
		}

		public void keyReleased(KeyEvent arg0) {}
		public void keyTyped(KeyEvent arg0) {}
	}
}
