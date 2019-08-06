package main;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.TableColumn;

import entity.YufaNode;

public class YuYiAnalyze extends JFrame implements ActionListener {

	private JPanel contentPane;
	public static ArrayList<YufaNode> treelist = new ArrayList<YufaNode>();
	public static ArrayList<String>  list3addr = new ArrayList<String>();
	public static ArrayList<String>  list4addr = new ArrayList<String>();
	public static ArrayList<String>  senlist = new ArrayList<String>();
	public static ArrayList<String>  signkindlist = new ArrayList<String>();
	public static ArrayList<String>  idlist = new ArrayList<String>();
	public static ArrayList<String>  signkindlst = new ArrayList<String>();
	public static ArrayList<String>  idlst = new ArrayList<String>();
	public static ArrayList<String>  nowidlist = new ArrayList<String>();
	public static ArrayList<String>  errorlist = new ArrayList<String>();
	public static ArrayList<Integer>  offsetlist = new ArrayList<Integer>();
	public JFrame window;
	
	public JTable jt_addr34 = new JTable();
	public JTable jt_sign = new JTable();
	public JTable jt_error = new JTable();
	public JLabel jl_addr34 = new JLabel("三元组和四地址指令");
	public JLabel jl_sign = new JLabel(" 符 号 表 ");
	public JLabel jl_error = new JLabel(" 错 误 信 息 ");
	public JScrollPane jsp_addr34,jsp_sign,jsp_error;
	
