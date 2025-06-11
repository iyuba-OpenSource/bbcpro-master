package com.ai.bbcpro.ui.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EvalWordBean {

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
        private List<WordsBean> words;
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

        public List<WordsBean> getWords() {
            return words;
        }

        public void setWords(List<WordsBean> words) {
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

        public static class WordsBean {
            @SerializedName("score")
            private String score;
            @SerializedName("substitute_orgi")
            private String substituteOrgi;
            @SerializedName("pron")
            private String pron;
            @SerializedName("index")
            private String index;
            @SerializedName("insert")
            private String insert;
            @SerializedName("user_pron2")
            private String userPron2;
            @SerializedName("delete")
            private String delete;
            @SerializedName("substitute_user")
            private String substituteUser;
            @SerializedName("content")
            private String content;
            @SerializedName("pron2")
            private String pron2;
            @SerializedName("user_pron")
            private String userPron;

            public String getScore() {
                return score;
            }

            public void setScore(String score) {
                this.score = score;
            }

            public String getSubstituteOrgi() {
                return substituteOrgi;
            }

            public void setSubstituteOrgi(String substituteOrgi) {
                this.substituteOrgi = substituteOrgi;
            }

            public String getPron() {
                return pron;
            }

            public void setPron(String pron) {
                this.pron = pron;
            }

            public String getIndex() {
                return index;
            }

            public void setIndex(String index) {
                this.index = index;
            }

            public String getInsert() {
                return insert;
            }

            public void setInsert(String insert) {
                this.insert = insert;
            }

            public String getUserPron2() {
                return userPron2;
            }

            public void setUserPron2(String userPron2) {
                this.userPron2 = userPron2;
            }

            public String getDelete() {
                return delete;
            }

            public void setDelete(String delete) {
                this.delete = delete;
            }

            public String getSubstituteUser() {
                return substituteUser;
            }

            public void setSubstituteUser(String substituteUser) {
                this.substituteUser = substituteUser;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getPron2() {
                return pron2;
            }

            public void setPron2(String pron2) {
                this.pron2 = pron2;
            }

            public String getUserPron() {
                return userPron;
            }

            public void setUserPron(String userPron) {
                this.userPron = userPron;
            }
        }
    }
}
