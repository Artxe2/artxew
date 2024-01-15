## INFORMATION_SCHEMA
```sql
CREATE FUNCTION to_camel_case(str VARCHAR(255))
RETURNS VARCHAR(255)
DETERMINISTIC
BEGIN
	DECLARE i INT DEFAULT 1;
	SET str = LOWER(str);
	WHILE i < LENGTH(str) DO
		IF SUBSTRING(str, i, 1) REGEXP '_' THEN
			SET str = CONCAT(
				SUBSTRING(str, 1, i - 1)
				, UPPER(
					SUBSTRING(str, i + 1, 1)
				)
				, SUBSTRING(str, i + 2)
			);
		ELSE
	    	SET i = i + 1;
		END IF;
	END WHILE;
  RETURN str;
END;
```
```sql
WITH TB AS (
	SELECT *
	FROM INFORMATION_SCHEMA.COLUMNS
	WHERE table_name = 'TB_USER'
)
SELECT 'SELECT'
	, concat(
		'SELECT '
		, group_concat(
			concat(
				CASE
						WHEN data_type = 'datetime'
							THEN concat('date_format(A.', column_name, ',\'%Y-%m-%d %H:%i:%s\')')
						ELSE concat('A.', column_name)
					END
				, ' AS '
				, to_camel_case(column_name)
			)
			ORDER BY ordinal_position ASC
			SEPARATOR '\n\t, '
		)
		, '\nFROM '
		, table_name
		, ' A\nWHERE '
		, group_concat(
			CASE
				WHEN COLUMN_KEY = 'PRI'
					THEN concat(
						'A.'
						, column_name
						, ' = #{'
						, to_camel_case(column_name)
						, '}'
					)
				ELSE NULL
			END
			ORDER BY ordinal_position ASC
			SEPARATOR '\nAND '
			)
	)
FROM TB A
UNION ALL
SELECT 'INSERT'
	, concat(
		'INSERT INTO '
		, table_name
		, ' (\n\t'
		, group_concat(
			column_name
			ORDER BY ordinal_position ASC
			SEPARATOR '\n\t, '
		)
		, '\n) VALUES (\n\t'
		, group_concat(
			concat(
				'#{'
				, to_camel_case(column_name)
				, '}'
			)
			ORDER BY ordinal_position ASC
			SEPARATOR '\n\t, '
		)
		, '\n)'
	)
FROM TB A
UNION ALL
SELECT 'UPDATE'
	, concat(
		'UPDATE '
		, table_name
		, ' A SET\n\t'
		, group_concat(
			CASE
				WHEN COLUMN_KEY != 'PRI'
					THEN concat(
						'A.'
						, column_name
						, ' = #{'
						, to_camel_case(column_name)
						, '}'
					)
				ELSE NULL
			END
			ORDER BY ordinal_position ASC
			SEPARATOR '\n\t, '
		)
		, '\nWHERE '
		, group_concat(
			CASE
				WHEN COLUMN_KEY = 'PRI'
					THEN concat(
						'A.'
						, column_name
						, ' = #{'
						, to_camel_case(column_name)
						, '}'
					)
				ELSE NULL
			END
			ORDER BY ordinal_position ASC
			SEPARATOR '\nAND '
		)
	)
FROM TB A
UNION ALL
SELECT 'DELETE'
	, concat(
		'DELETE FROM '
		, table_name
		, '\nWHERE '
		, group_concat(
			CASE
				WHEN COLUMN_KEY = 'PRI'
					THEN concat(
						column_name
						, ' = #{'
						, to_camel_case(column_name)
						, '}'
					)
				ELSE NULL
			END
			ORDER BY ordinal_position ASC
			SEPARATOR '\nAND '
		)
	)
FROM TB A
UNION ALL
SELECT 'DTO'
	, concat(
		'\n'
		, group_concat(
			concat(
				'\n\t'
				, CASE
						WHEN is_nullable = 'NO'
							THEN CASE
								WHEN data_type = 'int'
									THEN '@NotNull\n\t'
								ELSE '@NotEmpty\n\t'
							END
						ELSE ''
					END
				, '@Schema(description = "'
				, column_comment
				, '", example = "'
				, CASE
						WHEN data_type = 'int'
							THEN '123456'
						WHEN data_type = 'datetime'
							THEN '2020-02-02 20:20:20'
						ELSE 'abcdefg'
					END
				, '")\n\t'
				, 'private '
				, CASE
						WHEN data_type = 'int'
							THEN 'Long'
						ELSE 'String'
					END
				, ' '
				, to_camel_case(column_name)
				, ';'
			)
			ORDER BY ordinal_position ASC
			SEPARATOR '\n'
		)
	)
FROM TB A
UNION ALL
SELECT 'TYPE'
	, concat(
		'\n'
		, group_concat(
			concat(
				'\t'
				, to_camel_case(column_name)
				, CASE
						WHEN is_nullable = 'NO'
							THEN ':'
						ELSE '?:'
					END
				, ' '
				, CASE
						WHEN data_type = 'int'
							THEN 'number'
						ELSE 'string'
					END
				, '; // '
				, column_comment
			)
			ORDER BY ordinal_position ASC
			SEPARATOR '\n'
		)
	)
	AS "TYPE"
FROM TB A
```