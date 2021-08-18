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
		// 设置此书名称
		bookname = "嫁给渣男死对头";
		// 设置此书第一章的地址
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
			 * 寻找章节标题
			 */
			Elements titles = doc.select("div:matchesOwn(第.{1,20}章),h1:matchesOwn(第.{1,20}章)");
			for (Element i : titles) {
				System.out.println(">>>>>>>>>" + i);
			}
			String title = doc.title();
			System.out.println("title===" + title);

//			Elements links = doc.getElementsByTag("a");
//			for (Element link : links) {
//			  String linkHref = link.attr("abs:href");
//			  String linkText = link.text();
//			  if(linkText.equals("下一章")) {
//				  System.out.println(linkHref+"================"+linkText);
//			  }	  
//			}
			/*
			 * 寻找下一章的链接
			 */
			Elements nextlinks = doc.select("a:matchesOwn(下一章)");
			String nextlink = nextlinks.get(0).attr("abs:href");

			WriteFile writeFile = new WriteFile(bookname + ".txt");
			writeFile.write(titles.get(0).text());
			Elements contents = doc.select("[class$=content],[id$=content]");
			Element content = contents.get(0);
			for (Node node : content.childNodes()) {
				String string = node.toString();
				if (string != null && !string.equals("")) {
					// 匹配所有html标记正则表达式并删除
					string = string.replaceAll("<(\\S*?)[^>]*>.*?|<.*? />", "");
					string = string.replace("&nbsp;", "");
					// System.out.println(string);
					writeFile.write(string);
					writeFile.write("\r\n");
				}
			}
			System.out.println(titles.get(0).text() + "下载完成");
			if (nextlink == null) {
				System.out.println("没有下一章！下载结束");
			}
			downLoad(nextlink);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			// 出现503错误重新爬此页
			downLoad(url);
		}
	}
}
