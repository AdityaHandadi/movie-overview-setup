# movie-overview-setup
Download and Process Imdb TSV files, and ingest into elasticsearch with only required fields

## Prerequisite

Install docker for mac(or any other docker) and give at least 6GB of memory and 2 CPUs for the docker setup.

Run the following command to spin off an elasticsearch cluster locally.

```
docker run -p 9200:9200 -p 9300:9300 -e "http.host=0.0.0.0" -e "transport.host=127.0.0.1" -e "discovery.type=single-node" elasticsearch:2.3.4
```
If you have already an elasticsearch instance running, you can use that too. This App needs atleast port 9300 to be exposed from the desired elasticsearch.
The elastic clustername has to be default. (elasticsearch).

## Properties

All the properties required to set are in `/resources/config.properties`

```
# DATA EXTRACTOR
title.input.file=/Users/adityahandadi/Documents/AWS/title.basics.tsv
cast.input.file=/Users/adityahandadi/Documents/AWS/name.basics.tsv
mapping.input.file=/Users/adityahandadi/Documents/AWS/title.principals.tsv
#title (or) cast (or) mapping
parse.subject=title

# DATA INGESTER
es.host=localhost
es.tcp.port=9300
```

For the `movie-api` to run successfully, all three input files `title.input.file`, `cast.input.file`, `mapping.input.file`
would need to be ingested.

Elasticsearch with indices `titles`, `names` and `mappings` would have json formatted records that are needed for `movie-api`
after executing this program.

`parse.subject` will run this module to ingest one particular domain at a given time.
Since each file size is very large, this above limitation is placed.

Also elasticsearch host and port is needed. This program will only communicate through Transport client of elasticsearch.