package app;

import java.awt.AWTException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;

import ch.swingfx.twinkle.NotificationBuilder;
import ch.swingfx.twinkle.style.INotificationStyle;
import ch.swingfx.twinkle.style.theme.DarkDefaultNotification;
import ch.swingfx.twinkle.window.Positions;

public class TimerList implements Runnable{
	private LinkedList<Timer> timerList;
	private ArrayList<Timer> finishedTimersList;
	private ArrayList<Date> timeOfFinish;
	
	private INotificationStyle style = new DarkDefaultNotification()
										.withWidth(250)
										.withAlpha(0.7f)
										.withWindowCornerRadius(20);
	
	private enum Order{
		EXPANDING, DECREASING
	}
	
	private String[] emptyStringArray = new String[1];
	
	public TimerList() throws AWTException {
		System.setProperty("swing.aatext", "true");
		
		timerList = new LinkedList<Timer>();
		finishedTimersList = new ArrayList<Timer>();
		timeOfFinish = new ArrayList<Date>();
		
		emptyStringArray[0] = "";
	}
	
	public synchronized String getTimersState(){
		StringBuilder sb = new StringBuilder();
		for(Timer t : timerList){
			sb.append(t.toString()+"\n");
		}
		return sb.toString();
	}
	
	public synchronized String[] getFinishedTimersID(){
		String[] s = new String[finishedTimersList.size()];
		for(int i=0; i<s.length; i++){
			s[i] = new String(finishedTimersList.get(i).getID()+" | "+DateFormat.getTimeInstance().format(timeOfFinish.get(i)));
		}
		return s;
	}
	
	public synchronized void addTimer(Timer t){
		timerList.add(t);
		Collections.sort(timerList, new TimerComparator(Order.EXPANDING));
	}
	
	public synchronized void remove(int index){
		finishedTimersList.remove(index);
		timeOfFinish.remove(index);
	}

	public synchronized void clearFinishedTimers(){
		finishedTimersList.clear();
		timeOfFinish.clear();
	}

	public synchronized void clearNotFinishedTimers(){
		timerList.clear();
	}
	
	@Override
	public void run() {
		while(true){
			long start = System.currentTimeMillis();
			timerList.removeAll(finishedTimersList);
			try { Thread.sleep(100); } catch (InterruptedException e) {}
			long stop = System.currentTimeMillis();
			for(Timer t : timerList){
				t.update(stop-start);
				if(t.isFinished()){
					finishedTimersList.add(t);
					timeOfFinish.add(new Date());
					new AePlayWave("sounds/bip.wav").start();
					new NotificationBuilder()
						.withStyle(style) // Required. here we set the previously set style
						.withDisplayTime(36000000)
						.withTitle("Timer finished!") // Required.
						.withMessage(t.getID()) // Optional
						.withIcon("images/icon.png") // Optional. You could also use a String path
						.withPosition(Positions.SOUTH_EAST) // Optional. Show it at the center of the screen
						.showNotification(); // this returns a UUID that you can use to identify events on the listener
				}
			}

			TimersMainFrame.timersTextArea.setText(getTimersState());
			TimersMainFrame.finishedTimersJList.setListData(getFinishedTimersID() == null ? emptyStringArray : getFinishedTimersID());
		}
	}
	
	private class TimerComparator implements Comparator<Timer>{
		Order order;
		
		public TimerComparator(Order order){
			this.order = order;
		}
		
		@Override
		public int compare(Timer t1, Timer t2) {
			if(t1.getTimeLeft() > t2.getTimeLeft()) return order == Order.EXPANDING ? 1 : -1;
			else if(t1.getTimeLeft() == t2.getTimeLeft()) return 0;
			else return order == Order.DECREASING ? 1 : -1;
		}
		
	}
}
