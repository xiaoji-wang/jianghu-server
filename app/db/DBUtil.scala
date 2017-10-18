package db

import java.util
import java.util.Properties
import java.util.stream.Collectors

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import org.apache.commons.dbutils.QueryRunner
import org.apache.commons.dbutils.handlers.{MapHandler, MapListHandler}

import scala.collection.JavaConversions._

/**
  * Created by wxji on 2017-08-14.
  */
object DBUtil {

  val in = getClass.getClassLoader.getResourceAsStream("hikari.properties")
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
      val sceneCells: util.List[util.Map[String, AnyRef]] = new QueryRunner().query(conn,
        "select " +
          "sc.scene_cell_id id," +
          "sc.name," +
          "sc.description description," +
          "east_id eId," +
          "east_out eOut," +
          "south_east_id seId," +
          "south_east_out seOut," +
          "south_west_id swId," +
          "south_west_out swOut," +
          "west_id wId," +
          "west_out wOut," +
          "north_east_id neId," +
          "north_east_out neOut," +
          "north_west_id nwId," +
          "north_west_out nwOut," +
          "x," +
          "y, " +
          "n.npc_id npcId, " +
          "n.name npcName " +
          "from scene_cell sc " +
          "left join npc n on n.scene_cell_id = sc.scene_cell_id " +
          "where sc.scene_id = ?", new MapListHandler(), sceneId.asInstanceOf[java.lang.Long])
      val map = new util.HashMap[Long, util.Map[String, AnyRef]]()
      sceneCells.foreach((c: util.Map[String, AnyRef]) => {
        var t: util.Map[String, AnyRef] = c
        if (!map.containsKey(c.get("id"))) {
          c.put("npc", new util.ArrayList[util.Map[String, AnyRef]])
          map.put(c.get("id").asInstanceOf[Long], c)
        } else {
          t = map.get(c.get("id").asInstanceOf[Long])
        }
        if (c.get("npcId") != null) {
          val l = t.get("npc").asInstanceOf[util.ArrayList[util.Map[String, AnyRef]]]
          val n = new util.HashMap[String, AnyRef]()
          n.put("id", c.get("npcId"))
          n.put("name", c.get("npcName"))
          l.add(n)
        }
        c.remove("npcId")
        c.remove("npcName")
      })
      map.values().stream().collect(Collectors.toList[util.Map[String, AnyRef]])
    } finally {
      conn.close()
    }
  }

  def getNpcByCell(cellId: Long): util.List[util.Map[String, AnyRef]] = {
    val conn = dataSource.getConnection
    try {
      new QueryRunner().query(conn, s"select npc_id id,name from npc where scene_cell_id = $cellId", new MapListHandler())
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
