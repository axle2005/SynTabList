package io.github.axle2005.syntablist.common;

import java.io.Serializable;


public class ServerData implements Serializable {
		private static final long serialVersionUID = 7143543049235984071L;
		
		private final String serverName;
		private State packet;
		
		public ServerData(String serverName, State packet) {
			this.serverName = serverName;
			this.packet = packet;
		}
		public enum State {
			  START, STOP, CRASH;
			}
		public String getServerName() {
			return serverName;
		}
		public State getState()
		{
			return packet;
		}
}
