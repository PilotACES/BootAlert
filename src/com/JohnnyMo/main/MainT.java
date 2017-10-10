package com.JohnnyMo.main;

import java.util.Timer;
import java.util.TimerTask;

import com.JohnnyMo.sendService.MessageService;

public class MainT {
	
	public static void main(String[] args) {
		MessageService ms = new MessageService();
		if(ms.reSendBootInfo()){      //运行时尝试发送上次未成功发送的记录
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					MessageService mService = new MessageService();
					mService.sendPost();
				}
			}, 90000);
		}else{
			ms.sendPost();
		}
	}
}