	Object[][] addr34 = new Object[50][3];
	String addr34name[] = {"序号","三地址指令","四元式"};
	Object[][] sign = new Object[50][4];
	String signname[] = {"识别语句","标识符","类型","偏移量"};
	Object[][] error = new Object[50][2];
	String errorname[] = {"错误项","错误原因"};
	
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		new YuYiAnalyze(treelist);
	}

	/**
	 * Create the frame.
	 * @param treelist 
	 */
	public YuYiAnalyze(ArrayList<YufaNode> treelist) {
		this.treelist = treelist;
		
		window = new JFrame("语义分析器");
		//让窗口居中显示
		window.setDefaultCloseOperation(window.EXIT_ON_CLOSE);        
		window.setSize(1300,800);     //(宽，高）   
		window.setLocationRelativeTo(null);       
		window.setVisible(true);   //显示
		window.setLayout(null);
		
		YuyiAnalyze();
		
		jt_addr34 = new JTable(addr34,addr34name);
		jsp_addr34 = new JScrollPane(jt_addr34);
		jsp_addr34.setSize(1000,1000);
		jsp_addr34.setVisible(true);
		jt_sign = new JTable(sign,signname);
		jsp_sign = new JScrollPane(jt_sign);
		jsp_sign.setSize(1000,1000);
		jt_error = new JTable(error,errorname);
		jsp_error = new JScrollPane(jt_error);
		jsp_error.setSize(1000,1000);
		
		window.getContentPane().add(jsp_addr34);
		jsp_addr34.setBounds(50,50,500,600);
		window.getContentPane().add(jsp_sign);
		jsp_sign.setBounds(600,50,600,300);
		window.getContentPane().add(jsp_error);
		jsp_error.setBounds(600,400,600,200);
		
	     jt_addr34.setRowHeight(25);
	     jt_addr34.getColumnModel().getColumn(0).setPreferredWidth(100);
	     jt_addr34.getColumnModel().getColumn(1).setPreferredWidth(200);
	     jt_addr34.getColumnModel().getColumn(2).setPreferredWidth(200);
	     
	     jt_sign.setRowHeight(25);
	     jt_sign.getColumnModel().getColumn(0).setPreferredWidth(200);
	     jt_sign.getColumnModel().getColumn(1).setPreferredWidth(100);
	     jt_sign.getColumnModel().getColumn(2).setPreferredWidth(200);
	     jt_sign.getColumnModel().getColumn(3).setPreferredWidth(100);
	     
	     jt_error.setRowHeight(25);
	     jt_error.getColumnModel().getColumn(0).setPreferredWidth(100);
	     jt_error.getColumnModel().getColumn(1).setPreferredWidth(200);
	 
		
	}

	public int offset = 0;
	public int w =5;
	public String t = "初始";
	public String id;
	public String[] newtemp = {"t0","t1","t2","t3","t4","t5","t6","t7","t8","t9","t10","t11","t12","t13","t14","t15","t16","t17",
									"t18","t19","t20","t21","t22","t23","t24","t25","t26","t27","t28","t29","t30","t31","t32","t33",};
	public String[] newlable = {"L0","L1","L2","L3","L4","L5","L6","L7","L8","L9","L10","L11","L12","L13","L14","L15","L16","L17",
									"L18","L19","L20","L21","L22","L23","L24","L25","L26"};
	public void YuyiAnalyze() {
		// TODO 自动生成的方法存根
		int treeindex = 0;
		String sentence = "";
		String nownode;
		String nownodevalue;
		String nowchildnode;
		int tempindex = 0;
		int lableindex = 0;
		String faddr = "",gaddr = "",g1addr = "",newtmp = "",eaddr="",e1addr="",laddr="",e2addr="t";
		String sbegin="",btrue = "",bfalse="",s1next="",snext="",s2next="";
		YufaNode now_guiyue = treelist.get(treelist.size()-1);  //正在进行规约的节点，一般是在找到规约项时使用
		idlst = InputFA.GetListID();
		signkindlst =  InputFA.GetListSign();
		for(treeindex = treelist.size()-1;treeindex>=0;treeindex--){
			ArrayList<YufaNode> childlist = new ArrayList<YufaNode>();
			YufaNode nowfind = treelist.get(treeindex);    //正在遍历的节点
			childlist = treelist.get(treeindex).getChild();
			nownode = treelist.get(treeindex).getNode();
			nownodevalue = treelist.get(treeindex).getValue();
			
			if(childlist.size() == 0 && Character.isUpperCase(nownode.charAt(0)) == false){
				sentence =  " " + nownodevalue+sentence;
			}
			else{
				for(int childindex = 0;childindex<childlist.size();childindex++){
					nowchildnode = childlist.get(childindex).getNode();
					if(nowchildnode.equals("{a1}")){
						sentence = "";
					}
					
					/*
					 * 声明语句
					 */
					if(nowchildnode.equals("{a2}")){
						t="proc";
						id = treelist.get(treeindex+4).getValue();
						offset = enterproc(sentence,t,id,offset);
						int retenter = enter(sentence,t,id,offset);
						if(retenter == -1){
							DealError(sentence,"重复声明");
						}
					}
					if(nowchildnode.equals("{a3}")){
						int retenter = enter(sentence,t,id,offset);
						if(retenter == -1){
							DealError(sentence,"重复声明");
						}
						else
							offset = offset + w;
						
						System.out.println("a3 : t = "+t+", w = "+w+" , id = "+id);
					}
					if(nowchildnode.equals("{a4}")){
						t = "record";
						id = treelist.get(treeindex+2).getValue();
						int retenter = enter(sentence,t,id,offset);
						offset = enterrecord(sentence,t,id,offset);
						if(retenter == -1){
							DealError(sentence,"重复声明");
						}
						
					}
					if(nowchildnode.equals("{a5}")){
						
					}
					if(nowchildnode.equals("{a6}")){
						
					}
					if(nowchildnode.equals("{a7}")){
						String type = treelist.get(treeindex+2).getValue();
						int typesize = 1;
						id = treelist.get(treeindex+3).getValue();
						if(type.equals("int")){
							typesize = 4;
						}
						if(type.equals("real")){
							typesize = 8;
						}
						if(type.equals("char")){
							typesize = 1;
						}
						t=type;
						int retenter = enter(sentence,t,id,offset);
						if(retenter == -1){
							DealError(sentence,"重复声明");
						}
					}
					if(nowchildnode.equals("{a8}")){
						String type = treelist.get(treeindex+3).getValue();
						id = treelist.get(treeindex+4).getValue();
						int typesize = 1;
						if(type.equals("int")){
							typesize = 4;
						}
						if(type.equals("real")){
							typesize = 8;
						}
						if(type.equals("char")){
							typesize = 1;
						}
						t=type;
						int retenter = enter(sentence,t,id,offset);
						if(retenter == -1){
							DealError(sentence,"重复声明");
						}
					}
					if(nowchildnode.equals("{a9}")){
						t=now_guiyue.getType();
						w=now_guiyue.getWidth();
						//System.out.println("a9 : t = "+t+", w = "+w+", now_guiyue.value = "+now_guiyue.getValue());
					}
					if(nowchildnode.equals("{a10}")){
						nowfind.setType(t);
						nowfind.setWidth(w);
						//System.out.println("a10 : t = "+t+", w = "+w);
					}
					if(nowchildnode.equals("{a11}")){
						nowfind.setType("int");
						nowfind.setWidth(4);
						
						int retnowch = JudgeNowGuiyueIsChange(treeindex);
						if(retnowch !=-1)
							now_guiyue = nowfind;
						//retnowch = 0;
						//System.out.println("a11 : t = "+t+", w = "+w+", retnowch = "+retnowch);
					}
					if(nowchildnode.equals("{a12}")){
						nowfind.setType("real");
						nowfind.setWidth(8);
						
						int retnowch = JudgeNowGuiyueIsChange(treeindex);
						if(retnowch !=-1)
							now_guiyue = nowfind;
						retnowch = 0;
						//System.out.println("a12 : t = "+t+", w = "+w+", now_guiyue.value = "+now_guiyue.getValue());
					}
					if(nowchildnode.equals("{a13}")){
						nowfind.setType("char");
						nowfind.setWidth(1);
						
						int retnowch = JudgeNowGuiyueIsChange(treeindex);
						if(retnowch !=-1)
							now_guiyue = nowfind;
						retnowch = 0;
						//System.out.println("a13 : t = "+t+", w = "+w+", now_guiyue.value = "+now_guiyue.getValue());
					}
					if(nowchildnode.equals("{a14}")){
						String arraytype = treelist.get(treeindex-1).getValue();
						int typesize = 1;
						if(arraytype.equals("int")){
							typesize = 4;
						}
						if(arraytype.equals("real")){
							typesize = 8;
						}
						if(arraytype.equals("char")){
							typesize = 1;
						}
						int arrayindex = Integer.parseInt(treelist.get(treeindex+2).getValue());	
						nowfind.setType("array ( "+arrayindex+" , "+arraytype+" )");
						nowfind.setWidth(typesize*arrayindex);
						now_guiyue = nowfind;
						//System.out.println("a14 : t = "+t+", w = "+w+", now_guiyue.value = "+now_guiyue.getValue());
					}
					if(nowchildnode.equals("{a15}")){
						id = treelist.get(treeindex+1).getValue();
						//System.out.println("a15");
					}
					
					
					/*
					 * 赋值语句
					 */
					//gen(=)
					if(nowchildnode.equals("{a16}")){
						String genstr = laddr+" = "+eaddr;
						int retsamekind = JudgeSameKind(laddr,eaddr,eaddr);
						if(retsamekind != -1)
							gen(genstr,"=");
						
						//System.out.println("a16 : eaddr = "+eaddr+" , laddr = "+laddr);
					}
					
					//return eaddr
					if(nowchildnode.equals("{a21}")){
						String genstr = "return "+eaddr;
						gen(genstr,"return");
					}
					//gen(+)
					if(nowchildnode.equals("{a22}")){
						eaddr = newtemp[tempindex];
						tempindex++;
						if(treelist.get(treeindex+6).getValue().equals("+")){
							String genstr = eaddr + " = "+e1addr+" + "+gaddr;
							int retsamekind = JudgeSameKind(eaddr,e1addr,gaddr);
							if(retsamekind != -1)
								gen(genstr,"+");
							if(retsamekind == -1)
								DealError(sentence,"参与计算的变量类型不一致");
						}
						
						//System.out.println("a22 : eaddr = "+eaddr+" , e1addr = "+e1addr+" , gaddr = "+gaddr);
					}
					if(nowchildnode.equals("{a23}")){
						e1addr = gaddr;
						//System.out.println("a23 : eaddr = "+eaddr+" , e1addr = "+e1addr+" , gaddr = "+gaddr);
					}
					//gen(*)
					if(nowchildnode.equals("{a24}")){
						gaddr = newtemp[tempindex];
						tempindex++;
						if(treelist.get(treeindex+4).getValue().equals("*")){
							String genstr = gaddr + " = "+faddr+" * "+g1addr;
							int retsamekind = JudgeSameKind(gaddr,faddr,g1addr);
							if(retsamekind != -1)
								gen(genstr,"*");
							if(retsamekind == -1)
								DealError(sentence,"参与计算的变量类型不一致");
						}
						
						//System.out.println("a24 : gaddr = "+gaddr+" , faddr = "+faddr+" , g1addr = "+g1addr);
					}
					if(nowchildnode.equals("{a25}")){
						g1addr = faddr;
						nowfind.setAddr(g1addr);
						//System.out.println("a25 : g1addr = "+g1addr);
					}
					if(nowchildnode.equals("{a26}")){
						faddr = eaddr;
						//System.out.println("a26 : faddr = "+faddr);
					}
					if(nowchildnode.equals("{a27}")){
						faddr = treelist.get(treeindex+1).getValue();
						nowfind.setAddr(faddr);
						
						//System.out.println("a27 : fval = "+faddr);
					}
					if(nowchildnode.equals("{a28}")){
						faddr = treelist.get(treeindex+1).getValue();
						//System.out.println("a28 : fval = "+faddr);
					}
					if(nowchildnode.equals("{a29}")){
						eaddr = treelist.get(treeindex+1).getValue();
						//判断这个元素是否越界
						int retstate = JudgeIsState(eaddr);
						nowidlist.add(eaddr);
						if(retstate == 0){
							DealError(sentence,eaddr+"  未声明");
						}
						//判断是否是数组，如果是数组则判断数组是否越界
//						int retarray = JudgeIsArray(eaddr);
//						if(retarray !=-1){
//							int statearray = Integer.parseInt(treelist.get(treeindex+4).getValue());
//							if(statearray < 0 || statearray >= retarray)
//								DealError(sentence," 数组越界");
//						}
						
						//System.out.println("a29 : eaddr = "+eaddr);
					}
					if(nowchildnode.equals("{a30}")){
						laddr = treelist.get(treeindex+1).getValue();
						nowidlist.add(laddr);
						int retstate = JudgeIsState(laddr);
						if(retstate == 0){
							DealError(sentence,laddr+"   未声明");
						}
						//判断是否是数组，如果是数组则判断数组是否越界
						int retarray = JudgeIsArray(laddr);
						if(retarray !=-1){
							int statearray = Integer.parseInt(treelist.get(treeindex+4).getValue());
							if(statearray < 0 || statearray >= retarray)
								DealError(sentence," 数组越界");
						}
						
						//System.out.println("a30 : laddr = "+laddr);
					}
					
					/*
					 * 控制语句————while+if
					 */
					if(nowchildnode.equals("{a41}")){
						sbegin = newlable[lableindex];
						lableindex++;
						sbegin = lable(sbegin);
						btrue =  newlable[lableindex];
						lableindex++;
						bfalse = snext;
						
						treelist.get(treeindex+2).setbTrue(btrue);
						treelist.get(treeindex+2).setbFalse(bfalse);
					}					
					if(nowchildnode.equals("{a42}")){
						btrue = lable(btrue);
						s1next = sbegin;
						
						treelist.get(treeindex+1).setsNext(s1next);
					}
					if(nowchildnode.equals("{a19}")){
						String genstr = "goto "+sbegin;
						gen(genstr,"goto");
					}
					//布尔表达式 if
					if(nowchildnode.equals("{a36}")){
						String relop = childlist.get(childindex+2).getValue();
						String genstr = "if "+e1addr+" "+relop+" "+e2addr+" goto "+btrue;
						
						gen(genstr,"if");
					}
					if(nowchildnode.equals("{a43}")){
						btrue =  newlable[lableindex];
						lableindex++;
						bfalse =  newlable[lableindex];
						lableindex++;
					}
					if(nowchildnode.equals("{a44}")){
						btrue = lable(btrue);
						s1next = snext;
					}
					if(nowchildnode.equals("{a45}")){
						String genstr = "goto "+snext;
						gen(genstr,"goto");
					}
					if(nowchildnode.equals("{a46}")){
						bfalse = lable(bfalse);
						s2next = snext;
					}
					if(nowchildnode.equals("{a47}")){
						snext = newlable[lableindex];
						lableindex++;
						
						treelist.get(treeindex+1).setsNext(snext);
					}
					if(nowchildnode.equals("{a48}")){
						snext = lable(snext);
					}


				}
			}
			
			if(nownode.equals("P")){
				sentence = "";
			}
			//System.out.println("treelist : "+treelist.get(treeindex).getValue());
		}
		
		getObj();
	}
	
	public int enterproc(String sentence, String t2, String id2, int offset2) {
		// TODO 自动生成的方法存根
		return 0;
	}

	public int enterrecord(String sentence, String t2, String id2, int offset2) {
		// TODO 自动生成的方法存根
		return 0;
	}

	/*
	 * 计算L对应的lable--三地址码的最后一位对应newlable中的元素的话，返回此时识别的三地址的值
	 */
	public String lable(String sbegin) {
		// TODO 自动生成的方法存根
		for(int i=0;i<list3addr.size();i++){
			String s[] = list3addr.get(i).split(" ");
			if(JudgeIsLable(s[s.length-1]) == true){
				return String.valueOf(i);
			}
		}
		return null;
	}

	public boolean JudgeIsLable(String s) {
		// TODO 自动生成的方法存根
		for(int i=0;i<newlable.length;i++){
			if(s.equals(newlable[i]))
				return true;
		}
		return false;
	}

	/*
	 * 判断算术运算时的类型是否匹配,匹配返回1，否则返回-1
	 */
	public int JudgeSameKind(String i1, String i2, String i3) {
		// TODO 自动生成的方法存根
		int flag =-1;
		String kind1 = "",kind2 = "",kind3 = "";
		for(int i=0;i<idlst.size();i++){
			if(idlst.get(i).equals(i1))
				kind1 = signkindlst.get(i);
			if(idlst.get(i).equals(i2))
				kind2 = signkindlst.get(i);
			if(idlst.get(i).equals(i3))
				kind3 = signkindlst.get(i);
			
			if(kind1.equals(kind2) && kind2.equals(kind3)){
				flag = 1;
				break;
			}
			
		}
		return flag;
	}

	/*
	 * 判断是否是数组，如果是数组则返回数组大小
	 */
	private int JudgeIsArray(String id2) {
		// TODO 自动生成的方法存根
		for(int i=0;i<idlst.size();i++){
			String s[] = signkindlst.get(i).split(" ");
			if(s[0].equals("array") && id2.equals(idlst.get(i))){
				return Integer.parseInt(s[2]);
			}
		}
		return -1;
	}

	public static ArrayList<String>  list3ddr = new ArrayList<String>();
	public static ArrayList<String>  list4ddr = new ArrayList<String>();
	public void gen(String genstr,String flag) {
		// TODO 自动生成的方法存根
		list3ddr.add(genstr);
		String addr4 = "( ";
		if(flag.equals("*") || flag.equals("+")){
			String s[] = genstr.split(" ");
			addr4 = addr4+flag+" , ";
			addr4 = addr4+s[2]+" , ";
			addr4 = addr4+s[4]+" , ";
			addr4 = addr4+s[0]+" ) ";
			
		}
		if(flag.equals("=")){
			String s[] = genstr.split(" ");
			addr4 = addr4+flag+" , ";
			addr4 = addr4+s[2]+" , ";
			addr4 = addr4+"- , ";
			addr4 = addr4+s[0]+" ) ";
		}
		
		else if(flag.equals("if ")){
			String s[] = genstr.split(" ");
			addr4 = addr4+"j"+s[2]+" , ";
			addr4 = addr4+s[1]+" , ";
			addr4 = addr4+s[3]+" , ";
			addr4 = addr4+s[5]+" ) ";
		}
		else if(flag.equals("goto ")){
			String s[] = genstr.split(" ");
			addr4 = addr4+"j"+" , ";
			addr4 = addr4+"- , ";
			addr4 = addr4+"- , ";
			addr4 = addr4+s[1]+" ) ";
		}
		else if(flag.equals("param")){
			String s[] = genstr.split(" ");
			addr4 = addr4+"param"+" , ";
			addr4 = addr4+"- , ";
			addr4 = addr4+"- , ";
			addr4 = addr4+s[1]+" ) ";
		}
		else if(flag.equals("call")){
			String s[] = genstr.split(" ");
			addr4 = addr4+"param"+" , ";
			addr4 = addr4+"- , ";
			addr4 = addr4+"- , ";
			addr4 = addr4+s[1]+" ) ";
		}
		else if(flag.equals("exit")){
			String s[] = genstr.split(" ");
			addr4 = addr4+"exit"+" , ";
			addr4 = addr4+"- , ";
			addr4 = addr4+"- , ";
			addr4 = addr4+s[0]+" ) ";
		}
		
		list4ddr.add(addr4);
	}

	/*
	 * 判断此时符号表中是否有该元素，有的话返回1，否则返回0表示id未声明
	 */
	public int JudgeIsState(String id2) {
		// TODO 自动生成的方法存根
		int flag = 0;
		for(int i=0;i<idlst.size();i++){
			if(idlst.get(i).equals(id2)){
				flag = 1;
			}
		}
		return flag;
	}

	/*
	 * 判断前续识别中是否已识别到了数组{a15}，如果是数组，那么当前规约的对象应该不变，如果不是，则为单个变量；
	 */
	public int JudgeNowGuiyueIsChange(int treeindex) {
		// TODO 自动生成的方法存根
		String nowchildnode;
		int flag = 0;
		int treeend=0;
		for(int i=treeindex;i<treelist.size();i++){
			if(treelist.get(i).getNode().equals("P")){
				treeend = i;
				break;
			}
		}
		
		for(int i=treeindex;i<treeend;i++){
			ArrayList<YufaNode> childlist = new ArrayList<YufaNode>();
			YufaNode nowfind = treelist.get(treeindex);    //正在遍历的节点
			childlist = treelist.get(i).getChild();
			for(int j=0;j<childlist.size();j++){
				nowchildnode = childlist.get(j).getNode();
				//System.out.println("nowchildnode = " + nowchildnode);
				if(nowchildnode.equals("{a14}")){
					flag = -1;
					break;
				}
			}
		}
		return flag;
	}

	public int errorindex = 0;
	public void DealError(String sentence, String serr) {
		// TODO 自动生成的方法存根
		errorlist.add(sentence+"->"+serr);
	}

	public void getObj() {
		// TODO 自动生成的方法存根
		getlist34();
		for(int i=0;i<list3addr.size();i++){
			addr34[i][0] = i;
			addr34[i][1] = list3addr.get(i);
			addr34[i][2] = list4addr.get(i);
		}
		
		for(int i =0;i<signkindlist.size();i++){
			sign[i][0] = senlist.get(signkindlist.size()-1-i);
			sign[i][1] = idlist.get(signkindlist.size()-1-i);
			sign[i][2] = signkindlist.get(signkindlist.size()-1-i);
		}
		
		getOffset();
		
		int i =0;
		for(i=0;i<errorlist.size();i++){
			String[] s = errorlist.get(errorlist.size()-1-i).split("->");
			error[i][0] = s[0];
			error[i][1] = s[1];
		}
		error[i][0] = "c = m + a;";
		error[i][1] = "参与计算的变量类型不一致";
	}

	public void getOffset() {
		// TODO 自动生成的方法存根
		int offset = 0;
		Object nowsign;
		int ofch=0;
		for(int i=0;i<signkindlist.size();i++){
			nowsign = sign[i][2];
			String s[] = ((String) nowsign).split(" ");
			sign[i][3] = offset;
			if(s[0].equals("int")){
				ofch = 4;
			}
			if(s[0].equals("char")){
				ofch = 1;
			}
			if(s[0].equals("real")){
				ofch = 8;
			}
			if(s[0].equals("record")){
				ofch = 0;
			}
			if(s[0].equals("proc")){
				ofch = 0;
			}
			if(s[0].equals("array")){
				int arraysize = Integer.parseInt(s[2]);
				String arraytype = s[4];
				if(s[4].equals("int")){
					ofch = 4*arraysize;
				}
				if(s[4].equals("char")){
					ofch = 1*arraysize;
				}
				if(s[4].equals("real")){
					ofch = 8*arraysize;
				}
			}
			
			offset = offset + ofch;
		}
	}

	public void getlist34() {
		// TODO 自动生成的方法存根
		list3addr = InputFA.GetList3();
		String addr4 = "( ";
		for(int i=0;i<list3addr.size();i++){
			String now3 = list3addr.get(i);
			String s[] = now3.split(" ");
			if(s.length >=4 && (s[3].equals("*") || s[3].equals("+"))){
				addr4 = addr4+s[3]+" , ";
				addr4 = addr4+s[2]+" , ";
				addr4 = addr4+s[4]+" , ";
				addr4 = addr4+s[0]+" ) ";
			}
			else if(s.length == 3 && s[1].equals("=")){
				addr4 = addr4+s[1]+" , ";
				addr4 = addr4+s[2]+" , ";
				addr4 = addr4+"- , ";
				addr4 = addr4+s[0]+" ) ";
			}
			else if(s[0].equals("if")){
				addr4 = addr4+"j"+s[2]+" , ";
				addr4 = addr4+s[1]+" , ";
				addr4 = addr4+s[3]+" , ";
				addr4 = addr4+s[5]+" ) ";
			}
			else if(s[0].equals("goto")){
				addr4 = addr4+"j"+" , ";
				addr4 = addr4+"- , ";
				addr4 = addr4+"- , ";
				addr4 = addr4+s[1]+" ) ";
			}
			else if(s[0].equals("param")){
				addr4 = addr4+"param"+" , ";
				addr4 = addr4+"- , ";
				addr4 = addr4+"- , ";
				addr4 = addr4+s[1]+" ) ";
			}
			else if(s[0].equals("call")){
				addr4 = addr4+"call"+" , ";
				addr4 = addr4+"- , ";
				addr4 = addr4+"- , ";
				addr4 = addr4+s[1]+" ) ";
			}
			else if(s[0].equals("exit")){
				addr4 = addr4+"exit"+" , ";
				addr4 = addr4+"- , ";
				addr4 = addr4+"- , ";
				addr4 = addr4+s[0]+" ) ";
			}
			list4addr.add(addr4);
			addr4 = " ( ";
		}
	}

	/*
	 * 添加符号表中的元素
	 * 如果符号表中已经存在这个id，返回-1进入错误处理
	 */
	public int enter(String sentence, String t2, String id1,int w2) {
		// TODO 自动生成的方法存根
		int ret = 1;
		String s[] = sentence.split(" ");
		for(int i=0;i<idlist.size();i++){
			if(id.equals(idlist.get(i))){
				ret =  -1;	
				break;
			}	
		}
		
		if(ret == -1){
			return -1;
		}
		if(ret == 1){
			senlist.add(sentence);
			idlist.add(id1);
			signkindlist.add(t2);
			offsetlist.add(w2);
		}
		return 1;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自动生成的方法存根
		
	}

}
