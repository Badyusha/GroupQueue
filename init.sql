use group_queue;

CREATE TABLE `group` (
     `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
     `number` INT(10) UNSIGNED NOT NULL,
     PRIMARY KEY (`id`) USING BTREE,
     UNIQUE INDEX `number` (`number`) USING BTREE
)
    COLLATE='utf8mb4_general_ci'
    ENGINE=InnoDB
    AUTO_INCREMENT=33
;

CREATE TABLE `role` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `name` ENUM('USER','GROUP_ADMIN','SUDO') NOT NULL DEFAULT 'USER' COLLATE 'utf8mb4_general_ci',
    PRIMARY KEY (`id`) USING BTREE
)
    COLLATE='utf8mb4_general_ci'
    ENGINE=InnoDB
    AUTO_INCREMENT=4
;

CREATE TABLE `student` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `group_id` BIGINT(20) UNSIGNED NOT NULL,
    `role_id` BIGINT(20) UNSIGNED NOT NULL,
    `username` VARCHAR(65) NOT NULL COLLATE 'utf8mb4_general_ci',
    `first_name` VARCHAR(65) NOT NULL COLLATE 'utf8mb4_general_ci',
    `last_name` VARCHAR(65) NOT NULL COLLATE 'utf8mb4_general_ci',
    `password` VARCHAR(65) NOT NULL COLLATE 'utf8mb4_general_ci',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `username` (`username`) USING BTREE,
    INDEX `FK_student_role` (`role_id`) USING BTREE,
    INDEX `FK_student_group` (`group_id`) USING BTREE,
    CONSTRAINT `FK_student_group` FOREIGN KEY (`group_id`) REFERENCES `group` (`id`) ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT `FK_student_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON UPDATE RESTRICT ON DELETE RESTRICT
)
    COLLATE='utf8mb4_general_ci'
    ENGINE=InnoDB
    AUTO_INCREMENT=47
;

CREATE TABLE `schedule` (
    `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `subject_name` VARCHAR(20) NOT NULL COLLATE 'utf8mb4_general_ci',
    `subject_full_name` VARCHAR(100) NOT NULL COLLATE 'utf8mb4_general_ci',
    `subgroup_type` ENUM('FIRST','SECOND','ALL') NOT NULL COLLATE 'utf8mb4_general_ci',
    `start_time` TIME NOT NULL,
    `group_id` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
    `week_type` ENUM('FIRST','SECOND','THIRD','FOURTH') NOT NULL COLLATE 'utf8mb4_general_ci',
    `day_of_week` ENUM('MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY','SUNDAY') NOT NULL COLLATE 'utf8mb4_general_ci',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `FK_schedule_group` (`group_id`) USING BTREE,
    CONSTRAINT `FK_schedule_group` FOREIGN KEY (`group_id`) REFERENCES `group` (`id`) ON UPDATE RESTRICT ON DELETE RESTRICT
)
    COMMENT='schedule for group from BSUIR API'
    COLLATE='utf8mb4_general_ci'
    ENGINE=InnoDB
    AUTO_INCREMENT=369
;

CREATE TABLE `lesson` (
      `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
      `schedule_id` BIGINT(20) UNSIGNED NOT NULL,
      `sort_type` ENUM('SIMPLE','RANDOM','HIGHEST_LAB_COUNT','LOWEST_LAB_COUNT','HIGHEST_LAB_SUM','LOWEST_LAB_SUM') NOT NULL DEFAULT 'SIMPLE' COLLATE 'utf8mb4_general_ci',
      `date` DATE NOT NULL,
      PRIMARY KEY (`id`) USING BTREE,
      INDEX `FK_lesson_schedule` (`schedule_id`) USING BTREE,
      CONSTRAINT `FK_lesson_schedule` FOREIGN KEY (`schedule_id`) REFERENCES `schedule` (`id`) ON UPDATE RESTRICT ON DELETE CASCADE
)
    COMMENT='schedule for each subject, in concrete study week\r\nunique lesson (in full year)\r\nexm: 1-st of Sep'
    COLLATE='utf8mb4_general_ci'
    ENGINE=InnoDB
    AUTO_INCREMENT=159
;

