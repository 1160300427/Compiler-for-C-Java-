package main;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import entity.*;

public class WordWindow extends JFrame implements ActionListener {

	private JPanel contentPane;
	public JFrame window;
	public JButton jb_InputFile = new JButton("读入文件");
	public JButton jb_StartAnalyze = new JButton("词法分析");
	public JButton jb_yufaAnalyze = new JButton("语法分析");
	public JTextArea jta_test;
	public JTextArea jta_token ,jta_dfa,jta_error;
	public JScrollPane jsp_test,jsp_token,jsp_dfa,jsp_error;
	public JLabel jtf_test = new JLabel(" 测 试 程 序 ");
	public JLabel jtf_token = new JLabel(" Token 分 析");
	public JLabel jtf_dfa = new JLabel(" DFA 转 化 ");
	public JLabel jtf_error = new JLabel(" 错 误 分 析 ");
	
	public String[][] all_fa = new String[50][30];                          //读入的dfa
	public ArrayList<String> inop_fa = new ArrayList<String>();             //根据dfa得到的输入符号集合
	public ArrayList<String> state_fa_s = new ArrayList<String>();          //根据dfa得到的状态集合集合
	public ArrayList<Integer> state_fa = new ArrayList<Integer>();          //将状态集合转化为Int形式
	public ArrayList<Integer> finalstate_fa = new ArrayList<Integer>();     //根据状态集合得到的终止状态集合
	
	public ArrayList<String> test_txt = new ArrayList<String>();            //读入文本
	public ArrayList<Token>  tokenlist = new ArrayList<Token>();            //记录Token
	public ArrayList<ChangeDFA> chdfalist = new ArrayList<ChangeDFA>();     //记录DFA的转换过程
	public ArrayList<ErrorMes> errorlist = new ArrayList<ErrorMes>();       //记录错误信息
	
	public ArrayList<Token>  tokenlist1 = new ArrayList<Token>();  
	
