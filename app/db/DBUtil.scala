package db

import java.sql.{Connection, DriverManager}

import com.typesafe.config.ConfigFactory
import org.json4s.JsonAST
import org.json4s.JsonDSL._

/**
  * Created by wxji on 2017-08-14.
  */
object DBUtil {

  private val config = ConfigFactory.load("application.conf").getConfig("db.default")

  Class.forName(config.getString("driver"))

  private def getConnection: Connection = {
    DriverManager.getConnection(config.getString("url"), config.getString("username"), config.getString("password"))
  }

  def getSceneById(sceneId: Long): JsonAST.JObject = {
    val conn = DBUtil.getConnection
    try {
      var result = JsonAST.JObject()
      val ps = conn.prepareStatement("select * from jh_scene where scene_id=?")
      ps.setLong(1, sceneId)
      val rs = ps.executeQuery()
      while (rs.next()) {
        result = "name" -> rs.getString("name")
      }
      result
    } finally {
      conn.close()
    }
  }

  def getSceneCellBySceneId(sceneId: Long): List[JsonAST.JObject] = {
    val conn = DBUtil.getConnection
    try {
      var result = List[JsonAST.JObject]()
      val ps = conn.prepareStatement("select * from jh_scene_cell where scene_id=?")
      ps.setLong(1, sceneId)
      val rs = ps.executeQuery()
      while (rs.next()) {
        if (rs.getBoolean("arrive")) {
          result = result :+ ("name" -> rs.getString("name")) ~ ("axisPoint" -> ("x" -> rs.getInt("x")) ~ ("y" -> rs.getInt("y"))) ~ ("startPoint" -> rs.getBoolean("start_point"))
        }
      }
      result
    } finally {
      conn.close()
    }
  }
}
