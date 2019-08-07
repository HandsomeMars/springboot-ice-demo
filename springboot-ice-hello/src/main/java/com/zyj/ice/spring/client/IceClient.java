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
 * @Date 2018/11/15 17:20
 */
@Service
public class IceClient implements Runnable{

    private static final String SERVER_NAME="Hello";
    private static final String SERVER_ENDPOINT="tcp -h 127.0.0.1 -p 10006";


    @PostConstruct
    private void startIceClient() {
        LinkedBlockingQueue<Runnable> runnableList=new LinkedBlockingQueue<Runnable>();
        ThreadPoolExecutor threadPoolExecutor= new ThreadPoolExecutor(100,100,1L, TimeUnit.SECONDS,runnableList);
        threadPoolExecutor.execute(this);
    }


    @Override
    public void run() {
        Ice.Communicator communicator = null;
        try {
            //初始化ice通信器communicator,可以使用args传入一下ice初始化的参数如超时时间，线程池大小等
            communicator = Ice.Util.initialize();
            // 传入远程服务单元的 ice对象标识符  协议默认tcp 主机 已经服务监听端口
            Ice.ObjectPrx op = communicator.stringToProxy(SERVER_NAME+":"+SERVER_ENDPOINT);
            // 检查通用客户端代理op 是不是queryServer对象标识符所关联的ice对象的代理
            HelloIPrx qp = HelloIPrxHelper.checkedCast(op);

            if(qp == null){
                throw new Exception("qp == null");
            }
            StringHolder holder=new StringHolder();
            int result = qp.hello("hello",holder);
            // 输出服务端返回结果
            System.out.println("client接受："+holder.value+" 时间："+System.currentTimeMillis());

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
