package com.simplemad.android.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.simplemad.android.SimpleMadApp;
import com.simplemad.android.server.SimplemadServer;
import com.simplemad.android.util.IPAddressHelper;
import com.simplemad.bean.Message;
import com.simplemad.bean.MessageType;
import com.simplemad.bean.NetAddress;
import com.simplemad.message.client.ClientHandlerAdapter;
import com.simplemad.message.client.IMessageClient;
import com.simplemad.message.client.UdpMobileClient;
import com.simplemad.message.util.AddressUtil;
import com.simplemad.message.util.JacksonUtil;
import com.simplemad.parameter.ClientParameter;

public class ClientUtil {
	
	public static void doSend(MessageType msgType, Object obj) {
		if(!isInNetwork()) {
			return;
		}
		SimplemadServer server = SimplemadServer.instance();
		InetSocketAddress host = new InetSocketAddress(ClientParameter.UDP_HOST, ClientParameter.UDP_PORT);
		
		Message msg = new Message();
		
		NetAddress target = AddressUtil.convert(host);
		msg.setTarget(target);
		msg.setSendDate(new Date());
		msg.setType(msgType);
		msg.setContent(JacksonUtil.toJSON(obj));
		
		server.getServer().send(host, msg);
	}
	
	public static void doSendEmpty() {
		if(!isInNetwork()) {
			return;
		}
		SimplemadServer server = SimplemadServer.instance();
		InetSocketAddress host = new InetSocketAddress(ClientParameter.UDP_HOST, ClientParameter.UDP_PORT);
		server.getServer().sendEmpty(host);
	}
	
	/**
	 * no use in phone app
	 * @param msgType
	 * @param obj
	 * @param hostName
	 * @param port
	 */
	@Deprecated
	public static void doSend(MessageType msgType, Object obj, String hostName, int port) {
		if(!isInNetwork()) {
			return;
		}
		InetSocketAddress host = new InetSocketAddress(hostName, port);
		InetSocketAddress local = new InetSocketAddress(IPAddressHelper.getLocalIpAddress(), ClientParameter.UDP_PORT);
		Message msg = new Message();
		
		NetAddress target = AddressUtil.convert(host);
		msg.setTarget(target);
		msg.setSendDate(new Date());
		msg.setType(msgType);
		msg.setContent(JacksonUtil.toJSON(obj));
		
		IMessageClient client = new UdpMobileClient();
		client.setHandler(new ClientHandlerAdapter());
		client.send(host, local, msg);
	}
	
	public static <T> T doGet(String url, Class<T> clazz) throws IOException {
		if(!isInNetwork()) {
			return null;
		}
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		HttpResponse response = client.execute(request);
		if(response.getStatusLine().equals(HttpStatus.SC_OK)) {
			String content = EntityUtils.toString(response.getEntity());
			return JacksonUtil.getObject(content, clazz);
		} else {
			return null;
		}
	}
	
	public static <T> T doPost(String url, Map<String, Object> params, Class<T> clazz) throws IOException {
		if(!isInNetwork()) {
			return null;
		}
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(url);
		 List<NameValuePair> paramList = createNameValuePairs(params);
		 HttpEntity entity = new UrlEncodedFormEntity(paramList, "UTF-8");
         request.setEntity(entity);
		HttpResponse response = client.execute(request);
		if(response.getStatusLine().getStatusCode() ==HttpStatus.SC_OK) {
			String content = EntityUtils.toString(response.getEntity());
			return JacksonUtil.getObject(content, clazz);
		} else {
			try {
				return clazz.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
				return null;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	private static List<NameValuePair> createNameValuePairs(Map<String, Object> params) {
		List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		if(params == null) {
			return paramList;
		}
		Set<Entry<String, Object>> paramsSet = params.entrySet();
		Iterator<Entry<String, Object>> paramsIt = paramsSet.iterator();
		while(paramsIt.hasNext()) {
			Entry<String, Object> entry = paramsIt.next();
			paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
		}
		return paramList;
	}
	
	private static boolean isInNetwork() {
		return SimpleMadApp.instance().isInNetwork();
	}
}
