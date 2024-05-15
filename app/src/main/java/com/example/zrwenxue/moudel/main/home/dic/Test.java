package com.example.zrwenxue.moudel.main.home.dic;//package com.example.zrwenxue.moudel.main.home.dic;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.util.ArrayList;
//
//public class Test {
//	public static void main(String[] args) throws Exception {
//		method(fileLocation,fileLocation2);
//	}
//
//	private static String fileLocation = "C:" + File.separator + "Users" + File.separator + "86182" + File.separator
//			+ "Desktop" + File.separator + "english" + File.separator + "123.txt";
//	private static String fileLocation2 = "C:" + File.separator + "Users" + File.separator + "86182" + File.separator
//			+ "Desktop" + File.separator + "english" + File.separator + "456.txt";
//
//	public static void method(String fileStr,String fileStr2) throws Exception {
//		ArrayList<String> itemList = new ArrayList<>();// ����װ�ؼ���
//		File file = new File(fileStr);// �½��ļ���
//		File file2 = new File(fileStr2);// �½��ļ���
//
//
//	     BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileStr2,true),"UTF-8"));
////	     bufferedWriter.write("");//д���ı�
////	     bufferedWriter.close();
//
//		// ��ȡ�������ݷ���itemList
//		BufferedReader bufferedReader = new BufferedReader(
//				new InputStreamReader(new FileInputStream(fileStr), "UTF-8"));
//		String line;
//		while ((line = bufferedReader.readLine()) != null) {
//			itemList.add(line);
//		}
//		bufferedReader.close();
//
//		System.out.print("���ϳ���Ϊ��" + itemList.size());// ������ϳ���
//
//		ArrayList<EngBean> engBeansList = new ArrayList<>();// ���ö��󼯺�
//
//		// �˴��ж�itemlist��һ�ַ�Ϊ"��" �Ͳ�װ����
//		int index = 0;
//		for (int i = 0; i < 1042172; i++) {
//
//				if(itemList.get(i).contains("<br>")) {
//					 bufferedWriter.write("��"+i+"�����ݣ�"+name(itemList.get(i)));
////					 System.out.print("��"+i+"�����ݣ�"+name(itemList.get(i)));
//			}
////			System.out.print("\r\n" + "��" + i + "�����ݣ�" + "------���ʣ�"+engBeansList.get(i).getWord()
////					+"------���ԣ�"+engBeansList.get(i).getSpeech()
////					+ "------���ͣ�"+engBeansList.get(i).getExplain().get(0));
//		}
//		System.out.print("���----------------------");
//		   bufferedWriter.close();
//	}
//
//	private static String name(String str) {
//		String word = "",speech = "",explain = "";//����   ����  ����
//
//		if (str.indexOf("<br>") == str.length() - 6) {//����һ��BrΪ����ʱ
//
//			word = str.substring(str.indexOf("<h3>") + 4, str.indexOf("</h3>"));//��ȡ<h3> </h3> ����
//
//			if (str.contains("<i>")) {
//				speech = str.substring(str.indexOf("<i>") + 3, str.indexOf("</i>"));//��ȡ<i> </i> ����
//			}
//
//			explain = str.substring(str.lastIndexOf("\">") + 2, str.lastIndexOf("<br>"));
//
//		} else {
//			word = str.substring(str.indexOf("<h3>") + 4, str.indexOf("</h3>"));
//			if (str.contains("<i>")) {
//				speech = str.substring(str.indexOf("<i>") + 3, str.indexOf("</i>"));
//			}
//
//			explain = str.substring(str.indexOf("f\">") + 3, str.indexOf("<br>",str.indexOf("f\">") + 3));
//
//		}
//		return word+"#"+speech+"#"+explain+"\r\n";
//	}
//
//}
