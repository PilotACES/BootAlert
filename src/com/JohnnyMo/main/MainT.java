package com.JohnnyMo.main;

import com.JohnnyMo.sendService.MessageService;

public class MainT {
	
	public static void main(String[] args) {
		MessageService ms = new MessageService();
		ms.sendPost();
	}

}
