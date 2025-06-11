package com.ai.bbcpro.ui.player.eval;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EvalResponse {

    @SerializedName("result")
    private String result;
    @SerializedName("data")
    private DataBean data;
    @SerializedName("message")
    private String message;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        @SerializedName("sentence")
        private String sentence;
        @SerializedName("filepath")
        private String filepath;
        @SerializedName("scores")
        private int scores;
        @SerializedName("words")
        private List<EvalWord> words;
        @SerializedName("total_score")
        private double totalScore;
        @SerializedName("URL")
        private String url;

        public String getSentence() {
            return sentence;
        }

        public void setSentence(String sentence) {
            this.sentence = sentence;
        }

        public String getFilepath() {
            return filepath;
        }

        public void setFilepath(String filepath) {
            this.filepath = filepath;
        }

        public int getScores() {
            return scores;
        }

        public void setScores(int scores) {
            this.scores = scores;
        }

        public List<EvalWord> getWords() {
            return words;
        }

        public void setWords(List<EvalWord> words) {
            this.words = words;
        }

        public double getTotalScore() {
            return totalScore;
        }

        public void setTotalScore(double totalScore) {
            this.totalScore = totalScore;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "sentence='" + sentence + '\'' +
                    ", filepath='" + filepath + '\'' +
                    ", scores=" + scores +
                    ", words=" + words.toString() +
                    ", totalScore=" + totalScore +
                    ", url='" + url + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "EvalResponse{" +
                "result='" + result + '\'' +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}
