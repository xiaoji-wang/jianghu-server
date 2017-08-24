package db

import java.sql.{Connection, DriverManager}
import java.util
import java.util.Comparator

import com.typesafe.config.ConfigFactory

/**
  * Created by wxji on 2017-08-14.
  */
object DBUtil {

  private val config = ConfigFactory.load("application.conf").getConfig("db.default")

  Class.forName(config.getString("driver"))

  private def getConnection: Connection = {
    DriverManager.getConnection(config.getString("url"), config.getString("username"), config.getString("password"))
  }

  def getSceneById(sceneId: Long): java.util.Map[String, Any] = {
    val conn = DBUtil.getConnection
    try {
      val result = new util.HashMap[String, Any]()
      val ps = conn.prepareStatement("select * from jh_scene where scene_id=?")
      ps.setLong(1, sceneId)
      val rs = ps.executeQuery()
      if (rs.next()) {
        result.put("name", rs.getString("name"))
      }
      result
    } finally {
      conn.close()
    }
  }

  def getSceneCellBySceneId(sceneId: Long): util.Collection[util.HashMap[String, Any]] = {
    val conn = DBUtil.getConnection
    try {
      val sb = new StringBuilder(100);
      sb append "select sc.*,c.character_id,c.name character_name from "
      sb append "jh_scene_cell sc "
      sb append "left join jh_scene_cell_character scc on sc.scene_cell_id = scc.scene_cell_id "
      sb append "left join jh_character c on c.character_id = scc.character_id "
      sb append "where sc.arrive = 1 and scene_id = ? order by c.character_id"
      val ps = conn.prepareStatement(sb.toString)
      ps.setLong(1, sceneId)
      val rs = ps.executeQuery()
      val map = new java.util.HashMap[Long, java.util.HashMap[String, Any]]()
      while (rs.next()) {
        val id = rs.getLong("scene_cell_id")
        var temp = new java.util.HashMap[String, Any]()
        if (map.containsKey(id)) {
          temp = map.get(id)
        } else {
          temp.put("name", rs.getString("name"))
          val axisPoint = new java.util.HashMap[String, Any]()
          axisPoint.put("x", rs.getInt("x"))
          axisPoint.put("y", rs.getInt("y"))
          temp.put("axisPoint", axisPoint)
          temp.put("startPoint", rs.getBoolean("start_point"))
          temp.put("npc", new java.util.ArrayList[java.util.HashMap[String, Any]]())
          map.put(id, temp)
        }
        if (rs.getLong("character_id") != null) {
          val list = temp.get("npc").asInstanceOf[java.util.ArrayList[java.util.HashMap[String, Any]]]
          val npc = new java.util.HashMap[String, Any]()
          npc.put("id", rs.getString("character_id"))
          npc.put("name", rs.getString("character_name"))
          list.add(npc)
          temp.put("npc", list)
        }
      }
      map.values()
    } finally {
      conn.close()
    }
  }
}
