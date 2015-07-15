

CREATE TABLE users
(
 firstName varchar (255),
 lastName varchar (255),
 email varchar (255),
 username  varchar (255) UNIQUE NOT NULL,
 password varchar (255) NOT NULL,
 friends TEXT,
 PRIMARY KEY (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


CREATE TABLE sessions
(
 username varchar (255) UNIQUE NOT NULL,
 sessionId varchar(128) UNIQUE NOT NULL,
 PRIMARY KEY (sessionId),
 FOREIGN KEY (username) REFERENCES users(username)
) ENGINE=InnoDB ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE chat_messages
(
 id BIGINT NOT NULL AUTO_INCREMENT UNIQUE,
 username VARCHAR (255) NOT NULL,
 chatRoom TEXT NOT NULL,
 time BIGINT NOT NULL,
 message TEXT NOT NULL,
 PRIMARY KEY (id),
 UNIQUE (username, time),
 FOREIGN KEY (username) REFERENCES users(username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE games
(
 id BIGINT NOT NULL AUTO_INCREMENT UNIQUE,
 owner VARCHAR (255) NOT NULL,
 users VARCHAR (10000),
 boardType VARCHAR (255),
 PRIMARY KEY (id),
 FOREIGN KEY (owner) REFERENCES users(username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE moves
(
 id BIGINT NOT NULL AUTO_INCREMENT UNIQUE,
 gameId BIGINT NOT NULL,
 country VARCHAR (3),
 evaluation ENUM('pending','success','failure','invalid'),
 moveText varchar(20),
 stateNumber MEDIUMINT,
 date BIGINT NOT NULL,
 PRIMARY KEY (id),
 FOREIGN KEY (gameId) REFERENCES games(id)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE gamestate
(
 id BIGINT NOT NULL AUTO_INCREMENT UNIQUE,
 gameId BIGINT NOT NULL,
 board VARCHAR(10000),
 stateNumber MEDIUMINT,
 PRIMARY KEY (id),
 FOREIGN KEY (gameId) REFERENCES games(id)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


#
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('tester1','ONE','tester1@email.com','user1','user1');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('tester2','TWO','tester2@email.com','user2','user2');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('tester3','THREE','tester3@email.com','user3','user3');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('tester4','FOUR','tester4@email.com','user4','user4');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('tester5','FIVE','tester5@email.com','user5','user5');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('tester6','SIX','tester6@email.com','user6','user6');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('tester7','SEVEN','tester7@email.com','user7','user7');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('tester8','EIGHT','tester8@email.com','user8','user8');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('tester9','NINE','tester9@email.com','user9','user9');
#
#
#
##jim
#
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('jim1','ONE','jim1@email.com','jim1','jim1');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('jim2','TWO','jim2@email.com','jim2','jim2');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('jim3','THREE','jim3@email.com','jim3','jim3');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('jim4','FOUR','jim4@email.com','jim4','jim4');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('jim5','FIVE','jim5@email.com','jim5','jim5');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('jim6','SIX','jim6@email.com','jim6','jim6');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('jim7','SEVEN','jim7@email.com','jim7','jim7');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('jim8','EIGHT','jim8@email.com','jim8','jim8');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('jim9','NINE','jim9@email.com','jim9','jim9');
#
#
##seth
#
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('seth1','ONE','seth1@email.com','seth1','seth1');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('seth2','TWO','seth2@email.com','seth2','seth2');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('seth3','THREE','seth3@email.com','seth3','seth3');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('seth4','FOUR','seth4@email.com','seth4','seth4');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('seth5','FIVE','seth5@email.com','seth5','seth5');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('seth6','SIX','seth6@email.com','seth6','seth6');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('seth7','SEVEN','seth7@email.com','seth7','seth7');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('seth8','EIGHT','seth8@email.com','seth8','seth8');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('seth9','NINE','seth9@email.com','seth9','seth9');
#
##ian
#
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('ian1','ONE','ian1@email.com','ian1','ian1');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('ian2','TWO','ian2@email.com','ian2','ian2');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('ian3','THREE','ian3@email.com','ian3','ian3');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('ian4','FOUR','ian4@email.com','ian4','ian4');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('ian5','FIVE','ian5@email.com','ian5','ian5');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('ian6','SIX','ian6@email.com','ian6','ian6');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('ian7','SEVEN','ian7@email.com','ian7','ian7');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('ian8','EIGHT','ian8@email.com','ian8','ian8');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('ian9','NINE','iand9@email.com','ian9','ian9');
#
##will
#
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('will1','ONE','will1@email.com','will1','will1');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('will2','TWO','will2@email.com','will2','will2');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('will3','THREE','will3@email.com','will3','will3');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('will4','FOUR','will4@email.com','will4','will4');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('will5','FIVE','will5@email.com','will5','will5');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('will6','SIX','will6@email.com','will6','will6');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('will7','SEVEN','will7@email.com','will7','will7');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('will8','EIGHT','will8@email.com','will8','will8');
#INSERT INTO users (firstName, lastName, email, username, password) VALUES ('will9','NINE','will9@email.com','will9','will9');
#
#
#
##insert into games (owner,users,boardType) values ('user1','{"ENG":"user1","FRA":"user2","AUS":"user3","ITA":"user4","GER":"user5","RUS":"user6","TUR":"user7"}','EUROPE');
##insert into gamestate (gameId, stateNumber, board) values('1','0','{"ENG":{"LON":"F","EDI":"F","LVP":"A"},"FRA":{"PAR":"A","MAR":"A","BRE":"F"},"AUS":{"VIE":"A","BUD":"A","TRI":"F"},"ITA":{"ROM":"A","VEN":"A","NAP":"F"},"GER":{"BER":"A","MUN":"A","KIE":"F"},"RUS":{"MOS":"A","SEV":"F","WAR":"A","STPS":"F"},"TUR":{"ANK":"F","CON":"A","SMY":"A"},"supply":{"LON":"ENG","EDI":"ENG","LVP":"ENG","PAR":"FRA","MAR":"FRA","BRE":"FRA","VIE":"AUS","BUD":"AUS","TRI":"AUS","ROM":"ITA","VEN":"ITA","NAP":"ITA","BER":"GER","MUN":"GER","KIE":"GER","MOS":"RUS","SEV":"RUS","WAR":"RUS","STP":"RUS","ANK":"TUR","CON":"TUR","SMY":"TUR","POR":"NON","SPA":"NON","TUN":"NON","BEL":"NON","HOL":"NON","DEN":"NON","NWY":"NON","SWE":"NON","SER":"NON","BUL":"NON","RUM":"NON","GRE":"NON"}}');
##insert into games (owner,users,boardType) values ('will1','{"ENG":"will1","FRA":"will2","AUS":"will3","ITA":"will4","GER":"will5","RUS":"will6","TUR":"will7"}','EUROPE');
##insert into gamestate (gameId, stateNumber, board) values('2','0','{"ENG":{"LON":"F","EDI":"F","LVP":"A"},"FRA":{"PAR":"A","MAR":"A","BRE":"F"},"AUS":{"VIE":"A","BUD":"A","TRI":"F"},"ITA":{"ROM":"A","VEN":"A","NAP":"F"},"GER":{"BER":"A","MUN":"A","KIE":"F"},"RUS":{"MOS":"A","SEV":"F","WAR":"A","STPS":"F"},"TUR":{"ANK":"F","CON":"A","SMY":"A"},"supply":{"LON":"ENG","EDI":"ENG","LVP":"ENG","PAR":"FRA","MAR":"FRA","BRE":"FRA","VIE":"AUS","BUD":"AUS","TRI":"AUS","ROM":"ITA","VEN":"ITA","NAP":"ITA","BER":"GER","MUN":"GER","KIE":"GER","MOS":"RUS","SEV":"RUS","WAR":"RUS","STP":"RUS","ANK":"TUR","CON":"TUR","SMY":"TUR","POR":"NON","SPA":"NON","TUN":"NON","BEL":"NON","HOL":"NON","DEN":"NON","NWY":"NON","SWE":"NON","SER":"NON","BUL":"NON","RUM":"NON","GRE":"NON"}}');
##insert into games (owner,users,boardType) values ('ian1','{"ENG":"ian1","FRA":"ian2","AUS":"ian3","ITA":"ian4","GER":"ian5","RUS":"ian6","TUR":"ian7"}','EUROPE');
##insert into gamestate (gameId, stateNumber, board) values('3','0','{"ENG":{"LON":"F","EDI":"F","LVP":"A"},"FRA":{"PAR":"A","MAR":"A","BRE":"F"},"AUS":{"VIE":"A","BUD":"A","TRI":"F"},"ITA":{"ROM":"A","VEN":"A","NAP":"F"},"GER":{"BER":"A","MUN":"A","KIE":"F"},"RUS":{"MOS":"A","SEV":"F","WAR":"A","STPS":"F"},"TUR":{"ANK":"F","CON":"A","SMY":"A"},"supply":{"LON":"ENG","EDI":"ENG","LVP":"ENG","PAR":"FRA","MAR":"FRA","BRE":"FRA","VIE":"AUS","BUD":"AUS","TRI":"AUS","ROM":"ITA","VEN":"ITA","NAP":"ITA","BER":"GER","MUN":"GER","KIE":"GER","MOS":"RUS","SEV":"RUS","WAR":"RUS","STP":"RUS","ANK":"TUR","CON":"TUR","SMY":"TUR","POR":"NON","SPA":"NON","TUN":"NON","BEL":"NON","HOL":"NON","DEN":"NON","NWY":"NON","SWE":"NON","SER":"NON","BUL":"NON","RUM":"NON","GRE":"NON"}}');
##insert into games (owner,users,boardType) values ('jim1','{"ENG":"jim1","FRA":"jim2","AUS":"jim3","ITA":"jim4","GER":"jim5","RUS":"jim6","TUR":"jim7"}','EUROPE');
##insert into gamestate (gameId, stateNumber, board) values('4','0','{"ENG":{"LON":"F","EDI":"F","LVP":"A"},"FRA":{"PAR":"A","MAR":"A","BRE":"F"},"AUS":{"VIE":"A","BUD":"A","TRI":"F"},"ITA":{"ROM":"A","VEN":"A","NAP":"F"},"GER":{"BER":"A","MUN":"A","KIE":"F"},"RUS":{"MOS":"A","SEV":"F","WAR":"A","STPS":"F"},"TUR":{"ANK":"F","CON":"A","SMY":"A"},"supply":{"LON":"ENG","EDI":"ENG","LVP":"ENG","PAR":"FRA","MAR":"FRA","BRE":"FRA","VIE":"AUS","BUD":"AUS","TRI":"AUS","ROM":"ITA","VEN":"ITA","NAP":"ITA","BER":"GER","MUN":"GER","KIE":"GER","MOS":"RUS","SEV":"RUS","WAR":"RUS","STP":"RUS","ANK":"TUR","CON":"TUR","SMY":"TUR","POR":"NON","SPA":"NON","TUN":"NON","BEL":"NON","HOL":"NON","DEN":"NON","NWY":"NON","SWE":"NON","SER":"NON","BUL":"NON","RUM":"NON","GRE":"NON"}}');
##insert into games (owner,users,boardType) values ('seth1','{"ENG":"seth1","FRA":"seth2","AUS":"seth3","ITA":"seth4","GER":"seth5","RUS":"seth6","TUR":"seth7"}','EUROPE');
##insert into gamestate (gameId, stateNumber, board) values('5','0','{"ENG":{"LON":"F","EDI":"F","LVP":"A"},"FRA":{"PAR":"A","MAR":"A","BRE":"F"},"AUS":{"VIE":"A","BUD":"A","TRI":"F"},"ITA":{"ROM":"A","VEN":"A","NAP":"F"},"GER":{"BER":"A","MUN":"A","KIE":"F"},"RUS":{"MOS":"A","SEV":"F","WAR":"A","STPS":"F"},"TUR":{"ANK":"F","CON":"A","SMY":"A"},"supply":{"LON":"ENG","EDI":"ENG","LVP":"ENG","PAR":"FRA","MAR":"FRA","BRE":"FRA","VIE":"AUS","BUD":"AUS","TRI":"AUS","ROM":"ITA","VEN":"ITA","NAP":"ITA","BER":"GER","MUN":"GER","KIE":"GER","MOS":"RUS","SEV":"RUS","WAR":"RUS","STP":"RUS","ANK":"TUR","CON":"TUR","SMY":"TUR","POR":"NON","SPA":"NON","TUN":"NON","BEL":"NON","HOL":"NON","DEN":"NON","NWY":"NON","SWE":"NON","SER":"NON","BUL":"NON","RUM":"NON","GRE":"NON"}}');
##
##insert into games (owner,users,boardType) values ('user1','{"ENG":"jim2","FRA":"seth1","AUS":"ian1","ITA":"will1","GER":"user1","RUS":"user2","TUR":"user3"}','EUROPE');
##insert into gamestate (gameId, stateNumber, board) values('6','0','{"ENG":{"LON":"F","EDI":"F","LVP":"A"},"FRA":{"PAR":"A","MAR":"A","BRE":"F"},"AUS":{"VIE":"A","BUD":"A","TRI":"F"},"ITA":{"ROM":"A","VEN":"A","NAP":"F"},"GER":{"BER":"A","MUN":"A","KIE":"F"},"RUS":{"MOS":"A","SEV":"F","WAR":"A","STPS":"F"},"TUR":{"ANK":"F","CON":"A","SMY":"A"},"supply":{"LON":"ENG","EDI":"ENG","LVP":"ENG","PAR":"FRA","MAR":"FRA","BRE":"FRA","VIE":"AUS","BUD":"AUS","TRI":"AUS","ROM":"ITA","VEN":"ITA","NAP":"ITA","BER":"GER","MUN":"GER","KIE":"GER","MOS":"RUS","SEV":"RUS","WAR":"RUS","STP":"RUS","ANK":"TUR","CON":"TUR","SMY":"TUR","POR":"NON","SPA":"NON","TUN":"NON","BEL":"NON","HOL":"NON","DEN":"NON","NWY":"NON","SWE":"NON","SER":"NON","BUL":"NON","RUM":"NON","GRE":"NON"}}');
#

#
# Quartz seems to work best with the driver mm.mysql-2.0.7-bin.jar
#
# PLEASE consider using mysql with innodb tables to avoid locking issues
#
# In your Quartz properties file, youll need to set 
# org.quartz.jobStore.driverDelegateClass = org.quartz.impl.jdbcjobstore.StdJDBCDelegate
#

DROP TABLE IF EXISTS QRTZ_FIRED_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_PAUSED_TRIGGER_GRPS;
DROP TABLE IF EXISTS QRTZ_SCHEDULER_STATE;
DROP TABLE IF EXISTS QRTZ_LOCKS;
DROP TABLE IF EXISTS QRTZ_SIMPLE_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_SIMPROP_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_CRON_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_BLOB_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_JOB_DETAILS;
DROP TABLE IF EXISTS QRTZ_CALENDARS;


CREATE TABLE QRTZ_JOB_DETAILS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    JOB_NAME  VARCHAR(200) NOT NULL,
    JOB_GROUP VARCHAR(200) NOT NULL,
    DESCRIPTION VARCHAR(250) NULL,
    JOB_CLASS_NAME   VARCHAR(250) NOT NULL,
    IS_DURABLE VARCHAR(1) NOT NULL,
    IS_NONCONCURRENT VARCHAR(1) NOT NULL,
    IS_UPDATE_DATA VARCHAR(1) NOT NULL,
    REQUESTS_RECOVERY VARCHAR(1) NOT NULL,
    JOB_DATA BLOB NULL,
    PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
);

CREATE TABLE QRTZ_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    JOB_NAME  VARCHAR(200) NOT NULL,
    JOB_GROUP VARCHAR(200) NOT NULL,
    DESCRIPTION VARCHAR(250) NULL,
    NEXT_FIRE_TIME BIGINT(13) NULL,
    PREV_FIRE_TIME BIGINT(13) NULL,
    PRIORITY INTEGER NULL,
    TRIGGER_STATE VARCHAR(16) NOT NULL,
    TRIGGER_TYPE VARCHAR(8) NOT NULL,
    START_TIME BIGINT(13) NOT NULL,
    END_TIME BIGINT(13) NULL,
    CALENDAR_NAME VARCHAR(200) NULL,
    MISFIRE_INSTR SMALLINT(2) NULL,
    JOB_DATA BLOB NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
        REFERENCES QRTZ_JOB_DETAILS(SCHED_NAME,JOB_NAME,JOB_GROUP)
);

CREATE TABLE QRTZ_SIMPLE_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    REPEAT_COUNT BIGINT(7) NOT NULL,
    REPEAT_INTERVAL BIGINT(12) NOT NULL,
    TIMES_TRIGGERED BIGINT(10) NOT NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);

CREATE TABLE QRTZ_CRON_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    CRON_EXPRESSION VARCHAR(200) NOT NULL,
    TIME_ZONE_ID VARCHAR(80),
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);

CREATE TABLE QRTZ_SIMPROP_TRIGGERS
  (          
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    STR_PROP_1 VARCHAR(512) NULL,
    STR_PROP_2 VARCHAR(512) NULL,
    STR_PROP_3 VARCHAR(512) NULL,
    INT_PROP_1 INT NULL,
    INT_PROP_2 INT NULL,
    LONG_PROP_1 BIGINT NULL,
    LONG_PROP_2 BIGINT NULL,
    DEC_PROP_1 NUMERIC(13,4) NULL,
    DEC_PROP_2 NUMERIC(13,4) NULL,
    BOOL_PROP_1 VARCHAR(1) NULL,
    BOOL_PROP_2 VARCHAR(1) NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP) 
    REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);

CREATE TABLE QRTZ_BLOB_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    BLOB_DATA BLOB NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);

CREATE TABLE QRTZ_CALENDARS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    CALENDAR_NAME  VARCHAR(200) NOT NULL,
    CALENDAR BLOB NOT NULL,
    PRIMARY KEY (SCHED_NAME,CALENDAR_NAME)
);

CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_GROUP  VARCHAR(200) NOT NULL, 
    PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP)
);

