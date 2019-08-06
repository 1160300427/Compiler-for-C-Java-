package main;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import entity.*;

public class YufaAnalyze extends JFrame implements ActionListener {

	public static ArrayList<Token>  tokenlist = new ArrayList<Token>();  
	public JTextArea jta_test,jta_tree;
	public JScrollPane jsp_test,jsp_tree;
	public JButton jb_yufa = new JButton("语法分析");
	public JButton jb_yuyi = new JButton("语义分析");
	public JLabel jl_test = new JLabel("程序");
	public JLabel jl_tree = new JLabel("语法错误");
	public JPanel contentPane;
	public JFrame window;
	public JTree tree;
	public JScrollPane jsp_tree1;

	DefaultMutableTreeNode top = new DefaultMutableTreeNode("Program");
	
	public String[][] all_ll = new String[50][30];                          //读入的LL1转化表
	public ArrayList<String> finallist = new ArrayList<String>();             //根据LL1转化表得到的输入符号集合：终结符
	public ArrayList<String> notfinallist = new ArrayList<String>();          //根据LL1转化表得到的非终结符
	public ArrayList<YufaNode> treelist = new ArrayList<YufaNode>();    //输出的树的集合
	public ArrayList<String> senlist = new ArrayList<String>();     //输出的句子集合
	public ArrayList<String> errorlist = new ArrayList<String>(); 
	
	public ArrayList<YufaNode> treesdtlist = new ArrayList<YufaNode>();  //输出的sdt树
	
