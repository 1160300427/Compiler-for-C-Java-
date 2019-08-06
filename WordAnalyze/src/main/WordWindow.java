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
	public JButton jb_InputFile = new JButton("�����ļ�");
	public JButton jb_StartAnalyze = new JButton("�ʷ�����");
	public JButton jb_yufaAnalyze = new JButton("�﷨����");
	public JTextArea jta_test;
	public JTextArea jta_token ,jta_dfa,jta_error;
	public JScrollPane jsp_test,jsp_token,jsp_dfa,jsp_error;
	public JLabel jtf_test = new JLabel(" �� �� �� �� ");
	public JLabel jtf_token = new JLabel(" Token �� ��");
	public JLabel jtf_dfa = new JLabel(" DFA ת �� ");
	public JLabel jtf_error = new JLabel(" �� �� �� �� ");
	
	public String[][] all_fa = new String[50][30];                          //�����dfa
	public ArrayList<String> inop_fa = new ArrayList<String>();             //����dfa�õ���������ż���
	public ArrayList<String> state_fa_s = new ArrayList<String>();          //����dfa�õ���״̬���ϼ���
	public ArrayList<Integer> state_fa = new ArrayList<Integer>();          //��״̬����ת��ΪInt��ʽ
	public ArrayList<Integer> finalstate_fa = new ArrayList<Integer>();     //����״̬���ϵõ�����ֹ״̬����
	
	public ArrayList<String> test_txt = new ArrayList<String>();            //�����ı�
	public ArrayList<Token>  tokenlist = new ArrayList<Token>();            //��¼Token
	public ArrayList<ChangeDFA> chdfalist = new ArrayList<ChangeDFA>();     //��¼DFA��ת������
	public ArrayList<ErrorMes> errorlist = new ArrayList<ErrorMes>();       //��¼������Ϣ
	
	public ArrayList<Token>  tokenlist1 = new ArrayList<Token>();  
	
	//�ؼ��ּ���
	 public static String keywords[] = { "auto", "double", "int", "struct",  
		        "break", "else", "long", "switch", "case", "enum", "register",  
		        "typedef", "char", "extern", "return", "union", "const", "float",  
		        "short", "unsigned", "continue", "for", "signed", "void",  
		        "default", "goto", "sizeof", "volatile", "do", "if", "while", "then","statck",
		        "static", "main", "String","boolean","false","true","proc","record","real","call"};
	//�߽������
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
		
		window = new JFrame("�ʷ�������");
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
		jb_StartAnalyze.setEnabled(false);  //������ť���ɵ��
		jb_yufaAnalyze.setEnabled(false); 
		

		//�ô��ھ�����ʾ
		window.setDefaultCloseOperation(window.EXIT_ON_CLOSE);        
		window.setSize(1700,1500);     //(�ߣ���   
		window.setLocationRelativeTo(null);       
		window.setVisible(true);   //��ʾ
		
		
		
	}
	
	 @SuppressWarnings("deprecation")
		public void actionPerformed(ActionEvent e)
		 {
		 	if(e.getSource() == jb_InputFile)
		 	{
		 		jb_StartAnalyze.setEnabled(true);
		 		
		 		all_fa = InputFA.readFileFA();           //���FA����
		 		//System.out.println("all_fa.length = "+ all_fa.length + "all_fa[0].length = " + all_fa[0].length);
		 		state_fa_s = InputFA.GetState(all_fa, all_fa.length);     //���FA��һ�С���״̬��String��ʽ
		 		inop_fa = InputFA.GetInOps(all_fa, all_fa[0].length);     //���FA��һ�С����������
		 		state_fa = InputFA.GetInOp(state_fa_s);                   //ͨ��״̬���ϻ���ս�״̬���ϣ�����*��
		 		finalstate_fa = InputFA.GetFinalState(state_fa_s);        //��״̬������Stringת��Ϊint��ʽ��ȥ��*
		 		
		 		readTestFile();
		 	}
		 	
		 	if(e.getSource() == jb_StartAnalyze)
		 	{
		 		
		 		//���ж�ȡjta_test�����ݴ���test_txt��
		 		String txt=jta_test.getText();
		 		BufferedReader br=new BufferedReader(new StringReader(txt));
		 		String line=null;
		 		try {
					while((line=br.readLine())!=null){
						test_txt.add(line);

					}
					//��ʼ����
					Analyze(test_txt,1);  
				} catch (IOException e1) {
					// TODO �Զ����ɵ� catch ��
					e1.printStackTrace();
				}

		 		//��ȥtokenlist�е�null�������Ƿ��ַ�ʱ�õ���token
		 		 tokenlist1 = new ArrayList<Token>();   
		 		tokenlist1 = cleartokennull();
		 		
		 		//��ȥerrorlist�в���Ҫ����
		 		ArrayList<ErrorMes>  errorlist1 = new ArrayList<ErrorMes>();   
		 		errorlist1 = clearerrornull();
				//���½���
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
		// TODO �Զ����ɵķ������
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
		// TODO �Զ����ɵķ������
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
	
			jta_error.append("  "+word+" , ��"+line+"��ʶ���"+temp+"���ַ��� "+ch+" �������� "+kind+" ��\r\n");
			//System.out.println(" ( "+nowstate+" , "+ch+" ) == "+nextstate+"\r\n");
				
		    //����ҳ����ʾ��ÿһ�еĸ�
			int height = 50;
			Point p = new Point();
			p.setLocation(0, this.jta_error.getLineCount()*height);
			this.jsp_error.getViewport().setViewPosition(p);
					
			//����ҳ����ʾ������Ĵ�С��20��ʾ�ֺţ�0 ��ʾ������1����2б��֮��
			Font x = new Font("Serif",0,22);
			jta_error.setFont(x);
			
			j++;
	  	}
	}

	//��ȥtokenlist�е�null�������Ƿ��ַ�ʱ�õ���token
	 private ArrayList<Token> cleartokennull() {
		// TODO �Զ����ɵķ������
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
		// TODO �Զ����ɵķ������
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
		 int flag = 0;  //���dfaת�������ж�Ӧ״̬��Ϊ1��dfa = -1��Ϊ0���Ƿ�������Ϊ2
		 for(int i =0;i<line.length();i++)
		 {
			 ch = line_ch[i];
			 chs = String.valueOf(ch);
			 ChangeDFA changedfa = new ChangeDFA();
 
			 chkindnum =  JudgeKindByCh(ch);
			 
			 changedfa.setCh(ch);
			 //System.out.println("-1֮ǰ��ch = "+ch+" , state = "+state+" , chkindnum = "+chkindnum);
			 //System.out.println("ch = "+ch);
			 
			 /*
			  * ���ch�ǺϷ����룬��ô����dfa��dfa[state,chKind]��������һ��state
			  */
			 if(chkindnum !=-1)
			 {
				 state_fa = Integer.parseInt(all_fa[state][chkindnum]);    //������һ��״̬
				
				 changedfa.setNowState(state);
				 changedfa.setNextState(state_fa);
				 
				 if(state_fa != -1 ) //��һ��״̬����
				 {
					// System.out.println("�Ϸ���-1��ch = "+ch+" , state = "+state+" , state_fa = "+state_fa + " , chkindnum = "+chkindnum);
					 state = state_fa;     //����state
					 //System.out.println("�Ϸ�-1��ch = "+ch+" , state = "+state+" , state_fa = "+state_fa);
					 word = word +chs;
					 //System.out.println("word = "+word);
				 }
				 
				 //��һ��״̬��dfa�в�����
				 if(state_fa == -1 ) 
				 {
					 cleartokenlist();
					 DealError(j,i,ch,state,word);
					 
					 //����while(,��e״̬����)ʱdfaΪ-1
					 if(isFinalState(state) == true){       //�����ǰ״̬����Ϊ�ս�״̬���Ϸ����token 
						 //��װ�ֶΣ����token
						// System.out.println("�Ϸ�-1�����ch = "+ch+" , state = "+state+" , state_fa = "+state_fa + " , chkindnum = "+chkindnum+" , word = "+word);
						 MakeToken(state,word,j); 
						 word = "";
						 state = 1;
						 i=i-1;
						
					 }
					 else{
						 //DealError(j,i,ch,state,word);
						 //System.out.println("�Ϸ�-1����ch = "+ch+" , state = "+state+" , state_fa = "+state_fa + " , chkindnum = "+chkindnum+" , word = "+word);
						 state = 1;
						 word = "";
					 }
					 
						 
				 }
			 }
			 
			 /*
			  * ��ǰ�ַ��ǷǷ�����ʱ���ж�ǰһ�ַ�������״̬�Ƿ�����ֹ״̬��������򹹽��ֶκϷ���������򣬽��������
			  */
			 else
			 {
				 cleartokenlist();
				 
				 changedfa.setNowState(state);
				 changedfa.setNextState(1);
				 
				 DealError(j,i,ch,state,word);

				 //�ж�ǰһ��״̬�Ƿ����ս�״̬������ǣ��������װ�ֶ����
				 if(state != -1){
					 
					// System.out.println("�Ƿ������ch = "+ch+" , state = "+state+" , chkindnum = "+chkindnum+" , word = "+word);
					 MakeToken(state,word,j);
					 word = "";
					 state = 1;
				 }
				 
				 //���Ǿͽ��������
				 if(state == -1){
					 //DealError(j,i,ch,state,word);
					// System.out.println("�Ƿ�����ch = "+ch+" , state = "+state+" , chkindnum = "+chkindnum+" , word = "+word);
					 state = 1;
					 word="";
					 
				 }
					
			 }
	       //����ʱch��״̬�����Ӧlist��
			 chdfalist.add(changedfa);       //����ʱDFA��ʶ��ʽ�����Ӧlist�� 
		 } 
	     
	     }
	 
	}

	private void DealErrortest(int j, int i, char ch, int state,String word) {
		// TODO �Զ����ɵķ������
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
				 error.setKind("��ʶ��ʶ�����");
			 }
			 break;
		 case 3:case 9:                       //���ͳ���
			 if(all_fa[state][temp].equals("-1"))
			 {
				 error.setKind("ʮ�����޷�������ʶ�����");
			 }
			 break;
		 case 5:                            //�����ͳ���                     
			 if(all_fa[state][temp].equals("-1"))
			 {
				 error.setKind("������ʶ�����");
			 }
			 break;
		 case 8:                           //��ѧ����������
			 if(all_fa[state][temp].equals("-1"))
			 {
				 error.setKind("��ѧ������ʶ�����");
			 }
			 break;
		 case 11:
			 if(all_fa[state][temp].equals("-1"))
			 {
				 error.setKind("ʮ�������޷�������ʶ�����");
			 }
			 break;
		 case 12:                          //ע��
			 if(all_fa[state][temp].equals("-1"))
			 {
				 error.setKind("�˽����޷�������ʶ�����");
			 }
			 break;
		 case 13:case 17:case 18:case 19:case 20:case 21:    //13��17��18��19��20��21�� ���������   <307,value>
			 if(all_fa[state][temp].equals("-1"))
			 {
				 error.setKind("���������ʶ�����");
			 }
			 break;
		 case 16:                   //16��ע��                              <308,value>
			 if(all_fa[state][temp].equals("-1"))
			 {
				 error.setKind("ע��ʶ�����");
			 }
			 break;
		 case 24:case 30:case 33:case 36:       //24��30��33��36�����    <value,->
			 if(all_fa[state][temp].equals("-1"))
			 {
				 error.setKind("���ʶ�����");
			 }
			 break; 
		 case 25:case 27:case 29:         //25��27��29���߼������  <309,value>
			 if(all_fa[state][temp].equals("-1"))
			 {
				 error.setKind("�߼������ʶ�����");
			 }
			 break;
		 case 32:                   //32���ַ�������                     <310,value>
			 if(all_fa[state][temp].equals("-1"))
			 {
				 error.setKind("�ַ�������ʶ�����");
			 }
			 break;
		 case 35:                   //35���ַ�����                          <311,value>
			 if(all_fa[state][temp].equals("-1"))
			 {
				 error.setKind("�ַ�����ʶ�����");
			 }
			 break;
		 case 39:                   //39������                                  <312,value>
			 if(all_fa[state][temp].equals("-1"))
			 {
				 error.setKind("����ʶ�����");
			 }
			 break;
		 case 22:case 23:                   //22��23����ϵ�����              <313,value>
			 if(all_fa[state][temp].equals("-1"))
			 {
				 error.setKind("��ϵ�����ʶ�����");
			 }
			 break;
		 default:
			 break;
		}
	}

	private void DealError(int j, int i, char ch, int state,String word) {
		// TODO �Զ����ɵķ������
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
			 error.setKind("������ʶ��ʧ��");
			 break;
		 case 6:case 7:
			 error.setKind("��ѧ������ʶ��ʧ��");
			 break;
		 case 10:
			 error.setKind("ʮ�������޷�������ʧ��");
			 break;
		 case 14:case 15:
			 error.setKind("ע��ʶ��ʧ��");
			 break;
		 case 26:case 28:
			 error.setKind("�߼������ʶ��ʧ��");
			 break;
		 case 31:
			 error.setKind("�ַ�������ʶ��ʧ��");
			 break;
		 case 34:
			 error.setKind("�ַ�����ʶ��ʧ��");
			 break;
		 case 37:case 38:
			 error.setKind("����ʶ��ʧ��");			 
			 break;
			
		 default:
			 break;	 
			 
		 }
		 
		 
		 String s1 = word.substring(0);
		 String s2 = word.substring(len);
		 if(s1.equals("\"") && !s2.equals("\""))
			 error.setKind("�ַ�������ʶ��ʧ��");
		 if(s1.equals("'") && !s2.equals("'"))
			 error.setKind("�ַ�����ʶ��ʧ��");
		 if(s1.equals("/") && !s2.equals("/"))
			 error.setKind("ע��ʶ��ʧ��");
		 if(word.equals("+-") || word.equals("-+"))
			 error.setKind("���������ʶ��ʧ��");
		 
		 errorlist.add(error);
	}

	private void cleartokenlist() {
		// TODO �Զ����ɵķ������
		for(int i=0;i<tokenlist.size();i++)
		{
			if(tokenlist.get(i).getName().equals(" "))
			{
				tokenlist.remove(i);
			}
		}
	}

	//��ʾDFAת��
	 private void ShowGUI2(ArrayList<ChangeDFA> list) {
		// TODO �Զ����ɵķ������
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
					
			    //����ҳ����ʾ��ÿһ�еĸ�
				int height = 50;
				Point p = new Point();
				p.setLocation(0, this.jta_dfa.getLineCount()*height);
				this.jsp_dfa.getViewport().setViewPosition(p);
						
				//����ҳ����ʾ������Ĵ�С��20��ʾ�ֺţ�0 ��ʾ������1����2б��֮��
				Font x = new Font("Serif",0,22);
				jta_dfa.setFont(x);
				
				j++;
		  	}
	}

	 //��ʾToken
	private void ShowGUI1(ArrayList<Token> list) {
		// TODO �Զ����ɵķ������
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
				
		    //����ҳ����ʾ��ÿһ�еĸ�
			int height = 50;
			Point p = new Point();
			p.setLocation(0, this.jta_token.getLineCount()*height);
			this.jsp_token.getViewport().setViewPosition(p);
					
			//����ҳ����ʾ������Ĵ�С��20��ʾ�ֺţ�0 ��ʾ������1����2б��֮��
			Font x = new Font("Serif",0,22);
			jta_token.setFont(x);
	  	}
	}

	/*
	  * ����tokenlist
	  * 2����ʶ��                    	 <300,value>      �ؼ��� <value,->
		3��9: ʮ�����޷�������        <301,value>
		5��������                             <302,value>
		8����ѧ������                      <303,value>
		11��ʮ�������޷�������     <304,value>
		12���˽����޷�������        <305,value>
		13��17��18��19��20��21�� ���������   <307,value>
		16��ע��                              <308,value>
		24��30��33��36�����    <value,->
		25��27��29���߼������  <309,value>
		32���ַ�������                     <310,value>
		35���ַ�����                          <311,value>
		39������                                  <312,value>
		22��23����ϵ�����              <313,value>

	  * 
	  */
	 private void MakeToken(int state, String word,int j) {
		// TODO �Զ����ɵķ������
		 Token token = new Token();
		 token.setName(word);
		 token.setLine(j+1);
		 //System.out.println("token.state = "+state+" , token.word = "+word);
		 
		 switch(state) {
		 case 2:
			 if(isKeyWord(word) == true){   //�ж��Ƿ��ǹؼ���
				 token.setKind(word);
				 token.setValue("-");
				 token.setOutKind("�ؼ���");
				 
			 }
			 else{
				 token.setKind("id");     
				 token.setValue(word);
				 token.setOutKind("��ʶ��");
				 
			 }
			 break;
		 case 3:case 9:                       //���ͳ���
			 token.setKind("digit");
			 token.setValue(word);
			 token.setOutKind("ʮ�����޷�������");
			 break;
		 case 5:                            //�����ͳ���                     
			 token.setKind("digit");
			 token.setValue(word);
			 token.setOutKind("������");
			 break;
		 case 8:                           //��ѧ����������
			 token.setKind("digit");
			 token.setValue(word);
			 token.setOutKind("��ѧ������");
			 break;
		 case 11:
			 token.setKind("digit");
			 token.setValue(word);
			 token.setOutKind("ʮ�������޷�������");
			 break;
		 case 12:                          
			 token.setKind("digit");
			 token.setValue(word);
			 token.setOutKind("�˽����޷�������");
			 break;
		 case 13:case 17:case 18:case 19:case 20:case 21:    //13��17��18��19��20��21�� ���������   <307,value>
			 token.setKind(word);
			 token.setValue("-");
			 token.setOutKind("���������");
			 break;
//		 case 16:                   //16��ע��                              <308,value>
//			 token.setKind("308");
//			 token.setValue(word);
//			 token.setOutKind("ע��");
//			 break;
		 case 24:case 30:case 33:case 36:       //24��30��33��36�����    <value,->
			 token.setKind(word);
			 token.setValue("-");
			 token.setOutKind("���");
			 break; 
		 case 25:
			 token.setKind("not");
			 token.setValue("-");
			 token.setOutKind("�߼������");
			 break;
		 case 27:
			 token.setKind("and");
			 token.setValue("-");
			 token.setOutKind("�߼������");
			 break;
		 case 29:
			 token.setKind("or");
			 token.setValue("-");
			 token.setOutKind("�߼������");
			 break;
//		 case 32:                   //32���ַ�������                     <310,value>
//			 token.setKind("310");
//			 token.setValue(word);
//			 token.setOutKind("�ַ�������");
//			 break;
//		 case 35:                   //35���ַ�����                          <311,value>
//			 token.setKind("311");
//			 token.setValue(word);
//			 token.setOutKind("�ַ�����");
//			 break;
//		 case 39:                   //39������                                  <312,value>
//			 token.setKind("312");
//			 token.setValue(word);
//			 token.setOutKind("����");
//			 break;
		 case 22:case 23:                   //22��23����ϵ�����              <313,value>
			 token.setKind(word);
			 token.setValue("-");
			 token.setOutKind("��ϵ�����");
			 break;
		 default:
			 break;	 
			 
		 }
		 tokenlist.add(token);
		
	}

	private boolean isKeyWord(String word) {
		// TODO �Զ����ɵķ������
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


	//�жϵ�ǰ�ַ��Ƿ����ս��
	 private boolean isFinalState(int state) {
		// TODO �Զ����ɵķ������
		 
		 for( int i = 0 ; i < finalstate_fa.size() ; i++) {
			 if(state == finalstate_fa.get(i))
	  		  {
	  			  return true;
	  		  }
	  	}
		return false;
	}

	 
	//�ж�����һ�����͵�����
	private int JudgeKindByCh(char ch) {
		// TODO �Զ����ɵķ������
		
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

	//�ж�����ַ��Ƿ�����ĳ������
	private boolean isOp(String[] col, String word) {
		// TODO �Զ����ɵķ������
		
		if(word.equals(",") || word.equals(";") || word.equals("(") || word.equals(")") || word.equals("{") ||
				word.equals("}") || word.equals("[") || word.equals("]") || word.equals("\"") || word.equals("'"))
			return true;
		else
			return false;
	}

	public  void readTestFile() {
	        String pathname = "test1.txt"; // ����·�������·�������ԣ�
	        //��ֹ�ļ��������ȡʧ�ܣ���catch��׽���󲢴�ӡ��Ҳ����throw;
	        //���ر��ļ��ᵼ����Դ��й¶����д�ļ���ͬ��
	 
	        try (FileReader reader = new FileReader(pathname);
	             BufferedReader br = new BufferedReader(reader) // ����һ�����������ļ�����ת�ɼ�����ܶ���������
	        ) {
	            String line;
	           
	            while ((line = br.readLine()) != null) {
	                // һ�ζ���һ������
	            	//����������ʾ��ҳ����
	            	jta_test.append(line + "\n");
					
	            	//����ҳ����ʾ��ÿһ�еĸ�
					int height = 50;
					Point p = new Point();
					p.setLocation(0, this.jta_test.getLineCount()*height);
					this.jsp_test.getViewport().setViewPosition(p);
					
					//����ҳ����ʾ������Ĵ�С��20��ʾ�ֺţ�0 ��ʾ������1����2б��֮��
					Font x = new Font("Serif",0,20);
					jta_test.setFont(x);
					
					
	               // System.out.println(line);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }


}
