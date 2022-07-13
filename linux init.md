>> sudo passwd
>
>> systemctl start firewalld
>
>> systemctl enable firewalld
>
>> firewall-cmd --permanent --zone=public --add-port=5432/tcp
>
>> firewall-cmd --permanent --zone=public --add-port=6379/tcp
>
>> firewall-cmd --permanent --zone=public --add-port=9200/tcp
>
>> firewall-cmd --permanent --zone=public --add-port=8080/tcp
>
>> sudo firewall-cmd --permanent --add-source=0.0.0.0/24
>
>> firewall-cmd --reload

> PostgreSQL
>> sudo yum install postgresql-server postgresql-contrib
>
>> sudo postgresql-setup initdb
>
>> sudo systemctl start postgresql
>
>> sudo systemctl enable postgresql
>
>> sudo -u postgres psql
>
>> alter user postgres with password '?';
>
>> show data_directory;
>
>> \q
>
>> exit
>
>> vi /var/lib/pgsql/data/postgresql.conf
>
>> #listen_addresses = 'localhost' => listen_addresses = '*'
>
>> :wq
>
>> vi /var/lib/pgsql/data/pg_hba.conf
>
>> local all all peer => local all all md5
>
>> host all all 127.0.0.1/32 ident => host all all 0.0.0.0/0 md5
>
>> host all all ::1/128 ident => host all all ::1/128 md5
>
>> :wq
>
>> sudo systemctl restart postgresql

> Redis
>> yum install redis
>
>> vi /etc/redis.conf
>
>> bind 127.0.0.1 => bind 0.0.0.0
>
>> requirepass ?
>
>> :wq
>
>> sudo systemctl start redis
>
>> sudo systemctl enable redis

> Elastic search
>> https://www.elastic.co/kr/downloads/elasticsearch
>
>> /usr/lib/elasticsearch-8.3.1
>
>> useradd elasticsearch
>
>> chown -R elasticsearch /usr/lib/elasticsearch-8.3.1
>
>> vi usr/lib/elasticsearch-8.3.1/config/jvm.options
>
>> ## -Xms4g => -Xms128m
>
>> ## -Xmx4g => -Xmx128m
>
>> :wq
>
>> vi usr/lib/elasticsearch-8.3.1/config/elasticsearch.yml
>
>> network.host: 0.0.0.0
>
>> xpack.security.enabled: false
>
>> xpack.security.http.ssl.enabled: false
>
>> :wq
>
>> vi /etc/sysctl.conf
>
>> vm.max_map_count=262144
>
>> :wq
>
>> sysctl -p
>
>> vi /etc/security/limits.conf
>
>> elasticsearch hard nproc 4096
>
>> :wq
>
>> su elasticsearch
>
>> ulimit -u 4096
>
>> elasticsearch

> Java
>> https://adoptium.net/temurin/releases
>
>> /usr/lib/jdk-17.0.3+7
>
>> vi /etc/profile
>
>> export JAVA_HOME=/usr/lib/jdk-17.0.3+7
>
>> :wq
>
>> source /etc/profile
>
>> vi /etc/bashrc
>
>> alias java="/usr/lib/jdk-17.0.3+7/bin/java"
>
>> alias javac="/usr/lib/jdk-17.0.3+7/bin/javac"
>
>> :wq
>
>> source /etc/bashrc
>
>> java -jar -Xms128m -Xmx128m -Dspring.profiles.active=dev -Denvironment.password=artxew-enc-key /usr/lib/artxew/artxew-0.3.7.jar

>> kill $(lsof -t -i:6379)