CREATE TABLE `permission` (
      `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
      `name` ENUM('SHOW_BECOME_GROUP_ADMIN_REQUESTS','BECOME_GROUP_ADMIN','CHOOSE_SORT_TYPE') NOT NULL COLLATE 'utf8mb4_general_ci',
      PRIMARY KEY (`id`) USING BTREE
)
    COLLATE='utf8mb4_general_ci'
    ENGINE=InnoDB
    AUTO_INCREMENT=4
;

CREATE TABLE `permission_role` (
       `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
       `permission_id` BIGINT(20) UNSIGNED NOT NULL,
       `role_id` BIGINT(20) UNSIGNED NOT NULL,
       PRIMARY KEY (`id`) USING BTREE,
       INDEX `FK_permission_table_permission` (`permission_id`) USING BTREE,
       INDEX `FK_permission_table_role` (`role_id`) USING BTREE,
       CONSTRAINT `FK_permission_table_permission` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
       CONSTRAINT `FK_permission_table_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON UPDATE CASCADE ON DELETE CASCADE
)
    COLLATE='utf8mb4_general_ci'
    ENGINE=InnoDB
    AUTO_INCREMENT=5
;

CREATE TABLE `pre_queue` (
     `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
     `lesson_id` BIGINT(20) UNSIGNED NOT NULL,
     `student_id` BIGINT(20) UNSIGNED NOT NULL,
     `passing_labs` BLOB NOT NULL,
     `registration_time` TIME NOT NULL,
     PRIMARY KEY (`id`) USING BTREE,
     INDEX `FK_pre_queue_lesson` (`lesson_id`) USING BTREE,
     INDEX `FK_pre_queue_student` (`student_id`) USING BTREE,
     CONSTRAINT `FK_pre_queue_lesson` FOREIGN KEY (`lesson_id`) REFERENCES `lesson` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
     CONSTRAINT `FK_pre_queue_student` FOREIGN KEY (`student_id`) REFERENCES student (`id`) ON UPDATE CASCADE ON DELETE CASCADE
)
    COLLATE='utf8mb4_general_ci'
    ENGINE=InnoDB
    AUTO_INCREMENT=41
;

CREATE TABLE `queue` (
     `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
     `lesson_id` BIGINT(20) UNSIGNED NOT NULL,
     `student_id` BIGINT(20) UNSIGNED NOT NULL,
     `order` INT(10) UNSIGNED NOT NULL,
     PRIMARY KEY (`id`) USING BTREE,
     INDEX `FK_queue_student` (`student_id`) USING BTREE,
     INDEX `FK_queue_lesson` (`lesson_id`) USING BTREE,
     CONSTRAINT `FK_queue_lesson` FOREIGN KEY (`lesson_id`) REFERENCES `lesson` (`id`) ON UPDATE RESTRICT ON DELETE CASCADE,
     CONSTRAINT `FK_queue_student` FOREIGN KEY (`student_id`) REFERENCES student (`id`) ON UPDATE CASCADE ON DELETE CASCADE
)
    COLLATE='utf8mb4_general_ci'
    ENGINE=InnoDB
    AUTO_INCREMENT=7
;

CREATE TABLE `request` (
   `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
   `request_type` ENUM('BECOME_GROUP_ADMIN') NOT NULL COLLATE 'utf8mb4_general_ci',
   `student_id` BIGINT(20) UNSIGNED NOT NULL,
   PRIMARY KEY (`id`) USING BTREE,
   UNIQUE INDEX `student_id` (`student_id`) USING BTREE,
   CONSTRAINT `FK_request_student` FOREIGN KEY (`student_id`) REFERENCES student (`id`) ON UPDATE CASCADE ON DELETE CASCADE
)
    COLLATE='utf8mb4_general_ci'
    ENGINE=InnoDB
    AUTO_INCREMENT=6
;



INSERT INTO `permission`(id, name)
VALUES(1, 'SHOW_BECOME_GROUP_ADMIN_REQUESTS');

INSERT INTO `permission`(id, name)
VALUES(2, 'BECOME_GROUP_ADMIN');

INSERT INTO `permission`(id, name)
VALUES(3, 'CHOOSE_SORT_TYPE');



INSERT INTO `role`(id, name)
VALUES(1, 'USER');

INSERT INTO `role`(id, name)
VALUES(2, 'GROUP_ADMIN');

INSERT INTO `role`(id, name)
VALUES(3, 'SUDO');



INSERT INTO `permission_role`(id, permission_id, role_id)
VALUES(1, 1, 3);

INSERT INTO `permission_role`(id, permission_id, role_id)
VALUES(2, 2, 1);

INSERT INTO `permission_role`(id, permission_id, role_id)
VALUES(3, 3, 2);

INSERT INTO `permission_role`(id, permission_id, role_id)
VALUES(4, 3, 3);