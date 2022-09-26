package link.godot.loadBalance;

import link.godot.common.URL;

import java.util.List;
import java.util.Random;

public class LoadBalance {

    // Enhance load-balance(负载均衡) algorithm
    public static URL random(List<URL> urls){
        Random random = new Random();
        int i = random.nextInt(urls.size());
        return urls.get(i);
    }
}
