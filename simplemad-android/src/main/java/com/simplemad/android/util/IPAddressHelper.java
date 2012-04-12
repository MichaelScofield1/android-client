package com.simplemad.android.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;

public class IPAddressHelper {

	public static String getLocalIpAddress() {
	    try {
	        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
	            NetworkInterface intf = en.nextElement();
	            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                InetAddress inetAddress = enumIpAddr.nextElement();
	                if (!inetAddress.isLoopbackAddress()) {
	                    return inetAddress.getHostAddress().toString();
	                }
	            }
	        }
	    } catch (SocketException ex) {
	       ex.printStackTrace();
	    }
	    return null;
	}
	
	public static String getWebIp() {
		try {
			String strUrl = "http://www.ip138.com/ip2city.asp";
			URL url = new URL(strUrl);

			BufferedReader br = new BufferedReader(new InputStreamReader(url

			.openStream()));

			String s = "";

			StringBuffer sb = new StringBuffer("");

			String webContent = "";

			while ((s = br.readLine()) != null) {
				sb.append(s + "\r\n");

			}

			br.close();
			webContent = sb.toString();
			int start = webContent.indexOf("[") + 1;
			int end = webContent.indexOf("]");
			webContent = webContent.substring(start, end);

			return webContent;

		} catch (Exception e) {
			e.printStackTrace();
			return "error open url:" + null;

		}
	}
	
	public static void main(String[] args) {
		System.out.println(IPAddressHelper.getWebIp());
	}
}
