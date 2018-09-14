package test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class WebLogFile {

	public static void main(String[] args) {
		File file = new File("F:\\sparktest.log");
		try {
			FileWriter fileWriter = new FileWriter(file);// 新建一个文件
			
			Random random = new Random();// 生成随机数
			
			for (int i = 0; i < 1000000; i++) {
				int ip_1 = random.nextInt(220);
				int ip_2 = random.nextInt(220);
				int ip_3 = random.nextInt(220);
				int ip_4 = random.nextInt(220);
				
				fileWriter.write(ip_1 + "." + ip_2 + "." + ip_3 + "." + ip_4 + "-" + new SimpleDateFormat("yyyyMMddHH:mm:ss").format(new Date())); // 文件格式：ID
				fileWriter.write(System.getProperty("line.separator"));
			}
			
			fileWriter.flush();
			fileWriter.close();
			System.out.println("模拟日志文件生成成功！");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
