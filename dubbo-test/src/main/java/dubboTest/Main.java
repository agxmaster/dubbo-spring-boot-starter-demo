package dubboTest;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class Main
{
	public static void main(String[] args){
		StringBuilder resultSB = new StringBuilder();
		for(int i= 0;i< 10000;i++) {
			try {
				long start = System.currentTimeMillis();
				Document doc = Jsoup.connect("http://localhost:8088/hello/请求"+i).get();
				Elements elements = doc.select("body");
				long end = System.currentTimeMillis();
				String result = "执行时间" + (end-start) + "\t\t"+elements.text();
				System.out.println(result);
				resultSB.append(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}