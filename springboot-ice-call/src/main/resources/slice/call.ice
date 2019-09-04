#include <E:\SpringWorkSpace\springboot-ice-demo\springboot-ice-call\src\main\resources\lib\Identity.ice>
module slice2java{
/****************
  ice客户端
*****************/
interface IServerCallBack {
/****************
  客户端处理服务端返回
*****************/
bool response(string msg);
};

/***************
  ice服务端
****************/
interface IServer {
/***************
  服务端设置客户端回调对象
****************/
 bool setCallBack(Ice::Identity id);
/***************
  服务端处理客户端请求
****************/
 bool request(string msg);
 };
};
