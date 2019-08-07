package com.zyj.ice.servant;

import Ice.*;
import com.zyj.ice.watch.ConnectWatched;
import com.zyj.ice.watch.ConnectWatcher;
import com.zyj.ice.watch.TheardWatched;
import com.zyj.ice.watch.TheardWatcher;
import slice2java.ISwitchCallbackPrx;
import slice2java.ISwitchCallbackPrxHelper;
import slice2java._ISwitchDisp;

import java.lang.Exception;
import java.util.ArrayList;
import java.util.List;

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
 * @Date 2018/11/15 16:49
 */
public class ServerServant extends _ISwitchDisp implements Runnable,TheardWatched,ConnectWatched{
    private static final String ADAPTER_NAME="helloAdapter";
    private static final String SERVICE_NAME="SwitchServer";
    private static final String SERVICE_IP="127.0.0.1";
    private static final String SERVICE_PORT="10006";

    static int i=0;
    public ServerServant(){

    }
    public ServerServant(String adapterName, String serviceName, String ip, String port) {
        this.adapterName = adapterName;
        this.serviceName = serviceName;
        this.ip = ip;
        this.port = port;
    }


    public String getAdapterName() {
        return adapterName;
    }

    public void setAdapterName(String adapterName) {
        this.adapterName = adapterName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    private String adapterName=ADAPTER_NAME;
    private String serviceName=SERVICE_NAME;
    private String ip=SERVICE_IP;
    private String port=SERVICE_PORT;





    @Override
    public void run() {

        Ice.Communicator communicator = null;
        try {
            //初始化ice通信器communicator,可以使用args传入一下ice初始化的参数如超时时间，线程池大小等
            communicator = Ice.Util.initialize();

           //创建一个名为queryEmployeeAdapter的适配器并且默认使用tcp协议  服务部署在10.4.30.81机器上 服务开启10006监听端口
            Ice.ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints(this.adapterName,"tcp -p "+this.port);

            // 将servant与ice对象标识符建立映射关系，并添加到ice对象适配器中
            adapter.add(this, Ice.Util.stringToIdentity(this.serviceName));
            // 激活对象适配器
            adapter.activate();

            //模拟启动异常
//            i++;
//            if(i<50){
//                int b = Integer.parseInt("aa");
//            }

            System.out.println(this.adapterName+this.getPort()+" activate :"+this.serviceName);
            // 服务在退出之前一直保持监听状态
            communicator.waitForShutdown();

        } catch (Exception e) {
            // TODO: handle exception
            state = 1;
            System.out.println(e);
            notifyWatchers();
        } finally{
            if(communicator != null){
                communicator.destroy();
            }
        }
        System.out.println("state: "+ state);

    }

    private void holdHeartbeat(Connection connection) {
         connection.setCallback(new Ice.ConnectionCallback() {
        @Override
        public void heartbeat(Ice.Connection c) {
            System.out.println("sn:"  + " client heartbeat....");
        }

        @Override
        public void closed(Ice.Connection c) {
            System.out.println("sn:" + " " + "closed....");
        }
    });


    }


    public  int getState() {
        return state;
    }

    public  void setState(int state) {
        this.state = state;
    }

    private    int state = 0;



    @Override
    public boolean heartbeat(Identity id,Current __current) {
            ISwitchCallbackPrx iSwitchCallbackPrx=ISwitchCallbackPrxHelper.uncheckedCast(__current.con.createProxy(id));
            notifyWatchersToAdd(iSwitchCallbackPrx,iSwitchCallbackPrx.ice_getConnection());
            iSwitchCallbackPrx.ice_getConnection().setCallback(new ConnectionCallback() {

                @Override
                public void heartbeat(Ice.Connection c) {

                    System.out.println("sn:"  + " client heartbeat....");
                }

                @Override
                public void closed(Ice.Connection c) {
                    notifyWatchersToRemove(iSwitchCallbackPrx,c);
                    System.out.println("sn:" + " " + "closed....");
                }
            });
//     每30/2 s向对方做心跳
//     客户端向服务端做心跳 服务端打印服务端的con.setCallback(new Ice.ConnectionCallback()
        iSwitchCallbackPrx.ice_getConnection().setACM(new Ice.IntOptional(10), new Ice.Optional<Ice.ACMClose>(Ice.ACMClose.CloseOff),
                new Ice.Optional<Ice.ACMHeartbeat>(Ice.ACMHeartbeat.HeartbeatAlways));

        return true;
    }

    @Override
    public boolean callBack(String msg, Current __current) {
        System.out.println("server:"+msg);
//        holdHeartbeat(__current.con);
        return false;
    }

    private List<TheardWatcher> list = new ArrayList<TheardWatcher>();
    private List<ConnectWatcher> list2 = new ArrayList<ConnectWatcher>();
    @Override
    public void addWatcher(TheardWatcher watcher) {
        list.add(watcher);
    }

    @Override
    public void removeWatcher(TheardWatcher watcher) {
        list.remove(watcher);
    }

    @Override
    public void addWatcher(ConnectWatcher watcher) {
        list2.add(watcher);
    }

    @Override
    public void removeWatcher(ConnectWatcher watcher) {
        list.remove(watcher);
    }

    @Override
    public void notifyWatchersToRemove(ObjectPrx objectPrx, Connection connection) {
        if(list.size()>0){
            for(ConnectWatcher watcher:list2){
                watcher.remove(objectPrx,connection);
            }
        }
    }

    @Override
    public void notifyWatchersToAdd(ObjectPrx objectPrx, Connection connection) {
        if(list.size()>0){
            for(ConnectWatcher watcher:list2){
                watcher.add(objectPrx,connection);
            }
        }
    }



    @Override
    public void notifyWatchers() {
         if(list.size()>0){
             for(TheardWatcher watcher:list){
                 watcher.restart(this);
             }
         }

    }
}
