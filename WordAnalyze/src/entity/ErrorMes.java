package entity;

public class ErrorMes {
	public int line;
	public int temp;
	public char ch;
	public String kind;
	public String word;
	
	//获得正在处理的行号
	public void setLine(int line)
	{
		this.line = line;
	}
	public int getLine()
	{
		return line;
	}
	
	//获得正在处理的字符号
	public void setTemp(int temp)
	{
		this.temp = temp;
	}
	public int getTemp()
	{
		return temp;
	}
		
	//获得此时识别的字符
	public void setCh(char ch)
	{
		this.ch = ch;
	}
	public char getCh()
	{
		return ch;
	}
	
	public void setKind(String kind)
	{
		this.kind = kind;
	}
	public String getKind()
	{
		return kind;
	}
	
	public void setWord(String word)
	{
		this.word = word;
	}
	public String getWord()
	{
		return word;
	}


	

}
