package com.zyj.ice.spring.client;

import Ice.Identity;
import com.zyj.ice.servant.ClientServant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import slice2java.IServerPrx;
import slice2java.IServerPrxHelper;

import javax.annotation.PostConstruct;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author by zyj
 * @version V1.0
 * @Description:
 * @Date 2019/8/6 21:13
 */
@Service
public class IceClient implements Runnable {

    /**
     * 服务名
     */
    private static final String SERVER_NAME = "Hello";
    /**
     * 服务端点
     */
    private static final String SERVER_ENDPOINT = "tcp -h 127.0.0.1 -p 10006";

    @Autowired
    private ClientServant clientServant;

    @PostConstruct
    private void startIceClient() {
        //构造线程启动客户端
        LinkedBlockingQueue<Runnable> runnableList = new LinkedBlockingQueue<Runnable>();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(100, 100, 1L, TimeUnit.SECONDS, runnableList);
        threadPoolExecutor.execute(this);
    }


    @Override
    public void run() {
        //ice通信器
        Ice.Communicator communicator = null;
        try {
            //初始化ice通信器communicator,可以使用args传入一下ice初始化的参数如超时时间，线程池大小等
            communicator = Ice.Util.initialize();

            //构建服务端的代理对象  服务端对象标识以 SERVER_NAME:SERVER_ENDPOINT 格式
            Ice.ObjectPrx op = communicator.stringToProxy(SERVER_NAME + ":" + SERVER_ENDPOINT);

            //检查通用客户端代理op 是不是queryServer对象标识符所关联的ice对象的代理
            IServerPrx qp = IServerPrxHelper.checkedCast(op);

            if (qp == null) {
                throw new Exception("qp == null");
            }

            //测试发送信息到户无端
            boolean result = qp.request("hello");

            // 输出服务端返回结果
            System.out.println("spring:client请求结果：" + result + " 时间：" + System.currentTimeMillis());


            //创建客户端服务
            Ice.ObjectAdapter adapter = communicator.createObjectAdapter("");
            Ice.Identity id = new Identity();
            id.name = "client";
            id.category = "";
            adapter.add(clientServant, id);
            adapter.activate();

            //客户端服务设置服务端点
            qp.ice_getConnection().setAdapter(adapter);

            //设置回调对象
            qp.setCallBack(id);
            //设置心跳回调
            qp.ice_getConnection().setCallback(new Ice.ConnectionCallback() {
                @Override
                public void heartbeat(Ice.Connection c) {
                    System.out.println("sn:" + " server heartbeat....");
                }

                @Override
                public void closed(Ice.Connection c) {
                    System.out.println("sn:" + " " + "closed....");
                }
            });
            // 每30/2 s向对方做心跳
            // 客户端向服务端做心跳 服务端打印服务端的con.setCallback(new Ice.ConnectionCallback()
            qp.ice_getConnection().setACM(new Ice.IntOptional(10), new Ice.Optional<Ice.ACMClose>(Ice.ACMClose.CloseOff),
                    new Ice.Optional<Ice.ACMHeartbeat>(Ice.ACMHeartbeat.HeartbeatAlways));

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
