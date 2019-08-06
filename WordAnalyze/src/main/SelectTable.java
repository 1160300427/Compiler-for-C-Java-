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
	   	  // ��ʼ��һ�����ڴ洢txt���ݵ�����
		 	int index = 0;
	       BufferedReader br = null;
	       try {
	          // ���ļ���. ·�������Ǹ�txt�ļ�·��
	          br  = new BufferedReader(new FileReader(new File(filename)));
	           String str = null;
	           // ���ж�ȡ
	           while((str=br.readLine())!=null){
	           // ��\t�ָ�
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
		// TODO �Զ����ɵķ������
		int index = 0;
	       BufferedReader br = null;
	       try {
	          // ���ļ���. ·�������Ǹ�txt�ļ�·��
	          br  = new BufferedReader(new FileReader(new File("select.txt")));
	           String str = null;
	           // ���ж�ȡ
	           while((str=br.readLine())!=null){
	           // ��\t�ָ�
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
		// TODO �Զ����ɵķ������
		BufferedReader br = null;
		 br  = new BufferedReader(new FileReader(new File("LL1.txt")));
        String str = null;
        // ���ж�ȡ
        try {
			while((str=br.readLine())!=null){
			 // ��\t�ָ�
				//System.out.println(str);
				StringBuffer buffer=new StringBuffer(str);
				str=buffer.toString();
				String s[]=str.split("->");//���Ƶ���
				if(s.length==1)
					s=str.split("��");//���ǵ�����ϰ�ߺ���ʽ����
				if(s.length==1)
					s=str.split("=>");
				if(s.length==1){
					System.out.println("�ķ�����");
					System.exit(0);
				}
				StringTokenizer fx = new StringTokenizer(s[1],"|��");//��Ӣ�ĸ����𿪲���ʽ�����ĸ�����
				
				while(fx.hasMoreTokens()){
					String[] one = new String[2];//����һ�����ֻ�豣���������ݾͿ����ˣ�����󲿺�����Ҳ���һ���򵥵���ʽ�������л�����Ͱ��������
					one[0]=s[0];//ͷ����,0λ�÷ŷ��ս��
					one[1]=fx.nextToken();//1λ�÷ŵ����Ĳ���ʽ�����ǲ���ʽ�Ҳ���һ����򵥵���ʽ
					in.add(one);
				}
				//str=sc.nextLine();
			     
			 }
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}

	}
	 }


