基于springboot和redis实现的抢红包demo

发红包接口
http://localhost:9006/redpacket/create
参数:
{
  "userId": 用户id,
  "totalMoney": 红包金额,
  "number": 红包数量
}
返回结果:
{
  "redPacketId": 红包id,
  "message": 备注信息,
  "errMsg": 错误信息
}

抢红包接口:
http://localhost:9006/redpacket/create?userId=${用户id}&redPacketId=${红包id}
返回结果:
{
  "money", 抢到的红包金额,
  "message": 备注信息,
  "errMsg": 错误信息
}