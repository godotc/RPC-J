package link.godot.register;

import link.godot.common.URL;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapRemoteRegister {

    // 在get interfaceURL时，是从本地的 MapRemoteRegister中获取的，数据不共通， 所以需要再开一个实例来建造注册中心
    // 存在Redis, Zookeeper
    private static Map<String, List<URL>> map = new HashMap<>();

    public static void register(String interfaceName, URL url) {
        List<URL> list = map.get(interfaceName);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(url);

        map.put(interfaceName, list);

        saveFile();
    }

    public static List<URL> get(String interfaceName) {
        map = getFile();
        assert map != null;
        return map.get(interfaceName);
    }

    // 当前测试环境，Provider and Consumer 处在同一台机器， 所以我们可以通过文件来共享 remote register map
    private static void saveFile() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("/tmp.txt");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, List<URL>> getFile() {
        try {
            FileInputStream fileInputStream = new FileInputStream("/tmp.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return (Map<String, List<URL>>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
