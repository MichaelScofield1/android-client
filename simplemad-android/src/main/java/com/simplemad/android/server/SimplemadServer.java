package com.simplemad.android.server;

import org.apache.mina.core.service.IoHandlerAdapter;

import com.simplemad.bean.Message;
import com.simplemad.message.server.IMessageServer;
import com.simplemad.message.server.ServerHandlerAdapter;
import com.simplemad.message.server.UdpMessageServer;
import com.simplemad.parameter.ClientParameter;

public class SimplemadServer {

	private static SimplemadServer instance;
	
	private IMessageServer messageServer;
	
	private SimplemadServer() {
		startup();
	}
	
	public static synchronized SimplemadServer instance() {
		if(instance == null) {
			instance = new SimplemadServer();
		}
		return instance;
	}
	
	public IMessageServer getServer() {
		return messageServer;
	}
	
	private void startup() {
		messageServer = new UdpMessageServer(ClientParameter.UDP_PORT);
		IoHandlerAdapter handler = new ServerHandlerAdapter(messageServer) {
			@Override
			protected void onReceived(Message msg) {
				IMessageCommand command = CommandFactory.create(msg);
				if(command != null)
					command.execute();	
			}
		};
		messageServer.setHandler(handler);
		messageServer.startup();
	}
	
	public void shutdown() {
		if(messageServer != null) {
			messageServer.shutdown();
		}
	}
	
	public void restart() {
		shutdown();
		startup();
	}
	
	public boolean isDisposed() {
		return messageServer == null || messageServer.isDisposed();
	}
}
