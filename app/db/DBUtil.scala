package db

import java.sql.{Connection, DriverManager}
import java.util
import java.util.concurrent.ConcurrentHashMap

import com.typesafe.config.ConfigFactory
import org.apache.commons.dbutils.QueryRunner
import org.apache.commons.dbutils.handlers.{MapHandler, MapListHandler}


/**
  * Created by wxji on 2017-08-14.
  */
object DBUtil {

  private val config = ConfigFactory.load("application.conf").getConfig("db.default")
  private val sceneCache = new ConcurrentHashMap[Long, util.Map[String, AnyRef]]()
  private val sceneCellCache = new ConcurrentHashMap[Long, util.List[util.Map[String, AnyRef]]]()
  private val characterCache = new ConcurrentHashMap[Long, util.List[util.Map[String, AnyRef]]]()

  Class.forName(config.getString("driver"))

  private def getConnection: Connection = {
    DriverManager.getConnection(config.getString("url"), config.getString("username"), config.getString("password"))
  }

  def getSceneById(sceneId: Long): util.Map[String, AnyRef] = {
    if (sceneCache.contains(sceneId) && 1 != 1) {
      sceneCache.get(sceneId)
    } else {
      val conn = getConnection
      try {
        val scene = new QueryRunner().query(conn, s"select * from scene where scene_id = $sceneId", new MapHandler())
        sceneCache.put(sceneId, scene)
        scene
      } finally {
        conn.close()
      }
    }
  }

  def getSceneCellBySceneId(sceneId: Long): util.List[util.Map[String, AnyRef]] = {
    if (sceneCellCache.contains(sceneId) && 1 != 1) {
      sceneCellCache.get(sceneId)
    } else {
      val conn = getConnection
      try {
        val sceneCells = new QueryRunner().query(conn, s"select * from scene_cell sc where sc.scene_id = $sceneId", new MapListHandler())
        sceneCellCache.put(sceneId, sceneCells)
        sceneCells
      } finally {
        conn.close()
      }
    }
  }

  def getNpcByCell(cellId: Long): util.List[util.Map[String, AnyRef]] = {
    val conn = getConnection
    try {
      new QueryRunner().query(conn, s"select * from npc where scene_cell_id = $cellId", new MapListHandler())
    } finally {
      conn.close()
    }
  }
}
