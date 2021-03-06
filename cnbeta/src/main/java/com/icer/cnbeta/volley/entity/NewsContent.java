package com.icer.cnbeta.volley.entity;

import android.database.Cursor;

import com.icer.cnbeta.db.DBConstant;

/**
 * Created by icer on 2015-09-28.
 */
public class NewsContent {
    public String sid;
    public String catid;
    public String topic;
    public String aid;
    public String title;
    public String style;
    public String keywords;
    public String hometext;
    public String listorder;
    public String comments;
    public String counter;
    public String good;
    public String bad;
    public String score;
    public String ratings;
    public String score_story;
    public String ratings_story;
    public String elite;
    public String status;
    public String inputtime;
    public String updatetime;
    public String thumb;
    public String source;
    public String data_id;
    public String bodytext;
    public String time;

    public NewsContent() {

    }

    public NewsContent(Cursor cursor) {
        sid = cursor.getString(cursor.getColumnIndex(DBConstant.TableContent.COLUMN_SID));
        catid = cursor.getString(cursor.getColumnIndex(DBConstant.TableContent.COLUMN_CATID));
        topic = cursor.getString(cursor.getColumnIndex(DBConstant.TableContent.COLUMN_TOPIC));
        aid = cursor.getString(cursor.getColumnIndex(DBConstant.TableContent.COLUMN_AID));
        title = cursor.getString(cursor.getColumnIndex(DBConstant.TableContent.COLUMN_TITLE));
        style = cursor.getString(cursor.getColumnIndex(DBConstant.TableContent.COLUMN_STYLE));
        keywords = cursor.getString(cursor.getColumnIndex(DBConstant.TableContent.COLUMN_KEYWORDS));
        hometext = cursor.getString(cursor.getColumnIndex(DBConstant.TableContent.COLUMN_HOMETEXT));
        listorder = cursor.getString(cursor.getColumnIndex(DBConstant.TableContent.COLUMN_LISTORDER));
        comments = cursor.getString(cursor.getColumnIndex(DBConstant.TableContent.COLUMN_COMMENTS));
        counter = cursor.getString(cursor.getColumnIndex(DBConstant.TableContent.COLUMN_COUNTER));
        good = cursor.getString(cursor.getColumnIndex(DBConstant.TableContent.COLUMN_GOOD));
        bad = cursor.getString(cursor.getColumnIndex(DBConstant.TableContent.COLUMN_BAD));
        score = cursor.getString(cursor.getColumnIndex(DBConstant.TableContent.COLUMN_SCORE));
        ratings = cursor.getString(cursor.getColumnIndex(DBConstant.TableContent.COLUMN_RATINGS));
        score_story = cursor.getString(cursor.getColumnIndex(DBConstant.TableContent.COLUMN_SCORE_STORY));
        ratings_story = cursor.getString(cursor.getColumnIndex(DBConstant.TableContent.COLUMN_RATINGS_STORY));
        elite = cursor.getString(cursor.getColumnIndex(DBConstant.TableContent.COLUMN_ELITE));
        status = cursor.getString(cursor.getColumnIndex(DBConstant.TableContent.COLUMN_STATUS));
        inputtime = cursor.getString(cursor.getColumnIndex(DBConstant.TableContent.COLUMN_INPUTTIME));
        updatetime = cursor.getString(cursor.getColumnIndex(DBConstant.TableContent.COLUMN_UPDATETIME));
        thumb = cursor.getString(cursor.getColumnIndex(DBConstant.TableContent.COLUMN_THUMB));
        source = cursor.getString(cursor.getColumnIndex(DBConstant.TableContent.COLUMN_SOURCE));
        data_id = cursor.getString(cursor.getColumnIndex(DBConstant.TableContent.COLUMN_DATA_ID));
        bodytext = cursor.getString(cursor.getColumnIndex(DBConstant.TableContent.COLUMN_BODYTEXT));
        time = cursor.getString(cursor.getColumnIndex(DBConstant.TableContent.COLUMN_TIME));
    }

    @Override
    public String toString() {
        return "NewsContent{" +
                "sid='" + sid + '\'' +
                ", catid='" + catid + '\'' +
                ", topic='" + topic + '\'' +
                ", aid='" + aid + '\'' +
                ", title='" + title + '\'' +
                ", style='" + style + '\'' +
                ", keywords='" + keywords + '\'' +
                ", hometext='" + hometext + '\'' +
                ", listorder='" + listorder + '\'' +
                ", comments='" + comments + '\'' +
                ", counter='" + counter + '\'' +
                ", good='" + good + '\'' +
                ", bad='" + bad + '\'' +
                ", score='" + score + '\'' +
                ", ratings='" + ratings + '\'' +
                ", score_story='" + score_story + '\'' +
                ", ratings_story='" + ratings_story + '\'' +
                ", elite='" + elite + '\'' +
                ", status='" + status + '\'' +
                ", inputtime='" + inputtime + '\'' +
                ", updatetime='" + updatetime + '\'' +
                ", thumb='" + thumb + '\'' +
                ", source='" + source + '\'' +
                ", data_id='" + data_id + '\'' +
                ", bodytext='" + bodytext + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
