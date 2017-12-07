package com.processor;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.parser.model.ImdbComponents;
import com.parser.model.Title;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.util.List;

public class EsTransportClientUtil<T extends ImdbComponents> {

    private TransportClient client;
    private String esIndex;

    public EsTransportClientUtil(TransportClient client, String esIndex) {
        this.client = client;
        this.esIndex = esIndex;
    }

    public void insertBulkRecords(List<T> recordList) {
        //delete index if exists
        if (client.admin().indices().prepareExists(esIndex).execute().actionGet().isExists())
            client.admin().indices().prepareDelete(esIndex).execute().actionGet();

        final BulkProcessor bp = BulkProcessor.builder(client, new BulkProcessor.Listener() {
            public void beforeBulk(long executionId, BulkRequest request) {
            }

            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                System.out.println("Bulk execution failed ["+  executionId + "].\n" +
                        failure.toString());
            }

            public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                System.out.println("Bulk execution completed ["+  executionId + "].\n" +
                        "Took (ms): " + response.getTookInMillis() + "\n" +
                        "Failures: " + response.hasFailures() + "\n" +
                        "Count: " + response.getItems().length);
            }
        })
        .setConcurrentRequests(4)
        .setBulkActions(-1)
        .setBulkSize(new ByteSizeValue(1, ByteSizeUnit.MB))
        .build();

        try {
            Settings settings = Settings.builder()
                    .put("number_of_shards", 1)
                    .put("number_of_replicas", 0)
                    .build();

            client.admin().indices().prepareCreate(esIndex).setSettings(settings).execute().actionGet();
            client.admin().cluster().prepareHealth(esIndex).setWaitForYellowStatus().execute().actionGet();

            recordList.forEach(entity -> {
                try {
                    bp.add(Requests.indexRequest(esIndex).type("doc").source(serializeTitle(entity)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            settings = Settings.builder()
                    .put("refresh_interval", "1s")
                    .build();

            client.admin().indices().prepareUpdateSettings(esIndex).setSettings(settings).execute().actionGet();
        }
        finally {
            bp.close();
        }
    }

    private String serializeTitle(T entity) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(entity);
    }

}