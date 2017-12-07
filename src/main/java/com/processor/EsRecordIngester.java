package com.processor;

import com.parser.model.ImdbComponents;
import com.parser.model.Title;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class EsRecordIngester<T extends ImdbComponents> {

    private String esIndex;

    public EsRecordIngester(String esIndex) {
        this.esIndex = esIndex;
    }

    public void recordIngester(List<T> recordList) {
        try {
            Settings settings = Settings.builder()
                    .put("cluster.name", "elasticsearch")
                    .put("client.transport.sniff", false)
                    .build();

            InetSocketTransportAddress address =
                    new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300);

            TransportClient client = TransportClient.builder()
                    .settings(settings)
                    .build()
                    .addTransportAddress(address);

            EsTransportClientUtil<T> esTransportClientUtil = new EsTransportClientUtil<T>(client, esIndex);
            esTransportClientUtil.insertBulkRecords(recordList);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}