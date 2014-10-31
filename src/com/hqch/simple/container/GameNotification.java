package com.hqch.simple.container;

import com.hqch.simple.rpc.NotifEvent;

public interface GameNotification {

	public void onNotification(NotifEvent event);
}
