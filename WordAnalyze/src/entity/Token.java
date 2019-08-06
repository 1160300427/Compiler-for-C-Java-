package entity;

public class Token {
	
	public String name;
	public String kind;
	public String value;
	public String outkind;
	public int line;

	public void setName(String name)
	{
		this.name = name;
	}
	public String getName()
	{
		return name;
	}
	
	public void setKind(String kind)
	{
		this.kind = kind;
	}
	public String getKind()
	{
		return kind;
	}
	
	public void setValue(String value)
	{
		this.value = value;
	}
	public String getValue()
	{
		return value;
	}
	
	public void setOutKind(String outkind)
	{
		this.outkind = outkind;
	}
	public String getOutKind()
	{
		return outkind;
	}
	
	public void setLine(int line)
	{
		this.line = line;
	}
	public int getLine()
	{
		return line;
	}

}
