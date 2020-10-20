# ghcn-daily-analysis

This project loads NOAA Global Historical Climatology Network Daily(GHCN-D) from Amazon S3 to BigQuery

**Follow my medium post for how to guide** https://medium.com/swlh/explore-visualize-200-years-of-global-temperature-using-apache-spark-bigquery-and-google-data-699ed8b3c67?source=friends_link&sk=3721ca3220a75af80293ac3e4bf3f4d1

**Source** : NOAA Global Historical Climatology Network Daily (GHCN-D) pon AWS 
The GHCH-D data are stored in the noaa-ghcn-pds bucket:

http://noaa-ghcn-pds.s3.amazonaws.com/

The directory is structured by year from 1763 to present, with each file named after the respective year. The data are available in CSV file format and as .csv.gzip files, so any particular year will be named yyyy.csv and yyyy.csv.gz. For example to access the csv version of the data for 1788 use the bucket URL:
http://noaa-ghcn-pds.s3.amazonaws.com/csv/1788.csv

This project reads the csv source file. 

**Summary of the Source Data Format:**
The yearly files are formatted so that every observation is represented by a single row with the following fields:

ID = 11 character station identification code. Please see ghcnd-stations section below for an explantation
YEAR/MONTH/DAY = 8 character date in YYYYMMDD format (e.g. 19860529 = May 29, 1986)
ELEMENT = 4 character indicator of element type
DATA VALUE = 5 character data value for ELEMENT
M-FLAG = 1 character Measurement Flag
Q-FLAG = 1 character Quality Flag
S-FLAG = 1 character Source Flag
OBS-TIME = 4-character time of observation in hour-minute format (i.e. 0700 =7:00 am)
The fields are comma delimited and each row represents one station-day.

****ELEMENT Summary****
The five core elements used in this project are:

PRCP = Precipitation (tenths of mm)
SNOW = Snowfall (mm)
SNWD = Snow depth (mm)
TMAX = Maximum temperature (tenths of degrees C)
TMIN = Minimum temperature (tenths of degrees C)

For detailed information , checkout https://docs.opendata.aws/noaa-ghcn-pds/readme.html

**Summary of Output format - BigQuery Schema:**
```text
Field name	    Type
station_id	    STRING
date	        DATE
latitude	    FLOAT
longitude	    FLOAT
elevation	    FLOAT
max_temp	    FLOAT
min_temp	    FLOAT
mean_temp	    FLOAT
prcp	        FLOAT
snowfall	    FLOAT
snow_depth	    FLOAT
obs_time	    STRING
m_flag	        STRING
q_flag	        STRING
s_flag	        STRING
gsn_flag    	STRING
hcn_crn_flag	STRING
station_name	STRING
state_code	    STRING
state	        STRING
country_code	STRING
country	        STRING
wmo_id	        INTEGER
partition_date	DATE
```


To build and package the app
```shell script
##Build and package the spark job. Provide your aws key and aws secret as aruguments.
##example:   $HOME/scripts/build_spark_app.sh -a "AKYDESLKJS827DFSDF9R43RF" -b "FqDddfsL00vcMMMNNNSDFDaclF599999Prh+/vam"
$HOME/ghcn-daily-analysis/scripts/build_spark_app.sh -a "YOUR AWS_ACCESS_KEY" -b "YOUR AWS_SECRET_KEY"

```

**Setup GCP Datalake**
To setup the following services on GCP, execute the setup_gcp_datalake.sh script
Create a project
Enable billing(will still be in the free tier)
Enable Dataproc and BigQuery Services
Create a google cloud storage bucket
Create a BigQuery Dataset
Create a Dataproc cluster

Execute sh
```shell script
$HOME/ghcn-daily-analysis/scripts/setup_gcp_datalake.sh -a "YOUR_PROJECT_NAME" -b "YOUR_BILLING_ACC_ID" -c "GCP_REGION"
```

To run dataproc job
```shell script
export LOCATION="us-east1"  #Set your GCP region
gcloud dataproc jobs submit spark \
--cluster=ghcn-analysis  \
--region=$LOCATION \
--class=com.ksr.ghcn.Run \
--jars=/home/kapilsreed12/ghcn-daily-analysis/target/ghcn-daily-analysis-1.0-SNAPSHOT.jar,gs://spark-lib/bigquery/spark-bigquery-latest.jar 
```