	//关键字集合
	 public static String keywords[] = { "auto", "double", "int", "struct",  
		        "break", "else", "long", "switch", "case", "enum", "register",  
		        "typedef", "char", "extern", "return", "union", "const", "float",  
		        "short", "unsigned", "continue", "for", "signed", "void",  
		        "default", "goto", "sizeof", "volatile", "do", "if", "while", "then","statck",
		        "static", "main", "String","boolean","false","true","proc","record","real","call"};
	//边界符集合
	public static String boundary[] = { ",", ";", "(", ")", "{", "}","\"","'","[","]"};


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		new WordWindow();
	}

	/**
	 * Create the frame.
	 */
	@SuppressWarnings("unused")
	public WordWindow() {
		
		window = new JFrame("词法分析器");
		jta_test = new JTextArea(30,50);
		jsp_test = new JScrollPane(jta_test);
		jta_token =  new JTextArea(100,60);
		jsp_token = new JScrollPane(jta_token);
		jta_dfa =  new JTextArea(100,40);
		jsp_dfa = new JScrollPane(jta_dfa);
		jta_error =  new JTextArea(30,80);
		jsp_error = new JScrollPane(jta_error);
		
		window.getContentPane().add(jsp_test);
		jsp_test.setBounds(100,150,500,400);
		window.getContentPane().add(jsp_token);
		jsp_token.setBounds(750,150,450,600);
		window.getContentPane().add(jsp_dfa);
		jsp_dfa.setBounds(1250,150,300,600);
		window.getContentPane().add(jsp_error);
		jsp_error.setBounds(100,600,600,300);
		window.setLayout(null);
		
		window.getContentPane().add(jb_InputFile);
		jb_InputFile.setBounds(300,30,200,50);
		window.getContentPane().add(jb_StartAnalyze);
		jb_StartAnalyze.setBounds(600,30,200,50);
		window.getContentPane().add(jb_yufaAnalyze);
		jb_yufaAnalyze.setBounds(900,30,200,50);
		
		window.getContentPane().add(jtf_test);
		jtf_test.setBounds(100,90,150,50);
		window.getContentPane().add(jtf_token);
		jtf_token.setBounds(750,90,150,50);
		window.getContentPane().add(jtf_dfa);
		jtf_dfa.setBounds(1250,90,150,50);
		window.getContentPane().add(jtf_error);
		jtf_error.setBounds(100,550,150,50);
		
		jb_InputFile.addActionListener(this);
		jb_StartAnalyze.addActionListener(this);
		jb_yufaAnalyze.addActionListener(this);
		jb_StartAnalyze.setEnabled(false);  //结束按钮不可点击
		jb_yufaAnalyze.setEnabled(false); 
		

		//让窗口居中显示
		window.setDefaultCloseOperation(window.EXIT_ON_CLOSE);        
		window.setSize(1700,1500);     //(高，宽）   
		window.setLocationRelativeTo(null);       
		window.setVisible(true);   //显示
		
		
		
	}
	
	 @SuppressWarnings("deprecation")
		public void actionPerformed(ActionEvent e)
		 {
		 	if(e.getSource() == jb_InputFile)
		 	{
		 		jb_StartAnalyze.setEnabled(true);
		 		
		 		all_fa = InputFA.readFileFA();           //获得FA矩阵
		 		//System.out.println("all_fa.length = "+ all_fa.length + "all_fa[0].length = " + all_fa[0].length);
		 		state_fa_s = InputFA.GetState(all_fa, all_fa.length);     //获得FA第一列——状态，String形式
		 		inop_fa = InputFA.GetInOps(all_fa, all_fa[0].length);     //获得FA第一行——输入符号
		 		state_fa = InputFA.GetInOp(state_fa_s);                   //通过状态集合获得终结状态集合，包含*的
		 		finalstate_fa = InputFA.GetFinalState(state_fa_s);        //将状态集合由String转化为int形式，去除*
		 		
		 		readTestFile();
		 	}
		 	
		 	if(e.getSource() == jb_StartAnalyze)
		 	{
		 		
		 		//按行读取jta_test的内容存入test_txt中
		 		String txt=jta_test.getText();
		 		BufferedReader br=new BufferedReader(new StringReader(txt));
		 		String line=null;
		 		try {
					while((line=br.readLine())!=null){
						test_txt.add(line);

					}
					//开始分析
					Analyze(test_txt,1);  
				} catch (IOException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}

		 		//除去tokenlist中的null项，即输入非法字符时得到的token
		 		 tokenlist1 = new ArrayList<Token>();   
		 		tokenlist1 = cleartokennull();
		 		
		 		//除去errorlist中不需要的项
		 		ArrayList<ErrorMes>  errorlist1 = new ArrayList<ErrorMes>();   
		 		errorlist1 = clearerrornull();
				//更新界面
				ShowGUI1(tokenlist1);
				ShowGUI2(chdfalist);
				ShowGUI3(errorlist1);
				
				jb_yufaAnalyze.setEnabled(true); 
		 		
		 	}
		 	
		 	if(e.getSource() == jb_yufaAnalyze){
		 		new YufaAnalyze(tokenlist1);
		 	}
		 	
		 }
		 
	private ArrayList<ErrorMes> clearerrornull() {
		// TODO 自动生成的方法存根
		ArrayList<ErrorMes>  errorlist1 = new ArrayList<ErrorMes>(); 
		 for(int i =0;i<errorlist.size();i++)
		 {
			 if(errorlist.get(i).getKind() != null){
				 errorlist1.add(errorlist.get(i));
			 }
				 
		 }
		return errorlist1;
	}

	private void ShowGUI3(ArrayList<ErrorMes> errorlist1) {
		// TODO 自动生成的方法存根
		int line;
		int temp;
		char ch;
		String kind;
		String word;
		
		int flag;
		int j=0;
		for( int i = 0 ; i < errorlist1.size() ; i++) {
			 
			line = errorlist1.get(i).getLine();
			temp = errorlist1.get(i).getTemp();
			ch = errorlist1.get(i).getCh();
			kind = errorlist1.get(i).getKind();
			word = errorlist1.get(i).getWord();
	
			jta_error.append("  "+word+" , 第"+line+"行识别第"+temp+"个字符【 "+ch+" 】发生【 "+kind+" 】\r\n");
			//System.out.println(" ( "+nowstate+" , "+ch+" ) == "+nextstate+"\r\n");
				
		    //设置页面显示中每一行的高
			int height = 50;
			Point p = new Point();
			p.setLocation(0, this.jta_error.getLineCount()*height);
			this.jsp_error.getViewport().setViewPosition(p);
					
			//设置页面显示中字体的大小，20表示字号，0 表示字形如1粗体2斜体之类
			Font x = new Font("Serif",0,22);
			jta_error.setFont(x);
			
			j++;
	  	}
	}

	//除去tokenlist中的null项，即输入非法字符时得到的token
	 private ArrayList<Token> cleartokennull() {
		// TODO 自动生成的方法存根
		 ArrayList<Token>  tokenlist1 = new ArrayList<Token>();  
		 for(int i =0;i<tokenlist.size();i++)
		 {
			 if(tokenlist.get(i).getKind() != null && tokenlist.get(i).getValue() != null){
				 tokenlist1.add(tokenlist.get(i));
			 }
				 
		 }
		return tokenlist1;
	}

	private void Analyze(ArrayList<String> test_txt2,int state){ 
		// TODO 自动生成的方法存根
		 String word = new String();
	     for(int j=0;j<test_txt2.size();j++)
	     {
	     String	 line = test_txt2.get(j);
		 char[] line_ch = line.toCharArray();
		 String chKind;
		 int chkindnum;
		 char ch;
		 //String chs = String.valueOf(line_ch[0]);
		 String chs;
		 
		 
		 int state_fa;
		 int flag = 0;  //如果dfa转换表中有对应状态则为1，dfa = -1则为0，非法输入则为2
		 for(int i =0;i<line.length();i++)
		 {
			 ch = line_ch[i];
			 chs = String.valueOf(ch);
			 ChangeDFA changedfa = new ChangeDFA();
 
			 chkindnum =  JudgeKindByCh(ch);
			 
			 changedfa.setCh(ch);
			 //System.out.println("-1之前：ch = "+ch+" , state = "+state+" , chkindnum = "+chkindnum);
			 //System.out.println("ch = "+ch);
			 
			 /*
			  * 如果ch是合法输入，那么查找dfa表dfa[state,chKind]，计算下一个state
			  */
			 if(chkindnum !=-1)
			 {
				 state_fa = Integer.parseInt(all_fa[state][chkindnum]);    //查找下一个状态
				
				 changedfa.setNowState(state);
				 changedfa.setNextState(state_fa);
				 
				 if(state_fa != -1 ) //下一个状态存在
				 {
					// System.out.println("合法非-1：ch = "+ch+" , state = "+state+" , state_fa = "+state_fa + " , chkindnum = "+chkindnum);
					 state = state_fa;     //更新state
					 //System.out.println("合法-1：ch = "+ch+" , state = "+state+" , state_fa = "+state_fa);
					 word = word +chs;
					 //System.out.println("word = "+word);
				 }
				 
				 //下一个状态在dfa中不存在
				 if(state_fa == -1 ) 
				 {
					 cleartokenlist();
					 DealError(j,i,ch,state,word);
					 
					 //例如while(,在e状态读入)时dfa为-1
					 if(isFinalState(state) == true){       //如果当前状态正好为终结状态，合法输出token 
						 //组装字段，输出token
						// System.out.println("合法-1输出：ch = "+ch+" , state = "+state+" , state_fa = "+state_fa + " , chkindnum = "+chkindnum+" , word = "+word);
						 MakeToken(state,word,j); 
						 word = "";
						 state = 1;
						 i=i-1;
						
					 }
					 else{
						 //DealError(j,i,ch,state,word);
						 //System.out.println("合法-1错误：ch = "+ch+" , state = "+state+" , state_fa = "+state_fa + " , chkindnum = "+chkindnum+" , word = "+word);
						 state = 1;
						 word = "";
					 }
					 
						 
				 }
			 }
			 
			 /*
			  * 当前字符是非法输入时，判断前一字符的最终状态是否是终止状态，如果是则构建字段合法输出，否则，进入错误处理
			  */
			 else
			 {
				 cleartokenlist();
				 
				 changedfa.setNowState(state);
				 changedfa.setNextState(1);
				 
				 DealError(j,i,ch,state,word);

				 //判断前一个状态是否是终结状态，如果是，则可以组装字段输出
				 if(state != -1){
					 
					// System.out.println("非法输出：ch = "+ch+" , state = "+state+" , chkindnum = "+chkindnum+" , word = "+word);
					 MakeToken(state,word,j);
					 word = "";
					 state = 1;
				 }
				 
				 //不是就进入错误处理
				 if(state == -1){
					 //DealError(j,i,ch,state,word);
					// System.out.println("非法错误：ch = "+ch+" , state = "+state+" , chkindnum = "+chkindnum+" , word = "+word);
					 state = 1;
					 word="";
					 
				 }
					
			 }
	       //将此时ch的状态加入对应list中
			 chdfalist.add(changedfa);       //将此时DFA的识别方式加入对应list中 
		 } 
	     
	     }
	 
	}

	private void DealErrortest(int j, int i, char ch, int state,String word) {
		// TODO 自动生成的方法存根
		 ErrorMes error = new ErrorMes();
		 word = word +ch;
		 error.setLine(j);
		 error.setTemp(i);
		 error.setCh(ch);
		 error.setWord(word);
		 int len = word.length();
		 int temp = JudgeKindByCh(ch);
		 String chs = String.valueOf(ch);
		 int flag = -1;
		switch(state) {
		 case 2:
			 if(all_fa[state][temp].equals("-1"))
			 {
				 error.setKind("标识符识别错误");
			 }
			 break;
		 case 3:case 9:                       //整型常量
			 if(all_fa[state][temp].equals("-1"))
			 {
				 error.setKind("十进制无符号整型识别错误");
			 }
			 break;
		 case 5:                            //浮点型常量                     
			 if(all_fa[state][temp].equals("-1"))
			 {
				 error.setKind("浮点数识别错误");
			 }
			 break;
		 case 8:                           //科学计数法常量
			 if(all_fa[state][temp].equals("-1"))
			 {
				 error.setKind("科学计数法识别错误");
			 }
			 break;
		 case 11:
			 if(all_fa[state][temp].equals("-1"))
			 {
				 error.setKind("十六进制无符号整数识别错误");
			 }
			 break;
		 case 12:                          //注释
			 if(all_fa[state][temp].equals("-1"))
			 {
				 error.setKind("八进制无符号整数识别错误");
			 }
			 break;
		 case 13:case 17:case 18:case 19:case 20:case 21:    //13、17、18、19、20、21： 算数运算符   <307,value>
			 if(all_fa[state][temp].equals("-1"))
			 {
				 error.setKind("算数运算符识别错误");
			 }
			 break;
		 case 16:                   //16：注释                              <308,value>
			 if(all_fa[state][temp].equals("-1"))
			 {
				 error.setKind("注释识别错误");
			 }
			 break;
		 case 24:case 30:case 33:case 36:       //24、30、33、36：界符    <value,->
			 if(all_fa[state][temp].equals("-1"))
			 {
				 error.setKind("界符识别错误");
			 }
			 break; 
		 case 25:case 27:case 29:         //25、27、29：逻辑运算符  <309,value>
			 if(all_fa[state][temp].equals("-1"))
			 {
				 error.setKind("逻辑运算符识别错误");
			 }
			 break;
		 case 32:                   //32：字符串常量                     <310,value>
			 if(all_fa[state][temp].equals("-1"))
			 {
				 error.setKind("字符串常量识别错误");
			 }
			 break;
		 case 35:                   //35：字符常量                          <311,value>
			 if(all_fa[state][temp].equals("-1"))
			 {
				 error.setKind("字符常量识别错误");
			 }
			 break;
		 case 39:                   //39：数组                                  <312,value>
			 if(all_fa[state][temp].equals("-1"))
			 {
				 error.setKind("数组识别错误");
			 }
			 break;
		 case 22:case 23:                   //22、23：关系运算符              <313,value>
			 if(all_fa[state][temp].equals("-1"))
			 {
				 error.setKind("关系运算符识别错误");
			 }
			 break;
		 default:
			 break;
		}
	}

	private void DealError(int j, int i, char ch, int state,String word) {
		// TODO 自动生成的方法存根
		 ErrorMes error = new ErrorMes();
		 word = word +ch;
		 error.setLine(j);
		 error.setTemp(i);
		 error.setCh(ch);
		 error.setWord(word);
		 int len = word.length();
		 int temp = JudgeKindByCh(ch);
		 String chs = String.valueOf(ch);
		 int flag = -1;
		 
		// System.out.println("error.state = "+state);
		 
		 switch(state)
		 {
		 case 4:
			 error.setKind("浮点数识别失败");
			 break;
		 case 6:case 7:
			 error.setKind("科学计数法识别失败");
			 break;
		 case 10:
			 error.setKind("十六进制无符号整型失败");
			 break;
		 case 14:case 15:
			 error.setKind("注释识别失败");
			 break;
		 case 26:case 28:
			 error.setKind("逻辑运算符识别失败");
			 break;
		 case 31:
			 error.setKind("字符串常量识别失败");
			 break;
		 case 34:
			 error.setKind("字符常量识别失败");
			 break;
		 case 37:case 38:
			 error.setKind("数组识别失败");			 
			 break;
			
		 default:
			 break;	 
			 
		 }
		 
		 
		 String s1 = word.substring(0);
		 String s2 = word.substring(len);
		 if(s1.equals("\"") && !s2.equals("\""))
			 error.setKind("字符串常量识别失败");
		 if(s1.equals("'") && !s2.equals("'"))
			 error.setKind("字符常量识别失败");
		 if(s1.equals("/") && !s2.equals("/"))
			 error.setKind("注释识别失败");
		 if(word.equals("+-") || word.equals("-+"))
			 error.setKind("算数运算符识别失败");
		 
		 errorlist.add(error);
	}

	private void cleartokenlist() {
		// TODO 自动生成的方法存根
		for(int i=0;i<tokenlist.size();i++)
		{
			if(tokenlist.get(i).getName().equals(" "))
			{
				tokenlist.remove(i);
			}
		}
	}

	//显示DFA转化
	 private void ShowGUI2(ArrayList<ChangeDFA> list) {
		// TODO 自动生成的方法存根
		    int nowstate;
			int nextstate;
			char ch;
			int flag;
			int j=0;
			for( int i = 0 ; i < list.size() ; i++) {
				 
				nowstate = list.get(i).getNowState();
				nextstate = list.get(i).getNextState();
				ch = list.get(i).getCh();
			
				jta_dfa.append(" (    "+nowstate+"   ,   "+ch+"    )    =   "+nextstate+"\r\n");
				//System.out.println(" ( "+nowstate+" , "+ch+" ) == "+nextstate+"\r\n");
					
			    //设置页面显示中每一行的高
				int height = 50;
				Point p = new Point();
				p.setLocation(0, this.jta_dfa.getLineCount()*height);
				this.jsp_dfa.getViewport().setViewPosition(p);
						
				//设置页面显示中字体的大小，20表示字号，0 表示字形如1粗体2斜体之类
				Font x = new Font("Serif",0,22);
				jta_dfa.setFont(x);
				
				j++;
		  	}
	}

	 //显示Token
	private void ShowGUI1(ArrayList<Token> list) {
		// TODO 自动生成的方法存根
		String name;
		String kind;
		String value;
		String outkind;
		for( int i = 0 ; i < list.size() ; i++) {
			 
			name = list.get(i).getName();
			kind = list.get(i).getKind();
			value = list.get(i).getValue();
			outkind = list.get(i).getOutKind();
				
			jta_token.append("   "+name+"\t"+"< "+kind+" , "+value+" >" +"\t"+outkind+"\r\n");
			//System.out.println("tokenlist : i = "+i+"\t"+name+"\t"+"< "+kind+" , "+value+" >\r\n");
				
		    //设置页面显示中每一行的高
			int height = 50;
			Point p = new Point();
			p.setLocation(0, this.jta_token.getLineCount()*height);
			this.jsp_token.getViewport().setViewPosition(p);
					
			//设置页面显示中字体的大小，20表示字号，0 表示字形如1粗体2斜体之类
			Font x = new Font("Serif",0,22);
			jta_token.setFont(x);
	  	}
	}

	/*
	  * 构建tokenlist
	  * 2：标识符                    	 <300,value>      关键字 <value,->
		3、9: 十进制无符号整型        <301,value>
		5：浮点数                             <302,value>
		8：科学计数法                      <303,value>
		11：十六进制无符号整型     <304,value>
		12：八进制无符号整型        <305,value>
		13、17、18、19、20、21： 算数运算符   <307,value>
		16：注释                              <308,value>
		24、30、33、36：界符    <value,->
		25、27、29：逻辑运算符  <309,value>
		32：字符串常量                     <310,value>
		35：字符常量                          <311,value>
		39：数组                                  <312,value>
		22、23：关系运算符              <313,value>

	  * 
	  */
	 private void MakeToken(int state, String word,int j) {
		// TODO 自动生成的方法存根
		 Token token = new Token();
		 token.setName(word);
		 token.setLine(j+1);
		 //System.out.println("token.state = "+state+" , token.word = "+word);
		 
		 switch(state) {
		 case 2:
			 if(isKeyWord(word) == true){   //判断是否是关键字
				 token.setKind(word);
				 token.setValue("-");
				 token.setOutKind("关键字");
				 
			 }
			 else{
				 token.setKind("id");     
				 token.setValue(word);
				 token.setOutKind("标识符");
				 
			 }
			 break;
		 case 3:case 9:                       //整型常量
			 token.setKind("digit");
			 token.setValue(word);
			 token.setOutKind("十进制无符号整型");
			 break;
		 case 5:                            //浮点型常量                     
			 token.setKind("digit");
			 token.setValue(word);
			 token.setOutKind("浮点数");
			 break;
		 case 8:                           //科学计数法常量
			 token.setKind("digit");
			 token.setValue(word);
			 token.setOutKind("科学计数法");
			 break;
		 case 11:
			 token.setKind("digit");
			 token.setValue(word);
			 token.setOutKind("十六进制无符号整数");
			 break;
		 case 12:                          
			 token.setKind("digit");
			 token.setValue(word);
			 token.setOutKind("八进制无符号整数");
			 break;
		 case 13:case 17:case 18:case 19:case 20:case 21:    //13、17、18、19、20、21： 算数运算符   <307,value>
			 token.setKind(word);
			 token.setValue("-");
			 token.setOutKind("算数运算符");
			 break;
//		 case 16:                   //16：注释                              <308,value>
//			 token.setKind("308");
//			 token.setValue(word);
//			 token.setOutKind("注释");
//			 break;
		 case 24:case 30:case 33:case 36:       //24、30、33、36：界符    <value,->
			 token.setKind(word);
			 token.setValue("-");
			 token.setOutKind("界符");
			 break; 
		 case 25:
			 token.setKind("not");
			 token.setValue("-");
			 token.setOutKind("逻辑运算符");
			 break;
		 case 27:
			 token.setKind("and");
			 token.setValue("-");
			 token.setOutKind("逻辑运算符");
			 break;
		 case 29:
			 token.setKind("or");
			 token.setValue("-");
			 token.setOutKind("逻辑运算符");
			 break;
//		 case 32:                   //32：字符串常量                     <310,value>
//			 token.setKind("310");
//			 token.setValue(word);
//			 token.setOutKind("字符串常量");
//			 break;
//		 case 35:                   //35：字符常量                          <311,value>
//			 token.setKind("311");
//			 token.setValue(word);
//			 token.setOutKind("字符常量");
//			 break;
//		 case 39:                   //39：数组                                  <312,value>
//			 token.setKind("312");
//			 token.setValue(word);
//			 token.setOutKind("数组");
//			 break;
		 case 22:case 23:                   //22、23：关系运算符              <313,value>
			 token.setKind(word);
			 token.setValue("-");
			 token.setOutKind("关系运算符");
			 break;
		 default:
			 break;	 
			 
		 }
		 tokenlist.add(token);
		
	}

	private boolean isKeyWord(String word) {
		// TODO 自动生成的方法存根
		int len = keywords.length;
		for(int i=0;i<len;i++)
		{
			if(word.equals(keywords[i]))
			{
				//System.out.println("word = " +word+ " , keyword[i] +"+keywords[i]);
				return true;
			}
		}
		return false;
	}


	//判断当前字符是否是终结符
	 private boolean isFinalState(int state) {
		// TODO 自动生成的方法存根
		 
		 for( int i = 0 ; i < finalstate_fa.size() ; i++) {
			 if(state == finalstate_fa.get(i))
	  		  {
	  			  return true;
	  		  }
	  	}
		return false;
	}

	 
	//判断是哪一种类型的输入
	private int JudgeKindByCh(char ch) {
		// TODO 自动生成的方法存根
		
		int temp = -1;
		for(int i = 5;i<inop_fa.size();i++)
		{
			
			if(String.valueOf(ch).equals(inop_fa.get(i)))
			{
				temp = i+1;
				break;
			}
		}
		
		//a-f
		if((ch > 'f' && ch < 'x') || (ch > 'x' && ch <= 'z') || (ch >= 'A' && ch < 'E' && ch > 'E' && ch <= 'Z') || ch == '_' )
			return 1;
		if(ch >= '1' && ch <= '7')
			return 2;
		if(ch == '8' || ch == '9')
			return 3;
		if(isOp(boundary,String.valueOf(ch)) == true)
			return 4;
		if(ch >= 'a' && ch <='f')
			return 5;
		return temp;	
	}

	//判断这个字符是否属于某个集合
	private boolean isOp(String[] col, String word) {
		// TODO 自动生成的方法存根
		
		if(word.equals(",") || word.equals(";") || word.equals("(") || word.equals(")") || word.equals("{") ||
				word.equals("}") || word.equals("[") || word.equals("]") || word.equals("\"") || word.equals("'"))
			return true;
		else
			return false;
	}

	public  void readTestFile() {
	        String pathname = "test1.txt"; // 绝对路径或相对路径都可以，
	        //防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw;
	        //不关闭文件会导致资源的泄露，读写文件都同理
	 
	        try (FileReader reader = new FileReader(pathname);
	             BufferedReader br = new BufferedReader(reader) // 建立一个对象，它把文件内容转成计算机能读懂的语言
	        ) {
	            String line;
	           
	            while ((line = br.readLine()) != null) {
	                // 一次读入一行数据
	            	//读入内容显示到页面上
	            	jta_test.append(line + "\n");
					
	            	//设置页面显示中每一行的高
					int height = 50;
					Point p = new Point();
					p.setLocation(0, this.jta_test.getLineCount()*height);
					this.jsp_test.getViewport().setViewPosition(p);
					
					//设置页面显示中字体的大小，20表示字号，0 表示字形如1粗体2斜体之类
					Font x = new Font("Serif",0,20);
					jta_test.setFont(x);
					
					
	               // System.out.println(line);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }


}
