package link.godot.register;

import java.util.HashMap;
import java.util.Map;

public class LocalRegister {

    private static Map<String, Class> map = new HashMap<>();

    public static void register(String interfaceName, String version, Class implClass) {
        map.put(interfaceName+version, implClass);
    }

    public static Class get(String interfaceName,String verison) {
        return map.get(interfaceName+verison);
    }


}
