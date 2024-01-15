## CMD

#### run
```bash
su - artxew -c "nohup $JAVA_HOME/bin/java -jar -Xmx32g -Xms16g -Dspring.profiles.active=stg -Denvironment.password=artxew-enc-key -XX:+ZGenerational -XX:+UseZGC --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED -Dfile.encoding=COMPAT -Djava.net.preferIPv4Stack=true /usr/lib/artxew/artxew.jar&"
^Z
bg
```
#### tail log
```bash
tail -f /usr/logs/artxew/artxew_log_`date +%Y-%m-%d`.*
```
#### kill
```bash
kill $(lsof -t -i:8000)
```
#### slow query
```bash
\grep -Poz '\n\d{4}-\d\d-\d\d \d\d:\d\d:\d\d《[^《]+\{executed in (?:\d{4,}|[2-9]\d\d) msec\}' /usr/logs/artxew/artxew_log_`date +%Y-%m-%d`.*
```