CREATE TABLE QRTZ_FIRED_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    ENTRY_ID VARCHAR(95) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    INSTANCE_NAME VARCHAR(200) NOT NULL,
    FIRED_TIME BIGINT(13) NOT NULL,
    SCHED_TIME BIGINT(13) NOT NULL,
    PRIORITY INTEGER NOT NULL,
    STATE VARCHAR(16) NOT NULL,
    JOB_NAME VARCHAR(200) NULL,
    JOB_GROUP VARCHAR(200) NULL,
    IS_NONCONCURRENT VARCHAR(1) NULL,
    REQUESTS_RECOVERY VARCHAR(1) NULL,
    PRIMARY KEY (SCHED_NAME,ENTRY_ID)
);

CREATE TABLE QRTZ_SCHEDULER_STATE
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    INSTANCE_NAME VARCHAR(200) NOT NULL,
    LAST_CHECKIN_TIME BIGINT(13) NOT NULL,
    CHECKIN_INTERVAL BIGINT(13) NOT NULL,
    PRIMARY KEY (SCHED_NAME,INSTANCE_NAME)
);

CREATE TABLE QRTZ_LOCKS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    LOCK_NAME  VARCHAR(40) NOT NULL, 
    PRIMARY KEY (SCHED_NAME,LOCK_NAME)
);


commit;
