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
    new Gson().toJson(mapAsJavaMap(Map("name" -> scene.get("name"), "cells" -> sceneCell)))
  }
}
