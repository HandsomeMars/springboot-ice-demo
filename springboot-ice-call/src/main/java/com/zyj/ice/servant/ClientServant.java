package com.zyj.ice.servant;

import Ice.Current;
import org.springframework.stereotype.Service;
import slice2java._IServerCallBackDisp;


/**
 * @author by zyj
 * @version V1.0
 * @Description:
 * @Date 2019/8/6 21:13
 */
@Service
public class ClientServant extends _IServerCallBackDisp {


    /**
     * 客户端处理服务端返回
     *
     * @param msg
     * @param __current The Current object for the invocation.
     **/

    @Override
    public boolean response(String msg, Current __current) {
        System.out.println("serverCallBack:" + msg);
        return false;
    }
}
