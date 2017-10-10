package com.JohnnyMo.sendService;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

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
 * @version ALPHA 0.1
 *
 */
public class MessageService {
	
	private String sendServiceUrl = "https://sc.ftqq.com/";
	private static String SCKEY = "SCU11937T3197efd6904b7910395c6d558ce8440259c1d79e8e7ae";
	private String endMark = ".send";
	private String alertMessage = "主人！！！！有熊孩子在动你的电脑！(｀･ω･´)赶紧干掉他！";
	private SystemInfo systemInfo = new SystemInfo();
	
	public String sendPost() {
		String sendResult = "";
		Map<String,String> paramsMap = new HashMap<String, String>();
		try {
			String sysInfo = systemInfo.getSystemBootTime();
			this.writeBootInfo(alertMessage, sysInfo, "1");
			paramsMap.put("text", alertMessage);		//微信提示标题
			paramsMap.put("desp", sysInfo);		//长消息内容
			sendResult = this.sendMessage(sendServiceUrl, SCKEY, paramsMap, "UTF-8");
			if(!sendResult.equals("") || !sendResult.equals("no response")){
				this.writeBootInfo(alertMessage, sysInfo, "0");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sendResult;
	}
	
	/**
	 * POST请求发送
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
			}else{
				result = "no response";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 写入历史开机记录
	 * @param title 标题
	 * @param desp 长内容
	 */
	public void writeBootInfo(String title, String desp, String status) {
		InputStream in = null;
		OutputStream out = null;
		try {
			File bootFile = new File("bootHis.properties");
			if(!bootFile.exists()){
				bootFile.createNewFile();
			}
			in = new BufferedInputStream(new FileInputStream(bootFile));
			out = new FileOutputStream(bootFile);
			Properties pro = new Properties();
			pro.load(in);
			pro.setProperty("title", title);
			pro.setProperty("desp", desp);
			pro.setProperty("status",status);
			pro.store(out, "Update boot history");
			out.flush();
			out.close();
			in.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 尝试重新发送开机信息
	 * @return
	 */
	public boolean reSendBootInfo(){
		boolean sendResult = false;
		try {
			File bootFile = new File("bootHis.properties");
			if(bootFile.exists()){
				Properties pro = new Properties();				//读取历史记录
				InputStream in = new BufferedInputStream(new FileInputStream(bootFile));
				pro.load(in);
				String statusValue = pro.getProperty("status");
				if(!statusValue.equals("0")){				//判断该记录是否发送过
					Map<String, String> paramsMap = new HashMap<String, String>();
					String textValue = pro.getProperty("title");
					String despValue = pro.getProperty("desp");
					paramsMap.put("text", textValue);
					paramsMap.put("desp", despValue);
					String result = this.sendMessage(sendServiceUrl, SCKEY, paramsMap, "UTF-8");
					if(!result.equals("") || !result.equals("no response")){				//是否发送成功
						sendResult = true;
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sendResult;
	}
	
	/*public static void main(String[] args) {
	    MessageService ms = new MessageService();
	    System.out.println(ms.sendPost());
	}*/
}
