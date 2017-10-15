package db

import java.io.InputStream
import java.nio.file.{Files, Paths}
import java.util
import java.util.Properties

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import org.apache.commons.dbutils.QueryRunner
import org.apache.commons.dbutils.handlers.{MapHandler, MapListHandler}


/**
  * Created by wxji on 2017-08-14.
  */
object DBUtil {

  val in: InputStream = Files.newInputStream(Paths.get("conf/hikari.properties"))
  val properties = new Properties()
  properties.load(in)
  in.close()

  val dataSource: HikariDataSource = new HikariDataSource(new HikariConfig(properties))

  def getSceneById(sceneId: Long): util.Map[String, AnyRef] = {
    val conn = dataSource.getConnection
    try {
      val scene = new QueryRunner().query(conn, s"select * from scene where scene_id = $sceneId", new MapHandler())
      scene
    } finally {
      conn.close()
    }
  }

  def getSceneCellBySceneId(sceneId: Long): util.List[util.Map[String, AnyRef]] = {
    val conn = dataSource.getConnection
    try {
      val sceneCells = new QueryRunner().query(conn, s"select * from scene_cell sc where sc.scene_id = $sceneId", new MapListHandler())
      sceneCells
    } finally {
      conn.close()
    }
  }

  def getNpcByCell(cellId: Long): util.List[util.Map[String, AnyRef]] = {
    val conn = dataSource.getConnection
    try {
      new QueryRunner().query(conn, s"select * from npc where scene_cell_id = $cellId", new MapListHandler())
    } finally {
      conn.close()
    }
  }

  def getNpcById(id: Long): util.Map[String, AnyRef] = {
    val conn = dataSource.getConnection
    try {
      val scene = new QueryRunner().query(conn, s"select * from npc where npc_id = $id", new MapHandler())
      scene
    } finally {
      conn.close()
    }
  }
}
