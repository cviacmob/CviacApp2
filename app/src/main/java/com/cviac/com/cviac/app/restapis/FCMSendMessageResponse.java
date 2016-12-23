package com.cviac.com.cviac.app.restapis;

import java.util.List;

/**
 * Created by Cviac on 19/12/2016.
 */
public class FCMSendMessageResponse {

    private long multicast_id;

    private int success;

    private int failure;

    private int canonical_ids;

    private List<ResultInfo> results;

    public long getMulticast_id() {
        return multicast_id;
    }

    public void setMulticast_id(long multicast_id) {
        this.multicast_id = multicast_id;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getFailure() {
        return failure;
    }

    public void setFailure(int failure) {
        this.failure = failure;
    }

    public int getCanonical_ids() {
        return canonical_ids;
    }

    public void setCanonical_ids(int canonical_ids) {
        this.canonical_ids = canonical_ids;
    }

    public List<ResultInfo> getResults() {
        return results;
    }

    public void setResults(List<ResultInfo> results) {
        this.results = results;
    }

    public class ResultInfo {
        private String message_id;

        public ResultInfo() {
        }

        public String getMessage_id() {
            return message_id;
        }

        public void setMessage_id(String message_id) {
            this.message_id = message_id;
        }
    }


}
