## Init DB

## Tables
```sql
CREATE USER 'artxew'@'localhost' IDENTIFIED BY 'artxew';
CREATE DATABASE artxew CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci;
GRANT SELECT, INSERT, UPDATE, DELETE ON artxew.* TO 'artxew'@'localhost';

CREATE TABLE `TB_DMN` (
	`ABR` varchar(4) NOT NULL COMMENT '약어'
	, `ENG_NM` varchar(40) NOT NULL COMMENT '영문명'
	, `HG_NM` varchar(100) NOT NULL COMMENT '한글명'
	, PRIMARY KEY (`ABR`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci COMMENT='도메인';

CREATE TABLE `TB_CM_CD_GRP` (
	`CD` varchar(20) NOT NULL COMMENT '코드'
	, `NM` varchar(20) NOT NULL COMMENT '이름'
	, PRIMARY KEY (`CD`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci COMMENT='공통코드그룹';

CREATE TABLE `TB_CM_CD` (
	`GRP_CD` varchar(20) NOT NULL COMMENT '그룹코드'
	, `CD` varchar(20) NOT NULL COMMENT '코드'
	, `NM` varchar(40) NOT NULL COMMENT '이름'
	, PRIMARY KEY (`GRP_CD`,`CD`)
	, CONSTRAINT `FK_CM_CD__GRP_CD` FOREIGN KEY (`GRP_CD`) REFERENCES `TB_CM_CD_GRP` (`CD`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci COMMENT='공통코드';

CREATE TABLE `TB_USER` (
	`SNO` int(1) unsigned NOT NULL AUTO_INCREMENT COMMENT '일련번호'
	, `ID` varchar(20) NOT NULL
	, `PWD` char(65) NOT NULL COMMENT '비밀번호 솔트:해시'
	, `CI` char(88) NOT NULL
	, `DI` char(64) NOT NULL
	, `NM` varchar(40) NOT NULL COMMENT '이름'
	, `BDAY` char(8) NOT NULL COMMENT '생년월일'
	, `TEL` varchar(12) NOT NULL COMMENT '전화번호'
	, `GNDR_CD` char(1) NOT NULL COMMENT '성별코드'
	, `FRGN_YN` char(1) NOT NULL COMMENT '외국인여부'
	, `MAIL` varchar(100) NOT NULL COMMENT '메일주소'
	, `POST` char(5) NOT NULL COMMENT '우편번호'
	, `HOME_ADDR` varchar(200) NOT NULL COMMENT '집주소'
	, `DTL_ADDR` varchar(100) NOT NULL COMMENT '상세주소'
	, `PWD_ERR_CNT` int(1) unsigned NOT NULL DEFAULT 0 COMMENT '비밀번호오류횟수'
	, `FRST_JOIN_DT` datetime NOT NULL COMMENT '최초가입일자'
	, `JOIN_DT` datetime NOT NULL DEFAULT current_timestamp() COMMENT '가입일자'
	, `BLCK_DT` datetime NULL DEFAULT NULL COMMENT '차단일자'
	, `WDRW_DT` datetime NULL DEFAULT NULL COMMENT '탈퇴일자'
	, `WDRW_RSN` varchar(200) NULL DEFAULT NULL COMMENT '탈퇴사유'
	, `TEMP_PWD` char(20) NULL DEFAULT NULL COMMENT '임시비밀번호'
	, PRIMARY KEY (`SNO`)
	, UNIQUE KEY `UK_USER__ID` (`ID`)
	, KEY `IX_USER__CI` (`CI`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci COMMENT='사용자';

CREATE TABLE `TB_LGIN_HST` (
	`USER_NO` int(1) unsigned NOT NULL COMMENT '사용자번호'
	, `IP` varchar(15) NOT NULL
	, `REG_DT` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자'
	, PRIMARY KEY (`USER_NO`, `REG_DT`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci COMMENT='로그인이력';

CREATE TABLE `TB_PWD_CHG_HST` (
	`USER_NO` int(1) unsigned NOT NULL COMMENT '사용자번호'
	, `PWD` char(65) NOT NULL COMMENT '비밀번호 솔트:해시'
	, `REG_DT` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자'
	, PRIMARY KEY (`USER_NO`, `PWD`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci COMMENT='비밀번호변경이력';

CREATE TABLE `TB_ROLE` (
	`CD` varchar(20) NOT NULL COMMENT '코드'
	, `NM` varchar(40) NOT NULL COMMENT '이름'
	, PRIMARY KEY (`CD`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci COMMENT='역할';

CREATE TABLE `TB_USER_ROLE` (
	`USER_NO` int(1) unsigned NOT NULL COMMENT '사용자번호'
	, `ROLE_CD` varchar(20) NOT NULL COMMENT '역할코드'
	, `REG_DT` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자'
	, PRIMARY KEY (`USER_NO`, `ROLE_CD`)
	, KEY `IX_USER_ROLE__ROLE_CD` (`ROLE_CD`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci COMMENT='사용자역할매핑';

CREATE TABLE `TB_IMG` (
	`CL_CD` char(2) NOT NULL COMMENT '구분코드'
	, `REF_KEY` varchar(40) NOT NULL COMMENT '참조키'
	, `ALT` varchar(40) NOT NULL COMMENT '대체텍스트'
	, `EXTS` varchar(5) NOT NULL COMMENT '확장자'
	, `REG_DT` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자'
	, PRIMARY KEY (`CL_CD`, `REF_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci COMMENT='이미지';

