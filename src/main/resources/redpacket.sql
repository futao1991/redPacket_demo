create database if not exists redis_packet;
use redis_packet;

create table `account` (
  `id` varchar(50) primary key not null,    -- 用户id
  `name` varchar(50) not null,              -- 用户姓名
  `money` bigint                            -- 账户余额
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table `record` (
  `id` integer primary key auto_increment, -- 主键id
  `user` varchar(50) not null,             -- 用户id
  `redpacket_id` varchar(50) not null,     -- 红包id
  `money` bigint not null,                 -- 红包金额
  `operation` varchar(50) not null,        -- 操作类型, 发或者抢
  `time` varchar(50) not null              -- 红包时间
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table `red_packet` (
  `id` varchar(50) primary key not null,   -- 红包id
  `user` varchar(50) not null,             -- 发该红包用户的id
  `money` bigint not null,                 -- 红包余额
  `number` int not null                    -- 红包数量
) ENGINE=InnoDB DEFAULT CHARSET=utf8;