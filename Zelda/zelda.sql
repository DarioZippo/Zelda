DROP DATABASE if exists zelda; 
CREATE DATABASE zelda; 
USE zelda; 

DROP TABLE IF EXISTS `record`;

CREATE TABLE `record` (
  `user` varchar(20) NOT NULL,
  `points` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`user`, `points`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

SELECT *
FROM record;