CREATE TABLE `TB_CHAT` (
	`SNO` int(1) unsigned NOT NULL AUTO_INCREMENT COMMENT '일련번호'
	, `NM` varchar(20) NULL DEFAULT NULL COMMENT '이름'
	, `LAST_CHAT` varchar(20) NOT NULL DEFAULT '' COMMENT '마지막채팅'
	, `REG_DT` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자'
	, `MOD_DT` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '수정일자'
	, `DEL_DT` datetime NULL DEFAULT NULL COMMENT '삭제일자'
	, PRIMARY KEY (`SNO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci COMMENT='채팅';

CREATE TABLE `TB_CHAT_JOIN` (
	`CHAT_NO` int(1) unsigned NOT NULL COMMENT '채팅번호'
	, `USER_NO` int(1) unsigned NOT NULL COMMENT '사용자번호'
	, `READ_TIME` datetime NOT NULL DEFAULT current_timestamp() COMMENT '읽은시간'
	, `REG_DT` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자'
	, PRIMARY KEY (`CHAT_NO`, `USER_NO`)
	, KEY `IX_CHAT_JOIN__USER_NO` (`USER_NO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci COMMENT='채팅참여';

CREATE TABLE `TB_CHAT_BLCK` (
	`REQ_USER_NO` int(1) unsigned NOT NULL COMMENT '요청사용자번호'
	, `BLCK_USER_NO` int(1) unsigned NOT NULL COMMENT '차단사용자번호'
	, `CHAT_NO` int(1) unsigned NOT NULL COMMENT '채팅번호'
	, `BLCK_RSN` varchar(100) NOT NULL DEFAULT '' COMMENT '차단사유'
	, `REG_DT` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자'
	, PRIMARY KEY (`REQ_USER_NO`, `BLCK_USER_NO`)
	, KEY `IX_CHAT_BLCK__BLCK_USER_NO` (`BLCK_USER_NO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci COMMENT='채팅차단';

CREATE TABLE `TB_MENU` (
	`SNO` int(1) unsigned NOT NULL AUTO_INCREMENT COMMENT '일련번호'
	, `PRNT_MENU_NO` int(1) unsigned NULL DEFAULT NULL COMMENT '부모메뉴번호'
	, `NM` varchar(20) NOT NULL COMMENT '이름'
	, `DESC` varchar(100) NULL DEFAULT NULL COMMENT '설명'
	, `URL` varchar(200) NOT NULL
	, PRIMARY KEY (`SNO`)
	, CONSTRAINT `FK_MENU__PRNT_MENU_NO` FOREIGN KEY (`PRNT_MENU_NO`) REFERENCES `TB_MENU` (`SNO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci COMMENT='메뉴';

CREATE TABLE `TB_MENU_ROLE` (
	`MENU_NO` int(1) unsigned NOT NULL COMMENT '메뉴번호'
	, `ROLE_CD` varchar(20) NOT NULL COMMENT '역할코드'
	, `REG_DT` datetime NOT NULL DEFAULT current_timestamp() COMMENT '등록일자'
	, PRIMARY KEY (`MENU_NO`, `ROLE_CD`)
	, KEY `IX_USER_ROLE__ROLE_CD` (`ROLE_CD`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci COMMENT='메뉴역할매핑';
```

### Data
```sql
DELETE FROM TB_DMN;
INSERT INTO TB_DMN (
	ABR
	, ENG_NM
	, HG_NM
) VALUES
	(
		'TB'
		, 'Table'
		, '테이블'
	)
	, (
		'DMN'
		, 'Domain'
		, '도메인'
	)
	, (
		'ABR'
		, 'Abbreviation'
		, '약어'
	)
	, (
		'ENG'
		, 'English'
		, '영어 영문'
	)
	, (
		'NM'
		, 'Name'
		, '이름'
	)
	, (
		'HG'
		, 'Hangul'
		, '한글'
	)
	, (
		'CD'
		, 'Code'
		, '코드'
	)
	, (
		'NO'
		, 'Number'
		, '넘버 숫자 번호'
	)
	, (
		'SNO'
		, 'Serial Number'
		, '일련번호'
	)
	, (
		'CM'
		, 'Common'
		, '공통'
	)
	, (
		'GRP'
		, 'Group'
		, '그룹'
	)
	, (
		'USER'
		, 'User'
		, '사용자 유저'
	)
	, (
		'ID'
		, 'Identifier'
		, '아이디'
	)
	, (
		'PWD'
		, 'Password'
		, '비밀번호 패스워드'
	)
	, (
		'SALT'
		, 'Salt'
		, '솔트 소금'
	)
	, (
		'LIST'
		, 'List'
		, '목록 리스트'
	)
	, (
		'HST'
		, 'History'
		, '이력 내역'
	)
	, (
		'INFO'
		, 'Information'
		, '정보'
	)
	, (
		'MAIL'
		, 'Mail'
		, '메일 이메일'
	)
	, (
		'STR'
		, 'String'
		, '문자열 스트링'
	)
	, (
		'TEL'
		, 'Telephone number'
		, '전화번호'
	)
	, (
		'AUTH'
		, 'Authorization'
		, '인증 인가'
	)
	, (
		'AUTR'
		, 'Authority'
		, '권한'
	)
	, (
		'GNDR'
		, 'Gender'
		, '성별'
	)
	, (
		'CI'
		, 'Connecting information'
		, 'CI'
	)
	, (
		'DI'
		, 'Duplication information'
		, 'DI'
	)
	, (
		'BDAY'
		, 'Birth date'
		, '생년월일'
	)
	, (
		'FRGN'
		, 'Foreigner'
		, '외국인'
	)
	, (
		'POST'
		, 'Post Postal'
		, '우편 포스트'
	)
	, (
		'HOME'
		, 'Home'
		, '집 자택 홈'
	)
	, (
		'ADDR'
		, 'Address'
		, '주소'
	)
	, (
		'DTL'
		, 'Detail'
		, '상세 디테일'
	)
	, (
		'OPER'
		, 'Operator'
		, '운영자'
	)
	, (
		'CHAT'
		, 'Chatting'
		, '챗 채팅'
	)
	, (
		'MENU'
		, 'Menu'
		, '메뉴'
	)
	, (
		'TEST'
		, 'Test'
		, '테스트'
	)
	, (
		'IMG'
		, 'Image'
		, '이미지'
	)
	, (
		'REQ'
		, 'Request'
		, '요청'
	)
	, (
		'RES'
		, 'Response'
		, '응답'
	)
	, (
		'DTO'
		, 'Data Transfer Object'
		, 'DTO'
	)
	, (
		'URL'
		, 'Uniform Resource Locator'
		, 'URL'
	)
	, (
		'ERR'
		, 'Error'
		, '오류 에러'
	)
	, (
		'CNT'
		, 'Count'
		, '횟수 갯수 카운트'
	)
	, (
		'SELF'
		, 'Self'
		, '본인 자신'
	)
	, (
		'CERT'
		, 'Certification'
		, '인증'
	)
	, (
		'LGIN'
		, 'Login'
		, '로그인'
	)
	, (
		'LOUT'
		, 'Logout'
		, '로그아웃'
	)
	, (
		'ROLE'
		, 'Role'
		, '역할'
	)
	, (
		'WDRW'
		, 'Withdrawal'
		, '탈퇴 출금'
	)
	, (
		'LAST'
		, 'Last'
		, '최총 마지막 라스트'
	)
	, (
		'FRST'
		, 'First'
		, '최초 처음 퍼스트'
	)
	, (
		'JOIN'
		, 'Join'
		, '가입 조인 합류'
	)
	, (
		'CHG'
		, 'Change'
		, '변경'
	)
	, (
		'REG'
		, 'Registration'
		, '등록'
	)
	, (
		'MOD'
		, 'Modifications'
		, '수정'
	)
	, (
		'DEL'
		, 'Delete'
		, '삭제'
	)
	, (
		'DT'
		, 'Date Time'
		, '일자 일시'
	)
	, (
		'TEMP'
		, 'Temp'
		, '임시 템프'
	)
	, (
		'BLCK'
		, 'Block'
		, '차단'
	)
	, (
		'UBLK'
		, 'Unblock'
		, '차단해제'
	)
	, (
		'EXST'
		, 'Exist'
		, '존재'
	)
	, (
		'YN'
		, 'Yes No'
		, '여부 유무'
	)
	, (
		'CL'
		, 'Classification'
		, '구분 분류'
	)
	, (
		'KEY'
		, 'Key'
		, '키 열쇠'
	)
	, (
		'ALT'
		, 'Alternative'
		, '대체 대안'
	)
	, (
		'REF'
		, 'Reference'
		, '참조'
	)
	, (
		'EXTS'
		, 'Extension'
		, '확장자'
	)
	, (
		'RSN'
		, 'Reason'
		, '이유 사유'
	)
	, (
		'PRNT'
		, 'Parent'
		, '부모 상위'
	)
	, (
		'CHLD'
		, 'Child'
		, '자식 하위'
	)
	, (
		'EXPL'
		, 'Explanation'
		, '설명 해석'
	)
	, (
		'ASC'
		, 'Ascending'
		, '오름차순'
	)
	, (
		'DESC'
		, 'Descending'
		, '내림차순'
	)
	, (
		'TIME'
		, 'Time'
		, '시간'
	)
	, (
		'READ'
		, 'Reading'
		, '읽다 읽은 읽기'
	)
	, (
		'WRTN'
		, 'Writing'
		, '쓰다 쓴 쓰기'
	)
	, (
		'MSG'
		, 'Message'
		, '통신 메세지'
	)
	, (
		'PTC'
		, 'Protocol'
		, '규약 프로토콜'
	)
	, (
		'ITLT'
		, 'Interlocutor'
		, '대화상대 상대방'
	);

DELETE FROM TB_CM_CD_GRP;
INSERT INTO TB_CM_CD_GRP (
	CD
	, NM
) VALUES
	(
		'ROLE_CD'
		, '역할코드'
	)
	, (
		'GNDR_CD'
		, '성별코드'
	);

DELETE FROM TB_CM_CD;
INSERT INTO TB_CM_CD (
	GRP_CD
	, CD
	, NM
) VALUES
	(
		'ROLE_CD'
		, 'SUPER'
		, '최고관리자'
	)
	, (
		'ROLE_CD'
		, 'ADMIN'
		, '관리자'
	)
	, (
		'ROLE_CD'
		, 'OPER'
		, '운영자'
	)
	, (
		'GNDR_CD'
		, 'M'
		, '남자'
	)
	, (
		'GNDR_CD'
		, 'F'
		, '여자'
	);

DELETE FROM TB_USER;
INSERT INTO TB_USER (
	SNO
	, ID
	, PWD
	, CI
	, DI
	, NM
	, BDAY
	, TEL
	, GNDR_CD
	, FRGN_YN
	, MAIL
	, POST
	, HOME_ADDR
	, DTL_ADDR
	, FRST_JOIN_DT
) VALUES (
	1
	,'admin'
	, 'w5lZ8IcruL4XmT/K87tdZZcP8+MqKwYE:ncmNN9iC15sMEURb+OAA073Un5wtcPdX'
	, 'abcdefg'
	, 'abcdefg'
	, '어드민'
	, '20001212'
	, '01012345678'
	, 'M'
	, 'Y'
	, 'test@test.test'
	, '01234'
	, '집주소'
	, '상세주소'
	, current_timestamp()
)
, (
	2
	,'test1234'
	, 'w5lZ8IcruL4XmT/K87tdZZcP8+MqKwYE:ncmNN9iC15sMEURb+OAA073Un5wtcPdX'
	, 'abcdefg'
	, 'abcdefg'
	, '테스트'
	, '20001212'
	, '01012345678'
	, 'M'
	, 'Y'
	, 'test@test.test'
	, '01234'
	, '집주소'
	, '상세주소'
	, current_timestamp()
);

DELETE FROM TB_USER_ROLE;
INSERT INTO TB_USER_ROLE (
	USER_NO
	, ROLE_CD
) VALUES
	(1, '01')
	, (1, '02')
	, (1, '03');

DELETE FROM TB_ROLE;
INSERT INTO TB_ROLE (
	CD
	, NM
) VALUES
	('00', 'SYSTEM 시스템')
	,('01', 'SUPER 최고관리자')
	, ('02', 'ADMIN 관리자')
	, ('03', 'OPER 운영자');
```