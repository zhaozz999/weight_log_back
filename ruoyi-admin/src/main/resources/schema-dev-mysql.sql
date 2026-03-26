create table if not exists app_user (
    user_id bigint primary key auto_increment,
    open_id varchar(64) not null,
    nick_name varchar(64) not null,
    avatar_url varchar(255) null,
    gender varchar(16) null,
    status char(1) not null default '0',
    last_login_time datetime null,
    create_time datetime null,
    update_time datetime null,
    remark varchar(255) null,
    unique key uk_app_user_open_id (open_id)
);
