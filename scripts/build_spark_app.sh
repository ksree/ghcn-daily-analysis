#!/bin/bash

helpFunction()
{
   echo ""
   echo "Usage: $0 -a \"AWS_ACCESS_KEY\" -b \"AWS_SECRET_KEY\""
   echo -e "\t-a Provide your AWS access key"
   echo -e "\t-b Provide your aws secret key"
   exit 1 # Exit script after printing help
}

while getopts "a:b:" opt
do
   case "$opt" in
      a ) AWS_ACCESS_KEY="$OPTARG" ;;
      b ) AWS_SECRET_KEY="$OPTARG" ;;
      ? ) helpFunction ;; # Print helpFunction in case parameter is non-existent
   esac
done

# Print helpFunction in case parameters are empty
if [ -z "$AWS_ACCESS_KEY" ] || [ -z "$AWS_SECRET_KEY" ]
then
   echo "Some or all of the parameters are empty";
   helpFunction
fi

# Begin script in case all parameters are correct
echo 'Setting up JAVA8'
sudo apt-get install -y openjdk-8-jre
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64

cd $HOME/ghcn-daily-analysis/
mvn package -DskipTests

echo 'Generating spark application config'

 echo "AWS_ACCESS_KEY="\""${AWS_ACCESS_KEY}"\""
 AWS_SECRET_KEY="\""${AWS_SECRET_KEY}"\""
 AWS_BUCKET="\"s3a://noaa-ghcn-pds/csv/"\"
 GCS_TEMPORARY_BUCKET="\""${GCS_TEMPORARY_BUCKET}"\""
 BIGQUERY_TABLE_NAME="\""${PROJECT_ID}:GlobalHistoricalWeatherData.ghcn_daily"\""
 startYear=1764
 endYear=2020" > $HOME/application.conf

 echo "Created a new configuration file $HOME/application.conf"
