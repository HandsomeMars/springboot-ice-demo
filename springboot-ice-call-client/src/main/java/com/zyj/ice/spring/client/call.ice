#include <F:\Mysoft\Zeroc\ice3.6.2\slice\Ice\Identity.ice>
module slice2java{
interface ISwitchCallback {
bool sendMsg(string msg);
};
interface ISwitch {
 bool heartbeat(Ice::Identity id);
 bool callBack(string msg);
 };
 };
