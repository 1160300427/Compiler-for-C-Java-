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
	public JButton jb_yufa = new JButton("�﷨����");
	public JButton jb_yuyi = new JButton("�������");
	public JLabel jl_test = new JLabel("����");
	public JLabel jl_tree = new JLabel("�﷨����");
	public JPanel contentPane;
	public JFrame window;
	public JTree tree;
	public JScrollPane jsp_tree1;

	DefaultMutableTreeNode top = new DefaultMutableTreeNode("Program");
	
	public String[][] all_ll = new String[50][30];                          //�����LL1ת����
	public ArrayList<String> finallist = new ArrayList<String>();             //����LL1ת����õ���������ż��ϣ��ս��
	public ArrayList<String> notfinallist = new ArrayList<String>();          //����LL1ת����õ��ķ��ս��
	public ArrayList<YufaNode> treelist = new ArrayList<YufaNode>();    //��������ļ���
	public ArrayList<String> senlist = new ArrayList<String>();     //����ľ��Ӽ���
	public ArrayList<String> errorlist = new ArrayList<String>(); 
	
	public ArrayList<YufaNode> treesdtlist = new ArrayList<YufaNode>();  //�����sdt��
	
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
		
		window = new JFrame("�﷨������");
		//�ô��ھ�����ʾ
		window.setDefaultCloseOperation(window.EXIT_ON_CLOSE);        
		window.setSize(1300,1000);     //(���ߣ�   
		window.setLocationRelativeTo(null);       
		window.setVisible(true);   //��ʾ
		
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
		// TODO �Զ����ɵķ������
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
		// TODO �Զ����ɵķ������
		Stack<String> st = new Stack<String>();           //�﷨����ʱʹ�õ���ͨջ
		Stack<String> st_sdt = new Stack<String>();       //�������ʱ��õ�sdtջ
		String sdttop;
		int i=0;    //ʶ���token
		String sentence = "";  //ʶ������
		String space = "  ";
		String tree_str = "";
		int line,row;  //ʶ����У���
		String line_str,row_str;
		String name = "";   //��tokenlist�еõ��ĵ�ǰ��Ҫʶ��ĵ���
		String kind = "";
		String value = "";
		int wordline;
		
		//��ʼ��ջ
		st.push("$");
		line_str = st.push(notfinallist.get(0));
		st_sdt.push("$");
		sdttop = st_sdt.push(notfinallist.get(0));
		
		String change;   //all_ll[A,a]
		String change_sdt;   //���º�������嶯����ֵ
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
			
			System.out.println("�������룺��i = "+i+",���ս�� = "+line_str+",�ս�� = "+row_str);
			//System.out.println("kind = "+kind+",line_str = "+line_str+",line = "+line+",row = "+row);

			/*ջ��Ԫ��������ս����Сд��������ջ������
			 * �Ϸ�ƥ�䡪������ջ�������������ƥ�䣬���뼯�������ƶ�һλ
			 * �����Ϸ����
			 */
			if(Character.isUpperCase(line_str.charAt(0)) == false){
				//���ջ���ս�����������ƥ�䣬������ս��,�������뼯�������ƶ�һλ
				if(line_str.equals(kind)){
					sentence = sentence + name+" ";
					System.out.println("��ջ���ս���Ϸ�����i = "+i+",ջ������ = "+line_str+",ʶ������ = "+sentence);
					i++;
					st.pop();
					line_str = st.peek();
					
					st_sdt.pop();
					sdttop = st_sdt.peek();
					
					YufaNode node1 = new YufaNode();
					node1.setNode(sdttop);
					node1.setLine(treeline);
					treesdtlist.add(node1);
					
					//������ڵ�
					if(value.equals("-") == false)
					{
					YufaNode node = new YufaNode();
					node.setNode(kind);
					treevalue = kind+" : "+value+" ("+wordline+")";
					node.setVaule(value);
					//System.out.println("�ս��value = "+node.getValue());
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
				//���ջ���ս����������Ų�ƥ�䣬����ջ���ս��
				else{
					System.out.println("��ջ���ս�����Ϸ�����i = "+i+",ջ������ = "+line_str+",ʶ������ = "+sentence);
					st.pop();
					line_str = st.peek();
					
					st_sdt.pop();
					sdttop = st_sdt.peek();
					
					errorlist.add("Error at�� "+wordline+" �� : ʶ�𵽡� "+name+" ��ʱ����");
				}
			}
			
			/*
			 * ջ��Ԫ�ط��ս������������д
			 * ���ս���Ϸ����ս���Ϸ����������۲�all_ll[line][row]
			 * ���ս���Ƿ�������������ջ��
			 * �ս���Ƿ���������������������ƶ�һλ
			 */
			else{
				//�Ϸ����룬all_ll[line][row]�ɲ�
				if(line != -1 && row != -1){
					//System.out.println("�Ϸ����� ��("+line+","+row+")");
					change = all_ll[line][row];
					//System.out.println("all_ll = "+change);
					
			
					//��ǰʶ�𵽵�LL1������Ϊ�գ����ӵ�ǰ���룬���뼯�������ƶ�һλ
					if(change.equals("-1"))	
					{
						System.out.println("��LL1������Ϊ�գ���i = "+i+",���ս�� = "+line_str+",�ս�� = "+row_str);
						i++;
						errorlist.add("Error at�� "+wordline+" �� : ʶ�𵽡� "+name+" ��ʱ����");
					}
					
					//ʶ��synch,����ջ��
					else if(change.equals("synch")){
						System.out.println("��synch����i = "+i+",���ս�� = "+line_str+",�ս�� = "+row_str);
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

						errorlist.add("synch:Error at�� "+wordline+" �� : ʶ�𵽡� "+name+" ��ʱ����");
					}
					
					//ʶ����Чֵ
					else{
						//����Чֵʹ��->�ָ���󲿡��Ҳ�
						String s[]=change.split("->");
						String ch_left = s[0];
						String ch_right = s[1];
						String sr[] = ch_right.split(" ");//�Ҳ�ʹ��" "�ָ�ɵ�������
						
						//��ô������嶯����sdt����ʽ
						change_sdt = getSDTbyChange(change);
						System.out.println("change_sdt = "+change_sdt+" , change = "+change);
						String s1[]=change_sdt.split("->");
						String ch1_left = s1[0];
						String ch1_right = s1[1];
						String sr1[] = ch1_right.split(" ");//�Ҳ�ʹ��" "�ָ�ɵ�������
						
						//û��sdt���﷨������--�﷨����
						YufaNode node = new YufaNode();
						ArrayList<YufaNode> chirdlist = new ArrayList<YufaNode>();
						node.setNode(line_str);
						treevalue = line_str + " ("+wordline+")";
						node.setVaule(line_str);
						//System.out.println("��Чֵvalue = "+node.getValue());
						for(int j=0;j<sr1.length;j++){//�Ҳ���˳���ջ
							if(sr1[j].equals("��") == false){
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

						String pop_str = st.pop();//�󲿳�ջ
						for(int j=sr.length-1;j>=0;j--){//�Ҳ���˳���ջ
							if(sr[j].equals("��") == false)
								line_str = st.push(sr[j]);
							//System.out.println("sr,length = "+sr.length+",sr[] = "+sr[j]);
						}
						line_str = st.peek();
						
						
						//��sdt�йص��﷨������--���岿��
						YufaNode node1 = new YufaNode();
						ArrayList<YufaNode> chirdlist1 = new ArrayList<YufaNode>();
						node1.setNode(sdttop);
						node1.setLine(treeline);
						for(int j=sr1.length-1;j>=0;j--){//�Ҳ���˳���ջ
							if(sr1[j].equals("��") == false){
								YufaNode childnode = new YufaNode();
								childnode.setNode(sr1[j]);
								childnode.setVaule(sr1[j]);
								childnode.setLine(treeline+1);
								chirdlist1.add(childnode);
							}
								
						}
						node.setChild(chirdlist1);
						treesdtlist.add(node1);
						
						st_sdt.pop();//�󲿳�ջ
						for(int j=sr1.length-1;j>=0;j--){//�Ҳ���˳���ջ
							if(sr1[j].equals("��") == false){
								sdttop = st_sdt.push(sr1[j]);
							}
								
							//System.out.println("sr,length = "+sr.length+",sr[] = "+sr[j]);
						}
		
						sdttop = st_sdt.peek();
						
						
						//��JTree�йصĽڵ����
						DefaultMutableTreeNode childtreenode = null;
						for(int j=0;j<sr.length;j++){	
							childtreenode =  new DefaultMutableTreeNode(sr[j]);
							nownode.add(childtreenode);
							System.out.println("���ڵ� = "+nownode.toString()+",sr1[] = "+sr1[j]+"change_sdt = "+change_sdt);
						}
						if(sr.length != 0 )
							nownode = (DefaultMutableTreeNode) nownode.getChildAt(0);
						
						String nownode_str = sr[0];
						if(nownode.equals("��") || Character.isUpperCase(nownode_str.charAt(0)) == false)
						{
						while(true){
							if(nownode.isRoot() == true){
								break;
							}
							
							//��ȡ���길�ڵ���ӽڵ㼯������
							int parentChildCount = nownode.getParent().getChildCount();
							int nowIndex = 0;//��ǰ���ڵ��ӽڵ㼯���е��±�
							for(nowIndex = 0;nowIndex<parentChildCount;nowIndex++){
								if(nownode.getParent().getChildAt(nowIndex) == nownode){//�ҵ���ǰ�ڵ���±�ֵ
									break;
								}
							}
							
							if(nowIndex == parentChildCount - 1){
									nownode = (DefaultMutableTreeNode) nownode.getParent();
									nownode_str = nownode.toString();
								System.out.println("���ڵ�1 = "+nownode.toString());
							}
							else{
								for(int k = nowIndex+1;k<parentChildCount;k++){	
									nownode_str = nownode.toString();
									if(Character.isUpperCase(nownode.getParent().getChildAt(k).toString().charAt(0)) == true){	
										nownode = (DefaultMutableTreeNode)nownode.getParent().getChildAt(k);
										System.out.println("���ڵ�2 = "+nownode.toString());
										break;
									}
									System.out.println("���ڵ�22 = "+nownode.toString());	
								}
								
								break;
							}
						}
					}
						
						System.out.println("����Чֵ����i = "+i+",ջ������ = "+line_str+",ʶ��ĵ��� = "+kind);	
						
					}
					
				}
				
				//���ս���Ƿ�
				else if(line == -1){
					st.pop();
					line_str = st.peek();
					
					st_sdt.pop();
					sdttop = st_sdt.peek();
					
				}
				
				//�ս���Ƿ�
				else if(row == -1){
					i++;
					
				}
			}
			
			//���ջΪ��ֵ����ǰʶ�����������Ϊ��
			if(st.empty() == true){
				senlist.add(sentence);
				sentence = "";
			}
			
			System.out.println("nownode = "+nownode.toString());
			System.out.println("stack : "+st);
			System.out.println("SDT-stack : "+st_sdt);
			System.out.println("************************1��ѭ��******************** = "+i);
		}
		
		printTree();
		ShowGUI();
	}

	//��ö�Ӧ����ʽ��sdt���ʽ
	public String getSDTbyChange(String change) {
		// TODO �Զ����ɵķ������
		String ret="";
		for(int i=0;i<59;i++){
			if(sdt[i][0].equals(change)){
				ret = sdt[i][2];
			}
				
		}
		return ret;
	}

	public String getTreeSpace(int treeline) {
		// TODO �Զ����ɵķ������
		String space = "  ";
		for(int i=0;i<treeline;i++)
		{
			space = space + "  ";
		}
		return space;
	}

	public void printTree() {
		// TODO �Զ����ɵķ������
		System.out.println("�﷨������");
		String child_str;
		 try {
	            File writeName = new File("tree.txt"); // ���·�������û����Ҫ����һ���µ�output.txt�ļ�
	            writeName.createNewFile(); // �������ļ�,��ͬ�����ļ��Ļ�ֱ�Ӹ���
	            try (FileWriter writer = new FileWriter(writeName);
	                 BufferedWriter out = new BufferedWriter(writer)
	            ) {
	            	for(int i =0;i<treelist.size();i++){
	        			child_str = getChildStr(treelist.get(i).getChild());
	        			System.out.println(treelist.get(i).getNode()+" : "+child_str);
	        			out.write(treelist.get(i).getNode()+" :  "+child_str+"\r\n");
	            	}
	                out.flush(); // �ѻ���������ѹ���ļ�
	                
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
		// TODO �Զ����ɵķ������
		String str = "";
		for(int i=0;i<child.size();i++){
			str = str+child.get(i).getNode()+"  ,";
		}
		if(str.length()!=0)
			str = str.substring(0, str.length()-1);
		return str;
	}

	public int getRow(String row_str) {
		// TODO �Զ����ɵķ������
		for(int i=0;i<finallist.size();i++){
			if(finallist.get(i).equals(row_str)){
				return i+1;
			}
		}
		return -1;
	}

	public int getLine(String line_str) {
		// TODO �Զ����ɵķ������
		for(int i=0;i<notfinallist.size();i++){
			if(notfinallist.get(i).equals(line_str)){
				return i+1;
			}
		}
		return -1;
	}

	
	public void ShowGUI() {
		// TODO �Զ����ɵķ������
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
				
		    //����ҳ����ʾ��ÿһ�еĸ�
			int height = 50;
			Point p = new Point();
			p.setLocation(0, this.jta_tree.getLineCount()*height);
			this.jsp_tree.getViewport().setViewPosition(p);
					
			//����ҳ����ʾ������Ĵ�С��20��ʾ�ֺţ�0 ��ʾ������1����2б��֮��
			Font x = new Font("Serif",0,22);
			jta_tree.setFont(x);
			
			j++;
	  	}
	}
}
