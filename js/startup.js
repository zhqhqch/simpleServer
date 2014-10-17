importPackage(com.hqch.simple.resource);
importPackage(com.hqch.simple.resource.sql);
importPackage(com.hqch.simple.server);

//日志级别 默认 DUBUG
//$.setLogLevel("ERROR");

//注册缓存
var caches = new Resource();
caches.host = "192.168.1.252";
caches.port = 11111;
$.registerCache("cache", caches);

//数据库连接
var dataSource = new C3P0ConnectionResource();
dataSource.setUser('orangegame');
dataSource.setPassword('orange.game');
dataSource.setJdbcUrl('jdbc:mysql://127.0.0.1:3306/card_game_data?characterEncoding=UTF-8');
dataSource.setDriverClass('com.mysql.jdbc.Driver');
dataSource.setMaxPoolSize(50);
dataSource.setMinPoolSize(25);
$.registerResource("dataSource",dataSource)


//配置本server
var server = new GameServer();
server.port = 10002;
server.protocol = "json"; //protobuf json
server.synchroData = false;
server.cached = "cache";
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