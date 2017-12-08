# movie-overview-setup
Download and Process Imdb TSV files, and ingest into elasticsearch with only required fields

## Prerequisite

Install Aws CLI and configure AWS CLI using below command.

```
aws configure
```
Provide your credentials, mention region(ex: us-east-1) and output format(ex: json)

Install docker for mac(or any other docker) and give at least 6GB of memory and 2 CPUs for the docker setup.

Run the following command to spin off an elasticsearch cluster locally.

```
docker run -p 9200:9200 -p 9300:9300 -e "http.host=0.0.0.0" -e "transport.host=127.0.0.1" -e "discovery.type=single-node" elasticsearch:2.3.4
```
If you have already an elasticsearch instance running, you can use that too. This App needs atleast port 9300 to be exposed from the desired elasticsearch.
The elastic clustername has to be default. (elasticsearch).