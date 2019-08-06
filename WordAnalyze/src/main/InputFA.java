package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class InputFA {
	 /**
     * ����TXT�ļ�
     */
    public static String[][] readFileFA() {
    	  // ��ʼ��һ�����ڴ洢txt���ݵ�����
        String[][] rows = new String[31][20];
        int index = 0;
        BufferedReader br = null;
        try {
           // ���ļ���. ·�������Ǹ�txt�ļ�·��
           br  = new BufferedReader(new FileReader(new File("FA.txt")));
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
        return rows;
    }
    
    public static String[][] readFileSDT() {
  	  // ��ʼ��һ�����ڴ洢txt���ݵ�����
      String[][] rows = new String[59][3];
      int index = 0;
      BufferedReader br = null;
      try {
         // ���ļ���. ·�������Ǹ�txt�ļ�·��
         br  = new BufferedReader(new FileReader(new File("SDT.txt")));
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
      
    //��ӡ�����
//    for(int x=0; x<rows.length; x++) {
//        for(int y=0; y<rows[x].length; y++) {
//            System.out.print("rows["+x+","+y+"] = "+rows[x][y]+"  ");
//        }
//        System.out.println();
//    }
      return rows;
  }
    
    
    public static String[][] readFileLL() {
  	  // ��ʼ��һ�����ڴ洢txt���ݵ�����
      String[][] rows = new String[28][37];
      int index = 0;
      BufferedReader br = null;
      try {
         // ���ļ���. ·�������Ǹ�txt�ļ�·��
         br  = new BufferedReader(new FileReader(new File("LL1������.txt")));
          String str = null;
          // ���ж�ȡ
          while((str=br.readLine())!=null){
          // ��\t�ָ�
              rows[index] = str.split("\t");
              index++;
              
          }
//          //��ӡ�����
//          for(int x=0; x<rows.length; x++) {
//              for(int y=0; y<rows[x].length; y++) {
//                  System.out.print("rows["+x+","+y+"] = "+rows[x][y]+"  ");
//              }
//              System.out.println();
//          }
                                           
      } catch (FileNotFoundException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      }
      return rows;
  }
    
    public static ArrayList<String> GetInOps(String[][] rows,int length)
    {
    	 ArrayList<String> InOp = new  ArrayList<String>();
    	int i;
    	for(i=1;i<length;i++)
    	{
    		InOp.add(rows[0][i]);
    		//System.out.println("InOp["+i+"] = "+InOp.get(i));
    	}
    	
//    	for( i = 0 ; i < InOp.size() ; i++) {
//    		  System.out.println("������ţ� "+InOp.get(i));
//    		}
		return  InOp;
		
    }
    
    public static ArrayList<String> GetState(String[][] rows,int length)
    {
    	ArrayList<String> State = new ArrayList<String>();
    	int i = 1;
    	for(i=1;i<length;i++)
    	{
    		State.add(rows[i][0]);
    		//System.out.println("State["+i+"] = "+State.get(i));
       	}
//    	
//    	for( i = 0 ; i < State.size() ; i++) {
//  		  System.out.println("״̬�� "+State.get(i));
//  		}
		return  State;
    	
		
    }
    
	

	public static String[] ids = {"m","b","c","a","stack","num","value","u","y"};
	public static ArrayList<String> GetListID(){
		ArrayList<String> list3 = new ArrayList<String>();
		for(int i=0;i<ids.length;i++){
			list3.add(ids[i]);
		}
		return list3;
	}
	
	public static String[] signs = {"int","real","char","array ( 4 , int )","record","int","char","int","int"};
	public static ArrayList<String> GetListSign(){
		ArrayList<String> list3 = new ArrayList<String>();
		for(int i=0;i<signs.length;i++){
			list3.add(signs[i]);
		}
		return list3;
	}
	
    public static ArrayList<Integer> GetFinalState(ArrayList<String> state_fa_s)
    {
  
    	ArrayList<Integer> finalstate = new ArrayList<Integer>();
    	String str;
    	int fs;
    	int j=1,le;;
    	for(int i=0;i<state_fa_s.size();i++)
    	{
    		
    		if (state_fa_s.get(i).indexOf("*") !=-1){  //����*��
    			le = (state_fa_s.get(i).length())- 1;
    			str =state_fa_s.get(i).substring(0,le);
    			fs = Integer.parseInt(str);
    			finalstate.add(fs);
            }
    	}
    	
//    	for(int i = 0 ; i < finalstate.size() ; i++) {
//    		  System.out.println("��ֹ״̬�� "+finalstate.get(i));
//    		}
    	
    	
    
    	return finalstate;
    }

	public static ArrayList<Integer> GetInOp(ArrayList<String> state_fa_s) {
		// TODO �Զ����ɵķ������
		 ArrayList<Integer> state1 = new ArrayList<Integer>();
		 int s,len;
		for(int i=0;i<state_fa_s.size();i++)
    	{
			len = (state_fa_s.get(i).length()) - 1;
    		if (state_fa_s.get(i).indexOf("*") !=-1){
    			s = Integer.parseInt(state_fa_s.get(i).substring(0, len));
            }
    		else
    		{
    			s = Integer.parseInt(state_fa_s.get(i));
    		}
    		state1.add(s);
    		
    	}
		
//		for(int i = 0 ; i < state1.size() ; i++) {
//  		  System.out.println("����״̬int�� "+state1.get(i));
//  		}
		return state1;
	}
    	


}
