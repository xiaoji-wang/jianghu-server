package actor

import akka.actor.{Actor, Props}
import com.google.gson.{Gson, JsonObject}
import db.DBUtil
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame

/**
  * Created by wxji on 2017-08-14.
  */
class DispatchActor extends Actor {

  override def receive: Receive = {
    case params: Map[String, Any] => {
      val data = new Gson().fromJson(params("data").asInstanceOf[String], classOf[JsonObject])
      val ref = context.actorOf(Props(WorkerActor(params("ctx").asInstanceOf[ChannelHandlerContext])))
      ref ! (data.get("action").getAsInt, data.get("data").getAsJsonObject)
    }
  }
}

case class WorkerActor(ctx: ChannelHandlerContext) extends Actor {
  override def receive: Receive = {
    case data: (Int, JsonObject) => data._1 match {
      case Constant.GET_MAP => {
        val scene = DBUtil.getSceneById(1L)
        val sceneCell = DBUtil.getSceneCellBySceneId(1L)
        val result = new java.util.HashMap[String, Any]()
        result.put("name", scene.get("name"))
        result.put("cells", sceneCell)
        ctx.writeAndFlush(new TextWebSocketFrame(new Gson().toJson(result)))
      }
      case Constant.NPC_SELECTED => {
        ctx.writeAndFlush(new TextWebSocketFrame(new Gson().toJson(DBUtil.getCharacterById(data._2.get("id").getAsInt))))
      }
      case Constant.NPC_TALK => {
        ctx.writeAndFlush(new TextWebSocketFrame(new Gson().toJson(DBUtil.getCharacterWord(data._2.get("id").getAsInt))))
      }
    }
    case _ => ctx.writeAndFlush(new TextWebSocketFrame(""))
  }
}