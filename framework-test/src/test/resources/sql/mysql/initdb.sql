-- ----------------------------
-- Table structure for DEMO
-- ----------------------------
DROP TABLE IF EXISTS DEMO;
CREATE TABLE DEMO (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  NAME varchar(765) DEFAULT NULL,
  PHONE varchar(765) DEFAULT NULL,
  ADDRESS varchar(765) DEFAULT NULL,
  REVIEW tinyint(1) DEFAULT NULL,
  PRIMARY KEY (ID)
) ;
