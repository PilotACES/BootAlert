package com.JohnnyMo.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SystemInfo {
	
	public String getSystemBootTime() {
		String bootTime = "";
		Process process = null;
		try {
			process = Runtime.getRuntime().exec("cmd /c systeminfo ");
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(),"GBK"));
			int i = 0;
			String info = "";
			while((info = br.readLine()) != null){		//获取系统启动时间
				if(i == 11){
					bootTime += info.replace(" ", "");
					break;
				}
				i++;
			}
			process.destroy();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			process.destroy();
			e.printStackTrace();
		}
		return bootTime;
	}
	
	/*public static void main(String[] args) {
		SystemInfo systemInfo = new SystemInfo();
		System.out.println(systemInfo.getSystemBootTime());
	}*/

}
