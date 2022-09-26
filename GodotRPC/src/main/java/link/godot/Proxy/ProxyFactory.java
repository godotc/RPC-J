package link.godot.Proxy;

import link.godot.common.Invocation;
import link.godot.common.URL;
import link.godot.loadBalance.LoadBalance;
import link.godot.protocol.HttpClient;
import link.godot.register.MapRemoteRegister;
import org.apache.juli.logging.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class ProxyFactory {

    public static <T> T getProxy(Class interfaceClass) {
        // User's config

        Object proxyInstance =
                Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new InvocationHandler() {

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                       // Mock
                        String mock = System.getProperty("mock");
                        if(mock != null && mock.startsWith("return:")){
                            String result = mock.replace("return:","");
                            return  result;
                        }



                        // 触发method 结构体
                        Invocation invocation = new Invocation(
                                interfaceClass.getName(), method.getName(),
                                method.getParameterTypes(), args);

                        // 服务发现 get URL from mapRemoteRegister
                        List<URL> list = MapRemoteRegister.get(interfaceClass.getName());
                        System.out.println("get remote hosts:" + list);

                        // Used urls
                        List<URL> invokeUrls = new ArrayList<>();

                        // 服务调用
                        HttpClient httpClient = new HttpClient();
                        String result = null;
                        int times = 0;
                        while (times < 3) {
                            try {
                                // 负载均衡
                                if (!list.isEmpty()) {
                                    list.remove(invokeUrls);
                                }
                                URL url = LoadBalance.random(list);
                                invokeUrls.add(url);

                                result = httpClient.send(url.getHostname(), url.getPort(), invocation);
                                return result;

                            } catch (Exception e) {
                                ++times;
                                if (times < 3) {
                                    System.out.println("The " + times + " times try.");
                                    continue;
                                }
                                // Fault-Tolerance 容错机制
                                // error-callback=link.godot.HelloServiceErrorCallback
                                return "Failed during send remote call";
                            }
                        }
                        return result;
                    }

                });
        return (T) proxyInstance; // 返回内部的InvocationHandler
    }
}
