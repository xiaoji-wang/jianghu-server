package handle

import com.google.gson.Gson
import db.DBUtil

/**
  * Created by wxji on 2017-09-22.
  */
object NpcHandle {
  def getNpc(npcId: Long): String = {
    new Gson().toJson(DBUtil.getCharacterById(npcId))
  }
}
