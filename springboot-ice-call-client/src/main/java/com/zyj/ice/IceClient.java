package com.zyj.ice;

import Ice.Connection;
import Ice.Identity;
import Ice.LocalException;
import Ice.StringHolder;
import com.zyj.ice.servant.ClientServant;
import slice2java.*;

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
public class IceClient {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Ice.Communicator communicator = null;
        try {

            communicator=Ice.Util.initialize(args);

            // 传入远程服务单元的 ice对象标识符  协议默认tcp 主机 已经服务监听端口
            Ice.ObjectPrx op = communicator.stringToProxy("SwitchServer:tcp -h 127.0.0.1 -p 14331");
            // 检查通用客户端代理op 是不是queryServer对象标识符所关联的ice对象的代理
            ISwitchPrx qp = ISwitchPrxHelper.checkedCast(op);
            if(qp == null){
                throw new Exception("qp == null");
            }
            //保持链接 发送ping包
//            qp.ice_ping();


            //创建客户端服务
            Ice.ObjectAdapter adapter = communicator.createObjectAdapter("");
            Ice.Identity id =new Identity();
            id.name="SwitchClient";
            id.category="";
            ClientServant clientServant=new ClientServant();
            adapter.add(clientServant,id);
            adapter.activate();
            //客户端服务设置服务端点
            qp.ice_getConnection().setAdapter(adapter);

            System.out.println(qp.ice_getConnection().getEndpoint()._toString());

            //设置回调对象
            qp.heartbeat(id);
            Thread.sleep(1000);

            //设置心跳
            holdHeartbeat(qp.ice_getConnection());
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
        }

//        communicator.waitForShutdown();
    }

    private static void holdHeartbeat(Connection connection) {
        connection.setCallback(new Ice.ConnectionCallback() {
            @Override
            public void heartbeat(Ice.Connection c) {
                System.out.println("sn:"  + " server heartbeat....");
            }

            @Override
            public void closed(Ice.Connection c) {
                System.out.println("sn:" + " " + "closed....");
            }
        });
        // 每30/2 s向对方做心跳
        // 客户端向服务端做心跳 服务端打印服务端的con.setCallback(new Ice.ConnectionCallback()
        connection.setACM(new Ice.IntOptional(10), new Ice.Optional<Ice.ACMClose>(Ice.ACMClose.CloseOff),
                new Ice.Optional<Ice.ACMHeartbeat>(Ice.ACMHeartbeat.HeartbeatAlways));

    }

}
