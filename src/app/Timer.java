package app;


public class Timer {
	private long maxTime;
	private long elapsedTime;
	private String ID;
	
	public Timer(long maxTime, String ID){
		this.maxTime = maxTime;
		this.ID = ID;
	}
	
	public void update(long elapsedTime){
		if(!isFinished())
			this.elapsedTime += elapsedTime;
		else
			this.elapsedTime = maxTime;
	}
	
	public boolean isFinished(){
		return elapsedTime >= maxTime;
	}
	
	public long getTimeLeft(){
		return maxTime - elapsedTime;
	}
	
	public String getID(){
		return ID;
	}
	
	public String toString(){
		long sek = (getTimeLeft()/1000)%60;
		long min = (getTimeLeft()/60000)%60;
		long godz = getTimeLeft()/3600000;
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		appendNumber(sb, godz);
		sb.append(":");
		appendNumber(sb, min);
		sb.append(":");
		appendNumber(sb, sek);
		sb.append("] "+ID);
		return sb.toString();
	}
	
	private void appendNumber(StringBuilder sb, long num){
		if(num < 10)
			sb.append("0"+num);
		else
			sb.append(num);
	}
}
