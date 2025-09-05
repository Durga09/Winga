package in.eightfolds.winga.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import in.eightfolds.winga.model.DurationDetails;
import in.eightfolds.winga.model.DurationDetailsServer;

@Dao
public interface DurationDetailsDao {

    @Insert
    void insertAll(DurationDetails... durationDetails);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(DurationDetails user);


    @Update
    void updateAll(DurationDetails... durationDetails);

    @Query("DELETE FROM durationdetails WHERE durationdetails.aduniqueid == :aduniqueid AND  isadddetail == :isadddetail")
    void deleteSubmittedQueries(long aduniqueid, int isadddetail);

    @Query("DELETE FROM durationdetails WHERE durationdetails.gameid == :gameId")
    void deleteSubmittedgameQueries(long gameId);


   /* @Query("DELETE FROM durationdetails WHERE durationdetails.gameid == :gameId")
    void deleteSubmittedQueries(List<Long> gameId);*/



    /*@Query("DELETE FROM durationdetails WHERE durationdetails.aduniqueid == :aduniqueid AND  isadddetail == 1")
    void deleteSubmittedQueries(long aduniqueid);*/

    @Update
    void update(DurationDetails durationDetails);

    @Query("SELECT * FROM durationdetails WHERE isadddetail == :isadddetail")
    List<DurationDetails> getAll(int isadddetail);

    @Query("SELECT * FROM durationdetails")
    List<DurationDetails> getAll();

    @Query("SELECT SUM(durationdetails.activemills)  " +
            "FROM durationdetails " +
            "GROUP BY durationdetails.videoStartUniqueId "+
            "HAVING durationdetails.gameid == :gameId AND  durationdetails.videoStartUniqueId != 0 ")
    List<Long> getUniqueVideoWatchDetails(long gameId);


    @Query("SELECT SUM(durationdetails.activemills)  " +
            "FROM durationdetails " +
            "GROUP BY durationdetails.videoStartUniqueId "+
            "HAVING durationdetails.gameid == :gameId AND durationdetails.contentId == :contentId AND  durationdetails.videoStartUniqueId != 0 AND  isadddetail == 0 ")
    List<Long> getUniqueVideoWatchDetails(long gameId, long contentId);


    @Query("SELECT SUM(durationdetails.activemills)  " +
            "FROM durationdetails " +
            "GROUP BY durationdetails.videoStartUniqueId "+
            "HAVING durationdetails.aduniqueid == :aduniqueid AND durationdetails.homePageAddId == :homePageAddId AND  durationdetails.videoStartUniqueId != 0 ")
    List<Long> getUniqueAdWatchDetails(long aduniqueid, long homePageAddId);

    @Query("SELECT DISTINCT durationdetails.gameid  " +
            "FROM durationdetails WHERE isadddetail == 0" )
    List<Long> getUniqueGameIdsInDB();

    @Query("SELECT DISTINCT durationdetails.aduniqueid  " +
            "FROM durationdetails WHERE isadddetail == 1" )
    List<Long> getUniqueAdIdsInDB();


    @Query("SELECT DISTINCT durationdetails.contentId  " +
            "FROM durationdetails " +
            "WHERE durationdetails.gameid == :gameId AND    isadddetail == 0") //durationdetails.videoStartUniqueId != 0 AND
    List<Long> getUniqueContentIdsInGame(long gameId);


    @Query("SELECT DISTINCT durationdetails.homepageaddid  " +
            "FROM durationdetails " +
            "WHERE durationdetails.aduniqueid == :aduniqueid AND  durationdetails.videoStartUniqueId != 0 ")
    List<Long> getUniqueHomeAdIdsInGame(long aduniqueid);


    @Query("SELECT durationdetails.gameid as gameId, durationdetails.contentId as contentId, SUM(durationdetails.activemills) as activeMills, " +
            " SUM(durationdetails.questionActivemillis) as  questionsActiveMills " +
            "FROM durationdetails " +
            "WHERE durationdetails.gameid == :gameId ")
    DurationDetailsServer getContentDurationDetails(long gameId);

    @Query("SELECT durationdetails.gameid as gameId, durationdetails.contentId as contentId, SUM(durationdetails.activemills) as activeMills, " +
            " SUM(durationdetails.questionActivemillis) as  questionsActiveMills " +
            "FROM durationdetails " +
            "WHERE durationdetails.gameid == :gameId AND durationdetails.contentId == :contentId AND isadddetail == 0")
    DurationDetailsServer getContentDurationDetails(long gameId, long contentId);


    @Query("SELECT durationdetails.gameid as gameId, durationdetails.homepageaddid as contentId, SUM(durationdetails.activemills) as activeMills, " +
            " SUM(durationdetails.questionActivemillis) as  questionsActiveMills " +
            "FROM durationdetails " +
            "WHERE durationdetails.aduniqueid == :aduniqueid  AND isadddetail == 1")
    DurationDetailsServer getHomeAdDurationDetails(long aduniqueid);

    @Query("SELECT *" +
            "FROM durationdetails " +
            "WHERE durationdetails.id == :id")
    DurationDetails getDurationDetailsForId(long id);


    @Query("SELECT *" +
            "FROM durationdetails " +
            "WHERE durationdetails.contentId == :videoId")
    DurationDetails getDurationDetailsForVideoId(long videoId);

    @Query("SELECT *" +
            "FROM durationdetails " +
            "WHERE durationdetails.questionId == :questionId")
    DurationDetails getDurationDetailsForQuestionId(long questionId);

    @Delete
    void deleteAll(DurationDetails... durationDetails);




}
