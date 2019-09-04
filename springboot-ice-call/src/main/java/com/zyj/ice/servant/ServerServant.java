package com.zyj.ice.servant;

import Ice.*;
import org.springframework.stereotype.Service;
import slice2java.IServerCallBackPrx;
import slice2java.IServerCallBackPrxHelper;
import slice2java._IServerDisp;


/**
 * @author by zyj
 * @version V1.0
 * @Description:
 * @Date 2019/8/6 21:13
 */
@Service
public class ServerServant extends _IServerDisp {


    /**
     * 服务端设置客户端回调对象
     *
     * @param id
     * @param __current The Current object for the invocation.
     **/
    @Override
    public boolean setCallBack(Identity id, Current __current) {
        IServerCallBackPrx iServerCallBackPrx = IServerCallBackPrxHelper.uncheckedCast(__current.con.createProxy(id));
        iServerCallBackPrx.ice_getConnection().setCallback(new ConnectionCallback() {

            @Override
            public void heartbeat(Connection c) {

                System.out.println("sn:" + " client heartbeat....");
            }

            @Override
            public void closed(Connection c) {

                System.out.println("sn:" + " " + "closed....");
            }
        });
        // 每30/2 s向对方做心跳
        // 客户端向服务端做心跳 服务端打印服务端的con.setCallback(new Ice.ConnectionCallback()
        iServerCallBackPrx.ice_getConnection().setACM(new IntOptional(10), new Optional<ACMClose>(ACMClose.CloseOff),
                new Optional<ACMHeartbeat>(ACMHeartbeat.HeartbeatAlways));

        return true;
    }

    /**
     * 服务端处理客户端请求
     *
     * @param msg
     * @param __current The Current object for the invocation.
     **/
    @Override
    public boolean request(String msg, Current __current) {
        System.out.println("client:" + msg);
        return false;
    }


}
