package link.godot;

import link.godot.common.URL;
import link.godot.protocol.HttpServer;
import link.godot.register.LocalRegister;
import link.godot.register.MapRemoteRegister;

public class Provider {

    public static void main(String[] args) {

        // 本地函数注册
        LocalRegister.register(HelloServiceImpl.class.getName(), "1.0", HelloServiceImpl.class);

        // 注册中心注册 本机的 url host
        URL url = new URL("localhost", 8080);  // 使用model获取本地的地址,端口
        MapRemoteRegister.register(HelloServiceImpl.class.getName(), url);

        // Netty、 Tomcat, socket server to receive Net Request
        HttpServer httpServer = new HttpServer();
        httpServer.start(url.getHostname(), url.getPort());

    }
}
