package KeyValueServer;

import java.io.Serializable;

public class Request implements Serializable {
        private String method;
        private String key;
        private String value;

        public Request(String method, String key) {
            this.method = method;
            this.key = key;
        }

        public Request(String method, String key, String value) {
            this.method = method;
            this.key = key;
            this.value = value;
        }

    public String getMethod() {
            return method;
    }

    public String getKey() {
            return key;
    }

    public String getValue() {
            return value;
    }
}
