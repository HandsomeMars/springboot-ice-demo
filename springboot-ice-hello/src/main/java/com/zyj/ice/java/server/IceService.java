package com.zyj.ice.java.server;

/**
 * 　　┏┓　　　┏┓+ +
 * 　┏┛┻━━━┛┻┓ + +
 * 　┃　　　　　　　┃
 * 　┃　　　━　　　┃ ++ + + +
 * ████━████ ┃+
 * 　┃　　　　　　　┃ +
 * 　┃　　　┻　　　┃
 * 　┃　　　　　　　┃ + +
 * 　┗━┓　　　┏━┛
 * 　　　┃　　　┃
 * 　　　┃　　　┃ + + + +
 * 　　　┃　　　┃
 * 　　　┃　　　┃ +  神兽保佑
 * 　　　┃　　　┃    代码无bug
 * 　　　┃　　　┃　　+
 * 　　　┃　 　　┗━━━┓ + +
 * 　　　┃ 　　　　　　　┣┓
 * 　　　┃ 　　　　　　　┏┛
 * 　　　┗┓┓┏━┳┓┏┛ + + + +
 * 　　　　┃┫┫　┃┫┫
 * 　　　　┗┻┛　┗┻┛+ + + +
 *
 * @author by zyj
 * @version V1.0
 * @Description:
 * @Date 2019/08/06 16:17
 */

public class IceService {

    private static final String SERVER_NAME="Hello";
    private static final String SERVER_ENDPOINT="tcp -p 10006";


    public static void main(String[] args) {
        HelloServant helloServant=new HelloServant();


        Ice.Communicator communicator = null;
        try {
            //初始化ice通信器communicator,可以使用args传入一下ice初始化的参数如超时时间，线程池大小等
            communicator = Ice.Util.initialize(args);
            //创建一个名为queryEmployeeAdapter的适配器并且默认使用tcp协议  服务部署在10.4.30.81机器上 服务开启10006监听端口
            Ice.ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints(SERVER_NAME,SERVER_ENDPOINT);
            // 将servant与ice对象标识符建立映射关系，并添加到ice对象适配器中
            adapter.add(helloServant, Ice.Util.stringToIdentity(SERVER_NAME));
            // 激活对象适配器
            adapter.activate();
            System.out.println("服务启动");
            // 服务在退出之前一直保持监听状态
            communicator.waitForShutdown();

        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(communicator != null){
                communicator.destroy();
            }
        }
    }


}
