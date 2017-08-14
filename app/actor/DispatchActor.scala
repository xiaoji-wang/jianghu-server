package actor

import akka.actor.{Actor, Props}
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

  import org.json4s.JsonDSL._
  import org.json4s.jackson.JsonMethods._

  override def receive: Receive = {
    case data: String => {
      val scene = DBUtil.getSceneById(1L)
      val sceneCell = DBUtil.getSceneCellBySceneId(1L)
      ctx.writeAndFlush(new TextWebSocketFrame(compact(render(("name" -> scene.values("name").toString) ~ ("cells" -> sceneCell)))))
    }
  }
}