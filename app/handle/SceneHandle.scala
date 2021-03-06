package handle

import java.util

import com.google.gson.Gson
import db.DBUtil

import scala.collection.JavaConversions._

/**
  * Created by wxji on 2017-09-22.
  */
object SceneHandle {
  def getMap(sceneId: Long): String = {
    val scene = DBUtil.getSceneById(sceneId)
    val sceneCell: util.List[util.Map[String, AnyRef]] = DBUtil.getSceneCellBySceneId(sceneId)

    val multiDimArr = Array.ofDim[util.Map[String, AnyRef]](scene("height").asInstanceOf[Int], scene("width").asInstanceOf[Int])

    sceneCell.foreach((c: util.Map[String, AnyRef]) => multiDimArr(c("y").asInstanceOf[Int])(c("x").asInstanceOf[Int]) = c)

    new Gson().toJson(mapAsJavaMap(Map("name" -> scene.get("name"), "cells" -> multiDimArr)))
  }
}
