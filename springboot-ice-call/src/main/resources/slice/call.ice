#include <F:\Mysoft\Zeroc\ice3.6.2\slice\Ice\Identity.ice>
module slice2java{

interface IServerCallBack {
bool response(string msg);
};

interface IServer {
 bool setCallBack(Ice::Identity id);
 bool request(string msg);
 };
};
