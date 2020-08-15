local uuid = string.format("redPacket-%s", KEYS[2])
local record_id = string.format("%s-%s", KEYS[1], KEYS[2])
if (redis.call('EXISTS', uuid) ~= 1) then
   return -2 -- not exists
end

if (redis.call('HEXISTS', 'redPacket_record', record_id) == 1) then
   return -1 -- has get red packet
end

local total_money = redis.call('GET', uuid);
local expire_time = redis.call('TTL', uuid);
local total_num = redis.call('HGET', 'redPacket_num', KEYS[2]);
if tonumber(total_num) > 0 then
   if (tonumber(total_num) == 1) then
      redis.call('HSET', 'redPacket_record', record_id, total_money)
      redis.call('SET', uuid, '0')
      redis.call('EXPIRE', uuid, expire_time)
      redis.call('HSET', 'redPacket_num', KEYS[2], '0')
      return total_money + 0
   else
      local money = math.random(1, total_money - 1)
      redis.call('HSET', 'redPacket_record', record_id, money)
      redis.call('SET', uuid, total_money - money)
      redis.call('EXPIRE', uuid, expire_time)
      redis.call('HSET', 'redPacket_num', KEYS[2], total_num - 1)
      return money
   end
else
   return 0
end