package app;

import ch.swingfx.twinkle.NotificationBuilder;
import ch.swingfx.twinkle.style.INotificationStyle;
import ch.swingfx.twinkle.style.theme.DarkDefaultNotification;
import ch.swingfx.twinkle.window.Positions;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		
		btnOk.addActionListener(e -> {
            parseHMS(textFieldHours.getText(),
                    textFieldMinutes.getText(),
                    textFieldSeconds.getText());

            HoursMinutesSecondsTimerAddingDial.this.dispose();
        });
		
		btnCancel.addActionListener(e -> HoursMinutesSecondsTimerAddingDial.this.dispose());
	}

	private void parseHMS(String h, String m, String s){
		Pattern pattern = Pattern.compile("[0-9]+");
		long hours = -1;
		long minutes = -1;
		long seconds = -1;
		String parseLog = "";

		Matcher matcher = pattern.matcher(h);
		if(matcher.matches()){
			hours = Long.parseLong(h)*3600000;
		}else{
			hours = 0;
			parseLog += "Problem przy parsowaniu godzin! Ustawiam na zero!\n";
		}

		matcher = pattern.matcher(m);
		if(matcher.matches()){
			minutes = Long.parseLong(m)*60000;
		}else{
			minutes = 0;
			parseLog += "Problem przy parsowaniu minut! Ustawiam na zero!\n";
		}

		matcher = pattern.matcher(s);
		if(matcher.matches()){
			seconds = Long.parseLong(s)*1000;
		}else{
			seconds = 0;
			parseLog += "Problem przy parsowaniu sekund! Ustawiam na zero!\n";
		}
		
		counter++;
		TimersMainFrame.timerList.addTimer(new Timer(hours + minutes + seconds,
				txtDescription.getText().replaceAll("\\s", "").equals("") ? "[" + counter + "]" : txtDescription.getText()));
		if(!parseLog.equals("")){
			new NotificationBuilder()
			.withStyle(style)
			.withTitle("Blad!")
			.withMessage(parseLog)
			.withIcon("images/icon.png")
			.withDisplayTime(5000)
			.withPosition(Positions.SOUTH_EAST)
			.showNotification();
		}
	}
	
	private class MyFocusListener extends FocusAdapter {
		@Override
		public void focusGained(FocusEvent arg0) {
			if(arg0.getComponent() instanceof JTextField)
				((JTextField)arg0.getSource()).selectAll();
		}
	}
	
	private class MyKeyListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				btnOk.doClick();
			}else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
				HoursMinutesSecondsTimerAddingDial.this.dispose();
			}
		}
	}
}
