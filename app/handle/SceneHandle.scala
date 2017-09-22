package handle

import java.util

import com.google.gson.Gson
import db.DBUtil

/**
  * Created by wxji on 2017-09-22.
  */
object SceneHandle {

  import scala.collection.JavaConversions._

  def getMap(sceneId: Long): String = {
    val scene = DBUtil.getSceneById(sceneId)
    val sceneCell: util.List[util.Map[String, AnyRef]] = DBUtil.getSceneCellBySceneId(sceneId)
//    val it = sceneCell.iterator()
//    while (it.hasNext) {
//      val m: util.Map[String, AnyRef] = it.next()
//      if (m.get("x").asInstanceOf[Int] == 5 && m.get("y").asInstanceOf[Int] == 4) {
//        m.put("startPoint", Boolean.box(true))
//      }
//    }
    //    sceneCell.stream()
    //      .filter((m: util.Map[String, AnyRef]) => Boolean = m.get("x").asInstanceOf[Int] == 5 && m.get("y").asInstanceOf[Int] == 4)
    //      .findAny()
    //      .get()
    //      .put("startPoint", Boolean.box(true))
//    sceneCell.get(100).put("startPoint", Boolean.box(true))
    new Gson().toJson(mapAsJavaMap(Map("name" -> scene.get("name"), "cells" -> sceneCell)))
  }
}
