#!/bin/bash

helpFunction()
{
   echo ""
   echo "Usage: $0 -a \"PROJECT_ID\" -b \"BILLING_ACC_ID\" -c \"GCP REGION\""
   echo -e "\t-a Provide your project name. Avoid special characters"
   echo -e "\t-b Provide billing ACCOUNT ID. You can find your billing account ID by executing the command gcloud beta billing accounts list"
   echo -e "\t-c GCP region"
   exit 1 # Exit script after printing help
}

while getopts "a:b:c:" opt
do
   case "$opt" in
      a ) PROJECT_ID="$OPTARG" ;;
      b ) BILLING_ACC_ID="$OPTARG" ;;
      c ) GCP_REGION="$OPTARG" ;;
      ? ) helpFunction ;; # Print helpFunction in case parameter is non-existent
   esac
done

# Print helpFunction in case parameters are empty
if [ -z "$PROJECT_ID" ] || [ -z "$BILLING_ACC_ID" ] || [ -z "$GCP_REGION" ]
then
   echo "Some or all of the parameters are empty";
   helpFunction
fi

# Begin script in case all parameters are correct
echo "PROJECT_ID: $PROJECT_ID"
echo "BILLING_ACC_ID: $BILLING_ACC_ID"
echo "GCP_REGION: $GCP_REGION"

echo "STORAGE_CLASS: standard(default)"
export STORAGE_CLASS=standard
echo "STORAGE LOCATION/REGION: us-east1(default)"

echo "Creating a new project $PROJECT_ID"
gcloud projects create ${PROJECT_ID} --set-as-default
gcloud config set project ${PROJECT_ID}

echo "Linking billing account $BILLING_ACC_ID to your project $PROJECT_ID"
gcloud beta billing projects link ${PROJECT_ID} --billing-account ${BILLING_ACC_ID}

echo "Enabling DataProc service"
gcloud services enable dataproc.googleapis.com

echo "Enabling bigquery service"
gcloud services enable bigquery-json.googleapis.com

export GCS_TEMPORARY_BUCKET="${PROJECT_ID}-temp-bucket"

echo " Creating GCS temporary bucket $GCS_TEMPORARY_BUCKET with storage class $STORAGE_CLASS and location $LOCATION"
gsutil mb -c $STORAGE_CLASS -l $GCP_REGION  gs://$GCS_TEMPORARY_BUCKET

echo "Creating Bigquery dataset ${PROJECT_ID}:GlobalHistoricalWeatherData"
bq --location=$LOCATION mk \ 
	--dataset \
	--description 'NOAA gsod weather data' \
	"${PROJECT_ID}:GlobalHistoricalWeatherData"

echo "Creating Dataproc cluster on free tier quota. Number of worke nodes=3 , worker machine type n1-standard-2, region: ${GCP_REGION}, zone: ${GCP_REGION}-c  "
echo "Limited quotas in free tier :( " 

gcloud dataproc clusters create cluster-ghcn --region $GCP_REGION --subnet default --zone "${GCP_REGION}-c" --master-machine-type n1-standard-2 --master-boot-disk-size 500 --num-workers 3 --worker-machine-type n1-standard-2 --worker-boot-disk-size 500 --image-version 1.3-debian10 --project $PROJECT_ID
