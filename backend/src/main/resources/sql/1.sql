
CREATE  TABLE wx_user(
    id SERIAL8 PRIMARY KEY NOT NULL,
    open_id VARCHAR (100) NOT NULL DEFAULT '',
    user_name VARCHAR(150) NOT NULL DEFAULT '',
    create_time BIGINT NOT NULL DEFAULT 0,
    nick_name VARCHAR(150) NOT NULL DEFAULT '',
    head_img   VARCHAR(500) NOT NULL  DEFAULT '',
    city VARCHAR(50) NOT NULL DEFAULT '',
    gender INT NOT NULL DEFAULT 0,
    mobile VARCHAR(20) NOT NULL DEFAULT '',
    password VARCHAR(150) NOT NULL DEFAULT '',
    user_type INT  NOT NULL DEFAULT 0 ,-- 1管理员
    session_key VARCHAR(300) DEFAULT '' NOT NULL;
)






--常用备忘
CREATE INDEX post_time_idx ON post (timestamp);
update board_info SET(new_post_id,old_post_id,last_board_page) =(0,0,0)
delete from post where id not in (select max(id) from post group by (board_name,post_id))