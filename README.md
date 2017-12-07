# movie-overview-setup
Download and Process Imdb TSV files, and ingest into elasticsearch with only required fields

## Prerequisite

Install docker for mac and give at least 6GB of memory and 2 CPUs for the docker setup.

Run the following command to spin off an elasticsearch cluster locally.

```
docker run -p 9200:9200 -p 9300:9300 -e "http.host=0.0.0.0" -e "transport.host=127.0.0.1" -e "discovery.type=single-node" elasticsearch:2.3.4
```
