package link.godot;

import link.godot.Proxy.ProxyFactory;
import link.godot.common.Invocation;
import link.godot.protocol.HttpClient;

import java.lang.reflect.Proxy;

public class Consumer {

    public static void main(String[] args) {
        HelloService helloService = ProxyFactory.getProxy(HelloService.class);
        String result = helloService.sayHello("godot Proxy Factory");
        System.out.println(result);
    }


}
