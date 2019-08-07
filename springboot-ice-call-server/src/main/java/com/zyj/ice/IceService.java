package com.zyj.ice;

import Ice.Connection;
import Ice.ObjectPrx;
import com.alibaba.fastjson.JSON;
import com.zyj.ice.servant.ServerServant;

import java.util.ArrayList;
import java.util.List;
import com.zyj.ice.watch.ConnectWatcher;
import com.zyj.ice.watch.TheardWatcher;
import org.springframework.stereotype.Component;

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
 * @Date 2018/11/15 16:17
 */
@Component
public class IceService implements TheardWatcher,ConnectWatcher {

    ThreadPoolExecutor threadPoolExecutor=null;
    public  static List <Connection> map=new ArrayList<>();

    @PostConstruct
    void init() {
        ServerServant helloServant1=new ServerServant();
        helloServant1.addWatcher((TheardWatcher) this);
        helloServant1.addWatcher((ConnectWatcher) this);
        helloServant1.setPort("10006");
        ServerServant helloServant2=new ServerServant();
        helloServant2.setPort("10007");
        helloServant2.addWatcher((TheardWatcher)this);
        helloServant2.addWatcher((ConnectWatcher) this);
        LinkedBlockingQueue<Runnable> runnableList=new LinkedBlockingQueue<Runnable>();
        threadPoolExecutor= new ThreadPoolExecutor(100,100,0, TimeUnit.SECONDS,runnableList);
        threadPoolExecutor.execute(helloServant1);
        threadPoolExecutor.execute(helloServant2);

    }


    @Override
    public void restart(Runnable runnable) {
        threadPoolExecutor.remove(runnable);
        threadPoolExecutor.execute(runnable);
    }

    @Override
    public void remove(ObjectPrx objectPrx, Connection connection) {
        map.remove(connection);
        System.out.println(JSON.toJSONString(map));
    }

    @Override
    public void add(ObjectPrx objectPrx, Connection connection) {
        map.add(connection);
        System.out.println(JSON.toJSONString(map));
    }
}
