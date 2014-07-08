drop table `user`;

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(256) DEFAULT NULL,
  `password` varchar(200) DEFAULT NULL,
  `gender` tinyint(4) DEFAULT NULL,
  `remark` text,
  PRIMARY KEY (`id`)
);