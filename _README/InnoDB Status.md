```sql
SELECT A.Innodb_buffer_pool_read_requests
	, B.Innodb_buffer_pool_reads
	, C.Innodb_buffer_pool_write_requests
	, (1 - B.Innodb_buffer_pool_reads / A.Innodb_buffer_pool_read_requests) AS Cache_Hit_Ratio
	, ((A.Innodb_buffer_pool_read_requests - B.Innodb_buffer_pool_reads) / C.Innodb_buffer_pool_write_requests) AS Read_Per_Write_Ratio
FROM (
	SELECT variable_value AS Innodb_buffer_pool_read_requests
	FROM information_schema.global_status
	WHERE variable_name = 'Innodb_buffer_pool_read_requests'
) A
CROSS JOIN (
	SELECT variable_value AS Innodb_buffer_pool_reads
	FROM information_schema.global_status
	WHERE variable_name = 'Innodb_buffer_pool_reads'
) B
CROSS JOIN (
	SELECT variable_value AS Innodb_buffer_pool_write_requests
	FROM information_schema.global_status
	WHERE variable_name = 'Innodb_buffer_pool_write_requests'
) C
```