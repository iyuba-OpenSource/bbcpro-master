package com.ai.bbcpro.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchBean {

    /**
     * {
     * "WordId": "290038",
     * "Word": "words",
     * "def": "[\"字( word的名词复数 )\",\"（说的）话\",\"诺言\",\"口令\"]n.",
     * "ph_am": "w%C9%9Cdz",
     * "ph_am_mp3": "",
     * "titleData": [
     * {
     * "Category": "10",
     * "Title_Cn": "In, On和At的用法",
     * "CreateTime": "2022-09-16 15:20:00.0",
     * "Title": "Big Trouble with Three Little Words – In, On and At",
     * "Texts": 41,
     * "Sound": "http://staticvip.iyuba.cn/sounds/voa/202209/16176.mp3",
     * "Pic": "http://staticvip.iyuba.cn/images/voa/202209/16176.jpg",
     * "VoaId": "16176",
     * "ReadCount": "18581"
     * }
     * ],
     * "ph_en": "w%C9%9C%3Adz",
     * "titleToal": 10,
     * "ph_en_mp3": "",
     * "textData": [
     * {
     * "EndTiming": "16.00",
     * "ParaId": "1",
     * "IdIndex": "1",
     * "SoundText": "http://staticvip.iyuba.cn/sounds/voa/sentence/202210/16262/16262_1_1.wav",
     * "Sentence_cn": "现在是美国之音慢速英语《词汇掌故》。",
     * "Timing": "4.17",
     * "VoaId": "16262",
     * "Sentence": "And now Words and Their Stories, from VOA Learning English."
     * }
     * ],
     * "English": true,
     * "WordCn": "字( word的名词复数 )；（说的）话；诺言；口令；\n",
     * "textToal": 10
     * }
     */

    @SerializedName("WordId")
    private String wordId;
    @SerializedName("Word")
    private String word;
    @SerializedName("def")
    private String def;
    @SerializedName("ph_am")
    private String phAm;
    @SerializedName("ph_am_mp3")
    private String phAmMp3;
    @SerializedName("titleData")
    private List<TitleDataDTO> titleData;
    @SerializedName("ph_en")
    private String phEn;
    @SerializedName("titleToal")
    private int titleToal;
    @SerializedName("ph_en_mp3")
    private String phEnMp3;
    @SerializedName("textData")
    private List<TitleDataDTO> textData;
    @SerializedName("English")
    private boolean english;
    @SerializedName("WordCn")
    private String wordCn;
    @SerializedName("textToal")
    private int textToal;

    public String getWordId() {
        return wordId;
    }

    public void setWordId(String wordId) {
        this.wordId = wordId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
    }

    public String getPhAm() {
        return phAm;
    }

    public void setPhAm(String phAm) {
        this.phAm = phAm;
    }

    public String getPhAmMp3() {
        return phAmMp3;
    }

    public void setPhAmMp3(String phAmMp3) {
        this.phAmMp3 = phAmMp3;
    }

    public List<TitleDataDTO> getTitleData() {
        return titleData;
    }

    public void setTitleData(List<TitleDataDTO> titleData) {
        this.titleData = titleData;
    }

    public String getPhEn() {
        return phEn;
    }

    public void setPhEn(String phEn) {
        this.phEn = phEn;
    }

    public int getTitleToal() {
        return titleToal;
    }

    public void setTitleToal(int titleToal) {
        this.titleToal = titleToal;
    }

    public String getPhEnMp3() {
        return phEnMp3;
    }

    public void setPhEnMp3(String phEnMp3) {
        this.phEnMp3 = phEnMp3;
    }

    public List<TitleDataDTO> getTextData() {
        return textData;
    }

    public void setTextData(List<TitleDataDTO> textData) {
        this.textData = textData;
    }

    public boolean isEnglish() {
        return english;
    }

    public void setEnglish(boolean english) {
        this.english = english;
    }

    public String getWordCn() {
        return wordCn;
    }

    public void setWordCn(String wordCn) {
        this.wordCn = wordCn;
    }

    public int getTextToal() {
        return textToal;
    }

    public void setTextToal(int textToal) {
        this.textToal = textToal;
    }

    public static class TitleDataDTO {

        @SerializedName("Category")
        private String category;
        @SerializedName("Title_Cn")
        private String titleCn;
        @SerializedName("CreateTime")
        private String createTime;
        @SerializedName("Title")
        private String title;
        @SerializedName("Texts")
        private int texts;
        @SerializedName("Sound")
        private String sound;
        @SerializedName("Pic")
        private String pic;
        @SerializedName("BbcId")
        private int BbcId;
        @SerializedName("ReadCount")
        private String readCount;

        @SerializedName("EndTiming")
        private String endTiming;
        @SerializedName("ParaId")
        private String paraId;
        @SerializedName("IdIndex")
        private String idIndex;
        @SerializedName("SoundText")
        private String soundText;
        @SerializedName("Sentence_cn")
        private String sentenceCn;
        @SerializedName("Timing")
        private String timing;
        @SerializedName("Sentence")
        private String sentence;
        @SerializedName("Word")
        private String Word;
        @SerializedName("def")
        private String def;
        @SerializedName("ph_en")
        private String ph_en;
        /**
         * flag  1 单词
         * 2 句子
         * 3 文章
         * 101  单词标题
         * 102  句子标题
         * 103  文章标题
         */
        @SerializedName("flag")
        private int flag = -1;

        /**
         * 播放总时长
         */
        private int duration = 0;

        /**
         * 播放音频的当前位置
         */
        private int curPosition = 0;

        /**
         * 评测得分
         */
        private int score = -1;

        /**
         * 单词音频
         */
        private String phAmMp3;

        /**
         * 0:未收藏
         * 1：已收藏
         */
        private int collect = 0;


        public int getCollect() {
            return collect;
        }

        public void setCollect(int collect) {
            this.collect = collect;
        }

        public String getPhAmMp3() {
            return phAmMp3;
        }

        public void setPhAmMp3(String phAmMp3) {
            this.phAmMp3 = phAmMp3;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getCurPosition() {
            return curPosition;
        }

        public void setCurPosition(int curPosition) {
            this.curPosition = curPosition;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public String getEndTiming() {
            return endTiming;
        }

        public void setEndTiming(String endTiming) {
            this.endTiming = endTiming;
        }

        public String getParaId() {
            return paraId;
        }

        public void setParaId(String paraId) {
            this.paraId = paraId;
        }

        public String getIdIndex() {
            return idIndex;
        }

        public void setIdIndex(String idIndex) {
            this.idIndex = idIndex;
        }

        public String getSoundText() {
            return soundText;
        }

        public void setSoundText(String soundText) {
            this.soundText = soundText;
        }

        public String getSentenceCn() {
            return sentenceCn;
        }

        public void setSentenceCn(String sentenceCn) {
            this.sentenceCn = sentenceCn;
        }

        public String getTiming() {
            return timing;
        }

        public void setTiming(String timing) {
            this.timing = timing;
        }

        public String getSentence() {
            return sentence;
        }

        public void setSentence(String sentence) {
            this.sentence = sentence;
        }

        public String getWord() {
            return Word;
        }

        public void setWord(String word) {
            Word = word;
        }

        public String getDef() {
            return def;
        }

        public void setDef(String def) {
            this.def = def;
        }

        public String getPh_en() {
            return ph_en;
        }

        public void setPh_en(String ph_en) {
            this.ph_en = ph_en;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getTitleCn() {
            return titleCn;
        }

        public void setTitleCn(String titleCn) {
            this.titleCn = titleCn;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getTexts() {
            return texts;
        }

        public void setTexts(int texts) {
            this.texts = texts;
        }

        public String getSound() {
            return sound;
        }

        public void setSound(String sound) {
            this.sound = sound;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getReadCount() {
            return readCount;
        }

        public void setReadCount(String readCount) {
            this.readCount = readCount;
        }

        public int getBbcId() {
            return BbcId;
        }

        public void setBbcId(int bbcId) {
            BbcId = bbcId;
        }
    }
}
