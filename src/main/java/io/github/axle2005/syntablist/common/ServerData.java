package io.github.axle2005.syntablist.common;

import java.io.Serializable;


public class ServerData implements Serializable {
		private static final long serialVersionUID = 7143543049235984071L;
		
		private State packet;
		
		public ServerData(State packet) {
			this.packet = packet;
		}
		public enum State {
			  START, STOP, CRASH;
			}
		public State getState()
		{
			return packet;
		}
}
