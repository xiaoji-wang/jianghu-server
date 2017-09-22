package actor

import akka.actor.{Actor, Props}
import com.google.gson.{Gson, JsonObject}
import handle.{NpcHandle, SceneHandle}
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
      case Constant.GET_MAP => ctx.writeAndFlush(new TextWebSocketFrame(SceneHandle.getMap(1L)))
      case Constant.NPC_SELECTED => ctx.writeAndFlush(new TextWebSocketFrame(NpcHandle.getNpc(data._2.get("id").getAsLong)))
    }
    case _ => ctx.writeAndFlush(new TextWebSocketFrame(""))
  }
}