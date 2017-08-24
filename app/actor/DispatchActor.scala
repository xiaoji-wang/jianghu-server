package actor

import java.util

import akka.actor.{Actor, Props}
import com.google.gson.Gson
import db.DBUtil
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame

/**
  * Created by wxji on 2017-08-14.
  */
class DispatchActor extends Actor {
  override def receive: Receive = {
    case params: Map[String, Any] => {
      val ref = context.actorOf(Props(WorkerActor(params("ctx").asInstanceOf[ChannelHandlerContext])))
      ref ! params("data")
    }
  }
}

case class WorkerActor(ctx: ChannelHandlerContext) extends Actor {

  override def receive: Receive = {
    case data: String => {
      val scene = DBUtil.getSceneById(1L)
      val sceneCell = DBUtil.getSceneCellBySceneId(1L)
      val result = new util.HashMap[String, Any]()
      result.put("name", scene.get("name"))
      result.put("cells", sceneCell)
      ctx.writeAndFlush(new TextWebSocketFrame(new Gson().toJson(result)))
    }
  }
}