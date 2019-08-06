package entity;

public class ChangeDFA {
	public int nowstate;
	public int nextstate;
	public char ch;
	
	public void setNowState(int nowstate)
	{
		this.nowstate = nowstate;
	}
	public int getNowState()
	{
		return nowstate;
	}
	
	public void setNextState(int nextstate)
	{
		this.nextstate = nextstate;
	}
	public int getNextState()
	{
		return nextstate;
	}
	
	public void setCh(char ch)
	{
		this.ch = ch;
	}
	public char getCh()
	{
		return ch;
	}

}
