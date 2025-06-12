package com.example.demo.dto;

import java.util.List;

public class GptResponse {
    private String plain_language_summary;
    private String recommendations;
    private List<RygAlert> ryg_alert;

    public String getPlain_language_summary() {
        return plain_language_summary;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public List<RygAlert> getRyg_alert() {
        return ryg_alert;
    }

    public GptResponse() {}
    public GptResponse(String aiResponse) { }
    public static class RygAlert {
        private String biomarker;
        private ScaleData scale_data;
        private String classification;
        private String reason;

        public String getBiomarker() {
            return biomarker;
        }

        public ScaleData getScale_data() {
            return scale_data;
        }

        public String getClassification() {
            return classification;
        }

        public String getReason() {
            return reason;
        }

        public static class ScaleData {
            private double yellow_lower_bound;
            private double yellow_upper_bound;

            public double getYellow_lower_bound() {
                return yellow_lower_bound;
            }

            public double getYellow_upper_bound() {
                return yellow_upper_bound;
            }
        }
    }
}
