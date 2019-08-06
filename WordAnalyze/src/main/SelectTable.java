package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;



public class SelectTable {

	public static String[][] first = new String[28][20]; 
	public static String[][] follow = new String[28][20];
	public static String[][] select = new String[60][50];
	public static String[][] table = new String[31][20];
	public static ArrayList<String[]> in=new ArrayList<String[]>();
	 
	 public static void readFile(String filename,String[][] rows){
	   	  // 初始化一个用于存储txt数据的数组
		 	int index = 0;
	       BufferedReader br = null;
	       try {
	          // 读文件了. 路径就是那个txt文件路径
	          br  = new BufferedReader(new FileReader(new File(filename)));
	           String str = null;
	           // 按行读取
	           while((str=br.readLine())!=null){
	           // 用\t分隔
	               rows[index] = str.split("\t");
	               index++;
	               
	           }
	                                         
	       } catch (FileNotFoundException e) {
	           // TODO Auto-generated catch block
	           e.printStackTrace();
	       } catch (IOException e) {
	           // TODO Auto-generated catch block
	           e.printStackTrace();
	       }
	       
	       return ;
	   }
	 
	 public static void main(String[] args) throws FileNotFoundException {
			readFile("first.txt",first);
			readFile("follow.txt",follow);
			getLL1();
			MakeSelect();
	 	}

	public static void MakeSelect() {
		// TODO 自动生成的方法存根
		int index = 0;
	       BufferedReader br = null;
	       try {
	          // 读文件了. 路径就是那个txt文件路径
	          br  = new BufferedReader(new FileReader(new File("select.txt")));
	           String str = null;
	           // 按行读取
	           while((str=br.readLine())!=null){
	           // 用\t分隔
	               select[index] = str.split("\t");
	               index++;
	               
	           }
	                                         
	       } catch (FileNotFoundException e) {
	           // TODO Auto-generated catch block
	           e.printStackTrace();
	       } catch (IOException e) {
	           // TODO Auto-generated catch block
	           e.printStackTrace();
	       }
	       
	       for(int x=1; x<select.length; x++) {
	    	   System.out.print("select["+select[x][0]+"] : {");
	             for(int y=1; y<select[x].length; y++) {
	                 System.out.print(select[x][y]+" ");
	             }
	             System.out.println("}");
	         }
	}

	public static void getLL1() throws FileNotFoundException {
		// TODO 自动生成的方法存根
		BufferedReader br = null;
		 br  = new BufferedReader(new FileReader(new File("LL1.txt")));
        String str = null;
        // 按行读取
        try {
			while((str=br.readLine())!=null){
			 // 用\t分隔
				//System.out.println(str);
				StringBuffer buffer=new StringBuffer(str);
				str=buffer.toString();
				String s[]=str.split("->");//左推导符
				if(s.length==1)
					s=str.split("→");//考虑到输入习惯和形式问题
				if(s.length==1)
					s=str.split("=>");
				if(s.length==1){
					System.out.println("文法有误");
					System.exit(0);
				}
				StringTokenizer fx = new StringTokenizer(s[1],"|︱");//按英文隔符拆开产生式或按中文隔符拆开
				
				while(fx.hasMoreTokens()){
					String[] one = new String[2];//对于一个语句只需保存两个数据就可以了，语句左部和语句右部的一个简单导出式，假如有或符，就按多条存放
					one[0]=s[0];//头不变,0位置放非终结符
					one[1]=fx.nextToken();//1位置放导出的产生式，就是产生式右部的一个最简单导出式
					in.add(one);
				}
				//str=sc.nextLine();
			     
			 }
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}
	 }


