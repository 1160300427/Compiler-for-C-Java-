package entity;

public class ErrorMes {
	public int line;
	public int temp;
	public char ch;
	public String kind;
	public String word;
	
	//������ڴ�����к�
	public void setLine(int line)
	{
		this.line = line;
	}
	public int getLine()
	{
		return line;
	}
	
	//������ڴ�����ַ���
	public void setTemp(int temp)
	{
		this.temp = temp;
	}
	public int getTemp()
	{
		return temp;
	}
		
	//��ô�ʱʶ����ַ�
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
