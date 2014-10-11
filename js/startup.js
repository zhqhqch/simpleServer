importPackage(com.hqch.simple.cache);
importPackage(com.hqch.simple.server);

//注册缓存
var caches = new Resource();
caches.host = "192.168.1.252";
caches.port = 11111;
$.registerCache("cache", caches);

//配置rmi
var rmi = new Resource();
rmi.host = "192.168.1.252";
rmi.port = 10001;
$.initRemote(rmi);

//配置本server
var server = new GameServer();
server.port = 10002;
server.protocol = "json";
$.initServer(server);

//配置rmi提供调用的server
var remoteList = [ {
	name : "system",
	host : "192.168.1.252",
	port : 40001
}, {
	name : "data",
	host : "192.168.1.252",
	port : 50001
} ];

for(var i = 0; i<remoteList.length; i++){
	server = remoteList[i];
	var remote = new Resource();
	remote.setName(server.name);
	remote.setHost(server.host);
	remote.setPort(server.port);
	$.addRemoteServer(remote);
}