package com.zyj.ice.spring.client;

import Ice.StringHolder;
import org.springframework.stereotype.Service;
import slice2java.HelloIPrx;
import slice2java.HelloIPrxHelper;

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
public class IceClient implements Runnable{

    /**服务名*/
    private static final String SERVER_NAME="Hello";
    /**服务端点*/
    private static final String SERVER_ENDPOINT="tcp -h 127.0.0.1 -p 10006";


    @PostConstruct
    private void startIceClient() {
        //构造线程启动客户端
        LinkedBlockingQueue<Runnable> runnableList=new LinkedBlockingQueue<Runnable>();
        ThreadPoolExecutor threadPoolExecutor= new ThreadPoolExecutor(100,100,1L, TimeUnit.SECONDS,runnableList);
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
            Ice.ObjectPrx op = communicator.stringToProxy(SERVER_NAME+":"+SERVER_ENDPOINT);

            //检查通用客户端代理op 是不是queryServer对象标识符所关联的ice对象的代理
            HelloIPrx qp = HelloIPrxHelper.checkedCast(op);

            if(qp == null){
                throw new Exception("qp == null");
            }

            //测试发送信息到户无端
            StringHolder holder=new StringHolder();
            int result = qp.hello("hello",holder);

            // 输出服务端返回结果
            System.out.println("client请求结果："+holder.value+" 时间："+System.currentTimeMillis());

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
