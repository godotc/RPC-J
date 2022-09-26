package link.godot.protocol;

import link.godot.common.Invocation;
import link.godot.register.LocalRegister;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HttpServletHandler {

    public void handler(HttpServletRequest req, HttpServletResponse resp) {

        // Handle request --> Interface, Method, Parameter
        try {
            Invocation invocation = (Invocation) new ObjectInputStream(req.getInputStream()).readObject();

            String interfaceName = invocation.getInterfaceName();
            Class classImpl = LocalRegister.get(interfaceName, "1.0");

            Method method = classImpl.getMethod(invocation.getMethodName(), invocation.getParameterTypes());

            String result = (String) method.invoke(classImpl.newInstance(), invocation.getParameters());

            IOUtils.write(result, resp.getOutputStream());

        } catch (IOException | ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }

    }

}
