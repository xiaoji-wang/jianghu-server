package handle

import com.google.gson.Gson
import db.DBUtil

/**
  * Created by wxji on 2017-09-22.
  */
object NpcHandle {
  def getNpcByCell(cellId: Long): String = {
    new Gson().toJson(DBUtil.getNpcByCell(cellId))
  }
}
