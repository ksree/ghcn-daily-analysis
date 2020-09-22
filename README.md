# ghcn-daily-analysis


GOOGLE_APPLICATION_CREDENTIALS=your_credentials.json

```shell script
sudo apt-get install -y openjdk-8-jre
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64

git clone https://github.com/ksree/ghcn-daily-analysis.git
mvn clean package
```

```shell script
gcloud dataproc jobs submit spark \
--cluster=ghcn-analysis  \
--region=us-central1 \
--class=com.ksr.ghcn.Run \
--jars=/home/kapilsreed12/ghcn-daily-analysis/target/ghcn-daily-analysis-1.0-SNAPSHOT.jar,gs://spark-lib/bigquery/spark-bigquery-latest.jar 
```


