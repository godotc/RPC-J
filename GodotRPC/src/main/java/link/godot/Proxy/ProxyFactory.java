package link.godot.Proxy;

import link.godot.common.Invocation;
import link.godot.common.URL;
import link.godot.loadBalance.LoadBalance;
import link.godot.protocol.HttpClient;
import link.godot.register.MapRemoteRegister;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class ProxyFactory {

    public static <T> T getProxy(Class interfaceClass) {
        // User's config

        Object proxyInstance =
                Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new InvocationHandler() {

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                        // 触发method 结构体
                        Invocation invocation = new Invocation(
                                interfaceClass.getName(), method.getName(),
                                method.getParameterTypes(), args);

                        // 服务发现 get URL from mapRemoteRegister
                        List<URL> list = MapRemoteRegister.get(interfaceClass.getName());
                        System.out.println("get remote hosts:" + list);
                        // 负载均衡
                        URL url = LoadBalance.random(list);

                        // 服务调用
                        HttpClient httpClient = new HttpClient();
                        String result = httpClient.send(url.getHostname(), url.getPort(), invocation);
                        return result;
                    }

                });
        return (T) proxyInstance; // 返回内部的InvocationHandler
    }
}
