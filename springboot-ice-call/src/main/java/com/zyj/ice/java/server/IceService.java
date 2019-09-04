package com.zyj.ice.java.server;

import com.zyj.ice.servant.ServerServant;

/**
 * @author by zyj
 * @version V1.0
 * @Description:
 * @Date 2019/8/6 21:13
 */
public class IceService {
    /**
     * 服务名
     */
    private static final String SERVER_NAME = "Hello";
    /**
     * 服务端点
     */
    private static final String SERVER_ENDPOINT = "tcp -p 10006";


    public static void main(String[] args) {
        //获取实现类 SpringContextUtil.getBean(helloServant)
        ServerServant serverServant = new ServerServant();

        //ice通信器
        Ice.Communicator communicator = null;
        try {
            //初始化ice通信器communicator,可以使用args传入一下ice初始化的参数如超时时间，线程池大小等
            communicator = Ice.Util.initialize(args);

            //创建一个名为queryEmployeeAdapter的适配器并且默认使用tcp协议  服务部署在10.4.30.81机器上 服务开启10006监听端口

            Ice.ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints(SERVER_NAME, SERVER_ENDPOINT);

            // 将servant与ice对象标识符建立映射关系，并添加到ice对象适配器中
            adapter.add(serverServant, Ice.Util.stringToIdentity(SERVER_NAME));

            // 激活对象适配器
            adapter.activate();

            System.out.println("服务启动");
            // 服务在退出之前一直保持监听状态
            communicator.waitForShutdown();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //异常销毁通信器
            if (communicator != null) {
                communicator.destroy();
            }
        }
    }


}
