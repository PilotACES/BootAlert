package com.JohnnyMo.sendService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.JohnnyMo.main.SystemInfo;

/**
 * 
 * @author Johnny Mo
 * @version ALPHA 0.1 Hotfix
 *
 */
public class MessageService {
	
	private String sendServiceUrl = "https://sc.ftqq.com/";
	private static String SCKEY = "SCU11937T3197efd6904b7910395c6d558ce8440259c1d79e8e7ae";
	private String endMark = ".send";
	private String alertMessage = "主人！！！！有熊孩子在动你的电脑！(｀･ω･´)赶紧干掉他！";
	private String despInfo = "&desp=";
	private SystemInfo systemInfo = new SystemInfo();
	
	public String sendPost() {
		String sendResult = "";
		Map<String,String> paramsMap = new HashMap<String, String>();
		try {
			String sysInfo = systemInfo.getSystemBootTime();
			//String sysInfo = new String(systemInfo.getSystemBootTime().getBytes("iso-8859-1"),"UTF-8");
			paramsMap.put("text", alertMessage);
			paramsMap.put("desp", sysInfo);
			sendResult = this.sendMessage(sendServiceUrl, SCKEY, paramsMap, "UTF-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sendResult;
	}
	
	/**
	 * 通过POST请求
	 * @param url
	 * @param sckey
	 * @param map
	 * @param charset
	 * @return
	 */
	public String sendMessage(String url, String sckey, Map<String,String> map, String charset) {
		String result = "";
		String realUrl = url + sckey + endMark;		//server姬消息服务url
		//String realUrl = "https://sc.ftqq.com/SCU11937T3197efd6904b7910395c6d558ce8440259c1d79e8e7ae.send";
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		try {
			httpClient = new SSLClient();
			httpPost = new HttpPost(realUrl);
			List<NameValuePair> list = new ArrayList<NameValuePair>();		//需要传递的参数列表
			Iterator itor = map.entrySet().iterator();
			while(itor.hasNext()){		//将包含传递参数的map遍历
				Entry<String, String> elem = (Entry<String, String>)itor.next();
				list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));		//保存传递的参数
			}
			if(list.size() > 0){
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, charset);
				httpPost.setEntity(entity);
			}
			HttpResponse response = httpClient.execute(httpPost);
			if(response != null){
				HttpEntity resEntity = response.getEntity();
				if(resEntity != null){
					result = EntityUtils.toString(resEntity,charset);
				}else{
					result = "no response";
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/*public static void main(String[] args) {
	    MessageService ms = new MessageService();
	    System.out.println(ms.sendPost());
	}*/
}
