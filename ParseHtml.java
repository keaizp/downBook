package com.zp.test;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class ParseHtml {
	static String bookname;

	public static void main(String[] args) {
		// ���ô�������
		bookname = "�޸���������ͷ";
		// ���ô����һ�µĵ�ַ
		String url = "http://www.86zw.co/xiaoshuo/32149/68877110.html";
		// "http://www.ujxs.com/read/11437/8977598.html";
		// "http://book.zongheng.com/chapter/1081255/63543451.html";
		// "https://www.biquges.com/11_11744/7449439.html";
		// "https://www.biquges.com/11_11744/7449455.html";
		// "https://www.biquges.com/11_11744/7449459.html";
		ParseHtml parseHtml = new ParseHtml();
		parseHtml.downLoad(url);
	}

	public void downLoad(String url) {
		if (url == null) {
			return;
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			Connection con = Jsoup.connect(url);
			Document doc = con.get();
			System.out.println(url);
			/*
			 * Ѱ���½ڱ���
			 */
			Elements titles = doc.select("div:matchesOwn(��.{1,20}��),h1:matchesOwn(��.{1,20}��)");
			for (Element i : titles) {
				System.out.println(">>>>>>>>>" + i);
			}
			String title = doc.title();
			System.out.println("title===" + title);

//			Elements links = doc.getElementsByTag("a");
//			for (Element link : links) {
//			  String linkHref = link.attr("abs:href");
//			  String linkText = link.text();
//			  if(linkText.equals("��һ��")) {
//				  System.out.println(linkHref+"================"+linkText);
//			  }	  
//			}
			/*
			 * Ѱ����һ�µ�����
			 */
			Elements nextlinks = doc.select("a:matchesOwn(��һ��)");
			String nextlink = nextlinks.get(0).attr("abs:href");

			WriteFile writeFile = new WriteFile(bookname + ".txt");
			writeFile.write(titles.get(0).text());
			Elements contents = doc.select("[class$=content],[id$=content]");
			Element content = contents.get(0);
			for (Node node : content.childNodes()) {
				String string = node.toString();
				if (string != null && !string.equals("")) {
					// ƥ������html���������ʽ��ɾ��
					string = string.replaceAll("<(\\S*?)[^>]*>.*?|<.*? />", "");
					string = string.replace("&nbsp;", "");
					// System.out.println(string);
					writeFile.write(string);
					writeFile.write("\r\n");
				}
			}
			System.out.println(titles.get(0).text() + "�������");
			if (nextlink == null) {
				System.out.println("û����һ�£����ؽ���");
			}
			downLoad(nextlink);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			// ����503������������ҳ
			downLoad(url);
		}
	}
}