	public String[][] sdt = new String[59][3]; 
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		new YufaAnalyze(tokenlist);
	}

	/**
	 * Create the frame.
	 */
	public YufaAnalyze(ArrayList<Token> tokenlist) {
		this.tokenlist = tokenlist;
		Token token = new Token();
		token.setName(";");
		token.setKind(";");
		token.setValue("-");
		token.setLine(tokenlist.size()-1);
		tokenlist.add(token);
		
		window = new JFrame("语法分析器");
		//让窗口居中显示
		window.setDefaultCloseOperation(window.EXIT_ON_CLOSE);        
		window.setSize(1300,1000);     //(宽，高）   
		window.setLocationRelativeTo(null);       
		window.setVisible(true);   //显示
		
		jta_test = new JTextArea(50,80);
		jsp_test = new JScrollPane(jta_test);
		jta_tree = new JTextArea(50,100);
		jsp_tree = new JScrollPane(jta_tree);
		
		tree = new JTree(top);
		jsp_tree1 = new JScrollPane(tree);
		
//		window.getContentPane().add(jsp_test);
//		jsp_test.setBounds(50,150,500,700);
		window.getContentPane().add(jsp_tree);
		jsp_tree.setBounds(600,150,600,700);
		window.setLayout(null);
		
		window.getContentPane().add(jl_test);
		jl_test.setBounds(100,90,150,50);
		window.getContentPane().add(jl_tree);
		jl_tree.setBounds(700,90,150,50);
		
		window.getContentPane().add(jb_yufa);
		jb_yufa.setBounds(300,30,200,50);
		window.getContentPane().add(jb_yuyi);
		jb_yuyi.setBounds(600,30,200,50);
		
		jb_yufa.addActionListener(this);
		jb_yuyi.addActionListener(this);
		jb_yuyi.setEnabled(false);
		
		window.getContentPane().add(jsp_tree1);
		jsp_tree1.setBounds(50,150,500,700);
		
	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自动生成的方法存根
		if(e.getSource() == jb_yufa){
			all_ll = InputFA.readFileLL();
			notfinallist = InputFA.GetState(all_ll, all_ll.length); 
			finallist = InputFA.GetInOps(all_ll, all_ll[0].length);
			sdt = InputFA.readFileSDT();
			
			YufaAnalyze();
			jb_yuyi.setEnabled(true);
		}
		if(e.getSource() == jb_yuyi){
			new YuYiAnalyze(treelist);
		}
	}

	public void YufaAnalyze() {
		// TODO 自动生成的方法存根
		Stack<String> st = new Stack<String>();           //语法分析时使用的普通栈
		Stack<String> st_sdt = new Stack<String>();       //语义分析时获得的sdt栈
		String sdttop;
		int i=0;    //识别的token
		String sentence = "";  //识别的语句
		String space = "  ";
		String tree_str = "";
		int line,row;  //识别的行，列
		String line_str,row_str;
		String name = "";   //从tokenlist中得到的当前需要识别的单词
		String kind = "";
		String value = "";
		int wordline;
		
		//初始化栈
		st.push("$");
		line_str = st.push(notfinallist.get(0));
		st_sdt.push("$");
		sdttop = st_sdt.push(notfinallist.get(0));
		
		String change;   //all_ll[A,a]
		String change_sdt;   //更新后带有语义动作的值
		int treeline = 1;
		String treevalue;
	
		DefaultMutableTreeNode nownode = top;
		
		Token token = new Token();
		token.setName("$");
		token.setKind("$");
		token.setValue("-");
		token.setLine(tokenlist.get(tokenlist.size()-1).getLine());
		tokenlist.add(token);
		while(!st.peek().equals("$") && i<tokenlist.size()){
			name = tokenlist.get(i).getName();
			kind = tokenlist.get(i).getKind();
			value = tokenlist.get(i).getValue();
			wordline = tokenlist.get(i).getLine();
			
			row_str = kind;
			line = getLine(line_str);
			row = getRow(row_str);
			
			System.out.println("【看输入：】i = "+i+",非终结符 = "+line_str+",终结符 = "+row_str);
			//System.out.println("kind = "+kind+",line_str = "+line_str+",line = "+line+",row = "+row);

			/*栈顶元素如果是终结符，小写————栈顶弹出
			 * 合法匹配————栈顶与输入符号相匹配，输入集合向下移动一位
			 * 构建合法语句
			 */
			if(Character.isUpperCase(line_str.charAt(0)) == false){
				//如果栈顶终结符与输入符号匹配，则输出终结符,并且输入集合向下移动一位
				if(line_str.equals(kind)){
					sentence = sentence + name+" ";
					System.out.println("【栈顶终结符合法：】i = "+i+",栈顶符号 = "+line_str+",识别的语句 = "+sentence);
					i++;
					st.pop();
					line_str = st.peek();
					
					st_sdt.pop();
					sdttop = st_sdt.peek();
					
					YufaNode node1 = new YufaNode();
					node1.setNode(sdttop);
					node1.setLine(treeline);
					treesdtlist.add(node1);
					
					//获得树节点
					if(value.equals("-") == false)
					{
					YufaNode node = new YufaNode();
					node.setNode(kind);
					treevalue = kind+" : "+value+" ("+wordline+")";
					node.setVaule(value);
					//System.out.println("终结符value = "+node.getValue());
					node.setLine(wordline);
					treelist.add(node);
					
					}
					else{
						YufaNode node = new YufaNode();
						node.setNode(kind);
						treevalue = kind+" ("+wordline+")";
						node.setVaule(kind);
						System.out.println("- value = "+node.getValue());
						node.setLine(wordline);
						treelist.add(node);
					}
					
				}
				//如果栈顶终结符与输入符号不匹配，弹出栈顶终结符
				else{
					System.out.println("【栈顶终结符不合法：】i = "+i+",栈顶符号 = "+line_str+",识别的语句 = "+sentence);
					st.pop();
					line_str = st.peek();
					
					st_sdt.pop();
					sdttop = st_sdt.peek();
					
					errorlist.add("Error at【 "+wordline+" 】 : 识别到【 "+name+" 】时错误");
				}
			}
			
			/*
			 * 栈顶元素非终结符————大写
			 * 非终结符合法，终结符合法————观察all_ll[line][row]
			 * 非终结符非法————弹出栈顶
			 * 终结符非法————输入符号向下移动一位
			 */
			else{
				//合法输入，all_ll[line][row]可查
				if(line != -1 && row != -1){
					//System.out.println("合法输入 ：("+line+","+row+")");
					change = all_ll[line][row];
					//System.out.println("all_ll = "+change);
					
			
					//当前识别到的LL1分析表为空，忽视当前输入，输入集合向下移动一位
					if(change.equals("-1"))	
					{
						System.out.println("【LL1分析表为空：】i = "+i+",非终结符 = "+line_str+",终结符 = "+row_str);
						i++;
						errorlist.add("Error at【 "+wordline+" 】 : 识别到【 "+name+" 】时错误");
					}
					
					//识别到synch,弹出栈顶
					else if(change.equals("synch")){
						System.out.println("【synch：】i = "+i+",非终结符 = "+line_str+",终结符 = "+row_str);
						String synch_str = st.pop();
						line_str = st.peek();
						
						st_sdt.pop();
						sdttop = st_sdt.peek();
						
						YufaNode node = new YufaNode();
						node.setNode("synch");
						space = getTreeSpace(wordline); 
						treevalue ="synch" + " ("+wordline+")";
						node.setVaule("synch");
						System.out.println("synchvalue = "+node.getValue());
						node.setLine(wordline);
						treelist.add(node);
						
						
						YufaNode node1 = new YufaNode();
						node1.setNode(sdttop);
						node1.setLine(wordline);
						treesdtlist.add(node1);

						errorlist.add("synch:Error at【 "+wordline+" 】 : 识别到【 "+name+" 】时错误");
					}
					
					//识别到有效值
					else{
						//将有效值使用->分割成左部、右部
						String s[]=change.split("->");
						String ch_left = s[0];
						String ch_right = s[1];
						String sr[] = ch_right.split(" ");//右部使用" "分割成单个的项
						
						//获得带有语义动作的sdt产生式
						change_sdt = getSDTbyChange(change);
						System.out.println("change_sdt = "+change_sdt+" , change = "+change);
						String s1[]=change_sdt.split("->");
						String ch1_left = s1[0];
						String ch1_right = s1[1];
						String sr1[] = ch1_right.split(" ");//右部使用" "分割成单个的项
						
						//没有sdt的语法分析树--语法部分
						YufaNode node = new YufaNode();
						ArrayList<YufaNode> chirdlist = new ArrayList<YufaNode>();
						node.setNode(line_str);
						treevalue = line_str + " ("+wordline+")";
						node.setVaule(line_str);
						//System.out.println("有效值value = "+node.getValue());
						for(int j=0;j<sr1.length;j++){//右部按顺序进栈
							if(sr1[j].equals("ε") == false){
								YufaNode childnode = new YufaNode();
								childnode.setNode(sr1[j]);
								space = getTreeSpace(treeline); 
								treevalue =sr1[j] + " ("+wordline+")";
								childnode.setVaule(sr1[j]);
								childnode.setLine(wordline);
								chirdlist.add(childnode);
							}
								
						}
						node.setChild(chirdlist);
						node.setLine(wordline);
						treelist.add(node);
						treeline++;

						String pop_str = st.pop();//左部出栈
						for(int j=sr.length-1;j>=0;j--){//右部按顺序进栈
							if(sr[j].equals("ε") == false)
								line_str = st.push(sr[j]);
							//System.out.println("sr,length = "+sr.length+",sr[] = "+sr[j]);
						}
						line_str = st.peek();
						
						
						//与sdt有关的语法分析树--语义部分
						YufaNode node1 = new YufaNode();
						ArrayList<YufaNode> chirdlist1 = new ArrayList<YufaNode>();
						node1.setNode(sdttop);
						node1.setLine(treeline);
						for(int j=sr1.length-1;j>=0;j--){//右部按顺序进栈
							if(sr1[j].equals("ε") == false){
								YufaNode childnode = new YufaNode();
								childnode.setNode(sr1[j]);
								childnode.setVaule(sr1[j]);
								childnode.setLine(treeline+1);
								chirdlist1.add(childnode);
							}
								
						}
						node.setChild(chirdlist1);
						treesdtlist.add(node1);
						
						st_sdt.pop();//左部出栈
						for(int j=sr1.length-1;j>=0;j--){//右部按顺序进栈
							if(sr1[j].equals("ε") == false){
								sdttop = st_sdt.push(sr1[j]);
							}
								
							//System.out.println("sr,length = "+sr.length+",sr[] = "+sr[j]);
						}
		
						sdttop = st_sdt.peek();
						
						
						//与JTree有关的节点更新
						DefaultMutableTreeNode childtreenode = null;
						for(int j=0;j<sr.length;j++){	
							childtreenode =  new DefaultMutableTreeNode(sr[j]);
							nownode.add(childtreenode);
							System.out.println("父节点 = "+nownode.toString()+",sr1[] = "+sr1[j]+"change_sdt = "+change_sdt);
						}
						if(sr.length != 0 )
							nownode = (DefaultMutableTreeNode) nownode.getChildAt(0);
						
						String nownode_str = sr[0];
						if(nownode.equals("ε") || Character.isUpperCase(nownode_str.charAt(0)) == false)
						{
						while(true){
							if(nownode.isRoot() == true){
								break;
							}
							
							//获取当年父节点的子节点集合总数
							int parentChildCount = nownode.getParent().getChildCount();
							int nowIndex = 0;//当前父节点子节点集合中的下标
							for(nowIndex = 0;nowIndex<parentChildCount;nowIndex++){
								if(nownode.getParent().getChildAt(nowIndex) == nownode){//找到当前节点的下标值
									break;
								}
							}
							
							if(nowIndex == parentChildCount - 1){
									nownode = (DefaultMutableTreeNode) nownode.getParent();
									nownode_str = nownode.toString();
								System.out.println("父节点1 = "+nownode.toString());
							}
							else{
								for(int k = nowIndex+1;k<parentChildCount;k++){	
									nownode_str = nownode.toString();
									if(Character.isUpperCase(nownode.getParent().getChildAt(k).toString().charAt(0)) == true){	
										nownode = (DefaultMutableTreeNode)nownode.getParent().getChildAt(k);
										System.out.println("父节点2 = "+nownode.toString());
										break;
									}
									System.out.println("父节点22 = "+nownode.toString());	
								}
								
								break;
							}
						}
					}
						
						System.out.println("【有效值：】i = "+i+",栈顶符号 = "+line_str+",识别的单词 = "+kind);	
						
					}
					
				}
				
				//非终结符非法
				else if(line == -1){
					st.pop();
					line_str = st.peek();
					
					st_sdt.pop();
					sdttop = st_sdt.peek();
					
				}
				
				//终结符非法
				else if(row == -1){
					i++;
					
				}
			}
			
			//如果栈为空值，当前识别结束，句子为空
			if(st.empty() == true){
				senlist.add(sentence);
				sentence = "";
			}
			
			System.out.println("nownode = "+nownode.toString());
			System.out.println("stack : "+st);
			System.out.println("SDT-stack : "+st_sdt);
			System.out.println("************************1次循环******************** = "+i);
		}
		
		printTree();
		ShowGUI();
	}

	//获得对应产生式的sdt表达式
	public String getSDTbyChange(String change) {
		// TODO 自动生成的方法存根
		String ret="";
		for(int i=0;i<59;i++){
			if(sdt[i][0].equals(change)){
				ret = sdt[i][2];
			}
				
		}
		return ret;
	}

	public String getTreeSpace(int treeline) {
		// TODO 自动生成的方法存根
		String space = "  ";
		for(int i=0;i<treeline;i++)
		{
			space = space + "  ";
		}
		return space;
	}

	public void printTree() {
		// TODO 自动生成的方法存根
		System.out.println("语法分析树");
		String child_str;
		 try {
	            File writeName = new File("tree.txt"); // 相对路径，如果没有则要建立一个新的output.txt文件
	            writeName.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖
	            try (FileWriter writer = new FileWriter(writeName);
	                 BufferedWriter out = new BufferedWriter(writer)
	            ) {
	            	for(int i =0;i<treelist.size();i++){
	        			child_str = getChildStr(treelist.get(i).getChild());
	        			System.out.println(treelist.get(i).getNode()+" : "+child_str);
	        			out.write(treelist.get(i).getNode()+" :  "+child_str+"\r\n");
	            	}
	                out.flush(); // 把缓存区内容压入文件
	                
//	                for(int i =0;i<treesdtlist.size();i++){
//	        			child_str = getChildStr(treesdtlist.get(i).getChild());
//	        			System.out.println(treesdtlist.get(i).getNode()+" : "+child_str);
//	            	}
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}

	public String getChildStr(ArrayList<YufaNode> child) {
		// TODO 自动生成的方法存根
		String str = "";
		for(int i=0;i<child.size();i++){
			str = str+child.get(i).getNode()+"  ,";
		}
		if(str.length()!=0)
			str = str.substring(0, str.length()-1);
		return str;
	}

	public int getRow(String row_str) {
		// TODO 自动生成的方法存根
		for(int i=0;i<finallist.size();i++){
			if(finallist.get(i).equals(row_str)){
				return i+1;
			}
		}
		return -1;
	}

	public int getLine(String line_str) {
		// TODO 自动生成的方法存根
		for(int i=0;i<notfinallist.size();i++){
			if(notfinallist.get(i).equals(line_str)){
				return i+1;
			}
		}
		return -1;
	}

	
	public void ShowGUI() {
		// TODO 自动生成的方法存根
		int line;
		int temp;
		char ch;
		String kind;
		String word;
		
		int flag;
		int j=0;
		for( int i = 0 ; i < errorlist.size() ; i++) {
			 		
			jta_tree.append(errorlist.get(i)+"\r\n");
			//System.out.println(" ( "+nowstate+" , "+ch+" ) == "+nextstate+"\r\n");
				
		    //设置页面显示中每一行的高
			int height = 50;
			Point p = new Point();
			p.setLocation(0, this.jta_tree.getLineCount()*height);
			this.jsp_tree.getViewport().setViewPosition(p);
					
			//设置页面显示中字体的大小，20表示字号，0 表示字形如1粗体2斜体之类
			Font x = new Font("Serif",0,22);
			jta_tree.setFont(x);
			
			j++;
	  	}
	}
}
