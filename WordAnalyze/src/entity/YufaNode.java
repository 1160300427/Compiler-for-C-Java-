package entity;

import java.util.ArrayList;

public class YufaNode {
	public String node;
	public String nodevalue;
	public ArrayList<YufaNode> chirdlist = new ArrayList<YufaNode>();
	public int line;
	public String type;
	public int width;
	public String addr;
	public String btrue;
	public String code;
	public String bfalse;
	public String snext;
	
	public void setNode(String node)
	{
		this.node = node;
	}
	public String getNode()
	{
		return node;
	}
	
	public void setVaule(String nodevalue)
	{
		this.nodevalue = nodevalue;
	}
	public String getValue()
	{
		return nodevalue;
	}
	
	public void setChild(ArrayList<YufaNode> chirdlist)
	{
		this.chirdlist = chirdlist;
	}
	public ArrayList<YufaNode> getChild()
	{
		return chirdlist;
	}
	
	public void setLine(int line)
	{
		this.line = line;
	}
	public int getLine()
	{
		return line;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}
	public String getType()
	{
		return type;
	}
	
	public void setWidth(int width)
	{
		this.width = width;
	}
	public int getWidth()
	{
		return width;
	}
	
	public void setAddr(String addr)
	{
		this.addr = addr;
	}
	public String getAddr()
	{
		return addr;
	}
	
	public void setbTrue(String btrue)
	{
		this.btrue = btrue;
	}
	public String getbTrue()
	{
		return btrue;
	}
	
	public void setbFalse(String bfalse)
	{
		this.bfalse = bfalse;
	}
	public String getbFalse()
	{
		return bfalse;
	}
	
	public void setsNext(String snext)
	{
		this.snext = snext;
	}
	public String getsNext()
	{
		return snext;
	}
}
